package jun.spring.factory;

import jun.spring.model.Avante;
import jun.spring.model.Car;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class AutowiredAnnotationBeanPostProcessorTests {

    private DefaultListableBeanFactory factory;

    @Before
    public void setUp() {
        factory = new DefaultListableBeanFactory();
    }

    @Test
    public void 불완전한_빈_정의_테스트() {
        AutowiredAnnotationBeanPostProcessor bpp = new AutowiredAnnotationBeanPostProcessor();
        bpp.setBeanFactory(factory);
        factory.addBeanPostProcessor(bpp);
        factory.registerBeanDefinition("avante", new GenericBeanDefinition());
        try {
            factory.getBean("avante");
        } catch (BeanCreationException ex) {
            assertTrue(ex.getRootCause() instanceof IllegalStateException);
            assertTrue(ex.getMessage().contains("No bean class"));
        }
    }

    @Test
    public void RESOURCE_주입_테스트() {
        AutowiredAnnotationBeanPostProcessor bpp = new AutowiredAnnotationBeanPostProcessor();
        bpp.setBeanFactory(factory);
        factory.addBeanPostProcessor(bpp);
        RootBeanDefinition bd = new RootBeanDefinition(ResourceInjectionBean.class);
        bd.setScope(RootBeanDefinition.SCOPE_PROTOTYPE);
        factory.registerBeanDefinition("annotatedBean", bd);
        Avante avante = new Avante();
        factory.registerSingleton("avante", avante);

        ResourceInjectionBean bean1 = (ResourceInjectionBean) factory.getBean("annotatedBean");
        assertSame(avante, bean1.getAvante());
        assertSame(avante, bean1.getAvante2());

        ResourceInjectionBean bean2 = (ResourceInjectionBean) factory.getBean("annotatedBean");
        assertSame(avante, bean2.getAvante());
        assertSame(avante, bean2.getAvante2());

        assertNotSame(bean1, bean2);
    }

    @Test
    public void 확장된_RESOURCE_주입_테스트() {
        factory.registerResolvableDependency(BeanFactory.class, factory);
        AutowiredAnnotationBeanPostProcessor bpp = new AutowiredAnnotationBeanPostProcessor();
        bpp.setBeanFactory(factory);
        factory.addBeanPostProcessor(bpp);
        RootBeanDefinition bd = new RootBeanDefinition();
    }

    public static class ResourceInjectionBean {

        @Autowired(required = false)
        private Avante avante;

        private Avante avante2;

        @Autowired
        public void setAvante2(Avante avante2) {
            if (this.avante2 != null) {
                throw new IllegalStateException("Already called");
            }
            this.avante2 = avante2;
        }

        public Avante getAvante() {
            return avante;
        }

        public Avante getAvante2() {
            return avante2;
        }
    }

    public static class NonPublicResourceInjectionBean<T> extends ResourceInjectionBean {

        @Autowired
        public final Car avante3 = null;

        private T nestedAvante;

        private Car avante4;

        protected BeanFactory beanFactory;

        public boolean baseInjected = false;

        public NonPublicResourceInjectionBean() {}

        @Override
        @Autowired
        @Required
        public void setAvante2(Avante avante2) {
            super.setAvante2(avante2);
        }

        @Autowired
        private void inject(Car avante4, T nestedAvante) {
            this.avante4 = avante4;
            this.nestedAvante = nestedAvante;
        }

        @Autowired
        private void inject(Car car) {
            this.baseInjected = true;
        }

        @Autowired
        protected void initBeanFactory(BeanFactory beanFactory) {
            this.beanFactory = beanFactory;
        }

        public Car getAvante3() {
            return this.avante3;
        }

        public Car getAvante4() {
            return this.avante4;
        }

        public T getNestedAvante() {
            return this.nestedAvante;
        }

        public BeanFactory getBeanFactory() {
            return this.beanFactory;
        }
    }


}
