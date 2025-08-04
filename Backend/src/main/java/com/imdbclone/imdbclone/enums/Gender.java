package com.imdbclone.imdbclone.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.*;
import java.util.stream.Collectors;

public enum Gender implements DescriptiveEnum {
    MALE("M", "Male"),
    FEMALE("F", "Female"),
    OTHER("O", "Other");

    private final String code;
    private final String label;

    private static final Map<String, Gender> BY_CODE = Arrays.stream(values())
            .collect(Collectors.toMap(g -> g.code, g -> g));

    Gender(String code, String label) {
        this.code = code;
        this.label = label;
    }

    @Override
    @JsonValue
    public String getCode() {
        return code;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @JsonCreator
    public static Gender fromCode(String code) {
        if (code == null) return null;
        Gender gender = BY_CODE.get(code);
        if (gender == null) {
            throw new IllegalArgumentException("Invalid gender.");
        }
        return gender;
    }

}
