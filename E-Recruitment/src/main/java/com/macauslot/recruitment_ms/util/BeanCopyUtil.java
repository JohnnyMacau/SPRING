package com.macauslot.recruitment_ms.util;


import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


public class BeanCopyUtil {
    //空属性不复制
    public static <T> void beanCopyExceptNull(T source, T target) {
        if (source == null) {
            return;
        }
        BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
    }

    //空和指定属性不复制
    public static <T> void beanCopyExceptNullWithIngore(T source, T target, String... ignoreProperties) {
        if (source == null) {
            return;
        }
        BeanUtils.copyProperties(source, target, getNullAndIgnorePropertyNames(source, ignoreProperties));
    }

    //空和指定属性不复制, 但空属于被强调的时候复制
    public static <T> void beanCopyExceptNullWithIngoreAndNotice(T source, T target, String[] ignoreProperties, String... noticeProperties) {
        if (source == null) {
            return;
        }
        BeanUtils.copyProperties(source, target, getNullAndIgnorePropertyNames(source, ignoreProperties, noticeProperties));
    }

    //指定属性不复制, 空会复制
    public static <T> void beanCopyWithNullAndIgnore(T source, T target, String... ignoreProperties) {
        if (source == null) {
            return;
        }
        BeanUtils.copyProperties(source, target, getIgnorePropertyNames(source, ignoreProperties));
    }


    private static String[] getNullAndIgnorePropertyNames(Object source, String[] ignoreProperties, String... noticeProperties) {
        Set<String> emptyNames = getNullAndExceptNoticePropertyNameSet(source, noticeProperties);
        Collections.addAll(emptyNames, ignoreProperties);
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    private static String[] getNullAndIgnorePropertyNames(Object source, String... ignoreProperties) {
        Set<String> emptyNames = getNullPropertyNameSet(source);
        Collections.addAll(emptyNames, ignoreProperties);
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    private static String[] getIgnorePropertyNames(Object source, String... ignoreProperties) {
        Set<String> emptyNames = new HashSet<>();
        Collections.addAll(emptyNames, ignoreProperties);
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    private static String[] getNullPropertyNames(Object source) {
        Set<String> emptyNames = getNullPropertyNameSet(source);
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    private static Set<String> getNullPropertyNameSet(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();
        Set<String> emptyNames = new HashSet<>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            //所有 为空的 属性值 均不更新到target
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }
        return emptyNames;
    }

    private static Set<String> getNullAndExceptNoticePropertyNameSet(Object source, String... noticeProperties) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();
        Set<String> emptyNames = new HashSet<>();
        for (java.beans.PropertyDescriptor pd : pds) {
            String pdName = pd.getName();
//            for (String noticeProperty : noticeProperties) {
//                //所有 为空的 且 非notice的 属性值 均不更新到target
//                if (!pdName.equals(noticeProperty) && src.getPropertyValue(pdName) == null) {
//                    emptyNames.add(pdName);
//                    break;
//                }
//            }
            //所有 为空的 且 非notice的 属性值 均不更新到target
            if (!Arrays.asList(noticeProperties).contains(pdName) && src.getPropertyValue(pdName) == null) {
                emptyNames.add(pd.getName());
            }


        }
        return emptyNames;
    }


}

