package jun.spring.model;

import lombok.Data;

public class Seat {

    private boolean auto;

    private Boolean use;

    public boolean isAuto() {
        return auto;
    }

    public void setAuto(boolean auto) {
        this.auto = auto;
    }

    public Boolean getUse() {
        return use;
    }

    public void setUse(Boolean use) {
        this.use = use;
    }
}
