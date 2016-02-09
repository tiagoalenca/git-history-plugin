package br.uece.ees.githistoryplugin.helper;

import java.lang.reflect.Method;

public class ReflectionHelper {
	
	public Class<?> getPropertyType(Class<?> clazz, String name) {
		Method method = getPropertyMethodRead(clazz, name);
		return method.getReturnType();
	}
	
	public Method getPropertyMethodRead(Class<?> clazz, String name) {
		try {
			return clazz.getMethod("get" + name);
		} catch (NoSuchMethodException e) {
			try {
				return clazz.getMethod("is" + name);
			} catch (Exception e1) {
				throw new RuntimeException(e1);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}

	public Method getPropertyMethodWrite(Class<?> clazz, String name) {
		try {
			return clazz.getMethod("set" + name);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getPropertyValue(Object instance, String name) {
		Method method = getPropertyMethodRead(instance.getClass(), name);
		try {
			return (T) method.invoke(instance);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}
	
}
