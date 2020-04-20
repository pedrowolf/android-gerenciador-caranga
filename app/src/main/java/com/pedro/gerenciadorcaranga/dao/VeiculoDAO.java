package com.pedro.gerenciadorcaranga.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.pedro.gerenciadorcaranga.domain.Veiculo;

import java.util.ArrayList;
import java.util.List;

public class VeiculoDAO {

    public static void criarTabela(SQLiteDatabase db){
        String sqlCreate = "CREATE TABLE tb_veiculos (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "apelido TEXT NOT NULL," +
                "dono TEXT NOT NULL," +
                "montadora TEXT NOT NULL," +
                "tipo_combustivel TEXT NOT NULL,"+
                "km_rodado INTEGER NOT NULL," +
                "ano INTEGER NOT NULL,"+
                "ativo INTEGER NOT NULL);";
        db.execSQL(sqlCreate);
    }

    public static void removeTabela(SQLiteDatabase db){
        db.execSQL("DROP TABLE IF EXISTS tb_veiculos");
    }

    public List<Veiculo> listar(SQLiteDatabase db){
        try {
            Cursor c = db.rawQuery("select * from tb_veiculos order by id desc", null);

            List<Veiculo> lista = new ArrayList<>();
            while (c.moveToNext()) {
                Veiculo v = new Veiculo();
                v.setId(c.getInt(0));
                v.setApelido(c.getString(1));
                v.setDono(c.getString(2));
                v.setMontadora(c.getString(3));
                v.setTipoCombustivel(c.getString(4));
                v.setKmRodado(c.getInt(5));
                v.setAno(c.getInt(6));
                v.setAtivo((c.getInt(7) == 1 ? true : false));
                lista.add(v);
            }
            c.close();
            return lista;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public boolean inserir(Veiculo v, SQLiteDatabase db){
        try{
            ContentValues values = new ContentValues();
            values.put("apelido", v.getApelido());
            values.put("dono",v.getDono());
            values.put("montadora",v.getMontadora());
            values.put("tipo_combustivel",v.getTipoCombustivel());
            values.put("km_rodado",v.getKmRodado());
            values.put("ano",v.getAno());
            values.put("ativo",(v.getAtivo() ? 1 : 0));

            db.insert("tb_veiculos",null, values);

            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean alterar(Veiculo v, SQLiteDatabase db){
        try{
            ContentValues values = new ContentValues();
            values.put("apelido", v.getApelido());
            values.put("dono",v.getDono());
            values.put("montadora",v.getMontadora());
            values.put("tipo_combustivel",v.getTipoCombustivel());
            values.put("km_rodado",v.getKmRodado());
            values.put("ano",v.getAno());
            values.put("ativo",(v.getAtivo() ? 1 : 0));

            db.update("tb_veiculos", values, "id = ?", new String[]{String.valueOf(v.getId())});

            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean excluir(Integer id, SQLiteDatabase db){
        try{

            db.delete("tb_veiculos", "id = ?", new String[]{String.valueOf(id)});

            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }


}
