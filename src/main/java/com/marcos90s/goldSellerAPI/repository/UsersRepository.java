package com.marcos90s.goldSellerAPI.repository;

import com.marcos90s.goldSellerAPI.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, String> {
    //Carregar lista de transações ao buscar um usuário pelo ID
    @Query("SELECT u FROM Users u LEFT JOIN FETCH u.realTransactions WHERE u.id = :id")
    Optional<Users> findByIdWithTransactions(@Param("id") String id);
}
