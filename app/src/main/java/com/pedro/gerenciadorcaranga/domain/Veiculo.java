package com.pedro.gerenciadorcaranga.domain;

import java.io.Serializable;

public class Veiculo implements Serializable {

    private Integer id;

    private String apelido;

    private String dono;

    private Integer kmRodado;

    private String tipoCombustivel;

    private String montadora;

    private Integer ano;

    private Boolean ativo;

    public Veiculo(Integer id, String apelido, String dono, Integer kmRodado, String tipoCombustivel, String montadora, Integer ano, Boolean ativo) {
        this.id = id;
        this.apelido = apelido;
        this.dono = dono;
        this.kmRodado = kmRodado;
        this.tipoCombustivel = tipoCombustivel;
        this.montadora = montadora;
        this.ano = ano;
        this.ativo = ativo;
    }

    public Veiculo() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getApelido() {
        return apelido;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
    }

    public String getDono() {
        return dono;
    }

    public void setDono(String dono) {
        this.dono = dono;
    }

    public Integer getKmRodado() {
        return kmRodado;
    }

    public void setKmRodado(Integer kmRodado) {
        this.kmRodado = kmRodado;
    }

    public String getTipoCombustivel() {
        return tipoCombustivel;
    }

    public void setTipoCombustivel(String tipoCombustivel) {
        this.tipoCombustivel = tipoCombustivel;
    }

    public String getMontadora() {
        return montadora;
    }

    public void setMontadora(String montadora) {
        this.montadora = montadora;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}
