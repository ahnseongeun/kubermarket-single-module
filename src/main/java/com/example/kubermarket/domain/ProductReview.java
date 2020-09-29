package com.example.kubermarket.domain;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"user","product"})
@Table(name = "productReview")
//@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
public class ProductReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @OneToOne
    @JsonBackReference("b")
    @JoinColumn(name="product_id")
    private Product product;

    @NotNull
    @ManyToOne
    @JsonBackReference("e")
    @JoinColumn(name="user_id")
    private User user;

    @NotNull
    @Column(columnDefinition = "TEXT",nullable = false)
    private String content;

    @NotNull
    private String nickName;

    @NotNull
    @CreationTimestamp
    private LocalDateTime createDate;

    @UpdateTimestamp
    private LocalDateTime updateDate;
}