package com.marcos90s.goldSellerAPI.entities;

import com.marcos90s.goldSellerAPI.enums.GameTransactionType;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class GameTransaction {

    @Id
    private String id = UUID.randomUUID().toString();
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;
    @Enumerated(EnumType.STRING)
    private GameTransactionType type;
    private String itemName;
    private Integer amount;
    private Integer quantity;
    private LocalDateTime date;

    public GameTransaction() {
    }

    public GameTransaction(String id, Users user, GameTransactionType type, String itemName, Integer amount, Integer quantity, LocalDateTime date) {
        this.id = id;
        this.user = user;
        this.type = type;
        this.itemName = itemName;
        this.amount = amount;
        this.quantity = quantity;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public GameTransactionType getType() {
        return type;
    }

    public void setType(GameTransactionType type) {
        this.type = type;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
