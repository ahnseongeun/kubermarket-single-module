//package com.example.kubermarket.domain;
//
//
//import com.fasterxml.jackson.annotation.JsonBackReference;
//import com.fasterxml.jackson.annotation.JsonManagedReference;
//import lombok.*;
//import lombok.experimental.Accessors;
//
//import javax.persistence.*;
//import java.util.ArrayList;
//import java.util.List;
//
//@Data
//@Accessors(chain = true)
//@Builder
//@Entity
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@ToString(exclude = "product")
//@Table(name = "chatRoom")
//public class ChatRoom {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    Long id;
//
//    @ManyToOne
//    @JsonBackReference("p")
//    @JoinColumn(name="product_id")
//    private Product product;
//
//    @ManyToOne
//    @JsonBackReference("u")
//    @JoinColumn(name="user_id")
//    private User user;
//
//    @OneToMany(mappedBy = "chatRoom", fetch=FetchType.LAZY, cascade = CascadeType.ALL)
//    @JsonManagedReference("r")
//    private List<ChatMessage> chatMessages = new ArrayList<>();
//}
