package ru.geekbrains.supershop.controllers;

import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ru.geekbrains.supershop.services.ImageService;
import ru.geekbrains.supershop.services.ProductService;

@Controller
@RequiredArgsConstructor
public class ShopController {

    private final ProductService productService;
//это не JSON
    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public String index(Model model, @RequestParam(required = false) Integer category) {

        model.addAttribute("products", productService.findAll(category));
        return "index";
    }

    @GetMapping(value = "/available", produces = MediaType.TEXT_HTML_VALUE)
    public String indexAvailable(Model model, @RequestParam(required = false) Boolean available) {
        model.addAttribute("products", productService.findByAvailability(available));
        return "index";
    }
}