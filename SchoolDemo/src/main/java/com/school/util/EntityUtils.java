package com.school.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.school.annotation.Sign;
import com.school.exception.EntityCastException;


/**
 * @author jim.deng
 */
public class EntityUtils {
    /*
     * 极度依赖于 VO类/VO类的构造器/db查出数据 三者的参数顺序一致 而且 参数长度一致 , 否则报错!
     *
     */
//    public static <T> List<T> castEntity(List<Object[]> list, Class<T> clazz) {
//        List<T> returnList = new ArrayList<>();
//        if (list.isEmpty()) {
//            return returnList;
//        }
//        try {
//            Class<?>[] parameterTypes = Arrays.stream(clazz.getDeclaredFields())
//                    .map(Field::getType)
//                    .toArray(Class[]::new);
//            Constructor<T> constructor = clazz.getConstructor(parameterTypes);
//            for (Object[] objList : list) {
//                returnList.add(constructor.newInstance(objList));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new EntityCastException("Object[]数据转化为VO类发生异常");
//        }
//        return returnList;
//    }


    /*
     * 非lambda写法,兼容非java8
     * 极度依赖于 VO类/VO类的构造器/db查出数据 三者的参数顺序一致 而且 参数长度一致 , 否则报错!
     *
     */
//    public static <T> List<T> castEntity(List<Object[]> list, Class<T> clazz) {
//        List<T> returnList = new ArrayList<>();
//        if (list.isEmpty()) {
//            return returnList;
//        }
//        try {
//
//            List<Class<?>> typeList = new ArrayList<>();
//            for (Field declaredField : clazz.getDeclaredFields()) {
//                typeList.add(declaredField.getType());
//            }
//
////            //初始化需要得到的数组
////            Class[] array = new Class[typeList.size()];
////            //使用for循环得到数组
////            for (int i = 0; i < typeList.size(); i++) {
////                array[i] = typeList.get(i);
////            }
//
//
//            Constructor<T> constructor = clazz.getConstructor( typeList.toArray(new Class[0]));
////            Constructor<T> constructor = clazz.getConstructor(Arrays.stream(clazz.getDeclaredFields())
////                    .map(Field::getType)
////                    .toArray(Class[]::new));
//            for (Object[] objList : list) {
//                returnList.add(constructor.newInstance(objList));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new EntityCastException("Object[]数据转化为VO类发生异常");
//        }
//        return returnList;
//    }


    public static <T> List<T> castEntity(List<Object[]> list, Class<T> clazz) {
        List<T> returnList = new ArrayList<>();
        if (list.isEmpty()) {
            return returnList;
        }
        List<Constructor<T>> constructorList = getMatchedLengthConstructors(list.get(0).length, clazz);

        if (constructorList.size() > 1) {
            throw new EntityCastException("符合的构造方法超过1个 .");

        }
        if (constructorList.isEmpty()) {
            throw new EntityCastException("找不到符合的构造方法");
        }

//        List<Object[]> subList = list;
        for (Constructor<T> constructor : constructorList) {
//        for (int i = constructorList.size() - 1; i >= 0; i--) {
//            Constructor<T> constructor = constructorList.get(i);

//            //debug分析用
            Class<?>[] constructorParameterTypes = constructor.getParameterTypes();
            System.err.println("本次构造器结构如下:");
            for (Class<?> constructorParameterType : constructorParameterTypes) {
                System.err.println(constructorParameterType.getName());
            }

            try {
                for (Object[] objList : list) {
                    for (int i = 0; i < constructorParameterTypes.length; i++) {
                        if (constructorParameterTypes[i].getName().compareTo("java.lang.String") == 0) {
                            if (objList[i] == null) {
                                objList[i] = "";
                            } else {
                                objList[i] = String.valueOf(objList[i]);
                            }
                        }
                        if (constructorParameterTypes[i].getName().compareTo("java.lang.Integer") == 0) {
                            if (objList[i] == null) objList[i] = 0;
                            else
                                objList[i] = Integer.valueOf(objList[i].toString());
                        }
                    }
                    returnList.add(constructor.newInstance(objList));
                }
                return returnList;
            } catch (Exception e) {
                System.err.println("构造器不符合,更换中..");
//                subList = list.subList(returnList.size(), list.size());//此方法参数左闭右开
                returnList = new ArrayList<>();
            }
        }

        throw new EntityCastException("遍历后,找不到符合的构造方法");

    }


    public static <T> List<T> castEntity(List<Object[]> list, Class<T> clazz, Class<?>[] parameterTypes) {
        List<T> returnList = new ArrayList<>();
        if (list.isEmpty()) {
            return returnList;
        }
        try {
            Constructor<T> constructor = clazz.getConstructor(parameterTypes);
            Class<?>[] constructorParameterTypes = constructor.getParameterTypes();

            for (Object[] objList : list) {
                for (int i = 0; i < objList.length; i++) {
                    if (objList[i] == null) {
                        if (constructorParameterTypes[i].getName().compareTo("java.lang.String") == 0) {
                            objList[i] = "";
                        }
                    }
                }

                returnList.add(constructor.newInstance(objList));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new EntityCastException("castEntity(List<Object[]> list, Class<T> clazz,Class<?>[] parameterTypes) error!");
        }
        return returnList;
    }


    @SuppressWarnings("unchecked")
    private static <T> List<Constructor<T>> getMatchedLengthConstructors(int objLength, Class<T> clazz) {
        List<Constructor<T>> constructorList = Arrays.stream(clazz.getConstructors()).filter(x -> x.getParameterCount() == objLength).map(x -> (Constructor<T>) x).collect(Collectors.toList());
        if (constructorList.isEmpty()) {
            throw new EntityCastException("cannot get any Matched Length Constructors. The length is " + objLength);
        }
        return constructorList;


//        List<Constructor<T>> constructorList = new ArrayList<>();
//        for (Constructor<?> constructor : clazz.getConstructors()) {
//            if (constructor.getParameterCount() == objLength) {
//                constructorList.add((Constructor<T>) constructor);
//            }
//        }
//        return constructorList;
    }

//    /*
//     * 兼容java7
//     * */
//    @SuppressWarnings("unchecked")
//    private static <T> List<Constructor<T>> getMatchedLengthConstructors(int objLength, Class<T> clazz) {
//        List<Constructor<T>> constructorList = new ArrayList<>();
//        for (Constructor<?> constructor : clazz.getConstructors()) {
//            Class<?>[] constructorParameterTypes = constructor.getParameterTypes();
//            int len = constructorParameterTypes.length;
//            if (len == objLength) {
//                constructorList.add((Constructor<T>) constructor);
//            }
//        }
//        return constructorList;
//    }

    private static <T> void debuggerMatchedTypes(Constructor<T> constructor, List<Object[]> list) {


        Class<?>[] constructorParameterTypes = constructor.getParameterTypes();
        Parameter[] parameters = constructor.getParameters();



        b1:
        for (int i = 0, len = constructorParameterTypes.length; i < len; i++) {

            for (int k = 0; k < list.size(); k++) {
                Object subData = list.get(k)[i];
                if (subData == null) {//构造器参数和db返回参数数目相同,而db返回null时,无法getClass来读取类型
                    System.err.println("[PASS]----第 " + (i + 1) +
                            " 个参数: constructorParameterType: [" +parameters[i].getName()+"] is null");
                    if (k == list.size() - 1) {
                        String s = "--------空數據已跳至末尾，無法檢查--------";
                        System.err.println(s);
                        break b1;
                    }
                    continue;
                }
                Class<?> dbParameterType = subData.getClass();
                if (constructorParameterTypes[i] == dbParameterType || constructorParameterTypes[i].isAssignableFrom(dbParameterType)) {
                    String s = "[SUCCESS]----第 " + (i + 1) +
                            " 个参数: constructorParameterType: [" +parameters[i].getName()+
                            "]-->" + constructorParameterTypes[i].getName()
                            + "    dbParameterType--> dbValue:[" + subData + "], " + dbParameterType.getName();
                    System.err.println(s);
                    continue b1;
                }
                String s = "[FAIL]----第 " + (i + 1) +
                        " 个参数: constructorParameterType: [" +parameters[i].getName()+
                        "]-->" + constructorParameterTypes[i].getName()
                        + "    dbParameterType--> dbValue:[" + subData + "], " + dbParameterType.getName();
                System.err.println(s);
                break b1;
            }
        }
    }


    public static <T> List<T> mapping(List<Object[]> list, Class<T> clazz, int sign) {
        if (list.isEmpty()) {
            return Collections.emptyList();
        }
        List<Constructor<T>> constructors = getMatchedLengthConstructors(list.get(0).length, clazz);

        for (Constructor<T> constructor : constructors) {
            if (constructor.isAnnotationPresent(Sign.class) && constructor.getAnnotation(Sign.class).num() == sign) {
                List<T> result = new ArrayList<>(list.size());
                try {
                    for (Object[] data : list) {
                        result.add(constructor.newInstance(data));
                    }
                    return result;
                } catch (Exception e) {
                    e.printStackTrace();
                    debuggerMatchedTypes(constructor, list);
                    throw new EntityCastException(e);
                }
            }
        }
        throw new EntityCastException("\"" + clazz.getName() + "\" constructor need use annotation \"" + Sign.class.getName() + "\"");
    }


    /**
     * 说明: 将JPA查询的List<Object[]>结果集封装到自定义对象中
     *
     * @param list  待处理数据
     * @param clazz 待映射对象类型，该对象的构造方法需要结合@Sign注解使用
     * @param sign  待映射对象构造器上的注解值，用于匹配不同的结果集映射
     * @param <T>   待映射对象泛型
     * @return 映射后的结果集
     */
    public static <T> List<T> mapping(List<Object[]> list, Class<T> clazz, String sign) {
        if (list.isEmpty()) {
            return Collections.emptyList();
        }
        List<Constructor<T>> constructors = getMatchedLengthConstructors(list.get(0).length, clazz);


        for (Constructor<T> constructor : constructors) {
            if (constructor.isAnnotationPresent(Sign.class) && constructor.getAnnotation(Sign.class).value().equals(sign)) {
                List<T> result = new ArrayList<>(list.size());
                try {
                    for (Object[] data : list) {
                        result.add(constructor.newInstance(data));
                    }
                    return result;
                } catch (Exception e) {
                    e.printStackTrace();
                    debuggerMatchedTypes(constructor, list);
                    throw new EntityCastException(e);
                }
            }
        }
           /* Constructor<?>[] constructors = clazz.getConstructors();
            for (Constructor<?> constructor : constructors) {
                if (constructor.isAnnotationPresent(Sign.class)) {
                    if (constructor.getAnnotation(Sign.class).strValue().equals(sign)) {
                        List<Object> result = new ArrayList<>(list.size());
                        try {
                            for (Object[] data : list) {
                                result.add(constructor.newInstance(data));
                            }
                            return (List<T>) result;
                        } catch (Exception e) {
                            e.printStackTrace();
                            throw new EntityCastException(e);
                        }
                    }
                }
            }*/
        throw new EntityCastException("\"" + clazz.getName() + "\" constructor need use annotation \"" + Sign.class.getName() + "\"");
    }


    /**
     * 说明: 将JPA查询的List<Object[]>结果集封装到自定义对象中
     * 为默认的注解值提供一个便捷的方法，不用传递注解参数0
     */
    public static <T> List<T> mapping(List<Object[]> list, Class<T> clazz) {
        return mapping(list, clazz, 0);
    }

}
