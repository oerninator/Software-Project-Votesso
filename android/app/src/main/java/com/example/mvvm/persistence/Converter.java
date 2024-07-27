package com.example.mvvm.persistence;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Konverter-Klasse für die Room-Datenbank
 */
public class Converter {

    /**
     * Konverter um aus einer Liste aus 'Long'-Elementen einen String zu generieren.
     *
     * @param id Liste vom Typ 'List<Long>'
     * @return Json-String der die Liste repräsentiert.
     */
    @TypeConverter
    public String LongListToJson(List<Long> id) {
        if (id == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Long>>() {}.getType();
        String json = gson.toJson(id, type);
        return json;
    }

    /**
     * Konverter um aus einem String eine 'Long'-Liste zu generieren.
     *
     * @param string Json-String
     * @return Liste von Typ 'List<Long>'
     */
    @TypeConverter
    public List<Long> jsonStringArrayToLongList(String string) {
        if (string.equals(null)) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Long>>() {}.getType();
        List<Long> countryLangList = gson.fromJson(string, type);
        return countryLangList;


    }
}
