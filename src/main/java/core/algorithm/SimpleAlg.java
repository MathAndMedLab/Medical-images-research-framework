package core.algorithm;

import core.data.Data;

import java.util.function.Function;

/**
 * Simple class to wrap methods into Algorithm interface in one line of code
 * @param <I>
 * @param <O>
 */
public class SimpleAlg<I extends Data, O extends Data> implements Algorithm<I, O> {

    private Function<I, O> func;

    public SimpleAlg(Function<I, O> func){
        this.func = func;
    }

    @Override
    public O execute(I input) {
        return func.apply(input);
    }
}
