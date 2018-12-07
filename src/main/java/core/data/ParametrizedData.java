package core.data;

public class ParametrizedData<T> extends Data {

    public T data;

    public ParametrizedData(T data) {
        super();
        this.data = data;
    }
}
