package com.example.demo.controller;

import com.example.demo.entity.Product;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ProductsController {

    private ProductRepository productRepository;

    @Autowired
    public ProductsController(ProductRepository repository) {
        this.productRepository = repository;
    }

    // напишите код, который возвратит все продукты по запросу на
    // url http://localhost:8080/all
    @GetMapping("/all")
    public List<Product> getAll() {
        List<Product> products = new ArrayList<>();

        productRepository.findAll().forEach(products::add);

        return products;
    }

    // GET   /products               retrieve all Products
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = new ArrayList<>();

        productRepository.findAll().forEach(products::add);

        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    // GET  /products/:id            retrieve a Product with it Cards
    @GetMapping("/products/{productId}")
    public ResponseEntity<Product> getProductById(
            @PathVariable(name = "productId") Long productId
    ) {
        Product product = productRepository.findById(productId)
                .orElseThrow(
                        () -> new IllegalArgumentException("There is no product with id: " + productId)
                );

        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    // GET http://localhost:8080/priceBetween?from=1&to=3
    @GetMapping("/priceBetween")
    public List<Product> priceBetween(
            @RequestParam(name = "from", defaultValue = "0.0") BigDecimal from,
            @RequestParam(name = "to", defaultValue = "0.0") BigDecimal to
    ) {

        return productRepository.getProductsWithPriceBetween(from, to);
    }

    // GET http://localhost:8080/getByStatus?active=true
    @GetMapping("/getByStatus")
    public List<Product> getByStatus(
            @RequestParam(name = "active", defaultValue = "false") boolean status
    ) {

        return productRepository.getProductWithStatus(status);
    }

    // GET http://localhost:8080/sort?column=price&direction=DESC
    @GetMapping("/sort")
    public List<Product> sort(
            @RequestParam(name = "column", defaultValue = "id") String column,
            @RequestParam(name = "direction", defaultValue = "ASC") String direction
    ) {
        Sort.Direction dir = Sort.Direction.ASC;
        if (direction.equalsIgnoreCase("DESC")) {
            dir = Sort.Direction.DESC;
        }

        return productRepository.getAll(Sort.by(dir, column));
    }

    // GET http://localhost:8080/page?page=0&size=5
    // page - номер страницы
    // size - кол-во элементов на странице
    @GetMapping("/page")
    public List<Product> getPage(
            @RequestParam(name = "page", defaultValue = "0") int pageNumber,
            @RequestParam(name = "size", defaultValue = "5") int pageSize
    ) {
        Pageable pageable = Pageable.ofSize(pageSize).withPage(pageNumber);
        return productRepository.getPage(pageable).stream().toList();
    }

    // GET http://localhost:8080/addProduct?name=Vegetarian burger&price=2.15&status=true
    @GetMapping("/addProduct")
    public void addProduct(
            @RequestParam(name = "name", defaultValue = "Product") String name,
            @RequestParam(name = "price", defaultValue = "0.00") BigDecimal price,
            @RequestParam(name = "status", defaultValue = "false") boolean status
    ) {
        productRepository.save(new Product(name, price, status));
    }

    @PostMapping("/products")
    public ResponseEntity<Product> createProduct(

            // Аннотация @RequestBody помечает
            //{
            //        "name": "Spagetti dolche",
            //        "price": 1.22,
            //        "isActive": true
            //}
            @RequestBody Product productRequest) {
        productRepository.save(productRequest);
        return new ResponseEntity<>(productRequest, HttpStatus.CREATED);
    }

    // DELETE 	/products/:id       	delete a Product (and its Comments) by :id
    @DeleteMapping("/products/{productId}")
    public ResponseEntity<HttpStatus> deleteProduct(
            @PathVariable(value = "productId") Long productId
    ) {
        if (!productRepository.existsById(productId)) {
            throw new IllegalArgumentException("There is no product with id: " + productId);
        }
        productRepository.deleteById(productId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}