package jun.spring.factory;

import com.sun.org.apache.bcel.internal.generic.DDIV;
import jun.spring.model.Avante;
import jun.spring.model.Company;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.AutowireCandidateQualifier;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Spring 의 테스트 코드
 * org.springframework.beans.factory.support
 * @QualifierAnnotationAutowireBeanFactoryTests
 */
public class BeanFactoryAutowireTests {

    DefaultListableBeanFactory factory;

    private static String JUN = "jun";

    private static String MIN = "min";

    @Before
    public void setUp() {
        factory = new DefaultListableBeanFactory();
    }

    @Test
    public void DESCRIPTOR_관련없는_DEFAULT_자동와이어링_후보자_테스트() throws NoSuchFieldException {
        ConstructorArgumentValues cavs = new ConstructorArgumentValues();
        cavs.addGenericArgumentValue(JUN);
        RootBeanDefinition rbd = new RootBeanDefinition(Person.class, cavs, null);
        factory.registerBeanDefinition(JUN, rbd);
        assertTrue(factory.isAutowireCandidate(JUN, null));
        assertTrue(factory.isAutowireCandidate(JUN, new DependencyDescriptor(Person.class.getDeclaredField("name"), false)));
        assertTrue(factory.isAutowireCandidate(JUN, new DependencyDescriptor(Person.class.getDeclaredField("name"), true)));
        Person person = factory.getBean(JUN, Person.class);
        System.out.println(person.getName());
    }

    @Test
    public void 필드_DESCRIPTOR_자동와이어링_테스트() throws NoSuchFieldException {
        ConstructorArgumentValues cavs = new ConstructorArgumentValues();
        cavs.addGenericArgumentValue(JUN);
        RootBeanDefinition person1 = new RootBeanDefinition(Person.class, cavs, null);
        person1.addQualifier(new AutowireCandidateQualifier(TestQualifier.class));
        factory.registerBeanDefinition(JUN, person1);

        ConstructorArgumentValues cavs2 = new ConstructorArgumentValues();
        cavs.addGenericArgumentValue(MIN);
        RootBeanDefinition person2 = new RootBeanDefinition(Person.class, cavs2, null);
        factory.registerBeanDefinition(MIN, person2);

        DependencyDescriptor qualifiedDescriptor = new DependencyDescriptor(QualifiedTestBean.class.getDeclaredField("qualified"), false);
        DependencyDescriptor nonqualifiedDescriptor = new DependencyDescriptor(QualifiedTestBean.class.getDeclaredField("nonqualified"), false);

        assertTrue(factory.isAutowireCandidate(JUN, null));
        assertTrue(factory.isAutowireCandidate(JUN,  nonqualifiedDescriptor));
        assertTrue(factory.isAutowireCandidate(JUN,  qualifiedDescriptor));
        assertTrue(factory.isAutowireCandidate(MIN, null));
        assertTrue(factory.isAutowireCandidate(MIN, nonqualifiedDescriptor));
        assertFalse(factory.isAutowireCandidate(MIN, qualifiedDescriptor));
    }

    @Test
    public void 이름에의한_자동_빈_주입() {
        DefaultListableBeanFactory dlbf = new DefaultListableBeanFactory();
        RootBeanDefinition bd = new RootBeanDefinition(Avante.class);
        dlbf.registerBeanDefinition("avante", bd);
        Company company = (Company) dlbf.autowire(Company.class, AutowireCapableBeanFactory.AUTOWIRE_BY_NAME, true);
        Avante avante = (Avante) dlbf.getBean("avante");
        assertEquals(avante, company.getAvante());
        assertSame(BeanFactoryUtils.beanOfType(dlbf, Avante.class), avante);

    }

    private static class QualifiedTestBean {

        @TestQualifier
        public Person qualified;

        private Person nonqualified;

        public QualifiedTestBean(@TestQualifier Person tpb) {}

        public void autowireQualified(@TestQualifier Person tpb) {}

        public void auatowireNonQualified(Person tpb) {}
    }

    private static class Person {
        private String name;

        public Person(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    @Target({ElementType.FIELD, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    @Qualifier
    private static @interface TestQualifier {}


}
