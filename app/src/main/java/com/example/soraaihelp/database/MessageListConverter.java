package com.example.soraaihelp.database;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class MessageListConverter {
    private static Gson gson = new Gson();
    
    @TypeConverter
    public static List<MessageEntity> fromString(String value) {
        if (value == null) {
            return Collections.emptyList();
        }
        
        Type listType = new TypeToken<List<MessageEntity>>() {}.getType();
        return gson.fromJson(value, listType);
    }
    
    @TypeConverter
    public static String fromMessageList(List<MessageEntity> list) {
        return gson.toJson(list);
    }
    
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }
    
    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
} 