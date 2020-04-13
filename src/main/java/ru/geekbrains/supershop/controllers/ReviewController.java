package ru.geekbrains.supershop.controllers;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ru.geekbrains.supershop.persistence.entities.Review;
import ru.geekbrains.supershop.services.ReviewService;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

//    private final ShopFeignClient shopFeignClient;
    private final ReviewService reviewService;

//    @GetMapping("/flyer")
//    public ResponseEntity<byte[]> getFlyer() {
//        return shopFeignClient.getFlyer();
//    }

//    @GetMapping("/{id}")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    public String moderateReview(@PathVariable UUID id, @RequestParam String option) throws EntityNotFoundException {
//        return "redirect:/products/" + reviewService.moderate(id, option);
//    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Review>> getAllReviews() {
        return new ResponseEntity<>(reviewService.getAll(), HttpStatus.OK);
    }

}