package jun.spring.bean;

import jun.spring.model.IndexedCar;
import org.junit.Test;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.fail;

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

    @Test
    public void 프로퍼티_읽기_NULL_체크_테스트() {
        NoRead nr = new NoRead();
        BeanWrapper bw = new BeanWrapperImpl(nr);
        try {
            bw.isReadableProperty(null);
            fail("Can't inquire into readability of null property");
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void 프토퍼티_쓰기_NULL_체크_테스트() {
        NoRead nr = new NoRead();
        BeanWrapper bw = new BeanWrapperImpl();
        try {
            bw.isWritableProperty(null);
            fail("Can't inquire nto writablility of null  property")
        } catch (IllegalArgumentException ignore) {}
    }

    @Test
    public void 읽기_쓰기_프로퍼티_테스트() {
        BeanWrapper bw = new BeanWrapperImpl(IndexedCar.class);

        assertTrue(bw.isReadableProperty("array"));

    }

    private static class NoRead {
        public void setAge(int age) {

        }
    }

}
