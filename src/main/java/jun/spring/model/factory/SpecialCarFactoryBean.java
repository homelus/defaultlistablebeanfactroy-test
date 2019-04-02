package jun.spring.model.factory;

import org.springframework.beans.factory.FactoryBean;

public class SpecialCarFactoryBean implements FactoryBean<SpecialCar> {

    public SpecialCar getObject() throws Exception {
        return SpecialCar.newSpecialCar("특카");
    }

    public Class<? extends SpecialCar> getObjectType() {
        return SpecialCar.class;
    }

    public boolean isSingleton() {
        return true;
    }
}
