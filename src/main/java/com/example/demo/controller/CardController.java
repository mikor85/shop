package com.example.demo.controller;

import com.example.demo.entity.Card;
import com.example.demo.entity.Product;
import com.example.demo.repository.CardRepository;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

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
    @PostMapping("/products/{productId}/card")
    public ResponseEntity<Card> addCard(
            @PathVariable(name = "productId") Long productId,
            @RequestBody Card cardRequest
    ) {
        // если карта уже есть в репозитории, то добавить ее в продукт
        // если карты в репозитории нет, то создаем из реквеста и добавляем в продукт
        Card card = productRepository.findById(productId).map(
                product -> {
                    Long cardId = cardRequest.getId();
                    if (cardId != null) {
                        Card _card = cardRepository.findById(cardId).orElseThrow(
                                () -> new IllegalArgumentException("Card with id " + cardId + " not found")
                        );
                        product.addCard(_card);
                        productRepository.save(product);
                        return _card;
                    }
                    product.addCard(cardRequest);
                    return cardRepository.save(cardRequest);
                }
        ).orElseThrow(
                () -> new IllegalArgumentException("Product with id " + productId + " not found")
        );

        return new ResponseEntity<>(card, HttpStatus.CREATED);
    }

    //GET 	/products/:id/card 	    	retrieve all Cards of a Product
    @GetMapping("/products/{productId}/card")
    public ResponseEntity<List<Card>> getAllCardsForAProduct(
            @PathVariable(name = "productId") Long productId
    ) {
        // проверить есть ли продукт и выбросить исключение если его нет
        if (!productRepository.existsById(productId))
            throw new IllegalArgumentException("No product with productId " + productId);

        // вернуть все его карты
        List<Card> cards = cardRepository.findCardsByProductsId(productId);
        return new ResponseEntity<>(cards, HttpStatus.OK);
    }

    // My realization with SET - not good for using
//    @GetMapping("/products/{productId}/card")
//    public ResponseEntity<Set<Card>> getAllCardsForAProduct(
//            @PathVariable(name = "productId") Long productId
//    ) {
//        Product product = productRepository.findById(productId)
//                .orElseThrow(
//                        () -> new IllegalArgumentException("Comment with id " + productId + " not found")
//                );
//
//        Set<Card> allCards = product.getCards();
//        return new ResponseEntity<>(allCards, HttpStatus.OK);
//    }

    //GET 	/card/:id/products 	    	retrieve all Products of a Card
    @GetMapping("/card/{cardId}/products")
    public ResponseEntity<List<Product>> getAllProductsOfACard(
            @PathVariable(name = "cardId") Long cardId
    ) {
        if (!cardRepository.existsById(cardId)) {
            throw new IllegalArgumentException("Card with id " + cardId + " not found");
        }

        return new ResponseEntity<>(productRepository.findProductsByCardsId(cardId), HttpStatus.OK);
    }

    //GET 	/cards/:id 			    	retrieve a Card by :id
    @GetMapping("/cards/{cardId}")
    public ResponseEntity<Card> getCardById(
            @PathVariable(name = "cardId") Long cardId
    ) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("Card with id " + cardId + " not found"));

        return new ResponseEntity<>(card, HttpStatus.OK);
    }

    //PUT 	/cards/:id 			    	update a Card by :id
    @PutMapping("/cards/{cardId}")
    public ResponseEntity<Card> updateCardById(
            @PathVariable(name = "cardId") Long cardId,
            @RequestBody Card cardRequest
    ) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(
                        () -> new IllegalArgumentException(
                                "Card with id " + cardId + " not found"
                        )
                );

        card.setName(cardRequest.getName());
        return new ResponseEntity<>(cardRepository.save(card), HttpStatus.ACCEPTED);
    }

    //DELETE 	/cards/:id 				delete a Card by :id
    @DeleteMapping("/cards/{cardId}")
    public ResponseEntity<HttpStatus> deleteCardById(
            @PathVariable(name = "cardId") Long cardId
    ) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(
                        () -> new IllegalArgumentException("Card with id " + cardId + " not found")
                );
        cardRepository.delete(card);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    //DELETE 	/products/:id/card/:id 	delete a Card from a Product by :id
    @DeleteMapping("/products/{productId}/card/{cardId}")
    public ResponseEntity<HttpStatus> deleteCardFromProduct(
            @PathVariable(name = "productId") Long productId,
            @PathVariable(name = "cardId") Long cardId
    ) {
        Product product = productRepository.findById(productId)
                .orElseThrow(
                        () -> new IllegalArgumentException("Product with id " + productId + " not found")
                );
        if (!cardRepository.existsById(cardId)) {
            throw new IllegalArgumentException("Card with id " + cardId + " not found");
        }
        product.removeCards(cardId);
        productRepository.save(product);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //DELETE 	/products/:id   delete a Product by :id          - already exists in ProductController
    //GET 	    /products       retrieve all Products            - Method is in ProductRepository
    //GET   	/products/:id   retrieve a Product with it Cards - Method is in ProductRepository
}