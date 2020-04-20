package com.pedro.gerenciadorcaranga.domain;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Gasto implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private Integer id;

    @NonNull
    private String descricao;

    @NonNull
    private Long timestamp;

    @NonNull
    private Float valor;

    @NonNull
    private Integer veiculoId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getVeiculoId() {
        return veiculoId;
    }

    public void setVeiculoId(Integer veiculoId) {
        this.veiculoId = veiculoId;
    }

    public void setValor(@NonNull Float valor) {
        this.valor = valor;
    }

    @NonNull
    public Float getValor() {
        return valor;
    }
}
