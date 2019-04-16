package com.mirf.core.common

import kotlin.reflect.KFunction2

class Event<T>(private val handlers: MutableCollection<(Any, T) -> Unit>) {
    operator fun plusAssign(handler: (Any, T) -> Unit) {
        handlers.add(handler)
    }

    operator fun minusAssign(handler: (Any, T) -> Unit) {
        handlers.remove(handler)
    }

    operator fun plusAssign(handler: KFunction2<Any, T, Unit>) {
        handlers.add(handler)
    }

    operator fun minusAssign(handler: KFunction2<Any, T, Unit>) {
        handlers.remove(handler)
    }
}