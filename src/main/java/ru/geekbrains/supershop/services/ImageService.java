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
import ru.geekbrains.supershop.persistence.repositories.ImageRepository;
import ru.geekbrains.supershop.utilities.UUIDValidator;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;

import java.io.FileNotFoundException;
import java.io.IOException;

import java.nio.charset.MalformedInputException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService {

    @Value("${files.storepath.images}")
    private Path IMAGES_STORE_PATH;

    @Value("${files.storepath.icons}")
    private Path ICONS_STORE_PATH;

    private static final String CART_ICON = "cart.png";

    private final ImageRepository imageRepository;
    private final UUIDValidator validator;

    private String getImageForSpecificProduct(UUID id) {
        return imageRepository.obtainImageNameByProductId(id);
    }

    public BufferedImage loadFileAsResource(String id) throws IOException {
        String imageName = null;

        try {
            Path filePath;

            if (validator.validate(id)) {

                imageName = getImageForSpecificProduct(UUID.fromString(id));

                if (imageName != null) {
                    filePath = IMAGES_STORE_PATH.resolve(imageName).normalize();
                } else {
                    imageName = "image_not_found.png";
                    filePath = ICONS_STORE_PATH.resolve(imageName).normalize();
                }
            } else {
                filePath = ICONS_STORE_PATH.resolve("cart.png").normalize();
            }

            if (filePath != null) {
                return ImageIO.read(new UrlResource(filePath.toUri()).getFile());
            } else {
                throw new IOException();
            }

        } catch (IOException ex) {
            log.error("Error! Image {} file wasn't found!", imageName);
            return null;
        }
    }

    public List<Image> findImages() {
        return imageRepository.findAll();
    }

    public BufferedImage loadImageByName(String imageName) throws IOException {
        try {
            if (imageName.equals(CART_ICON)) {
                return ImageIO.read(new UrlResource(ICONS_STORE_PATH.resolve(imageName).normalize().toUri()).getFile());
            }
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
    }

    @Transactional
    public Image uploadImage(MultipartFile image, String imageName, Product product) throws IOException {
        Path targetLocation = IMAGES_STORE_PATH.resolve(imageName);
        while (Files.exists(targetLocation)) {
            String extension = imageName.split("\\.")[1];
            imageName = UUID.randomUUID() + "." + extension;
            targetLocation = IMAGES_STORE_PATH.resolve(imageName);
        }
        Files.copy(image.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        log.info("File {} has been successfully uploaded!", imageName);
        return imageRepository.save(new Image(product.getImage(), product, imageName));
    }

    public List<Image> getImagesByProduct(UUID imageProd) {
        return imageRepository.findAllByIdImage(imageProd);
    }

}