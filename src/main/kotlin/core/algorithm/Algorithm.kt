package core.algorithm

import core.data.Data
import core.log.MirfLogFactory

/**
 * Any algorithm that may be executed by `Block`
 *
 *
 * Any feature that is to be added to the tool must use this class
 * in order to wrap any logic, that is later will be used in pipeline
 *
 * @param <I> is the type taken as an input for the algorithm
 * @param <O> is the type returned by the algorithm
</O></I> */
interface Algorithm<I : Data, O : Data> {

    /**
     * Runs the algorithms on the given input and produces the result,
     * that should be used by other algorithms in pipeline
     *
     * @param input algorithm's input data
     * @return algorithm's output of type `B`
     */
    fun execute(input: I): O
}
