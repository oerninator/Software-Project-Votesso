package de.cau.inf.se.sopro.persistence;

import android.content.Context;

import androidx.room.Room;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import de.cau.inf.se.sopro.model.BookDao;

@Module
@InstallIn(SingletonComponent.class)
public class BookDatabaseModule {

    @Singleton
    @Provides
    public BookDatabase provideDatabase(@ApplicationContext Context appContext) {
        return Room.databaseBuilder(appContext, BookDatabase.class, "SoPro-Database")
                .addTypeConverter(new Converter()).build();
    }

    @Singleton
    @Provides
    public BookDao provideBookDao(BookDatabase database) {
        return database.getBookDao();
    }

}
