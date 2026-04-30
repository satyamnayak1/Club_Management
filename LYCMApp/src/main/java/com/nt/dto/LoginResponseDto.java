package com.nt.dto;

import lombok.Data;

@Data
public class LoginResponseDto {
    private String accessToken;
    private UserResponseDto userResponseDto;

    public LoginResponseDto(String accessToken, UserResponseDto userResponseDto) {
        this.accessToken = accessToken;
        this.userResponseDto = userResponseDto;
    }
}
