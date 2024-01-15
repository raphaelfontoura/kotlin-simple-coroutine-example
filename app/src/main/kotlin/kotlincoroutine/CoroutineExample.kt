package kotlincoroutine

import java.util.UUID
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking


class CoroutineExample {

    private val customerList = generateTestData(1000000)

    fun doOperationSplit() {
        try {
            val customerListPix = customerList.filter { it.paymentMethod == PaymentMethodEnum.PIX }
            val customerListOthers = customerList.filter { it.paymentMethod != PaymentMethodEnum.PIX }
            runBlocking {

                val transactionsStarkbankDeferred = async { doOperationSplitStarkbank(customerListPix) }


                val transactionsToItauDeferred = async { doOperationSplitItau(customerListOthers) }

                val transactionsStarkbank = transactionsStarkbankDeferred.await()
                val transactionsToItau = transactionsToItauDeferred.await()

                println("starkbank = ${transactionsStarkbank.count()}, itau = ${transactionsToItau.count()}")
            }
        } catch (e: Exception) {
            println("Error occurred while processing transaction for customer")
        }
    }

    fun generateTestData(numberOfEntries: Int): List<CustomerResponse> {
        val testData = mutableListOf<CustomerResponse>()

        repeat(numberOfEntries) {
            testData.add(CustomerResponse(name = generateRandomName(), paymentMethod = randomPaymentMethod()))
        }

        return testData
    }

    private fun randomPaymentMethod(): PaymentMethodEnum {
        val paymentMethods = PaymentMethodEnum.values().toList()
        return paymentMethods.shuffled().first()
    }

    private fun generateRandomName(): String {
        val names = listOf("Alice", "Bob", "Charlie", "David", "Eva", "Frank", "Grace", "Hank", "Ivy", "Jack")
        return names.shuffled().first()
    }

    private suspend fun doOperationSplitStarkbank(transactions: List<CustomerResponse>): String {
        println("Run Split Starkbank")
        val result = StringBuffer()
        result.append("STARKBANK: ")
        transactions.forEach { result.append(it.name+",") }
        delay(1000L)
        println("Finished Split Starkbank")
        return result.toString()
    }

    private suspend fun doOperationSplitItau(transactions: List<CustomerResponse>): String {
        println("Run Split Itau")
        val result = StringBuffer()
        result.append("ITAU: ")
        transactions.forEach { result.append(it.name+",") }
        println("Finished Split ITAU")
        return result.toString()
    }
}

data class CustomerResponse(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val paymentMethod: PaymentMethodEnum = PaymentMethodEnum.PIX
)

enum class PaymentMethodEnum {
    PIX,
    CREDIT,
    DEBIT
}
