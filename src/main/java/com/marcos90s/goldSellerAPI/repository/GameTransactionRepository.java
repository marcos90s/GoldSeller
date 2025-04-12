package com.marcos90s.goldSellerAPI.repository;

import com.marcos90s.goldSellerAPI.entities.GameTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameTransactionRepository extends JpaRepository<GameTransaction ,String> {
}
