package com.marcos90s.goldSellerAPI.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class RealTransactionRequestDTO {

    //Mudar para String email depois
    @NotBlank(message = "Field UserId is required")
    private String userId;
    @NotNull(message = "Field Amount is required!")
    private Double amount;
    @NotNull(message = "Field Amount In Gold is required!")
    private Integer amountInGold;
    @NotBlank(message = "Field Char Name is required!")
    private String charName;
    private String description;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCharName() {
        return charName;
    }

    public void setCharName(String charName) {
        this.charName = charName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getAmountInGold() {
        return amountInGold;
    }

    public void setAmountInGold(Integer amountInGold) {
        this.amountInGold = amountInGold;
    }
}
