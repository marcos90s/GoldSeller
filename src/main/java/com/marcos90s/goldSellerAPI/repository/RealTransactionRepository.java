package com.marcos90s.goldSellerAPI.repository;

import com.marcos90s.goldSellerAPI.entities.RealTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RealTransactionRepository extends JpaRepository<RealTransaction, String> {
    List<RealTransaction> findByUserId(String id);
}
