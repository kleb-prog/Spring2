package ru.geekbrains.supershop.utilities;

import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

public class ImageValidator {

    public static boolean validate(MultipartFile image) {
        return Objects.equals(image.getContentType(), MediaType.IMAGE_JPEG_VALUE) || Objects.equals(image.getContentType(), MediaType.IMAGE_PNG_VALUE);
    }
}
