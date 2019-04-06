package jun.spring.model;

public class DerivedAvante extends Avante{

    String beanName;

    private boolean initialized;

    private boolean destroyed;

    public void initialize() {
        this.initialized = true;
    }

    public boolean wasInitialized() {
        return initialized;
    }

    public void destroy() {
        this.destroyed = true;
    }

    public boolean wasDestroyed() {
        return destroyed;
    }

}
