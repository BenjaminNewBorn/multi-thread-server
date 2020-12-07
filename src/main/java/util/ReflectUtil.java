package util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class ReflectUtil {
    public static boolean annotatedWith(Class<?> clazz, Class<? extends Annotation> annotation) {
        return clazz.getDeclaredAnnotation(annotation) != null;
    }

    public static boolean annotatedWith(Method method, Class<? extends Annotation> annotation) {
        return method.getDeclaredAnnotation(annotation) != null;
    }

    public static boolean annotatedWith(Parameter parameter, Class<? extends Annotation> annotation) {
        return parameter.getAnnotation(annotation) != null;
    }

}