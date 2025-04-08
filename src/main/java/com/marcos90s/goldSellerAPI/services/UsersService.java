package com.marcos90s.goldSellerAPI.services;

import com.marcos90s.goldSellerAPI.entities.Users;
import com.marcos90s.goldSellerAPI.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersService {

    @Autowired
    UsersRepository usersRepository;

    public Users createUser(Users obj){
        usersRepository.save(obj);
        return obj;
    }

    public List<Users> getAllUsers(){
        return usersRepository.findAll();
    }

    public Users getUserById(String id){
        return usersRepository.findById(id).orElseThrow(()-> new RuntimeException("Usuário não encontrado"));
    }

    public void deleteById(String id){
        usersRepository.deleteById(id);
    }

    public Users updateUser(String id, Users obj){
        try {
            Users entity = usersRepository.getReferenceById(id);
            updateData(entity,obj);
            return usersRepository.save(entity);
        }catch (RuntimeException e){
            throw new RuntimeException("Algo deu errado");
        }
    }

    public void updateData(Users entity, Users obj){
        if(obj.getName() != null){entity.setName(obj.getName());}
        if(obj.getPassword() != null){entity.setPassword(obj.getPassword());}
    }
}
