package net.simpleeventbus;

import net.simpleeventbus.event.Event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Very simple event bus
 * @param <BaseEventType> Base type for all events.
 */
public class EventBus<BaseEventType> {
    record MethodContainer(Object object, Method method) {}

    Map<Class<? extends BaseEventType>, List<MethodContainer>> events = new HashMap<>();
    Map<Class<? extends BaseEventType>, List<Class<?>>> eventTypeMap = new HashMap<>();
    Class<BaseEventType> clazz;

    EventBus(Class<BaseEventType> clazz) {
        this.clazz = clazz;
    }

    @SuppressWarnings("unchecked") // just to hide a compiler warning that is not of concern
    public void registerEventType(Class<? extends BaseEventType> type) {
        if (!events.containsKey(type)) events.put(type, new ArrayList<>());
        List<Class<?>> superclasses = Util.getSuperclasses(type, clazz);
        superclasses.add(type);
        eventTypeMap.put(type, superclasses);

        for (Class<?> clazz : superclasses) {
            if (!events.containsKey((Class<? extends BaseEventType>) clazz)) events.put((Class<? extends BaseEventType>) clazz, new ArrayList<>());
        }
    }

    void register(Class<?> clazz, Object obj) {
        Method[] methods = clazz.getMethods();

        for (Method method : methods) {
            if (method.isAnnotationPresent(Subscribe.class)) {
                Class<?>[] types = method.getParameterTypes();
                if (types.length != 1) throw new RuntimeException("Methods annotated by @Subscribe require at least one parameter.");

                Class<?> type = types[0];
                if (events.containsKey(type)) {
                    if (!Modifier.isStatic(method.getModifiers())) if (obj != null) events.get(type).add(new MethodContainer(obj, method));
                    else events.get(type).add(new MethodContainer(null, method));
                } else {
                    System.out.println("Skipping unregistered event type %s".formatted(type.toString()));
                }
            }
        }
    }

    public void register(Class<?> clazz) {
        register(clazz, null);
    }

    public void register(Object object) {
        register(object.getClass(), object);
    }


    public void post(BaseEventType event) {
        for (Class<?> clazz : eventTypeMap.get(event.getClass())) {
            for (MethodContainer container : events.get(clazz)) {
                try {
                    container.method.invoke(container.object, event);
                } catch (InvocationTargetException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @SuppressWarnings("unchecked") // just to hide a compiler warning that is not of concern
    public static EventBus<Event> createDefault() {
        return (EventBus<Event>) create(Event.class);
    }
    public static EventBus<?> create(Class<?> eventClass) {
        return new EventBus<>(eventClass);
    }
}
