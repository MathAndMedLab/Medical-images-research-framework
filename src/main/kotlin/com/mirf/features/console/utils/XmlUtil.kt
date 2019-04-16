package com.mirf.features.console.utils

import org.w3c.dom.Node
import org.w3c.dom.NodeList


fun NodeList.asList(): List<Node> {
    return if (this.length == 0)
        emptyList()
    else
        NodeListWrapper(this)
}

internal class NodeListWrapper(private val list: NodeList) : AbstractList<Node>(), RandomAccess {
    override val size: Int
        get() = list.length

    override fun get(index: Int): Node {
        return list.item(index)
    }
}
