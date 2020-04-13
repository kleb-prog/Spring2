package ru.geekbrains.supershop.services;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import ru.geekbrains.supershop.exceptions.ProductNotFoundException;
import ru.geekbrains.supershop.persistence.entities.Product;
import ru.geekbrains.supershop.persistence.entities.enums.ProductCategory;
import ru.geekbrains.supershop.persistence.pojo.ProductPojo;
import ru.geekbrains.supershop.persistence.repositories.ProductRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    @PersistenceContext
    private EntityManager entityManager;

    private final ProductRepository productRepository;

    public Product findOneById(UUID uuid) throws ProductNotFoundException {
        return productRepository.findById(uuid).orElseThrow(
                () -> new ProductNotFoundException("Oops! Product " + uuid + " wasn't found!")
        );
    }

    public List<Product> findAll(Integer category, String available) {
//        return category == null ? productRepository.findAll() : productRepository.findAllByCategory(ProductCategory.values()[category]);

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);

        Root<Product> root = criteriaQuery.from(Product.class);

        List<Predicate> predicates = new ArrayList<>();
        if (category != null) {
            predicates.add(criteriaBuilder.equal(root.get("category"), category));
        }

        if (available != null && !available.isEmpty()) {
            predicates.add(criteriaBuilder.equal(root.get("available"), Boolean.valueOf(available)));
        }

        if (!predicates.isEmpty()) {
            criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[]{})));
            return entityManager.createQuery(criteriaQuery).getResultList();
        } else {
            CriteriaQuery<Product> all = criteriaQuery.select(root);
            return entityManager.createQuery(all).getResultList();
        }
    }

    public List<Product> findByAvailability(Boolean available) {
        return available == null ? productRepository.findAll() : productRepository.findAllByAvailable(available);
    }

    @Transactional
    public Product save(ProductPojo productPogo, UUID image) {

        Product product = Product.builder()
                .added(new Date())
                .title(productPogo.getTitle())
                .description(productPogo.getDescription())
                .price(productPogo.getPrice())
                .available(productPogo.isAvailable())
                .category(productPogo.getCategory())
                .image(image)
                .build();

        Product savedProd = productRepository.save(product);
        log.info("New Product has been succesfully added! {}", product);
        return savedProd;
    }
}