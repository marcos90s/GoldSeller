package com.marcos90s.goldSellerAPI.dto;

public class RealTransactionRequestDTO {

    private String userId;
    private Double amount;
    private  Integer amountInGold;
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
