package com.example.agendaCorsi.database;

import android.provider.BaseColumns;

public class Contatto implements BaseColumns {

    public static String TABLE_NAME = "contatto";
    public static Integer ID_CONTATTO = 0;
    public static Integer NOME = 1;
    public static Integer INDIRIZZO = 2;
    public static Integer TELEFONO = 3;
    public static Integer EMAIL = 4;
    public static Integer DATA_CREAZIONE = 5;
    public static Integer DATA_ULTIMO_AGGIORNAMENTO = 6;

    public String id;
    public String nome;
    public String indirizzo;
    public String telefono;
    public String email;
    public String dataCreazione;
    public String dataUltimoAggiornamento;

    public Contatto(String id, String nome, String indirizzo, String telefono, String email) {
        this.id = id;
        this.nome = nome;
        this.indirizzo = indirizzo;
        this.telefono = telefono;
        this.email = email;
        this.dataCreazione = "";
        this.dataUltimoAggiornamento = "";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDataCreazione() {
        return dataCreazione;
    }

    public void setDataCreazione(String dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public String getDataUltimoAggiornamento() {
        return dataUltimoAggiornamento;
    }

    public void setDataUltimoAggiornamento(String dataUltimoAggiornamento) {
        this.dataUltimoAggiornamento = dataUltimoAggiornamento;
    }
}
