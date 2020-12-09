package com.example.kubermarket.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Accessors(chain = true)
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"product"})
@Table(name = "productImage")
@Proxy(lazy = false)
public class ProductImage implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="product_id")
    @JsonBackReference("product_productImage")
    private Product product;

    @NotNull
    private String fileUrl;

    @NotNull
    private String filename;

    @NotNull
    private boolean deleteFlag;






}
