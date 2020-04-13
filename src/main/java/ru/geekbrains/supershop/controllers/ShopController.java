package ru.geekbrains.supershop.controllers;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import ru.geekbrains.paymentservice.Payment;
import ru.geekbrains.supershop.beans.Cart;
import ru.geekbrains.supershop.persistence.entities.*;
import ru.geekbrains.supershop.services.*;
import ru.geekbrains.supershop.utilities.Validators;
import ru.geekbrains.supershop.utils.CaptchaGenerator;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class ShopController {

    private final Cart cart;
    private final ProductService productService;
    private final ShopuserService shopuserService;
    private final CaptchaGenerator captchaGenerator;
    private final ReviewService reviewService;
    private final ImageService imageService;
    private final PurchaseService purchaseService;
    private final EmailSenderService emailSenderService;

    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public String index(Model model, @RequestParam(required = false) Integer category, @RequestParam(required = false) String available) {
        model.addAttribute("cart", cart.getCartRecords());
        List<Product> products = productService.findAll(category, available);
        for (Product prod : products) {
            prod.setImages(imageService.getImagesByProduct(prod.getImage()));
        }
        model.addAttribute("products", products);
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

        model.addAttribute("products", productService.findAll(null, null));

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
    public @ResponseBody
    byte[] captcha(HttpSession session) {
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

    @GetMapping(value = "/revByPhone/{phone}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Review>> getReviewsByPhone(@PathVariable String phone) {
        return new ResponseEntity<>(reviewService.getReviewsByPhone(phone), HttpStatus.OK);
    }

    @PostMapping("/purchase")
    public String finishOrderAndPay(String phone, String email, Principal principal, Model model) {

        Shopuser shopuser = shopuserService.findByPhone(principal.getName());

        Purchase purchase = Purchase.builder()
                .shopuser(shopuser)
                .products(cart.getCartRecords()
                        .stream()
                        .map(CartRecord::getProduct)
                        .collect(Collectors.toList())
                )
                .price(cart.getPrice() + cart.getPayment().getFee())
                .phone(phone)
                .email(email)
                .build();

        model.addAttribute("purchase", purchaseService.makePurchase(purchase));

        cart.clear();

        emailSenderService.sendMail(email, purchase);

        return "orderdone";

    }
}