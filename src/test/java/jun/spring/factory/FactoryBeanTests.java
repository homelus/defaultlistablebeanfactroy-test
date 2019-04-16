package jun.spring.factory;

import org.junit.Test;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.util.Assert;

import static org.junit.Assert.assertNull;

public class FactoryBeanTests {

    @Test
    public void NULL_팩토리_빈_테스트() {
        DefaultListableBeanFactory dlbf = new DefaultListableBeanFactory();
        dlbf.registerBeanDefinition("factoryBean", new RootBeanDefinition(NullReturningFactoryBean.class));
        Object result = dlbf.getBean("factoryBean");
        assertNull(result);
    }

    // todo
    @Test
    public void 자동와이어링_팩토리_빈_테스트() {
        DefaultListableBeanFactory dlbf = new DefaultListableBeanFactory();
    }

    public static class NullReturningFactoryBean implements FactoryBean<Object> {
        @Override
        public Object getObject() throws Exception {
            return null;
        }

        @Override
        public Class<?> getObjectType() {
            return null;
        }

        @Override
        public boolean isSingleton() {
            return false;
        }
    }

    @Configuration
    @ImportResource("classpath:factory-bean.xml")
    static class AutowireConfig {

        @Bean(autowire = Autowire.BY_TYPE)
        public Alpha alpha() {
            return new Alpha();
        }

        @Bean(autowire = Autowire.BY_TYPE)
        public Beta beta() {
            return new Beta();
        }

        @Bean
        public Gamma gamma() {
            return new Gamma();
        }

        @Bean
        public BetaFactoryBean betaFactory() {
            BetaFactoryBean betaFactoryBean = new BetaFactoryBean();
            betaFactoryBean.setBeta(beta());
            return betaFactoryBean;
        }

    }

    public static class Alpha implements InitializingBean {

        private Beta beta;

        public Beta getBeta() {
            return beta;
        }

        public void setBeta(Beta beta) {
            this.beta = beta;
        }

        @Override
        public void afterPropertiesSet() throws Exception {
            Assert.notNull(beta,"'beta' property is required");
        }
    }

    public static class Beta implements InitializingBean {

        private Gamma gamma;

        @Value("myName")
        private String name;

        public Gamma getGamma() {
            return gamma;
        }

        public void setGamma(Gamma gamma) {
            this.gamma = gamma;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public void afterPropertiesSet() throws Exception {
            Assert.notNull(gamma, "'gamma' property is required");
        }
    }

    public static class Gamma {}

    public static class BetaFactoryBean implements FactoryBean<Object> {

        private Beta beta;

        public void setBeta(Beta beta) {
            this.beta = beta;
        }

        @Override
        public Object getObject() throws Exception {
            return this.beta;
        }

        @Override
        public Class<?> getObjectType() {
            return null;
        }

        @Override
        public boolean isSingleton() {
            return false;
        }
    }

}
