package jun.spring.factory;

import jun.spring.model.Avante;
import jun.spring.model.User;
import org.junit.Test;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;

import static org.junit.Assert.assertSame;

public class BeanFactoryConstructorTests {

    @Test
    public void 생성자_자동주입_테스트() {
        DefaultListableBeanFactory dlbf = new DefaultListableBeanFactory();
        RootBeanDefinition bd = new RootBeanDefinition(Avante.class);
        dlbf.registerBeanDefinition("avante", bd);
        User user = (User) dlbf.autowire(User.class, AutowireCapableBeanFactory.AUTOWIRE_CONSTRUCTOR, true);
        Object avante = dlbf.getBean("avante");
        assertSame(user.getAvante(), avante);
        assertSame(BeanFactoryUtils.beanOfType(dlbf, Avante.class), avante);
    }

}
