package com.example.kubermarket.dto;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor //service로직에서 Object를 Dto로 옮길때는 Setter 대신에 사용할수 있다.
@NoArgsConstructor //커맨드 객체로 받는 클래스에는 무조건 기본 생성자가 있어야한다.
public class PopularProductDto  implements Serializable {

    private Long id;
    private String title;
    private String content;
    /*
    아래 어노테이션을 안붙이면 직렬화나 역직렬화 할때 format이 맞지않아서 문제가 발생한다.
     */
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createDate;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime updateDate;
    private Integer price;
    private Integer interestCount;
    private String status;
    private String address1;
    private String address2;
    private String categoryName;
    private String nickName;
    private Integer chatCount;
    //private Integer totalCount;
}
