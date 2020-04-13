package ru.geekbrains.supershop.controllers;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.geekbrains.supershop.persistence.entities.Product;
import ru.geekbrains.supershop.persistence.entities.Review;
import ru.geekbrains.supershop.persistence.entities.Shopuser;
import ru.geekbrains.supershop.persistence.entities.enums.ProductCategory;
import ru.geekbrains.supershop.persistence.entities.enums.Role;
import ru.geekbrains.supershop.persistence.pojo.ProductPojo;
import ru.geekbrains.supershop.services.EmailSenderService;
import ru.geekbrains.supershop.services.ProductService;
import ru.geekbrains.supershop.services.ReviewService;
import ru.geekbrains.supershop.services.ShopuserService;

import java.util.UUID;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ReviewServiceTest {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ShopuserService shopuserService;

    @Autowired
    private EmailSenderService emailSenderService;

    private Product product;

    private Shopuser shopuser;

    @Before
    public void setUp() {
        ProductPojo productPojo = ProductPojo.builder()
                .title("TestProd")
                .available(true)
                .category(ProductCategory.FOOD)
                .build();
        productService.save(productPojo, null);

        product = productService.findAll(ProductCategory.FOOD.ordinal(), "true").get(0);

        shopuser = Shopuser.builder().firstName("TestUser").role(Role.ROLE_ADMIN).build();
        shopuserService.save(shopuser);

        Review review = Review.builder()
                .commentary("Test")
                .product(product)
                .shopuser(shopuser)
                .approved(false)
                .build();
        reviewService.save(review);
    }

    @After
    public void destroyAll() {
        reviewService.removeAll();
    }

    @Test
    public void testMustReturnReviewByProduct() throws Exception {
        assertEquals(1, reviewService.getReviewsByProduct(product).get().size());
    }

    @Test
    public void testMustReturnReviewByShopuser() throws Exception {
        assertEquals(1, reviewService.getReviewsByShopuser(shopuser).get().size());
    }

    @Test
    public void testMustModerateReviewByID() throws Exception {
        Review review = reviewService.getAll().get(0);
        assertNotNull(review);

        assertFalse(review.isApproved());

        UUID id = review.getId();
        reviewService.moderate(id, "approve");

        Review moderatedReview = reviewService.findById(id).get();
        assertNotNull(moderatedReview);

        assertTrue(moderatedReview.isApproved());
    }
}
