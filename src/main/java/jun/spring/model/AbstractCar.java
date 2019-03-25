package jun.spring.model;

import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractCar implements Car{

    @Autowired
    private Engine engine;

    public void on() {
        System.out.println("시동을 켜다");
    }

    public void running() {
        engine.run();
        run();
    }

    protected abstract void run();

    public void off() {
        System.out.println("시동을 끄다");
    }
}
