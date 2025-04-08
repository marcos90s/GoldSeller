package com.marcos90s.goldSellerAPI.entities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

@Component
public class TestConnection implements CommandLineRunner {

    @Autowired
    private DataSource dataSource;

    @Override
    public void run(String... args) throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            System.out.println("Conexão bem-sucedida com o banco de dados!");
            System.out.println("Driver: " + connection.getMetaData().getDriverName());
            System.out.println("Versão: " + connection.getMetaData().getDatabaseProductVersion());
        }
    }
}
