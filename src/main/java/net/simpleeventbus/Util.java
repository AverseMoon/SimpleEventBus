package net.simpleeventbus;

import java.util.ArrayList;
import java.util.List;

public class Util {
    public static List<Class<?>> getSuperclasses(Class<?> clazz, Class<?> upTo) {
        List<Class<?>> classList = new ArrayList<>();
        Class<?> sc = clazz;

        while ((sc = sc.getSuperclass()) != null && sc != upTo.getSuperclass()) classList.add(sc);

        return classList;
    }
    public static List<Class<?>> getSuperclasses(Class<?> clazz) {
        List<Class<?>> classList = new ArrayList<>();
        Class<?> sc;

        while ((sc = clazz.getSuperclass()) != null) classList.add(sc);

        return classList;
    }
}
