package ru.geekbrains.supershop.persistence.repositories;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;

import ru.geekbrains.supershop.persistence.entities.Product;
import ru.geekbrains.supershop.persistence.entities.enums.ProductCategory;

import java.io.IOException;

import java.util.List;

@DataJpaTest
@RunWith(SpringRunner.class)
@Ignore
public class ProductRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private ProductRepository productRepository;

    private List<Product> productMocks;

    {
        try {
            productMocks = new ObjectMapper().readValue(new ClassPathResource("mocks/products.json").getFile(), new TypeReference<List<Product>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Before
    public void setUp() {
        productMocks.forEach(product -> testEntityManager.persist(product));
    }

    @Test
    public void mustFindAllDrinkProducts() {
        Assert.assertEquals(3, productRepository.findAllByCategory(ProductCategory.FOOD).size());
    }

    @Test
    public void mustFindAllFoodProducts() {
        Assert.assertEquals(1, productRepository.findAllByCategory(ProductCategory.DRINK).size());
    }

    @Test
    public void mustFindNoProducts() {
        Assert.assertEquals(0, productRepository.findAllByCategory(null).size());
    }

    @Test
    public void mustFindAllProducts() {
        Assert.assertEquals(4, productRepository.findAll().size());
    }

}