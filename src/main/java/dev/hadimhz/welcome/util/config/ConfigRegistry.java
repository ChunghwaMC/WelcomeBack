package dev.hadimhz.welcome.util.config;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Optional;

public interface ConfigRegistry {

    <Type> Type register(Class<Type> clazz, Type instance, File file);

    void save(Object obj, File file);

    void save(Object obj);

    <Type> Optional<Type> load(Class<Type> clazz, File file);

    <Type> Optional<Type> load(Class<Type> clazz);

    default <Type> void reload(Class<Type> clazz, Object instance, File file) {
        load(clazz, file).ifPresent(config -> {
            for (Field field : config.getClass().getDeclaredFields()) {
                int mod = field.getModifiers();

                if (Modifier.isTransient(mod)) continue;
                if (Modifier.isFinal(mod)) continue;

                field.setAccessible(true);
                try {
                    field.set(instance, field.get(config));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    Collection<Object> getConfigs();
}
