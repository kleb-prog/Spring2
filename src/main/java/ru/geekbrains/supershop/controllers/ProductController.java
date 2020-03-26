package ru.geekbrains.supershop.controllers;

import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import ru.geekbrains.supershop.exceptions.InternalServerException;
import ru.geekbrains.supershop.exceptions.ProductNotFoundException;
import ru.geekbrains.supershop.persistence.entities.Image;
import ru.geekbrains.supershop.persistence.entities.Product;
import ru.geekbrains.supershop.persistence.entities.Review;
import ru.geekbrains.supershop.persistence.entities.Shopuser;
import ru.geekbrains.supershop.persistence.entities.enums.Role;
import ru.geekbrains.supershop.persistence.pojo.ProductPojo;
import ru.geekbrains.supershop.persistence.pojo.ReviewPojo;
import ru.geekbrains.supershop.services.ImageService;
import ru.geekbrains.supershop.services.ProductService;
import ru.geekbrains.supershop.services.ReviewService;
import ru.geekbrains.supershop.services.ShopuserService;
import ru.geekbrains.supershop.utilities.ImageValidator;
import ru.geekbrains.supershop.utilities.UUIDValidator;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ImageService imageService;
    private final ProductService productService;
    private final ShopuserService shopuserService;
    private final ReviewService reviewService;
    private final UUIDValidator validator;

    @GetMapping("/{id}")
    public String getOneProduct(Model model, @PathVariable String id) throws ProductNotFoundException, InternalServerException {
        if (validator.validate(id)) {
            Product product = productService.findOneById(UUID.fromString(id));
            List<Review> reviews = reviewService.getReviewsByProduct(product).orElse(new ArrayList<>());
			model.addAttribute("product", product);
            model.addAttribute("reviews", reviews);
			return "product";
        } else {
            throw new InternalServerException("UUID not valid");
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
	
	@PostMapping
    public String addOne(@RequestParam("image") MultipartFile[] images, ProductPojo productPojo) throws IOException {
        UUID imageId = UUID.randomUUID();
        Product product = productService.save(productPojo, imageId);
        for (MultipartFile image : images) {
            if (!ImageValidator.validate(image)) {
                continue;
            }
            imageService.uploadImage(image, image.getOriginalFilename(), product);
        }
        return "redirect:/";
    }
	
	@PostMapping("/reviews")
    public String addReview(ReviewPojo reviewPojo, HttpSession session, Principal principal) throws ProductNotFoundException {

        Product product = productService.findOneById(reviewPojo.getProductId());
        Shopuser shopuser = shopuserService.findByPhone(principal.getName());

        Review review = Review.builder()
            .commentary(reviewPojo.getCommentary())
                .product(product)
                .shopuser(shopuser)
                .approved(shopuser.getRole().equals(Role.ROLE_ADMIN))
        .build();

        reviewService.save(review);

        return "redirect:/products/" + product.getId();
    }

    @GetMapping("/approve/{id}")
    public String approveReview(@PathVariable UUID id) {
        Review review = reviewService.getReviewById(id);
        review.setApproved(true);
        reviewService.save(review);
        return "redirect:/products/" + review.getProduct().getId();
    }

}