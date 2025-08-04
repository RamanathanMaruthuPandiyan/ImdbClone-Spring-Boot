package com.imdbclone.imdbclone.controller;

import com.imdbclone.imdbclone.enums.DescriptiveEnum;
import com.imdbclone.imdbclone.util.EnumRegistry;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/enums")
public class EnumController {

    private final EnumRegistry enumRegistry;

    @GetMapping("/{enumName}")
    public Map<String, Object> getEnumValues(@PathVariable String enumName) {
        Class<?> raw = enumRegistry.getEnum(enumName.toLowerCase());
        if (raw == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Enum not found: " + enumName);
        }
        if (!DescriptiveEnum.class.isAssignableFrom(raw)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, raw.getSimpleName() + " is not a DescriptiveEnum");
        }

        @SuppressWarnings("unchecked")
        Class<? extends Enum<?>> enumClass = (Class<? extends Enum<?>>) raw;

        return Map.of(
                enumName,
                DescriptiveEnum.getEnumInfo((Class) enumClass)
        );
    }
}

