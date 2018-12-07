package core.data.attribute;

/**
 * Switch class is used to indicate {@link DataAttribute} that holds no value: it's either declared in attributes or no
 */
//TODO: (avlomakin) consider creation superclass for attributes with and without inner values
public final class Switch {

    public static final Switch instance = new Switch();

    private Switch(){

    }
}
