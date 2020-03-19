package ru.geekbrains.supershop.controllers;

import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import ru.geekbrains.supershop.exceptions.ProductNotFoundException;
import ru.geekbrains.supershop.persistence.entities.Product;
import ru.geekbrains.supershop.services.ImageService;
import ru.geekbrains.supershop.services.ProductService;
import ru.geekbrains.supershop.utilities.UUIDValidator;

import javax.imageio.ImageIO;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ImageService imageService;
    private final ProductService productService;
    private final UUIDValidator validator;

    @GetMapping("/{id}")
    public String getOneProduct(Model model, @PathVariable String id) throws ProductNotFoundException {
        if (validator.validate(id)) {
            Product product = productService.findOneById(UUID.fromString(id));
            model.addAttribute("product", product);
            return "product";
        } else {
            throw new RuntimeException();
        }
    }

    @GetMapping(value = "/images/{name}", produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody byte[] getImageByName(@PathVariable String name) {

        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(imageService.loadImageByName(name), "png", byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

}