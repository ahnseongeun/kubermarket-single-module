package com.example.kubermarket.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "")
@Table(name = "chatMessage")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonBackReference("chatRoom_chatMessage")
    @JoinColumn(name="chatRoom_id")
    private ChatRoom chatRoom;

    private Boolean senderType;

    @Column(columnDefinition = "TEXT",nullable = false)
    private String message;

    @CreationTimestamp
    private LocalDateTime createDate;

}
