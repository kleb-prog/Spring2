package ru.geekbrains.supershop.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.geekbrains.supershop.persistence.entities.Image;
import ru.geekbrains.supershop.persistence.entities.Product;
import ru.geekbrains.supershop.persistence.entities.ReviewImage;
import ru.geekbrains.supershop.persistence.repositories.ReviewImageRepository;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewImageService {

    @Value("${files.storepath.images}")
    private Path IMAGES_STORE_PATH;

    @Value("${files.storepath.icons}")
    private Path ICONS_STORE_PATH;

    private static String IMAGE_ON_APPROVING = "forApprove.png";
    public static String DEFAULT_IMAGE = "defaultImage.png";

    private final ReviewImageRepository reviewImageRepository;

    public BufferedImage loadReviewImage(String imageName, boolean isApproved) throws IOException {

        if (isApproved) {
            try {
                UrlResource resource = new UrlResource(IMAGES_STORE_PATH.resolve(imageName).normalize().toUri());
                if (resource.exists()) {
                    return ImageIO.read(resource.getFile());
                } else {
                    log.error("Image not found!");
                    throw new FileNotFoundException("File " + imageName + " not found!");
                }
            } catch (MalformedInputException | FileNotFoundException ex) {
                return null;
            }
        } else {
            try {
                UrlResource resource = new UrlResource(ICONS_STORE_PATH.resolve(IMAGE_ON_APPROVING).normalize().toUri());
                if (resource.exists()) {
                    return ImageIO.read(resource.getFile());
                } else {
                    log.error("Image not found!");
                    throw new FileNotFoundException("File " + imageName + " not found!");
                }
            } catch (MalformedInputException | FileNotFoundException ex) {
                return null;
            }
        }
    }

    @Transactional
    public ReviewImage uploadImage(MultipartFile image, String imageName) throws IOException {
        Path targetLocation = IMAGES_STORE_PATH.resolve(imageName);
        while (Files.exists(targetLocation)) {
            String extension = imageName.split("\\.")[1];
            imageName = UUID.randomUUID() + "." + image.getContentType();
            targetLocation = IMAGES_STORE_PATH.resolve(imageName);
        }
        Files.copy(image.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        log.info("File {} has been successfully uploaded!", imageName);
        return reviewImageRepository.save(new ReviewImage(imageName));
    }
}
