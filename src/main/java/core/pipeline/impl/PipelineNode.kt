package core.pipeline.impl

import core.algorithm.Algorithm
import core.algorithm.SimpleAlg
import core.data.Data
import core.pipeline.PipelineBlock

class PipelineNode<I: Data, O : Data> (private var value: PipelineBlock<I, O>) {

    constructor(algorithm: Algorithm<I, O>) : this(AlgorithmHostBlock(algorithm))

    var children: MutableList<PipelineNode<O, *>> = mutableListOf()

    fun <T : Data> addListener(listener: PipelineBlock<O, T>): PipelineNode<O, T> {
        val listenerNode: PipelineNode<O, T> = PipelineNode(listener)
        value.addListener(listener)
        children.add(listenerNode)

        return listenerNode
    }

    fun <T : Data> addListener(listener: Algorithm<O, T>): PipelineNode<O, T> = addListener(AlgorithmHostBlock(listener))

    fun run(data: I) = value.inputDataReady(null, data)

    fun addNode(node: PipelineNode<O, *>) {
        children.add(node)
        value.addListener(node.value)
    }

    override fun toString(): String {
        var s = "${value}"
        if (!children.isEmpty()) {
            s += " {" + children.map { it.toString() } + " }"
        }
        return s
    }

    fun <T : Data> addListener(listener: (O) -> T): PipelineNode<O, T>{
        return addListener(SimpleAlg(listener))
    }
}