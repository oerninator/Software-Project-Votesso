package de.cau.inf.se.sopro.persistence;

import androidx.room.ProvidedTypeConverter;
import androidx.room.TypeConverter;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.cau.inf.se.sopro.model.Author;
import de.cau.inf.se.sopro.model.Genre;

@ProvidedTypeConverter
public class Converter {

    JsonAdapter<Author> jsonAuthorAdapter = new Moshi.Builder().build().adapter(Author.class);
    JsonAdapter<List<Genre>> jsonGenreListAdapter = new Moshi.Builder().build().adapter(Types.newParameterizedType(List.class, Genre.class));

    @TypeConverter
    public Author jsonStringToAuthor(String string) {
        try {
            return jsonAuthorAdapter.fromJson(string);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Author();
    }

    @TypeConverter
    public String authorToJsonString(Author author) {
        return jsonAuthorAdapter.toJson(author);
    }

    @TypeConverter
    public List<Genre> jsonStringArrayToGenreList(String string) {
        try {
            return jsonGenreListAdapter.fromJson(string);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @TypeConverter
    public String authorToJsonString(List<Genre> genres) {
        return jsonGenreListAdapter.toJson(genres);
    }

}
