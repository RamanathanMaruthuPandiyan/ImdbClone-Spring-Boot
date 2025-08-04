package com.imdbclone.imdbclone.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
@Builder
@ToString
public class PersonDto {
    private UUID id;

    @NotBlank(message = "Name can't be a empty.")
    @NotNull(message = "Name can't be null.")
    private String name;

    @NotNull(message = "Date of Birth can't be null.")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dob;

    @NotBlank(message = "Gender can't be empty.")
    @NotNull(message = "Gender Can't be null.")
    private String gender;

    @NotBlank(message = "Bio can't be empty.")
    @NotNull(message="Bio can't be null")
    private String bio;
}
