package com.creditcardapi.project.vo.request;

import lombok.Data;

@Data
public class CreateUserPayload {

    private String name;

    private String email;
}
