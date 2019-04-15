package jun.spring.factory;

import jun.spring.model.Avante;
import jun.spring.model.Car;
import jun.spring.model.Spark;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;

public class BeanFactoryCrudTests {

    private DefaultListableBeanFactory dlbf;

    @Before
    public void init() {
        dlbf = new DefaultListableBeanFactory();
    }

    @Test
    public void 빈_정의_등록_테스트() {
        dlbf.registerBeanDefinition("avante", new RootBeanDefinition(Avante.class));
        assertEquals(dlbf.getBeanDefinitionCount(), 1);
        assertEquals(dlbf.getBeanDefinitionNames().length, 1);
        Car car = dlbf.getBean("avante", Car.class);
        assertEquals(dlbf.getSingletonCount(), 1);
        assertEquals(dlbf.getSingletonNames().length, 1);
        car.on();
    }

    @Test
    public void 싱글톤_빈_등록_테스트() {
        dlbf.registerSingleton("avante", new Avante("아방이"));
        assertEquals(dlbf.getBeanDefinitionCount(), 0);
        assertEquals(dlbf.getBeanDefinitionNames().length, 0);
        Car car = dlbf.getBean("avante", Car.class);
        assertEquals(dlbf.getSingletonCount(), 1);
        assertEquals(dlbf.getSingletonNames().length, 1);
        car.on();
    }

    @Test
    public void 빈_별칭_등록_테스트() {
        dlbf.registerSingleton("avante", new Avante("아방이"));
        dlbf.registerAlias("avante", "newAvante");

        assertEquals(dlbf.getAliases("newAvante").length, 1);
        assertFalse(dlbf.containsBean("test"));
        assertTrue(dlbf.containsBean("avante"));
        assertTrue(dlbf.containsBean("newAvante"));
        Avante avante = dlbf.getBean("newAvante", Avante.class);
        avante.on();
    }

    @Test
    public void 빈_조회_테스트() {
        dlbf.registerSingleton("avante", new Avante("아방이"));
        dlbf.registerSingleton("spark", new Spark("스팍"));

        assertThat(dlbf.getBean("avante"), is(notNullValue()));
        assertThat(dlbf.getBean("avante", Avante.class), is(notNullValue()));
        assertThat(dlbf.getBean(Avante.class), is(notNullValue()));
    }

    @Test(expected = BeanCreationException.class)
    public void 빈_예외_테스트() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        Outer outer = (Outer) context.getBean("outer");
        assertNotNull(outer);
    }

    @Configuration
    static class AppConfig {
        @Bean
        public Outer outer() {
            return new Outer();
        }
//        @Bean
//        public Inner inner() {
//            return new Inner();
//        }
    }

    static class Outer {
        @Autowired
        Inner inner;
    }

    static class Inner {

    }

}
