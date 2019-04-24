package com.mirf.core.algorithm

import com.mirf.core.data.Data

/**
 * Simple class to wrap methods into Algorithm interface in one line of code
 * @param <I>
 * @param <O>
</O></I> */
class SimpleAlg<I : Data, O : Data>(private val func: (I) -> O) : Algorithm<I, O> {

    override fun execute(input: I): O {
        return func.invoke(input)
    }
}
