package com.marcos90s.goldSellerAPI.controller;

import com.marcos90s.goldSellerAPI.entities.RealTransaction;
import com.marcos90s.goldSellerAPI.services.RealTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping(value = "/real")
public class RealTransactionController {

    @Autowired
    RealTransactionService realTransactionService;

    @PostMapping
    public ResponseEntity<RealTransaction> create(@RequestBody RealTransaction obj){
        obj = realTransactionService.create(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).body(obj);
    }

    @GetMapping
    public ResponseEntity<List<RealTransaction>> getAll(){
        List<RealTransaction> realList = realTransactionService.getAll();
        return ResponseEntity.ok().body(realList);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<RealTransaction> getById(@PathVariable String id){
        RealTransaction obj = realTransactionService.getById(id);
        return ResponseEntity.ok().body(obj);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id){
        realTransactionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
