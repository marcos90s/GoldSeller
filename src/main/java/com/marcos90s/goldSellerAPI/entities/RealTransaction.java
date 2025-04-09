package com.marcos90s.goldSellerAPI.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
public class RealTransaction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id = UUID.randomUUID().toString();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JsonBackReference
    private Users user;
    private Double amount;
    private String charName;
    private LocalDateTime date;
    private String description;


    public RealTransaction() {}

    public RealTransaction(String id, Users user, Double amount, String charName, LocalDateTime date, String description) {
        this.id = id;
        this.user = user;
        this.amount = amount;
        this.charName = charName;
        this.date = date;
        this.description = description;
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

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        RealTransaction that = (RealTransaction) o;
        return Objects.equals(id, that.id) && Objects.equals(user, that.user) && Objects.equals(amount, that.amount) && Objects.equals(charName, that.charName) && Objects.equals(date, that.date) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(user);
        result = 31 * result + Objects.hashCode(amount);
        result = 31 * result + Objects.hashCode(charName);
        result = 31 * result + Objects.hashCode(date);
        result = 31 * result + Objects.hashCode(description);
        return result;
    }

    @Override
    public String toString() {
        return "RealTransaction{" +
                "id='" + id + '\'' +
                ", user=" + user +
                ", gain=" + amount +
                ", charName='" + charName + '\'' +
                ", date=" + date +
                ", description='" + description + '\'' +
                '}';
    }
}
