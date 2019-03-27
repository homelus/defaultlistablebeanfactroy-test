package jun.spring.factory;

import jun.spring.model.Avante;
import jun.spring.model.Car;
import jun.spring.model.Engine;
import jun.spring.model.VDIEngine;
import org.junit.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.InitDestroyAnnotationBeanPostProcessor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.CommonAnnotationBeanPostProcessor;
import org.springframework.context.support.GenericApplicationContext;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import java.util.Properties;

import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BeanPostProcessorTests {

    @Test
    public void 빈_생성자_소멸자_테스트() {
        DefaultListableBeanFactory bf = new DefaultListableBeanFactory();
        bf.addBeanPostProcessor(new CommonAnnotationBeanPostProcessor());
        bf.registerBeanDefinition("annotatedBean", new RootBeanDefinition(AnnotatedInitDestroyBean.class));

        AnnotatedInitDestroyBean bean = (AnnotatedInitDestroyBean) bf.getBean("annotatedBean");
        assertTrue(bean.initCalled);
        bf.destroySingletons();
        assertTrue(bean.destroyCalled);
    }

    @Test
    public void 빈_생성자_소멸자_POST_PROCESSOR_테스트() {
        DefaultListableBeanFactory bf = new DefaultListableBeanFactory();
        bf.addBeanPostProcessor(new InitDestroyBeanPostProcessor());
        bf.addBeanPostProcessor(new CommonAnnotationBeanPostProcessor());
        bf.registerBeanDefinition("annotatedBean", new RootBeanDefinition(AnnotatedInitDestroyBean.class));
        System.out.println("빈 정의 등록 완료");

        AnnotatedInitDestroyBean bean = (AnnotatedInitDestroyBean) bf.getBean("annotatedBean");
        assertTrue(bean.initCalled);
        bf.destroySingletons();
        assertTrue(bean.destroyCalled);
    }

    @Test
    public void 빈_생성자_소멸자_POST_PROCSSOR_WITH_APPLICATION_CONTEXT() {
        GenericApplicationContext ctx = new GenericApplicationContext();
        ctx.registerBeanDefinition("bpp1", new RootBeanDefinition(InitDestroyBeanPostProcessor.class));
        ctx.registerBeanDefinition("bpp2", new RootBeanDefinition(CommonAnnotationBeanPostProcessor.class));
        ctx.registerBeanDefinition("annotatedBean", new RootBeanDefinition(AnnotatedInitDestroyBean.class));
        ctx.refresh();

        AnnotatedInitDestroyBean bean = (AnnotatedInitDestroyBean) ctx.getBean("annotatedBean");
        assertTrue(bean.initCalled);
        ctx.close();
        assertTrue(bean.destroyCalled);
    }

    @Test
    public void 빈_생성자_소멸자_수동_구성() {
        DefaultListableBeanFactory bf = new DefaultListableBeanFactory();
        InitDestroyAnnotationBeanPostProcessor bpp = new InitDestroyAnnotationBeanPostProcessor();
        bpp.setInitAnnotationType(PostConstruct.class);
        bpp.setDestroyAnnotationType(PreDestroy.class);
        bf.addBeanPostProcessor(bpp);
        bf.registerBeanDefinition("annotatedBean", new RootBeanDefinition(AnnotatedInitDestroyBean.class));

        AnnotatedInitDestroyBean bean = (AnnotatedInitDestroyBean) bf.getBean("annotatedBean");
        assertTrue(bean.initCalled);
        bf.destroySingletons();
        assertTrue(bean.destroyCalled);
    }

    @Test
    public void POST_PROCESSOR_BEAN_FACTORY_테스트() {
        DefaultListableBeanFactory bf = new DefaultListableBeanFactory();
        RootBeanDefinition rbd = new RootBeanDefinition(NotNullFactory.class);
        rbd.setFactoryMethodName("create");

        bf.registerBeanDefinition("bean", rbd);
        Avante avante = (Avante) bf.getBean("bean");
        assertEquals(avante.getName(), "아방이");
        bf.destroySingletons();

    }

    @Test
    public void POST_PROCESSOR_NULL_빈_테스트() {
        DefaultListableBeanFactory bf = new DefaultListableBeanFactory();
        RootBeanDefinition rbd = new RootBeanDefinition(NullFactory.class);
        rbd.setFactoryMethodName("create");
        bf.registerBeanDefinition("bean", rbd);

        assertNull(bf.getBean("bean"));
        bf.destroySingletons();
    }

    @Test
    public void RESOURCE_빈_주입_테스트() {
        DefaultListableBeanFactory bf = new DefaultListableBeanFactory();
        CommonAnnotationBeanPostProcessor bpp = new CommonAnnotationBeanPostProcessor();
        bpp.setResourceFactory(bf);
        bf.addBeanPostProcessor(bpp);
        bf.registerBeanDefinition("annotatedBean", new RootBeanDefinition(ResourceInjectionBean.class));

        Avante tb = new Avante("아방이");
        bf.registerSingleton("testBean", tb);
        Avante tb2 = new Avante("아방이");
        bf.registerSingleton("testBean2", tb2);

        ResourceInjectionBean bean = (ResourceInjectionBean) bf.getBean("annotatedBean");

        assertTrue(bean.initCalled);
        assertTrue(bean.init2Called);
        assertTrue(bean.init3Called);
        assertSame(tb, bean.getTestBean());
        assertSame(tb2, bean.getTestBean2());
        bf.destroySingletons();
        assertTrue(bean.destroyCalled);
        assertTrue(bean.destroy2Called);
        assertTrue(bean.destroy3Called);
    }

    @Test
    public void 프로토타입_리소스_주입_테스트() {
        DefaultListableBeanFactory bf = new DefaultListableBeanFactory();
        CommonAnnotationBeanPostProcessor bpp = new CommonAnnotationBeanPostProcessor();
        bpp.setResourceFactory(bf);
        bf.addBeanPostProcessor(bpp);

        RootBeanDefinition abd = new RootBeanDefinition(ResourceInjectionBean.class);
        abd.setScope(BeanDefinition.SCOPE_PROTOTYPE);
        bf.registerBeanDefinition("annotatedBean", abd);
        RootBeanDefinition tbd1 = new RootBeanDefinition(Avante.class);
        tbd1.setScope(BeanDefinition.SCOPE_PROTOTYPE);
        bf.registerBeanDefinition("testBean", tbd1);
        RootBeanDefinition tbd2 = new RootBeanDefinition(Avante.class);
        tbd2.setScope(BeanDefinition.SCOPE_PROTOTYPE);
        bf.registerBeanDefinition("testBean2", tbd2);

        ResourceInjectionBean bean = (ResourceInjectionBean) bf.getBean("annotatedBean");
        assertTrue(bean.initCalled);
        assertTrue(bean.init2Called);
        assertTrue(bean.init3Called);

        Avante tb = bean.getTestBean();
        Avante tb2 = bean.getTestBean2();
        assertNotNull(tb);
        assertNotNull(tb2);

        ResourceInjectionBean anotherBean = (ResourceInjectionBean) bf.getBean("annotatedBean");
        assertNotSame(anotherBean, bean);
        assertNotSame(anotherBean.getTestBean(), tb);
        assertNotSame(anotherBean.getTestBean2(), tb2);

        bf.destroyBean("annotatedBean", bean);
        assertTrue(bean.destroyCalled);
        assertTrue(bean.destroy2Called);
        assertTrue(bean.destroy3Called);
    }

    @Test
    public void 리소스_주입_의존성_타입_처리_테스트() {
        DefaultListableBeanFactory bf = new DefaultListableBeanFactory();
        CommonAnnotationBeanPostProcessor bpp = new CommonAnnotationBeanPostProcessor();
        bpp.setBeanFactory(bf);
        bf.addBeanPostProcessor(bpp);

        RootBeanDefinition abd = new RootBeanDefinition(ExtendedREsourceInjectionBean.class);
        abd.setScope(BeanDefinition.SCOPE_PROTOTYPE);
        bf.registerBeanDefinition("annotatedBean", abd);
        RootBeanDefinition tbd = new RootBeanDefinition(Avante.class);
        tbd.setScope(BeanDefinition.SCOPE_PROTOTYPE);
        bf.registerBeanDefinition("testBean4", tbd);

        bf.registerResolvableDependency(BeanFactory.class, bf);
        bf.registerResolvableDependency(Engine.class, new ObjectFactory<Object>() {
            public Object getObject() throws BeansException {
                return new VDIEngine();
            }
        });

        PropertyPlaceholderConfigurer ppc = new PropertyPlaceholderConfigurer();
        Properties props = new Properties();
        props.setProperty("tb", "testBean4");
        ppc.setProperties(props);
        ppc.postProcessBeanFactory(bf);

        ExtendedREsourceInjectionBean bean = (ExtendedREsourceInjectionBean) bf.getBean("annotatedBean");
        Engine tb = bean.getTestBean6();
        assertNotNull(tb);


        ExtendedREsourceInjectionBean anotherBean = (ExtendedREsourceInjectionBean) bf.getBean("annotatedBean");
        assertNotSame(anotherBean, bean);
        assertNotSame(anotherBean.getTestBean6(), tb);

        String[] depBeans = bf.getDependenciesForBean("annotatedBean");
        assertEquals(1, depBeans.length);
        assertEquals("testBean4", depBeans[0]);
    }

    public static class ExtendedREsourceInjectionBean extends NonPublicResourceInjectionBean {}

    public static class NonPublicResourceInjectionBean<B> extends ResourceInjectionBean {

        @Resource(name = "testBean4", type = Avante.class)
        protected Car testBean3;

        private B testBean4;

        @Resource
        Engine testBean5;

        Engine testBean6;

        @Resource
        BeanFactory beanFactory;

        @Override
        @Resource
        public void setTestBean2(Avante testBean2) {
            System.out.println("NonPublicResourceInjectionBean setTestBean2 실행");
            super.setTestBean2(testBean2);
        }

        @Resource(name = "${tb}", type = Car.class)
        private void setTestBean4(B testBean4) {
            System.out.println("NonPublicResourceInjectionBean setTestBean4 실행");
            if (this.testBean4 != null) {
                throw new IllegalStateException("Already called");
            }
            this.testBean4 = testBean4;
        }

        public void setTestBean6(Engine testBean6) {
            System.out.println("NonPublicResourceInjectionBean setTestBean6 실행");
            if (this.testBean6 != null) {
                throw new IllegalStateException("Already called");
            }
            this.testBean6 = testBean6;
        }

        public Car getTestBean3() {
            return testBean3;
        }

        public B getTestBean4() {
            return testBean4;
        }

        public Engine getTestBean5() {
            return testBean5;
        }

        public Engine getTestBean6() {
            return testBean6;
        }

        @Override
        @PostConstruct
        protected void init2() {
            System.out.println("NonPublicResourceInjectionBean init2 초기화");
            if (testBean3 == null || this.testBean4 == null) {
                throw new IllegalStateException("Resources not injected");
            }
            super.init2();
        }

        @Override
        @PreDestroy
        protected void destroy2() {
            super.destroy2();
        }
    }

    public static class ResourceInjectionBean extends AnnotatedInitDestroyBean {

        public ResourceInjectionBean() {
            System.out.println("하위 빈 초기화");
        }

        public boolean init2Called = false;

        public boolean init3Called = false;

        public boolean destroy2Called = false;

        public boolean destroy3Called = false;

        @Resource
        Avante testBean;

        Avante testBean2;

        @PostConstruct
        protected void init2() {
            System.out.println("ResourceInjectionBean init2 빈 초기화");
            if (this.testBean == null || this.testBean2 == null) {
                throw new IllegalStateException("Resources not injected");
            }
            if (!this.initCalled) {
                throw new IllegalStateException("Superclass init method not called yet");
            }
            if (this.init2Called) {
                throw new IllegalStateException("Already called");
            }
            this.init2Called = true;
        }

        @PostConstruct
        private void init() {
            System.out.println("ResourceInjectionBean init 빈 초기화");
            if (this.init3Called) {
                throw new IllegalStateException("Already called");
            }
            this.init3Called = true;
        }

        @PreDestroy
        protected void destroy2() {
            System.out.println("ResourceInjectionBean destroy2 빈 제거");
            if (this.destroyCalled) {
                throw new IllegalStateException("Superclass destroy called too soon");
            }
            if (this.destroy2Called) {
                throw new IllegalStateException("Already called");
            }
            this.destroy2Called = true;
        }

        @PreDestroy
        private void destroy() {
            System.out.println("ResourceInjectionBean destroy 빈 제거");
            if (this.destroyCalled) {
                throw new IllegalStateException("Superclass destroy called too soon");
            }
            if (this.destroy3Called) {
                throw new IllegalStateException("Already called");
            }
            this.destroy3Called = true;
        }

        @Resource
        public void setTestBean2(Avante testBean2) {
            System.out.println("ResourceInjectionBean testBean2 주입");
            if (this.testBean2 != null) {
                throw new IllegalStateException("Already called");
            }
            this.testBean2 = testBean2;
        }

        public Avante getTestBean() {
            return testBean;
        }

        public Avante getTestBean2() {
            return testBean2;
        }
    }

    public static class AnnotatedInitDestroyBean {

        public AnnotatedInitDestroyBean() {
            System.out.println("AnnotatedInitDestroyBean 빈 초기화");
        }

        public boolean initCalled = false;

        public boolean destroyCalled = false;


        @PostConstruct
        private void init() {
            System.out.println("AnnotatedInitDestroyBean 빈 후 초기화");
            if (this.initCalled) {
                throw new IllegalStateException("Already called");
            }
            this.initCalled = true;
        }

        @PreDestroy
        private void destroy() {
            System.out.println("AnnotatedInitDestroyBean 빈 종료");
            if (this.destroyCalled) {
                throw new IllegalStateException("Already called");
            }
            this.destroyCalled = true;
        }

    }

    public static class InitDestroyBeanPostProcessor implements DestructionAwareBeanPostProcessor {

        // 빈이 초기화 되기 전 단계
        public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
            System.out.println("빈 초기화 전(Before) PostProcess 실행");
            if (bean instanceof AnnotatedInitDestroyBean) {
                assertFalse(((AnnotatedInitDestroyBean) bean).initCalled);
            }
            return bean;
        }


        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
            System.out.println("빈 초기화 후(After) PostProcess 실행");
            if (bean instanceof AnnotatedInitDestroyBean) {
                assertTrue(((AnnotatedInitDestroyBean) bean).initCalled);
            }
            return bean;
        }

        public void postProcessBeforeDestruction(Object bean, String beanName) throws BeansException {
            System.out.println("빈 종료 전 Post Process 실행");
            if (bean instanceof AnnotatedInitDestroyBean) {
                assertFalse(((AnnotatedInitDestroyBean) bean).destroyCalled);
            }
        }
    }

    private static class NullFactory {
        public static Object create() {
            return null;
        }
    }

    private static class NotNullFactory {
        public static Avante create() {
            return new Avante("아방이");
        }
    }


}
