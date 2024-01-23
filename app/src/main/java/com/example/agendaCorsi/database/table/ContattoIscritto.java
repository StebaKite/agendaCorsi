package com.example.agendaCorsi.database.table;

import android.provider.BaseColumns;

public class ContattoIscritto implements BaseColumns {

    public static Integer NOME_CONTATTO = 0;
    public static Integer ID_ISCRIZIONE = 1;
    public static Integer STATO = 2;
    public static Integer DATA_NASCITA = 3;
    public static Integer ID_ELEMENTO = 4;
    public static Integer ID_PRESENZA = 5;
    public static Integer DATA_CONFERMA = 6;

    public String nomeContatto;
    public String idIscrizione;
    public String stato;
    public String dataNascita;
    public String idElemento;
    public String idPresenza;
    public String dataConferma;

    public ContattoIscritto(String nomeContatto, String idIscrizione, String stato, String data_nascita, String idElemento, String idPresenza, String dataConferma) {
        this.nomeContatto = nomeContatto;
        this.idIscrizione = idIscrizione;
        this.stato = stato;
        this.dataNascita = data_nascita;
        this.idElemento = idElemento;
        this.idPresenza = idPresenza;
        this.dataConferma = dataConferma;
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

    public String getIdElemento() {
        return idElemento;
    }

    public void setIdElemento(String idElemento) {
        this.idElemento = idElemento;
    }

    public String getIdPresenza() {
        return idPresenza;
    }

    public void setIdPresenza(String idPresenza) {
        this.idPresenza = idPresenza;
    }

    public String getDataConferma() {
        return dataConferma;
    }

    public void setDataConferma(String dataConferma) {
        this.dataConferma = dataConferma;
    }
}
