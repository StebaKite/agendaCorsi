package com.example.agendaCorsi.database.table;

import android.provider.BaseColumns;

public class ContattoIscritto implements BaseColumns {

    public static Integer NOME_CONTATTO = 0;
    public static Integer ID_ISCRIZIONE = 1;
    public static Integer STATO = 2;
    public static Integer DATA_NASCITA = 3;

    public String nomeContatto;
    public String idIscrizione;
    public String stato;
    public String dataNascita;

    public ContattoIscritto(String nomeContatto, String idIscrizione, String stato, String data_nascita) {
        this.nomeContatto = nomeContatto;
        this.idIscrizione = idIscrizione;
        this.stato = stato;
        this.dataNascita = data_nascita;
    }

    public String getNomeContatto() {
        return nomeContatto;
    }

    public void setNomeContatto(String nomeContatto) {
        this.nomeContatto = nomeContatto;
    }

    public String getIdIscrizione() {
        return idIscrizione;
    }

    public void setIdIscrizione(String idIscrizione) {
        this.idIscrizione = idIscrizione;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    public String getDataNascita() {
        return dataNascita;
    }

    public void setDataNascita(String dataNascita) {
        this.dataNascita = dataNascita;
    }
}
