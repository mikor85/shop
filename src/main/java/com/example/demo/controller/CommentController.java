package com.example.demo.controller;

import com.example.demo.entity.Comment;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ProductRepository productRepository;

    // POST 	/products/:id/comments 	create new Comment for a Product
    @PostMapping("/products/{id}/comments")
    public ResponseEntity<Comment> createComment(
            @PathVariable(value = "productId") Long productId,
            @RequestBody Comment commentRequest
    ) {
        // найти товар по идентификатору,
        // установить этот товар для коммента,
        // спасти коммент в базу данных,
        // вернуть json коммента с правильным идентификатором товара,
        // выбросить исключение, если такого товара нет.
        Comment comment = productRepository.findById(productId).map(
                product -> {
                    commentRequest.setProduct(product);
                    return commentRepository.save(commentRequest);
                }
        ).orElseThrow(
                () -> new IllegalArgumentException("Product not found " + productId)
        );
        return new ResponseEntity<>(comment, HttpStatus.CREATED);
    }

    // GET 	    /products/:id/comments 	retrieve all Comments of a Product
    @GetMapping("/products/{productId}/comments")
    public ResponseEntity<List<Comment>> getAllCommentsByProductId(
            @PathVariable(value = "productId") Long productId
    ) {
        if (!productRepository.existsById(productId)) {
            throw new IllegalArgumentException("Product not found: " + productId);
        }
        return new ResponseEntity<>(
                commentRepository.findByProductId(productId),
                HttpStatus.OK
        );
    }


    // GET  	/comments/:id       	retrieve a Comment by :id
    @GetMapping("/comments/{commentId}")
    public ResponseEntity<Comment> getCommentById(
            @PathVariable(value = "commentId") Long commentId
    ) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(
                        () -> new IllegalArgumentException(
                                "Comment not found " + commentId
                        )
                );
        return new ResponseEntity<>(comment, HttpStatus.OK);
    }

    // PUT  	/comments/:id       	update a Comment by :id
    @PutMapping("/comments/{commentId}")
    public ResponseEntity<Comment> updateCommentById(
            @PathVariable(value = "commentId") Long commentId,
            @RequestBody Comment commentRequest
    ) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(
                        () -> new IllegalArgumentException(
                                "Comment with id " + commentId + " not found"
                        )
                );

        comment.setContent(commentRequest.getContent());
        return new ResponseEntity<>(commentRepository.save(comment), HttpStatus.ACCEPTED);
    }

    // DELETE 	/comments/:id       	delete a Comment by :id
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<HttpStatus> deleteCommentById(
            @PathVariable(value = "commentId") Long commentId
    ) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(
                        () -> new IllegalArgumentException("Comment with id " + commentId + " not found")
                );

        commentRepository.delete(comment);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    // DELETE 	/products/:id/comments 	delete all Comments of a Product
    @DeleteMapping("/products/{productId}/comments")
    public ResponseEntity<HttpStatus> deleteCommentsByProductId(
            @PathVariable(value = "productId") Long productId
    ) {
        if (!productRepository.existsById(productId)) {
            throw new IllegalArgumentException("Product with id: " + productId + " not found");
        }
        commentRepository.deleteByProductId(productId);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}