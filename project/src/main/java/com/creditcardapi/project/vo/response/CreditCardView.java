package com.creditcardapi.project.vo.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CreditCardView {

    public CreditCardView(int id, String issuanceBank2, String number2) {
        //TODO Auto-generated constructor stub
        issuanceBank = issuanceBank2;
        number = number2;
    }

    private String issuanceBank;

    private String number;
}
