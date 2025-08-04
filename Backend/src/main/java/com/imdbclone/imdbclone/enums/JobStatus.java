package com.imdbclone.imdbclone.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum JobStatus implements DescriptiveEnum {
    NOT_STARTED("NS", "Not Started"),
    IN_PROGRESS("IP", "In Progress"),
    COMPLETED("CO", "Completed"),
    ERRORED("ER", "Error");

    private final String code;
    private final String label;

    private static final Map<String, JobStatus> BY_CODE = Arrays.stream(values())
            .collect(Collectors.toMap(js -> js.code.toUpperCase(), js -> js));

    JobStatus(String code, String label) {
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
    public static JobStatus fromCode(String code) {
        if (code == null) return null;
        JobStatus item = BY_CODE.get(code.toUpperCase());
        if (item == null) {
            throw new IllegalArgumentException("Invalid job status: " + code);
        }
        return item;
    }
}
