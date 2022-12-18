package com.example.demo.repository;

import com.example.demo.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardRepository extends JpaRepository<Card, Long> {

    List<Card> findCardsByProductsId(Long productId);

    // select distinct c.id, c.name from cards c
    // inner join product_cards pc on c.id=pc.card_id
    // inner join products p on pc.product_id=p.id
    // where p.product_name like '%a%'


}