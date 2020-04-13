package ru.geekbrains.supershop.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;

import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import ru.geekbrains.supershop.persistence.entities.Review;
import ru.geekbrains.supershop.services.ReviewService;
import ru.geekbrains.supershop.services.ShopuserService;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

import static org.mockito.BDDMockito.given;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = ReviewController.class)
public class ReviewControllerMVCTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewService reviewServiceMock;

//    @MockBean
//    private ShopFeignClient shopFeignClient;

    @MockBean
    private ShopuserService shopuserServiceMock;

    @Before
    public void setUp() {

        List<Review> reviews = new ArrayList<Review>() {{
            add(new Review());
            add(new Review());
            add(new Review());
            add(new Review());
        }};

        given(reviewServiceMock.getAll()).willReturn(reviews);

    }

    @Test
    public void testMustReturnAllAvailableReviews() throws Exception {
        mockMvc
            .perform(get("/reviews/all")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(4)));
    }




}
