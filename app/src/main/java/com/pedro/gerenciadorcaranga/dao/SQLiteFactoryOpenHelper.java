package com.pedro.gerenciadorcaranga.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SQLiteFactoryOpenHelper extends SQLiteOpenHelper {

    private static final String dbName = "carangamanger2.db";
    private static final  int version = 1;

    private static SQLiteFactoryOpenHelper instance;

    private SQLiteFactoryOpenHelper(Context context) {
        super(context, dbName, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        VeiculoDAO.criarTabela(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        VeiculoDAO.removeTabela(db);
        onCreate(db);
    }

    public static SQLiteFactoryOpenHelper getInstance(Context ctx) {
        if(instance == null){
            instance = new SQLiteFactoryOpenHelper(ctx);
        }
        return instance;
    }
}
