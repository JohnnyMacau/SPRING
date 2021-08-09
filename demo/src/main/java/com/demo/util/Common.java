package com.demo.util;

import java.lang.reflect.Method;


public class Common {	
	
	
	public static Object invokeMethod(Object owner, String methodName, Object[] args) throws Exception {  
	   
    Class ownerClass = owner.getClass();  
  
    Class[] argsClass = new Class[args.length];  
  
    for (int i = 0, j = args.length; i < j; i++) {  
        argsClass[i] = args[i].getClass();  
    }  
 
     Method method = ownerClass.getMethod(methodName,argsClass);  
  
    return method.invoke(owner, args);  
	}
	

	

}
