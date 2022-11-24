package com.hh.urm.notify.utils;

import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.HashSet;

/**
 * @ClassName: ObjectCopyUtils
 * @Author: MaxWell
 * @Description: 对象copy
 * @Date: 2022/11/22 15:26
 * @Version: 1.0
 */
public class ObjectCopyUtils {
    public static String[] getNullProps(Object object) {
        BeanWrapperImpl beanWrapper = new BeanWrapperImpl(object);
        PropertyDescriptor[] propertyDescriptors = beanWrapper.getPropertyDescriptors();
        HashSet<String> nullPropNames = new HashSet<String>();
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            Object propertyValue = beanWrapper.getPropertyValue(propertyDescriptor.getName());
            if (propertyValue == null) {
                nullPropNames.add(propertyDescriptor.getName());
            }
        }
        String[] nullPropNameArr = new String[nullPropNames.size()];
        return nullPropNames.toArray(nullPropNameArr);
    }
}
