package com.mirf.core.algorithm

import com.mirf.core.data.Data

fun <I : Data, O : Data> ((I) -> O).asAlg(): Algorithm<I, O> {
    return SimpleAlg(this)
}