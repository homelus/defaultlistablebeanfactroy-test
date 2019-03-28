package jun.spring.etc;

import jun.spring.model.Avante;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class InstantiationTests {

    @Test
    public void 생성자_빈_생성_테스트() {
        try {
            Constructor<Avante> avanteClazz = Avante.class.getConstructor((Class[]) null);
            Avante avante = avanteClazz.newInstance();
            assertNotNull(avante);
        } catch (NoSuchMethodException e) {
            fail();
        } catch (IllegalAccessException e) {
            fail();
        } catch (InstantiationException e) {
            fail();
        } catch (InvocationTargetException e) {
            fail();
        }
    }

}
