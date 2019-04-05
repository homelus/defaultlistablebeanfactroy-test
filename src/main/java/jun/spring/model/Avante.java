package jun.spring.model;

import lombok.Data;

public class Avante extends AbstractCar implements Comparable{

    public Avante() {}

    public Avante(String name) {
        this.name = name;
    }

    private String name;

    protected void run() {
        System.out.println(getName() + " 달리다");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int compareTo(Object other) {
        if (this.name != null && other instanceof Avante) {
            return this.name.compareTo(((Avante) other).getName());
        }  else {
            return 1;
        }
    }
}
