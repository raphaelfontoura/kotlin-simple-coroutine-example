package kotlincoroutine

class AggregateService {

    fun aggregateNames(customers: List<CustomerResponse>): List<String> {
        val result: MutableList<String> = mutableListOf()
        customers.forEach { result.add(it.name) }
        return result
    }

    fun aggregateValues(cutomers: List<CustomerResponse>): Long {
        val sum = cutomers.fold(0L) { acc, customerResponse -> acc + customerResponse.value }
        return sum
    }
}