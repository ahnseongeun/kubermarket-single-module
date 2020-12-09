package com.example.kubermarket.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor //접근 권한이 굳이 public일 필요가 없고, 최소한의 기본생성자는 필요하다.
@AllArgsConstructor
@Accessors(chain = true)
@Builder
@Entity
@Getter
@ToString(exclude = "products")
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column
    private String name;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference("category_product")
    private List<Product> products = new ArrayList<>();

    public void updateInformation(String name) {
        this.name=name;
    }

}

