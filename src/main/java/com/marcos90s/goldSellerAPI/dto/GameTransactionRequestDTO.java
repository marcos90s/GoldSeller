package com.marcos90s.goldSellerAPI.dto;

import com.marcos90s.goldSellerAPI.enums.GameTransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class GameTransactionRequestDTO {
    //Mudar para String email
    @NotBlank(message = "Field UserId is required")
    private String userId;
    private GameTransactionType type;
    @NotNull(message = "Field Amount is required!")
    private Integer amount;
    @NotNull(message = "Field Quantity is required!")
    private Integer quantity;
    @NotBlank(message = "Field ItemName is required!")
    private String itemName;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public GameTransactionType getType() {
        return type;
    }

    public void setType(GameTransactionType type) {
        this.type = type;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
