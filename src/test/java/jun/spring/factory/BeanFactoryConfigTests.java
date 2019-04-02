package jun.spring.factory;

import jun.spring.model.Avante;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.PropertyOverrideConfigurer;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import java.util.Properties;

import static org.junit.Assert.assertEquals;

/**
 * BeanFactoryPostProcessor 는 BeanFactory 를 받아와 데이터를 처리한다.
 * BeanPostProcessor 는 BeanFactory 에게 이용당하지만 BeanFactoryPostProcessor 는 BeanFactory 를 이용한다.
 */
public class BeanFactoryConfigTests {

    private DefaultListableBeanFactory factory;

    @Before
    public void init() {
        factory = new DefaultListableBeanFactory();
    }

    @Test
    public void 프로퍼티로_빈_주입_테스트() {
        BeanDefinition avanteDef1 = BeanDefinitionBuilder.genericBeanDefinition(Avante.class)
                .getBeanDefinition();

        BeanDefinition avanteDef2 = BeanDefinitionBuilder.genericBeanDefinition(Avante.class)
                .getBeanDefinition();

        factory.registerBeanDefinition("avante1", avanteDef1);
        factory.registerBeanDefinition("avante2", avanteDef2);

        PropertyOverrideConfigurer poc1 = new PropertyOverrideConfigurer();
        Properties props1 = new Properties();
        props1.setProperty("avante1.name", "아방이");
        props1.setProperty("avante1.wheel", "알루미늄");
        poc1.setProperties(props1);

        PropertyOverrideConfigurer poc2 = new PropertyOverrideConfigurer();
        Properties props2 = new Properties();
        props2.setProperty("avante2.name", "아방이2");
        props2.setProperty("avante2.wheel", "금");
        poc2.setProperties(props2);

        // BeanFactory 에서 Bean 을 검색한 후 Property 를 넣어준다.
        poc1.postProcessBeanFactory(factory);
        poc2.postProcessBeanFactory(factory);

        Avante avante1 = (Avante) factory.getBean("avante1");
        Avante avante2 = (Avante) factory.getBean("avante2");

        assertEquals(avante1.getName(), "아방이");
        assertEquals(avante1.getWheel(), "알루미늄");
        assertEquals(avante2.getName(), "아방이2");
        assertEquals(avante2.getWheel(), "금");
    }

}
