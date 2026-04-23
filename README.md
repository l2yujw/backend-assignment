# 결제 시스템 구축 과제

## 프로젝트에서 중요하게 생각한 것

### 1. 레이어드 패키지 구조와 도메인 간 참조 방향

패키지를 도메인(member, order, payment, discount) 단위로 나누고, 각 도메인 안에서는 `api → application → domain ← infra` 방향으로만 의존이 흐르도록 설계했습니다.

```
com.example.oms
├── core/           # 공통 인프라 (Guard, ErrorCode, AbstractTime, @Reader/@Writer)
├── platform/       # 웹/DB 설정 (GlobalExceptionHandler, JpaAuditingConfig)
├── member/
│   ├── api/        # Controller, Request/Response
│   ├── application/# Service, Reader, Writer, Command, Result
│   ├── domain/     # Member (AggregateRoot), MemberRepository (interface)
│   └── infra/      # JpaMemberRepository, MemberRepositoryAdapter
├── order/          # (동일 구조)
├── payment/        # (동일 구조)
└── discount/
    ├── application/ # DiscountService
    ├── domain/      # DiscountPolicy, PaymentMethodDiscountPolicy (interface)
    └── infra/       # DiscountPolicyConfig (Bean 등록)
```

이 구조에서 핵심으로 지킨 규칙은 **도메인 레이어가 인프라를 모른다**는 것입니다. `MemberRepository`, `PaymentRepository` 등의 저장소 인터페이스는 `domain` 패키지에 정의되어 있고, Spring Data JPA 구현체는 `infra` 패키지의 `RepositoryAdapter`가 담당합니다. 덕분에 도메인 로직을 테스트할 때 JPA 없이 Fake 구현체만으로 조립할 수 있습니다.

### 2. 도메인 간 참조 문제 — Facade를 통한 경계 격리

도메인 간 직접 참조가 생기면 결합도가 높아지고, 나중에 서비스를 분리하거나 테스트하기 어려워집니다. 이 프로젝트에서 가장 참조가 복잡한 지점은 결제 흐름입니다. 결제를 처리하려면 Order 정보와 Member 정보가 모두 필요한데, `PaymentService`가 `OrderService`와 `MemberService`를 직접 의존하면 세 도메인이 강하게 엮입니다.

이 문제를 `PaymentFacade`로 해결했습니다.

```java
// PaymentFacade: 도메인 간 조율만 담당, 비즈니스 로직 없음
public PaymentResult.Paid pay(Long orderId, String paymentMethod) {
    OrderResult.OrderInfo order = orderService.getInfo(orderId);
    MemberResult.MemberInfo member = memberService.getInfo(order.memberId());
    return paymentService.pay(new PaymentCommand.Pay(...));
}
```

`PaymentService`는 `PaymentCommand`만 받고 Order/Member 도메인을 전혀 모릅니다. 각 Service는 자신의 도메인 로직에만 집중하고, 도메인 간 조율은 Facade가 전담합니다. `OrderFacade`도 동일한 이유로 회원 존재 여부 검증(`memberService.assertExists`)을 Facade에서 처리합니다.

### 3. Result 객체를 통한 도메인 객체 유출 방지

도메인 객체(`Member`, `Order`, `Payment`)가 레이어 경계를 넘어가면 의도치 않은 수정이나 직렬화 문제가 생깁니다. 모든 레이어 간 데이터 전달은 `Command`(입력)와 `Result`(출력) record로만 이루어지도록 했습니다.

```
Controller  →  Command  →  Service  →  Result  →  Controller
                              ↑
                          도메인 객체는 이 경계 밖으로 나오지 않음
```

예를 들어 `PaymentResult.Paid`는 `Payment` 엔티티의 getter를 직접 노출하지 않고, `from(Payment)` 정적 팩토리 메서드로 변환해 반환합니다. 각 Response DTO도 `Result`를 받아 다시 변환하므로, Controller가 도메인 구조에 의존하지 않습니다.

### 4. @Writer / @Reader 어노테이션을 통한 트랜잭션 정책 명시화

모든 쓰기 컴포넌트에는 `@Writer`, 읽기 컴포넌트에는 `@Reader`를 붙였습니다.

```java
@Writer  // @Transactional(propagation = MANDATORY) — 반드시 트랜잭션 내에서 호출
public class PaymentWriter { ... }

@Reader  // @Transactional(readOnly = true, propagation = SUPPORTS)
public class PaymentReader { ... }
```

`@Writer`에 `MANDATORY`를 적용한 이유는, Writer가 트랜잭션 없이 단독으로 호출되는 상황을 방지하기 위해서입니다. 결제처럼 Payment 저장과 DiscountHistory 저장이 한 트랜잭션에 묶여야 하는 경우, Service가 트랜잭션을 열고 Writer들이 그 안에서 실행되는 구조를 코드 관습이 아닌 타입 수준에서 강제합니다.

### 5. 도메인 불변성과 Guard

`Payment`, `Member`, `Order` 등 도메인 객체는 생성자가 `private`이고, setter를 제공하지 않습니다. 생성 시점에 `Guard`로 불변식을 검증해 잘못된 값이 저장되는 것을 도메인 레이어에서 원천 차단합니다.

```java
// Payment 생성자
this.orderId = Guard.notNull(orderId, "orderId");
this.originalPrice = Guard.minValue(originalPrice, 1, "originalPrice");
this.finalAmount = Guard.minValue(finalAmount, 0, "finalAmount");
```

`@AggregateRoot` 어노테이션은 기능보다 **의도를 선언**하는 목적입니다. 이 클래스가 집합체의 루트임을 코드 자체로 문서화합니다.

### 6. 할인 이력을 스냅샷으로 기록 — 정책 변경으로부터 과거 데이터 보호

할인 정책은 코드에서 `DiscountPolicyConfig` 빈으로 등록됩니다. 즉, 정책 변경은 코드 배포로 이루어지고, 배포 후에는 이전 정책 객체가 사라집니다. 이 상황에서 과거 결제의 할인 내역을 재계산하거나 참조하는 것은 불가능합니다.

이 문제를 `DiscountHistory`를 별도 테이블에 스냅샷으로 기록하는 방식으로 해결했습니다. 결제 완료 시점에 `policyName`, `discountType`, `discountValue`, `gradeDiscountAmount`를 함께 기록하므로, 이후 어떤 정책이 변경되거나 제거되어도 과거 이력은 그대로 남습니다. 정합성 테스트(`PaymentHistoryIntegrityTest`)가 이 불변성을 직접 검증합니다.

---

## main → feature 브랜치 변경 내용과 근거

### 변경 1: DiscountService — 등급 할인과 결제 수단 할인을 메서드 분리

`main`에서는 등급 할인만 있어 메서드 하나로 충분했습니다. `feature`에서 결제 수단 할인이 추가되면서, 두 할인을 하나로 합치면 적용 순서와 기반 금액(원가 vs 등급 할인 후 금액)이 섞여 코드가 모호해집니다.

```java
// feature: 두 할인의 적용 시점과 기반 금액이 다름을 메서드 시그니처로 표현
DiscountResult.Grade  applyGradeDiscount(MemberGrade grade, int originalPrice)
DiscountResult.Method applyMethodDiscount(PaymentMethod method, int priceAfterGrade)
```

`PaymentService.pay()`에서 이 두 메서드를 순서대로 호출하는 것만으로 "등급 할인 → 결제 수단 할인" 순서가 명확하게 드러납니다.

### 변경 2: PaymentMethodDiscountPolicy 인터페이스 신설

등급 정책(`DiscountPolicy`)과 결제 수단 정책을 같은 인터페이스로 처리하면 `supports(MemberGrade)` 시그니처가 맞지 않습니다. 인터페이스를 분리해 타입 수준에서 두 정책이 다른 개념임을 명시했습니다.

```java
// 등급 정책
interface DiscountPolicy {
    boolean supports(MemberGrade grade);
    ...
}

// 결제 수단 정책 — 완전히 다른 타입
interface PaymentMethodDiscountPolicy {
    boolean supports(PaymentMethod method);
    int discountRate();
    ...
}
```

### 변경 3: DiscountHistory 엔티티 추가

`main`에서는 `Payment`에 정책 정보를 저장하는 것으로 충분했습니다. `feature`에서 "이력 관리" 요구사항이 추가되면서, 한 결제에 여러 할인(등급 + 결제 수단)이 적용되는 구조를 자연스럽게 수용하려면 별도 테이블이 필요하다고 판단했습니다. `Payment`는 "최종적으로 얼마를 결제했는가", `DiscountHistory`는 "어떤 할인이 어떻게 적용됐는가"로 책임을 분리합니다.

---

## 테스트 전략

**DiscountServiceTest** — 등급별 할인 금액 정확성, 등급 + 결제 수단 중복 할인 시 최종 금액 검증

**PaymentHistoryIntegrityTest** — Spring Context 없이 인메모리 Fake로 조립. 정책 변경/삭제 이후에도 과거 이력이 스냅샷으로 보존되는지 검증