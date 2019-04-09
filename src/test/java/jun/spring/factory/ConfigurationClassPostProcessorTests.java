package jun.spring.factory;

import org.junit.Test;
import org.springframework.beans.factory.annotation.QualifierAnnotationAutowireCandidateResolver;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;

import static junit.framework.TestCase.assertSame;

public class ConfigurationClassPostProcessorTests {

    @Test
    public void CONFIGURATION_CLASS_설정_테스트() {
        DefaultListableBeanFactory dlbf = new DefaultListableBeanFactory();
        QualifierAnnotationAutowireCandidateResolver acr = new QualifierAnnotationAutowireCandidateResolver();
        acr.setBeanFactory(dlbf);
        dlbf.setAutowireCandidateResolver(acr);
        dlbf.registerBeanDefinition("config", new RootBeanDefinition(SingletonBeanConfig.class));
        ConfigurationClassPostProcessor pp = new ConfigurationClassPostProcessor();
        pp.postProcessBeanFactory(dlbf); // 빈 등록, 주입
        Foo foo = dlbf.getBean("foo", Foo.class);
        Bar bar = dlbf.getBean("bar", Bar.class);
        assertSame(foo, bar.foo);
    }

    @Configuration
    static class SingletonBeanConfig {
        @Bean
        public Foo foo() {
            return new Foo();
        }

        @Bean
        public Bar bar() {
            return new Bar(foo());
        }

    }

    static class Foo {}

    static class Bar {
        final Foo foo;

        public Bar(Foo foo) {
            this.foo = foo;
        }
    }


}
