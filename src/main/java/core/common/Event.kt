package core.common

class Event<T>(private val handlers: MutableCollection<(Any, T) -> Unit>) {
    operator fun plusAssign(handler: (Any, T) -> Unit) { handlers.add(handler) }
    operator fun minusAssign(handler: (Any, T) -> Unit) { handlers.remove(handler) }
}