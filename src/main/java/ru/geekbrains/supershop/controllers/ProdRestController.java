package ru.geekbrains.supershop.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.geekbrains.supershop.persistence.entities.Product;
import ru.geekbrains.supershop.persistence.pojo.ProductPojo;
import ru.geekbrains.supershop.services.ProductService;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Api("Набор методов для получения списка продуктов")
public class ProdRestController {

    private final ProductService productService;

    @ApiOperation(value = "Получить список всех продуктов.", response = String.class)
    @RequestMapping(method = RequestMethod.GET)
    public List<Product> getProductsList() {
        return productService.findAll(null, null);
    }

    @ApiOperation(value = "Получить список продуктов по заданной категории.", response = String.class)
    @RequestMapping(value = "/prodByCategory/{cat}", method = RequestMethod.GET)
    public List<Product> getProductsByCategory(@PathVariable int cat) {
        return productService.findAll(cat, null);
    }

    @ApiOperation(value = "Получить список продуктов в зависимости от наличия", response = String.class)
    @RequestMapping(value = "/prodByAvailability/{avail}", method = RequestMethod.GET)
    public List<Product> getProductsByAvailability(@PathVariable boolean avail) {
        return productService.findByAvailability(avail);
    }

    @RequestMapping(value = "/addProd", method = RequestMethod.POST)
    public void addProduct(@RequestBody ProductPojo productPojo) {
        productService.save(productPojo, null);
    }
}
