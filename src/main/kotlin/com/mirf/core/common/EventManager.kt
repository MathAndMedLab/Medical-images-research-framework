package com.mirf.core.common

class EventManager<T> {

    private val handlers = hashSetOf<((Any, T) -> Unit)>()
    operator fun invoke(sender: Any, value: T) {
        for (handler in handlers) handler(sender, value)
    }

    val event: Event<T> = Event(handlers)
}