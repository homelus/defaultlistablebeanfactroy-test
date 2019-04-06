package jun.spring.bean;

import com.sun.javaws.jnl.PropertyDesc;
import jun.spring.model.Avante;
import lombok.Data;
import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.propertyeditors.CustomDateEditor;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class BeanUtilTests {

    @Test
    public void 클래스_생성_테스트() {
        BeanUtils.instantiate(ArrayList.class);

        try {
            // 인터페이스 사용 불가
            BeanUtils.instantiateClass(List.class);
        } catch (FatalBeanException ignore) {}

        try {
            // 파라미터가 없는 기본 생성자 사용 불가
            BeanUtils.instantiateClass(CustomDateEditor.class);
        } catch (FatalBeanException ignore) {}
    }

    @Test
    public void PROPERTY_DESCRIPTION_조회테스트() throws IntrospectionException {
        PropertyDescriptor[] actual = Introspector.getBeanInfo(Avante.class).getPropertyDescriptors();
        PropertyDescriptor[] descriptors = BeanUtils.getPropertyDescriptors(Avante.class);
        assertNotNull("Descriptors should not be null", descriptors);
        assertEquals("Invalid  number of descriptors returned", actual.length, descriptors.length);
    }

    @Test
    public void BEAN_PROPERTY_배열_테스트() {
        PropertyDescriptor[] descriptors = BeanUtils.getPropertyDescriptors(ContainerBean.class);
        for (PropertyDescriptor descriptor : descriptors) {
            if ("containedBeans".equals(descriptor.getName())) {
                assertTrue("Property should be an array", descriptor.getPropertyType().isArray());
                assertEquals(descriptor.getPropertyType().getComponentType(), ContainedBean.class);
            }
        }
    }


    @Data
    private static class ContainerBean {
        private ContainedBean[] containedBeans;
    }

    @Data
    private static class ContainedBean {
        private String name;
    }



}
