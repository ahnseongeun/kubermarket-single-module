package com.example.kubermarket.domain;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
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
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"user","product"})
@Table(name = "productReview")
public class ProductReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @OneToOne
    @JsonBackReference("product_productReview")
    @JoinColumn(name="product_id")
    private Product product;

    @NotNull
    @ManyToOne
    @JsonBackReference("user_productReview")
    @JoinColumn(name="user_id")
    private User user;

    @NotNull
    @Column(columnDefinition = "TEXT",nullable = false)
    private String content;

    @NotNull
    private String nickName;

    @NotNull
    @CreationTimestamp
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createDate;

    @UpdateTimestamp
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime updateDate;
}
