package com.example.demo.repository;

import com.example.demo.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends CrudRepository<Product, Long> {

    // нужны продукты с ценой от ... до ...
    // Hibernate запрос:
    // "select p from Product p where p.price > :priceFrom and p.price < :priceTo" // JPQL
    @Query(
            value = "SELECT * FROM PRODUCTS where PRODUCT_PRICE > :priceFrom and PRODUCT_PRICE < :priceTo",
            nativeQuery = true)
    // native - используется SQL базы данных
    List<Product> getProductsWithPriceBetween(BigDecimal priceFrom, BigDecimal priceTo);

    @Query("select p from Product p where p.isActive = :isActive")
    List<Product> getProductWithStatus(boolean isActive);

    @Query("select p from Product p")
    List<Product> getAll(Sort sort);

    @Query("select p from Product p ORDER BY p.id")
        // Pageable - параметр, какая страница, сколько записей на странице
    Page<Product> getPage(Pageable pageable);

    List<Product> findProductsByCardsId(Long cardId);

}