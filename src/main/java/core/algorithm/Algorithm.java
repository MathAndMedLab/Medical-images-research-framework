package core.algorithm;

import core.data.Data;

/**
 * Any algorithm that may be executed by {@code Block}
 *
 * <p>Any feature that is to be added to the tool must use this class
 * in order to wrap any logic, that is later will be used in pipeline
 *
 * @param <I> is the type taken as an input for the algorithm
 * @param <O> is the type returned by the algorithm
 */
public interface Algorithm<I extends Data, O extends Data> {
    /**
     * Runs the algorithms on the given input and produces the result,
     * that should be used by other algorithms in pipeline
     *
     * @param input algorithm's input data
     * @return algorithm's output of type {@code B}
     */
    O execute(I input);
}
