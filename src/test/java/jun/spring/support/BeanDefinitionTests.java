package jun.spring.support;

import jun.spring.model.Avante;
import org.junit.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.ChildBeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BeanDefinitionTests {

    /**
     * 빈 정의의 동일성은 내부 상태값들로 결정된다.
     */
    @Test
    public void 빈_정의_동일성_테스트() {
        RootBeanDefinition bd = new RootBeanDefinition(Avante.class);
        bd.setAbstract(true);
        bd.setLazyInit(true);
        bd.setScope("request");

        RootBeanDefinition otherBd = new RootBeanDefinition(Avante.class);

        assertTrue(!bd.equals(otherBd));
        assertTrue((!otherBd.equals(bd)));

        otherBd.setAbstract(true);
        otherBd.setLazyInit(true);
        otherBd.setScope("request");

        assertEquals(bd, otherBd);
        assertEquals(otherBd, bd);
        assertEquals(bd.hashCode(), otherBd.hashCode());
    }

    /**
     * 빈 정의의 동일성은 내부 상태값들로 결정된다. 내부 상태값에는 중 속성들의 목록도 들어간다.
     */
    @Test
    public void 속성포함_빈_정의_동일성_테스트() {
        RootBeanDefinition bd = new RootBeanDefinition(Avante.class);
        bd.getPropertyValues().add("name", "아방이");
        bd.getPropertyValues().add("wheel", "알루미늄");

        RootBeanDefinition otherBd = new RootBeanDefinition(Avante.class);
        otherBd.getPropertyValues().add("name", "아방이");

        assertTrue(!bd.equals(otherBd));
        assertTrue(!otherBd.equals(bd));

        otherBd.getPropertyValues().add("wheel", "금");

        assertTrue(!bd.equals(otherBd));
        assertTrue(!otherBd.equals(bd));

        otherBd.getPropertyValues().add("wheel", "알루미늄");

        assertEquals(bd, otherBd);
        assertEquals(otherBd, bd);
        assertEquals(bd.hashCode(), otherBd.hashCode());
    }

    @Test
    public void 타입_생성자_빈_정의_동일성_테스트() {
        RootBeanDefinition bd = new RootBeanDefinition(Avante.class);
        bd.getConstructorArgumentValues().addGenericArgumentValue("test", "int");
        bd.getConstructorArgumentValues().addIndexedArgumentValue(1, new Integer(5), "long");

        RootBeanDefinition otherBd = new RootBeanDefinition(Avante.class);
        otherBd.getConstructorArgumentValues().addGenericArgumentValue("test", "int");
        otherBd.getConstructorArgumentValues().addIndexedArgumentValue(1, new Integer(5));

        assertTrue(!bd.equals(otherBd));
        assertTrue(!otherBd.equals(bd));

        otherBd.getConstructorArgumentValues().addIndexedArgumentValue(1, new Integer(5), "int");

        assertTrue(!bd.equals(otherBd));
        assertTrue(!otherBd.equals(bd));

        otherBd.getConstructorArgumentValues().addIndexedArgumentValue(1, new Integer(5), "long");

        assertEquals(bd, otherBd);
        assertEquals(otherBd, bd);
        assertEquals(bd.hashCode(), otherBd.hashCode());
    }

    @Test
    public void 빈_정의_합치기_테스트() {
        RootBeanDefinition bd = new RootBeanDefinition(Avante.class);
        bd.getConstructorArgumentValues().addGenericArgumentValue("test");
        bd.getConstructorArgumentValues().addIndexedArgumentValue(1, new Integer(5));
        bd.getPropertyValues().add("name", "아방이");
        bd.getPropertyValues().add("wheel", "금");

        BeanDefinition childBd = new ChildBeanDefinition("bd");
        RootBeanDefinition mergedBd = new RootBeanDefinition(bd);
        mergedBd.overrideFrom(childBd);
        assertEquals(2, mergedBd.getConstructorArgumentValues().getArgumentCount());
        assertEquals(2, mergedBd.getPropertyValues().size());
        assertEquals(bd, mergedBd);

        mergedBd.getConstructorArgumentValues().getArgumentValue(1, null).setValue(new Integer(9));
        assertEquals(new Integer(5), bd.getConstructorArgumentValues().getArgumentValue(1, null).getValue());
    }
}
