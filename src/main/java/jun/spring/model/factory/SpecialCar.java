package jun.spring.model.factory;

import jun.spring.model.Car;

public class SpecialCar implements Car {

    private String name;

    private SpecialCar(String name) {
        this.name = name;
    }

    public static SpecialCar newSpecialCar(String name) {
        return new SpecialCar(name);
    }

    public void on() {
        System.out.println(name + " 특수차량 on");
    }

    public void off() {
        System.out.println(name + " 특수차량 off");
    }
}
