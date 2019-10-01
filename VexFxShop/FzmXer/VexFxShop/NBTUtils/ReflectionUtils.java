package FzmXer.VexFxShop.NBTUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class ReflectionUtils {
	 // Prevent accidental construction
    private ReflectionUtils() {
    }

    /**
     * get a method in a class
     *
     * @param clazz          class's object
     * @param methodName     method's name
     * @param parameterTypes the method's arguments
     * @return {@link Method}
     * @throws NoSuchMethodException If the method with the specified parameter types cannot be found
     */
    public static Method getMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) throws NoSuchMethodException {
        if (hasMethod(clazz, methodName, parameterTypes)) {
            return clazz.getDeclaredMethod(methodName, parameterTypes);
        }
        return null;
    }

    /**
     * get a method in a class
     *
     * @param classPath      class's path
     * @param methodName     method's name
     * @param parameterTypes the method's arguments
     * @return {@link Method}
     * @throws ClassNotFoundException If the class cannot be found
     * @throws NoSuchMethodException  If the method with the specified parameter types cannot be found
     * @see #getMethod(Class, String, Class[])
     */
    public static Method getMethod(String classPath, String methodName, Class<?>... parameterTypes) throws ClassNotFoundException, NoSuchMethodException {
        return getMethod(Class.forName(classPath), methodName, parameterTypes);
    }

    /**
     * Invoke a method
     *
     * @param method    the method object
     * @param object    the object will be invoke
     * @param arguments the method arguments
     * @return {@link Object}
     * @throws InvocationTargetException If the desired method cannot be invoked
     * @throws IllegalAccessException    If the desired method cannot be accessed due to certain circumstances
     */
    public static Object invokeMethod(Method method, Object object, Object... arguments) throws InvocationTargetException, IllegalAccessException {
        Object o;

        if (method.isAccessible()) {
            o = method.invoke(object, arguments);
        } else {
            method.setAccessible(true);
            o = method.invoke(object, arguments);
            method.setAccessible(false);
        }
        return o;
    }

    /**
     * Invoke a method
     *
     * @param methodName the method name
     * @param object     the object will be invoke
     * @param arguments  the method arguments
     * @return {@link Object}
     * @throws InvocationTargetException If the desired method cannot be invoked
     * @throws IllegalAccessException    If the desired method cannot be accessed due to certain circumstances
     */
    public static Object invokeMethod(String methodName, Object object, Object... arguments) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method;
        Class<?>[] classes = new Class[0];
        for (int i = 0; i < arguments.length; i++) {
            classes[i] = arguments[i].getClass();
        }

        method = getMethod(object.getClass(), methodName, classes);
        return invokeMethod(method, object, arguments);
    }


    /**
     * check a class has a specified constructor
     *
     * @param clazz          class's object
     * @param parameterTypes the constructor with the specified parameter types
     * @return return true if the constructor is exist
     */
    public static boolean hasConstructor(Class<?> clazz, Class<?>... parameterTypes) {
        boolean has;
        try {
            clazz.getDeclaredConstructor(parameterTypes);
            has = true;
        } catch (NoSuchMethodException e) {
            has = false;
        }
        return has;
    }

    /**
     * check a class has a specified constructor
     *
     * @param clazz  class's object
     * @param filter filter obj
     * @return return true if the constructor is exist
     */
    public static boolean hasConstructor(Class<?> clazz, ConstructorFilter filter) {
        boolean has = false;
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        for (Constructor<?> constructor : constructors) {
            if (filter.accept(constructor)) {
                has = true;
                break;
            }
        }
        return has;
    }

    /**
     * check a class has a specified constructor
     *
     * @param classPath      class's path
     * @param parameterTypes the constructor with the specified parameter types
     * @return return true if the constructor is exist
     * @see #hasField(Class, String)
     */
    public static boolean hasConstructor(String classPath, Class<?>... parameterTypes) throws ClassNotFoundException {
        return hasConstructor(Class.forName(classPath), parameterTypes);
    }


    /**
     * check a class has a specified method
     *
     * @param clazz          class's object
     * @param methodName     method's name
     * @param parameterTypes the method with the specified parameter types
     * @return return true if the method is exist
     */
    public static boolean hasMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
        boolean has;
        try {
            clazz.getDeclaredMethod(methodName, parameterTypes);
            has = true;
        } catch (NoSuchMethodException e) {
            has = false;
        }
        return has;
    }

    /**
     * check a class has a specified method
     *
     * @param clazz  class's object
     * @param filter filter obj
     * @return return true if the constructor is exist
     */
    public static boolean hasMethod(Class<?> clazz, MethodFilter filter) {
        boolean has = false;
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (filter.accept(method)) {
                has = true;
                break;
            }
        }
        return has;
    }

    /**
     * check a class has a specified method
     *
     * @param classPath      class's path
     * @param methodName     method's name
     * @param parameterTypes the method with the specified parameter types
     * @return return true if the constructor is exist
     * @see #hasMethod(Class, String, Class[])
     */
    public static boolean hasMethod(String classPath, String methodName, Class<?>... parameterTypes) throws ClassNotFoundException {
        return hasMethod(Class.forName(classPath), methodName, parameterTypes);
    }
}
