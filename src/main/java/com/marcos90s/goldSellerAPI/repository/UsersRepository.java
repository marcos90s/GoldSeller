package com.marcos90s.goldSellerAPI.repository;

import com.marcos90s.goldSellerAPI.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users, String> {


}
