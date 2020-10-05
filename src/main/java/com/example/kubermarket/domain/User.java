package com.example.kubermarket.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
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
@ToString (exclude = {"products","productReviews","chatRooms"})
@Table(name = "user")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long BuyerId;

//    @Column(updatable = false,nullable = false,unique = true)
//    private String userId;

    @Column(updatable = false,nullable = false,unique = true)
    private String email;

    @NotNull
    private String password;

    private String address1;

    private String address2;

    @CreationTimestamp
    private LocalDateTime createDate;

    @NotNull
    private String nickName;

    private String profileImageUrl;

    @OneToMany(mappedBy = "user", fetch= FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference("d")
    private List<Product> products = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch=FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference("e")
    private List<ProductReview> productReviews = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch=FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference("u")
    private List<ChatRoom> chatRooms = new ArrayList<>();



}
