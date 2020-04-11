package ru.geekbrains.supershop.utilities;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class UUIDValidator {

    public boolean validate(String uuid) {
        Pattern pattern = Pattern.compile("^[\\da-f]{8}-([\\da-f]{4}-){3}[\\da-f]{12}$", Pattern.CASE_INSENSITIVE);
        return pattern.matcher(uuid).matches();
    }
}
