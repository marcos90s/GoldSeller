package com.marcos90s.goldSellerAPI.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class Utils {

    public static LocalDateTime dateTimeFormatter(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyy hh:mm");
        LocalDateTime dateTime = LocalDateTime.now();
        dateTime.format(formatter);
        return dateTime;
    }
}
