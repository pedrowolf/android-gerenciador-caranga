package com.pedro.gerenciadorcaranga.dao;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.pedro.gerenciadorcaranga.domain.Gasto;

@Database(entities = {Gasto.class}, version = 1, exportSchema = false)
public abstract class RoomDatabaseFactory extends RoomDatabase {

    public abstract GastoDAO gastoDAO();

    private static RoomDatabaseFactory instance;

    public static RoomDatabaseFactory getInstance(Context context) {

        if(instance == null){
            synchronized (RoomDatabaseFactory.class){
                if(instance == null){
                    instance = Room.databaseBuilder(context,RoomDatabaseFactory.class,"carangamanger3.db").allowMainThreadQueries().build();
                }
            }
        }

        return instance;
    }
}
