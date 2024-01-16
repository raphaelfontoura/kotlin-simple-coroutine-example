package kotlincoroutine

import kotlin.random.Random
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.yield


class CoroutineExample(private val aggregateService: AggregateService = AggregateService()) {

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

                println("starkbank sum = ${(transactionsStarkbank.toDouble()/100)}, itau count names = ${transactionsToItau.count()}")
            }
        } catch (e: Exception) {
            println("Error occurred while processing transactions")
        }
    }

    fun generateTestData(numberOfEntries: Int): List<CustomerResponse> {
        val testData = mutableListOf<CustomerResponse>()

        repeat(numberOfEntries) {
            testData.add(CustomerResponse(name = generateRandomName(), value = Random.nextLong(1000), paymentMethod = randomPaymentMethod()))
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

    private suspend fun doOperationSplitStarkbank(transactions: List<CustomerResponse>): Long {
        println("Run Split Starkbank")
        delay(1000L)
        val result = aggregateService.aggregateValues(transactions)
        println("Finished Split Starkbank")
        return result
    }

    private suspend fun doOperationSplitItau(transactions: List<CustomerResponse>): List<String> {
        println("Run Split Itau")
        yield()
        val result = aggregateService.aggregateNames(transactions)
        println("Finished Split ITAU")
        return result.toList()
    }
}
