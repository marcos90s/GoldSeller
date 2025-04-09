package com.marcos90s.goldSellerAPI.services;

import com.marcos90s.goldSellerAPI.entities.RealTransaction;
import com.marcos90s.goldSellerAPI.entities.Users;
import com.marcos90s.goldSellerAPI.repository.RealTransactionRepository;
import com.marcos90s.goldSellerAPI.repository.UsersRepository;
import com.marcos90s.goldSellerAPI.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RealTransactionService {

    @Autowired
    RealTransactionRepository realTransactionRepository;

    @Autowired
    UsersRepository usersRepository;

    public RealTransaction create(RealTransaction obj){
        Users user = obj.getUser();
        user.applyRealTransaction(obj.getAmount());
        usersRepository.save(user);
        obj.setDate(Utils.dateTimeFormatter());
        System.out.println("Date: "+obj.getDate());
        return realTransactionRepository.save(obj);
    }

    public List<RealTransaction> getAll(){
        return realTransactionRepository.findAll();
    }

    public RealTransaction getById(String id){
        return realTransactionRepository.findById(id).orElseThrow(()-> new RuntimeException("Transaction not Found"));
    }

    public void deleteById(String id){
        realTransactionRepository.deleteById(id);
    }

}
