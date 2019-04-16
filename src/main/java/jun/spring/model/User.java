package jun.spring.model;

public class User {

    public User() {}

    public User(Avante avante) {
        this.avante = avante;
    }

    private String name;

    private Avante avante;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Avante getAvante() {
        return avante;
    }

    public void setAvante(Avante avante) {
        this.avante = avante;
    }
}
