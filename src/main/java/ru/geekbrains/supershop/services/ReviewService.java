package ru.geekbrains.supershop.services;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.geekbrains.supershop.exceptions.ProductNotFoundException;
import ru.geekbrains.supershop.persistence.entities.Product;
import ru.geekbrains.supershop.persistence.entities.Review;
import ru.geekbrains.supershop.persistence.entities.Shopuser;
import ru.geekbrains.supershop.persistence.repositories.ReviewRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewService {

    @PersistenceContext
    private EntityManager entityManager;

    private final ReviewRepository reviewRepository;

    public Optional<List<Review>> getReviewsByProduct(Product product) {
        return reviewRepository.findByProduct(product);
    }

    public Optional<List<Review>> getReviewsByShopuser(Shopuser shopuser) {
        return reviewRepository.findByShopuser(shopuser);
    }

    public Review getReviewById(UUID id) {
        return reviewRepository.getOne(id);
    }

    public Optional<Review> findById(UUID id) {
        return reviewRepository.findById(id);
    }

    @Transactional
    public void save(Review review) {
        reviewRepository.save(review);
    }

    public List<Review> getReviewsByPhone(String phone) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Review> criteriaQuery = criteriaBuilder.createQuery(Review.class);

        Root<Review> root = criteriaQuery.from(Review.class);
        Join<Review, Shopuser> reviewShopuserJoin = root.join("shopuser");

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.equal(reviewShopuserJoin.get("phone"), phone));

        criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[]{})));

        List<Review> reviews = entityManager.createQuery(criteriaQuery).getResultList();
        return reviews;
    }

    public UUID moderate(UUID id, String option) throws ProductNotFoundException {
        Review review = reviewRepository.findById(id).orElseThrow(
                () -> new ProductNotFoundException("Oops! Review " + id + " wasn't found!")
        );
        review.setApproved(option.equals("approve"));
        save(review);
        return review.getProduct().getId();
    }

    public List<Review> getAll() {
        return reviewRepository.findAll();
    }

    public void removeAll() {
        reviewRepository.deleteAll();
    }

}