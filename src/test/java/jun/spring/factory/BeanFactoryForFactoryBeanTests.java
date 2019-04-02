package jun.spring.factory;

import jun.spring.model.Car;
import jun.spring.model.factory.SpecialCar;
import jun.spring.model.factory.SpecialCarFactoryBean;
import org.junit.Test;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;

import static org.junit.Assert.assertNotNull;

public class BeanFactoryForFactoryBeanTests {

    @Test
    public void 팩토리_빈_등록_테스트() {
        DefaultListableBeanFactory dlbf = new DefaultListableBeanFactory();
        dlbf.registerBeanDefinition("specialCar", new RootBeanDefinition(SpecialCarFactoryBean.class));
        Car car = dlbf.getBean("specialCar", SpecialCar.class);
        car.on();
        assertNotNull(car);
    }

}
