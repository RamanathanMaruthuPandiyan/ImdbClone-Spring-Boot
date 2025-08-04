package com.imdbclone.imdbclone.util;

import com.imdbclone.imdbclone.enums.*;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class EnumRegistry {
    private final Map<String, Class<? extends Enum<?>>> enumMap = new HashMap<>();

    public EnumRegistry() {
        register("gender", Gender.class);
        register("actionItems", ActionItems.class);
        register("jobNames", JobName.class);
        register("jobStatus", JobStatus.class);
    }

    public void register(String name, Class<? extends Enum<?>> enumClass) {
        if (!DescriptiveEnum.class.isAssignableFrom(enumClass)) {
            throw new IllegalArgumentException(enumClass.getName() + " must implement DescriptiveEnum");
        }
        enumMap.put(name.toLowerCase(), enumClass);
    }

    public Class<? extends Enum<?>> getEnum(String name) {
        return enumMap.get(name.toLowerCase());
    }

    public boolean contains(String name) {
        return enumMap.containsKey(name.toLowerCase());
    }
}
