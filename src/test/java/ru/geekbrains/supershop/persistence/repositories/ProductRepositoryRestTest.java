package ru.geekbrains.supershop.persistence.repositories;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.geekbrains.supershop.controllers.ProdRestController;
import ru.geekbrains.supershop.persistence.entities.enums.ProductCategory;
import ru.geekbrains.supershop.persistence.pojo.ProductPojo;
import ru.geekbrains.supershop.services.ProductService;
import ru.geekbrains.supershop.services.ShopuserService;

import java.io.IOException;
import java.lang.reflect.Type;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = ProdRestController.class)
public class ProductRepositoryRestTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;
    @MockBean
    private ShopuserService shopuserService;

    private ProductPojo productPojo;

    private String json = "";

    @Before
    public void SetUp() {
        productPojo = ProductPojo.builder()
                .title("TestProd")
                .available(true)
                .category(ProductCategory.FOOD)
                .build();

        try {
            json = new ObjectMapper().writeValueAsString(productPojo);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void addProdTest() throws Exception {
        mockMvc.perform(post("/api/addProd")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
    }
}
