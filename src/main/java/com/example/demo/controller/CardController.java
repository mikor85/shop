package com.example.demo.controller;

import com.example.demo.entity.Card;
import com.example.demo.repository.CardRepository;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CardController {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private ProductRepository productRepository;

    //GET 	/cards 				    	retrieve all Cards
    @GetMapping("/cards")
    public ResponseEntity<List<Card>> getAllCards() {
        List<Card> cards = cardRepository.findAll();
        if (cards.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(cards, HttpStatus.OK);
        }
    }


    //POST 	/products/:id/card 	    	create/add Card for a Product
    //GET 	/products/:id/card 	    	retrieve all Cards of a Product
    //GET 	/card/:id/products 	    	retrieve all Products of a Card
    //GET 	/products 			    	retrieve all Products
    //GET 	/products/:id 		    	retrieve a Product with it Cards

    //GET 	/cards/:id 			    	retrieve a Card by :id
    //PUT 	/cards/:id 			    	update a Card by :id
    //DELETE 	/cards/:id 				delete a Card by :id
    //DELETE 	/products/:id 			delete a Product by :id
    //DELETE 	/products/:id/card/:id 	delete a Card from a Product by :id
}