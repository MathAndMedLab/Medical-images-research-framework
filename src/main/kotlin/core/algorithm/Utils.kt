package core.algorithm

import core.data.Data

fun <I : Data, O : Data> ((I) -> O).asAlg(): Algorithm<I, O> {
    return SimpleAlg(this)
}