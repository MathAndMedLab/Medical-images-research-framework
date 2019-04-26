


object RetryDefaultOptions{
    const val numOfRetries: Int = 10
    const val retryIntervalMs: Int = 1000
}

fun <T> invokeWithRetry(numOfRetries : Int = RetryDefaultOptions.numOfRetries, retryIntervalMs : Int = RetryDefaultOptions.retryIntervalMs, func: () -> T ) : T{
    for (i in 0 until numOfRetries){
        try {
            val result = func.invoke()
            return result
        }catch (e: Throwable){
            Thread.sleep(retryIntervalMs.toLong())
        }
    }

    throw Exception("Failed to invoke retryable method: numbers of attempt exceeded")
}