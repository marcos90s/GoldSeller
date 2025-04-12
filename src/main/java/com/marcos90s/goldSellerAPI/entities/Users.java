package com.marcos90s.goldSellerAPI.entities;

import com.marcos90s.goldSellerAPI.enums.UserRole;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "users")
public class Users implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id = UUID.randomUUID().toString();
    private String name;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private UserRole role;
    private Integer totalGold;
    private Double totalMoney;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<RealTransaction> realTransactions = new ArrayList<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<GameTransaction> gameTransactions = new ArrayList<>();


    public Users() {
    }

    public Users(String id, String name, String email, String password, UserRole role, Integer totalGold, Double totalMoney, List<RealTransaction> realTransactions, List<GameTransaction> gameTransactions) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.totalGold = totalGold;
        this.totalMoney = totalMoney;
        this.realTransactions = realTransactions;
        this.gameTransactions = gameTransactions;
    }

    public List<GameTransaction> getGameTransactions() {
        return gameTransactions;
    }

    public List<RealTransaction> getRealTransactions() {
        return realTransactions;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public UserRole getRole() {
        return role;
    }

    public Integer getTotalGold() {
        return totalGold;
    }

    public Double getTotalMoney() {
        return totalMoney;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public void setTotalGold(Integer totalGold) {
        this.totalGold = totalGold;
    }

    public void setTotalMoney(Double totalMoney) {
        this.totalMoney = totalMoney;
    }

    public void applyRealTransaction(Double amount, Integer amountInGold){
        if (amount != null) {
            this.totalMoney = (this.totalMoney != null ? this.totalMoney : 0.0) + amount;
            this.totalGold = (this.totalGold != null ? this.totalGold : 0) - amountInGold;
        }
    }

    public void applyGameTransaction(Integer amount, Integer quantity, String type){
        if(type.equals("SALE")){
            this.totalGold = (this.totalGold != null ? this.totalGold : 0) +(amount * quantity);
        }else {
            this.totalGold = (this.totalGold != null ? this.totalGold : 0) -(amount * quantity);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Users users = (Users) o;
        return Objects.equals(id, users.id) && Objects.equals(name, users.name) && Objects.equals(email, users.email) && Objects.equals(password, users.password) && role == users.role && Objects.equals(totalGold, users.totalGold) && Objects.equals(totalMoney, users.totalMoney);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(name);
        result = 31 * result + Objects.hashCode(email);
        result = 31 * result + Objects.hashCode(password);
        result = 31 * result + Objects.hashCode(role);
        result = 31 * result + Objects.hashCode(totalGold);
        result = 31 * result + Objects.hashCode(totalMoney);
        return result;
    }

    @Override
    public String toString() {
        return "Users{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", totalGold=" + totalGold +
                ", totalMoney=" + totalMoney +
                '}';
    }
}
