package com.example.kubermarket.domain;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"user", "productImages", "productReview","category"})
@NamedQuery(name="Product.findByInterestCountANDChatRoom",query = "select a , a.interestCount + count(b.product) as total   from Product as a left join ChatRoom as b  on a= b.product group by a.id ORDER BY total DESC ")
@NamedQuery(name="Product.findByAddress",query = "select a from Product as a left join User as b  on a.user= b where b.address1 = :address order by a.createDate DESC")
@NamedQuery(name="Product.findByKeyword",query = "select a from Product as a left join User as b  on a.user= b where b.address1 like :keyword or a.title like :keyword order by a.createDate DESC")
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT",nullable = false)
    private String content;

    @CreationTimestamp
    private LocalDateTime createDate;

    @UpdateTimestamp
    private LocalDateTime updateDate;

    @NotNull
    private Integer price;

    private Integer interestCount;

    @OneToMany(mappedBy = "product", fetch=FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference("a")
    private List<ProductImage> productImages = new ArrayList<>();

    @OneToOne(mappedBy = "product", fetch=FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference("b")
    private ProductReview productReview;

//    @OneToMany(mappedBy = "product", fetch=FetchType.LAZY, cascade = CascadeType.ALL)
//    @JsonManagedReference("p")
//    private List<ChatRoom> chatRooms = new ArrayList<>();

    @ManyToOne
    @JsonBackReference("c")
    @JoinColumn(name="category_id")
    private Category category;

    @ManyToOne
    @JsonBackReference("d")
    @JoinColumn(name="user_id")
    private User user;

    private String status;

    public void updateProduct(String title, String content, LocalDateTime updateDate, Integer price, Integer interestCount, String status) {
        this.title=title;
        this.content=content;
        this.updateDate=updateDate;
        this.price=price;
        this.interestCount=interestCount;
        this.status=status;
    }
}




