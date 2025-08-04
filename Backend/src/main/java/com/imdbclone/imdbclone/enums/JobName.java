package com.imdbclone.imdbclone.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum JobName implements DescriptiveEnum {
    IMPORT_IMDB("IIMBD", "Import from IMBD");

    private final String code;
    private final String label;

    private static final Map<String, JobName> BY_CODE = Arrays.stream(values())
            .collect(Collectors.toMap(j -> j.code.toUpperCase(), j -> j));

    JobName(String code, String label) {
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
    public static JobName fromCode(String code) {
        if (code == null) return null;
        JobName item = BY_CODE.get(code.toUpperCase());
        if (item == null) {
            throw new IllegalArgumentException("Invalid job name: " + code);
        }
        return item;
    }
}
