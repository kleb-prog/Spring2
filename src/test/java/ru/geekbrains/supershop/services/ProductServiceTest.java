package ru.geekbrains.supershop.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import ru.geekbrains.supershop.persistence.entities.enums.ProductCategory;
import ru.geekbrains.supershop.persistence.pojo.ProductPojo;

import static org.junit.Assert.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Before
    public void setUp() {

        ProductPojo productPojo = ProductPojo.builder()
            .title("Пепси")
            .available(true)
            .category(ProductCategory.DRINK)
            .price(220.0)
            .description("")
        .build();

        productService.save(productPojo, null);

    }

    @Test
    public void testSomething() throws Exception {
        assertEquals(1, productService.findAll(0, null).size());
    }

}
