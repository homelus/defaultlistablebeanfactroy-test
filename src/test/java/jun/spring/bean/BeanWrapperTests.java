package jun.spring.bean;

import org.junit.Test;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import static junit.framework.TestCase.assertFalse;

public class BeanWrapperTests {

    @Test
    public void 프로퍼티_읽기_테스트() {
        NoRead nr = new NoRead();
        BeanWrapper bw = new BeanWrapperImpl(nr);
        assertFalse(bw.isReadableProperty("age"));
    }

    @Test
    public void 프로퍼티_읽기_예외없이_무조건_FALSE_반환() {
        NoRead nr = new NoRead();
        BeanWrapper bw = new BeanWrapperImpl(nr);
        assertFalse(bw.isReadableProperty("xxxxx"));
    }

    private static class NoRead {
        public void setAge(int age) {

        }
    }

}
