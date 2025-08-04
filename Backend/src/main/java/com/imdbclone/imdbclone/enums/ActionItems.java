package com.imdbclone.imdbclone.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.*;
import java.util.stream.Collectors;

public enum ActionItems implements DescriptiveEnum {
    CREATE ("CR", "Create"),
    VIEW   ("VI", "View"),
    EDIT   ("ED", "Edit"),
    DELETE ("DE", "Delete"),
    IMPORT ("IM", "Import");

    private final String code;
    private final String label;

    /** Pre‑computed, case‑insensitive lookup table for fast fromCode(). */
    private static final Map<String, ActionItems> BY_CODE = Arrays.stream(values())
            .collect(Collectors.toMap(ai -> ai.code.toUpperCase(), ai -> ai));

    ActionItems(String code, String label) {
        this.code  = code;
        this.label = label;
    }

    // ──────────────────────────────
    // DescriptiveEnum implementation
    // ──────────────────────────────
    @Override
    @JsonValue          // serialises enum → "CR", "VI", ...
    public String getCode() {
        return code;
    }

    @Override
    public String getLabel() {
        return label;
    }

    // ──────────────────────────────
    // Reverse lookup (code → enum)
    // ──────────────────────────────
    @JsonCreator        // deserialises "CR" → ActionItems.CREATE
    public static ActionItems fromCode(String code) {
        if (code == null) return null;
        ActionItems item = BY_CODE.get(code.toUpperCase());
        if (item == null) {
            throw new IllegalArgumentException("Invalid action‑item." + code);
        }
        return item;
    }
}
