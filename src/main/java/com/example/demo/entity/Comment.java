package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "comments")
@Data // @Getter, @Setter, @ToString
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content") // название колонки в таблице базы данных
    private String content;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)  // много комментов могут относиться к одному продукту
    @JoinColumn(name = "product_id", nullable = false)  // название колонки
    @OnDelete(action = OnDeleteAction.CASCADE)   // удалять все комменты относящиеся к удаляемому продукту
    @JsonIgnore  // не сериализовать это поле в json
    private Product product;

    public Comment(String content, Product product) {
        this.content = content;
        this.product = product;
    }
}