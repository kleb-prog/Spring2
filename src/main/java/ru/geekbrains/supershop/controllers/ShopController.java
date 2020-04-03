package ru.geekbrains.supershop.controllers;

import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import ru.geekbrains.paymentservice.Payment;
import ru.geekbrains.supershop.beans.Cart;
import ru.geekbrains.supershop.persistence.entities.Shopuser;
import ru.geekbrains.supershop.services.ProductService;
import ru.geekbrains.supershop.services.ReviewService;
import ru.geekbrains.supershop.services.ShopuserService;
import ru.geekbrains.supershop.utilities.Validators;
import ru.geekbrains.supershop.utils.CaptchaGenerator;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;

@Controller
@RequiredArgsConstructor
public class ShopController {

    private final Cart cart;
    private final ProductService productService;
    private final ShopuserService shopuserService;
    private final CaptchaGenerator captchaGenerator;

    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public String index(Model model, @RequestParam(required = false) Integer category) {
		model.addAttribute("cart", cart.getCartRecords());
        model.addAttribute("products", productService.findAll(category));
        return "index";
    }

    @GetMapping(value = "/available", produces = MediaType.TEXT_HTML_VALUE)
    public String indexAvailable(Model model, @RequestParam(required = false) Boolean available) {
        model.addAttribute("products", productService.findByAvailability(available));
        return "index";
    }
	
	@GetMapping("/admin")
    public String adminPage(Model model, @CookieValue(value = "data", required = false) String data, Principal principal) {

        if (principal == null) {
            return "redirect:/";
        }

        if (data != null) {
            System.out.println(data);
        }

        model.addAttribute("products", productService.findAll(null));

        return "admin";
    }

    @GetMapping("/profile")
    public String profilePage(Model model, @CookieValue(value = "data", required = false) String data, Principal principal) {

        if (principal == null) {
            return "redirect:/";
        }

        model.addAttribute("shopuser", shopuserService.findByPhone(principal.getName()));

        if (data != null) {
            System.out.println(data);
        }

        return "profile";
    }
	
	@GetMapping(value = "/captcha", produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody byte[] captcha(HttpSession session) {
        try {
            BufferedImage img = captchaGenerator.getCaptchaImage();
            session.setAttribute("captchaCode", captchaGenerator.getCaptchaString());
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            ImageIO.write(img, "png", bao);
            return bao.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/checkout")
    public String proceedToCheckout(String paymentId, Model model) {

        Payment payment = cart.getPayments()
                .stream()
                .filter(p -> p.getId() == Integer.valueOf(paymentId))
                .collect(Validators.toSingleton());

        cart.setPayment(payment);

        model.addAttribute("cart", cart);

        return "checkout";

    }
}