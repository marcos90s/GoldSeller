package com.marcos90s.goldSellerAPI.utils;

import com.marcos90s.goldSellerAPI.entities.RealTransaction;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public final class Utils {

    //Método para montar informações das RealTransaction atribuídas ao usuário
    public static List<String> makeRealTransactionInfo(List<RealTransaction> transactions){
        return transactions.stream().map(tx -> String.format("Para: %s | Descrição: %s | Gold: %s | Valor: %s | Data: %s" , tx.getCharName()
        ,tx.getDescription(), tx.getAmountInGold(), tx.getAmount(), tx.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))).collect(Collectors.toList());
    }
}
