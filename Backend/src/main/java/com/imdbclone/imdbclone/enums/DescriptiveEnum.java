package com.imdbclone.imdbclone.enums;

import java.util.*;
public interface DescriptiveEnum {
    String getCode();
    String getLabel();

    default Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        map.put("code", getCode());
        map.put("label", getLabel());
        map.put("name", ((Enum<?>) this).name());
        return map;
    }

    static <T extends Enum<T> & DescriptiveEnum>
    List<Map<String, String>> getEnumList(Class<T> enumClass) {
        List<Map<String, String>> list = new ArrayList<>();
        for (T constant : enumClass.getEnumConstants()) {
            list.add(constant.toMap());
        }
        return list;
    }

    static <T extends Enum<T> & DescriptiveEnum>
    Map<String, Object> getEnumInfo(Class<T> enumClass) {

        Map<String, String> codeMap  = new LinkedHashMap<>(); //  "CR" -> "CREATE"
        Map<String, String> labelMap = new LinkedHashMap<>(); //  "CR" -> "Create"
        Map<String, Object> out      = new LinkedHashMap<>();

        for (T constant : enumClass.getEnumConstants()) {
            String code  = constant.getCode();
            String name  = ((Enum<?>) constant).name();
            String label = constant.getLabel();

            codeMap.put(code,  name);   // codes
            labelMap.put(code, label);  // labels
            out.put(name, code);        // reverse lookup  "CREATE" -> "CR"
        }

        out.put("code",  codeMap);
        out.put("label", labelMap);
        out.put("length", enumClass.getEnumConstants().length);
        return out;
    }
}
