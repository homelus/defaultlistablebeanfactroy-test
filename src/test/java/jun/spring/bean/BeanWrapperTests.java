package jun.spring.bean;

import jun.spring.model.IndexedCar;
import org.junit.Test;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.*;

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
            fail("Can't inquire nto writablility of null  property");
        } catch (IllegalArgumentException ignore) {}
    }

    @Test
    public void 읽기_쓰기_프로퍼티_테스트() {
        BeanWrapper bw = new BeanWrapperImpl(IndexedCar.class);

        assertTrue(bw.isReadableProperty("array"));
        assertTrue(bw.isReadableProperty("list"));
        assertTrue(bw.isReadableProperty("set"));
        assertTrue(bw.isReadableProperty("map"));
        assertFalse(bw.isReadableProperty("xxx"));

        assertTrue(bw.isWritableProperty("array"));
        assertTrue(bw.isWritableProperty("list"));
        assertTrue(bw.isWritableProperty("set"));
        assertTrue(bw.isWritableProperty("map"));
        assertFalse(bw.isWritableProperty("xxx"));

        assertTrue(bw.isReadableProperty("array[0]"));
        assertTrue(bw.isReadableProperty("array[0].name"));
        assertTrue(bw.isReadableProperty("list[0]"));
        assertTrue(bw.isReadableProperty("list[0].name"));
        assertTrue(bw.isReadableProperty("set[0]"));
        assertTrue(bw.isReadableProperty("set[0].name"));
        assertTrue(bw.isReadableProperty("map[key1]"));
        assertTrue(bw.isReadableProperty("map[key1].name"));
        assertTrue(bw.isReadableProperty("map[key4][0]"));
        assertTrue(bw.isReadableProperty("map[key4][0].name"));
        assertTrue(bw.isReadableProperty("map[key4][1]"));
        assertTrue(bw.isReadableProperty("map[key4][1].name"));
        assertFalse(bw.isReadableProperty("array[key1]"));

        assertTrue(bw.isWritableProperty("array[0]"));
        assertTrue(bw.isWritableProperty("array[0].name"));
        assertTrue(bw.isWritableProperty("list[0]"));
        assertTrue(bw.isWritableProperty("list[0].name"));
        assertTrue(bw.isWritableProperty("set[0]"));
        assertTrue(bw.isWritableProperty("set[0].name"));
        assertTrue(bw.isWritableProperty("map[key1]"));
        assertTrue(bw.isWritableProperty("map[key1].name"));
        assertTrue(bw.isWritableProperty("map[key4][0]"));
        assertTrue(bw.isWritableProperty("map[key4][0].name"));
        assertTrue(bw.isWritableProperty("map[key4][1]"));
        assertTrue(bw.isWritableProperty("map[key4][1].name"));
        assertFalse(bw.isWritableProperty("array[key1]"));
    }

    @Test
    public void 프로퍼티에_타입_SET_GET_테스트() {
        BeanWrapper bw = new BeanWrapperImpl(IndexedCar.class);
        assertEquals(null, bw.getPropertyType("map[key0]"));

        bw = new BeanWrapperImpl(IndexedCar.class);
        bw.setPropertyValue("map[key0]", "my String");
        assertEquals(String.class, bw.getPropertyType("map[key0]"));

        bw = new BeanWrapperImpl(IndexedCar.class);
        bw.registerCustomEditor(String.class, "map[key0]",  new StringTrimmerEditor(false));
        assertEquals(String.class, bw.getPropertyType("map[key0]"));
    }

    @Test
    public void 프로퍼티_가져오는_테스트() {

    }

    private static class NoRead {
        public void setAge(int age) {

        }
    }

}
