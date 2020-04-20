package com.pedro.gerenciadorcaranga.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.pedro.gerenciadorcaranga.domain.Gasto;

import java.util.List;

@Dao
public interface GastoDAO {

    @Query("SELECT * from gasto WHERE veiculoId = :idVeiculo ORDER BY id DESC")
    List<Gasto> loadAll(Integer idVeiculo);

    @Query("SELECT * FROM gasto WHERE veiculoId = :idVeiculo AND timestamp >= :initTimestamp AND timestamp <= :finalTimestamp")
    List<Gasto> loadByDates(Integer idVeiculo ,Long initTimestamp, Long finalTimestamp);

    @Insert
    void insert(Gasto gasto);

    @Update
    void update(Gasto gasto);

    @Delete
    void remove(Gasto gasto);
}
