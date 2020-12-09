package com.example.kubermarket.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Accessors(chain = true)
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString (exclude = {"products","productReviews","chatRooms"})
@Table(name = "user")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(updatable = false,nullable = false,unique = true)
    private String email;

    @NotNull
    private String password;

    private String address1;

    private String address2;

    @CreationTimestamp
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createDate;

    @NotNull
    private String nickName;

    private String profileImageUrl;

    @OneToMany(mappedBy = "user", fetch= FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference("product_user")
    private List<Product> products = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch=FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference("product_productReview")
    private List<ProductReview> productReviews = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch=FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference("user_chatRoom")
    private List<ChatRoom> chatRooms = new ArrayList<>();




}
