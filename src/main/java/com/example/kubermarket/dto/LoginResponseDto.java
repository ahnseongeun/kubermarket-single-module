package com.example.kubermarket.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
public class LoginResponseDto {
    private String accessToken;
}
