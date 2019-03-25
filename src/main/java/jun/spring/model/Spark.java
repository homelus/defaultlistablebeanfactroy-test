package jun.spring.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Spark extends AbstractCar {

    private String name;

    protected void run() {
        System.out.println("스파크가 움직인다.");
    }
}
