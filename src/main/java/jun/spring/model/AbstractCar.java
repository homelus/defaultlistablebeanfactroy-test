package jun.spring.model;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractCar implements Car{

    private String wheel;

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

    public String getWheel() {
        return wheel;
    }

    public void setWheel(String wheel) {
        this.wheel = wheel;
    }

    public Engine getEngine() {
        return engine;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }
}
