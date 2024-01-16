package kotlincoroutine

import java.util.UUID

data class CustomerResponse(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val value: Long = 0,
    val paymentMethod: PaymentMethodEnum = PaymentMethodEnum.PIX
)

enum class PaymentMethodEnum {
    PIX,
    CREDIT,
    DEBIT
}
