package com.macauslot.recruitment_ms.util;


import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


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
//            constructorList = getMatchedTypesConstructors(constructorList, list.get(0));
            throw new IllegalArgumentException("符合的构造方法超过1个 .");

        }
        if (constructorList.isEmpty()) {
            throw new IllegalArgumentException("找不到符合的构造方法");
        }

//        List<Object[]> subList = list;
        for (Constructor<T> constructor : constructorList) {
//        for (int i = constructorList.size() - 1; i >= 0; i--) {
//            Constructor<T> constructor = constructorList.get(i);

            Class<?>[] constructorParameterTypes = constructor.getParameterTypes();
//            debug分析用
//            System.err.println("本次构造器结构如下:");
//            for (Class<?> constructorParameterType : constructorParameterTypes) {
//                System.err.println(constructorParameterType.getName());
//            }

            try {
                for (Object[] objList : list) {
                	for(int i=0;i<constructorParameterTypes.length;i++) {
                		if(constructorParameterTypes[i].getName().compareTo("java.lang.String")==0) {
                			objList[i] = String.valueOf(objList[i]);
                			System.out.println(objList[i]);
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

        throw new IllegalArgumentException("遍历后,找不到符合的构造方法");

    }


    @SuppressWarnings("unchecked")
    private static <T> List<Constructor<T>> getMatchedLengthConstructors(int objLength, Class<T> clazz) {
        return Arrays.stream(clazz.getConstructors()).filter(x -> x.getParameterCount() == objLength).map(x -> (Constructor<T>) x).collect(Collectors.toList());


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

    private static <T> List<Constructor<T>> getMatchedTypesConstructors(List<Constructor<T>> constructorList, Object[] data) {

        List<Constructor<T>> subConstructorList = new ArrayList<>();

        b1:
        for (Constructor<T> constructor : constructorList) {

            Class<?>[] constructorParameterTypes = constructor.getParameterTypes();

            for (int i = 0, len = constructorParameterTypes.length; i < len; i++) {
                Object subData = data[i];

                if (subData != null) {  //构造器参数和db返回参数数目相同,而db返回null时,无法getClass来读取类型
                    Class<?> dbParameterType = subData.getClass();
                    if (constructorParameterTypes[i] != dbParameterType && !constructorParameterTypes[i].isAssignableFrom(dbParameterType)) {
//                        System.err.print("第 " + (i + 1) +
//                                " 个参数: constructorParameterType-->" + constructorParameterTypes[i].getName());
//                        System.err.println("    dbParameterType--> dbValue:[" + subData + "], " + dbParameterType.getName());
//                        System.err.println();
                        continue b1;
                    }
                }
            }
            subConstructorList.add(constructor);
        }
        return subConstructorList;
    }


}
