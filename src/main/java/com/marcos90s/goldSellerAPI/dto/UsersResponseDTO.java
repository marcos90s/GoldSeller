package com.marcos90s.goldSellerAPI.dto;

import java.util.ArrayList;
import java.util.List;

public class UsersResponseDTO {

        private String id;
        private String name;
        private String email;
        private String role;
        private Integer totalGold;
        private Double totalMoney;
        private List<String> realTransactionIds = new ArrayList<>();
        private List<String> gameTransactionIds = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getTotalGold() {
        return totalGold;
    }

    public void setTotalGold(Integer totalGold) {
        this.totalGold = totalGold;
    }

    public Double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(Double totalMoney) {
        this.totalMoney = totalMoney;
    }

    public List<String> getRealTransactionIds() {
        return realTransactionIds;
    }

    public void setRealTransactionIds(List<String> realTransactionIds) {
        this.realTransactionIds = realTransactionIds;
    }

    public List<String> getGameTransactionIds() {
        return gameTransactionIds;
    }

    public void setGameTransactionIds(List<String> gameTransactionIds) {
        this.gameTransactionIds = gameTransactionIds;
    }
}
