package ru.geekbrains.supershop.utilities;

import org.springframework.web.multipart.MultipartFile;

public class ImageValidator {

    public static boolean validate(MultipartFile image) {
        String type = image.getContentType();
        return image.getContentType().equals("image/jpeg") || image.getContentType().equals("image/png");
    }
}
