package com.example.agendaCorsi.database.table;

import android.provider.BaseColumns;

public class ContattoIscritto implements BaseColumns {

    public static Integer NOME_CONTATTO = 0;
    public static Integer ID_ISCRIZIONE = 1;
    public static Integer STATO = 2;
    public static Integer DATA_NASCITA = 3;
    public static Integer ID_ELEMENTO = 4;
    public static Integer ID_PRESENZA = 5;
    public static Integer DATA_CONFERMA_PRESENZA = 6;
    public static Integer ID_ASSENZA = 7;
    public static Integer DATA_CONFERMA_ASSENZA = 8;
    public static Integer STATO_ELEMENTO = 9;

    public String nomeContatto;
    public String idIscrizione;
    public String stato;
    public String dataNascita;
    public String idElemento;
    public String idPresenza;
    public String dataConfermaPresenza;
    public String idAssenza;
    public String dataConfermaAssenza;
    public String statoElemento;

    public ContattoIscritto(String nomeContatto, String idIscrizione, String stato,
                            String data_nascita, String idElemento,
                            String idPresenza, String dataConfermaPresenza, String idAssenza, String dataConfermaAssenza,
                            String statoElemento) {
        this.nomeContatto = nomeContatto;
        this.idIscrizione = idIscrizione;
        this.stato = stato;
        this.dataNascita = data_nascita;
        this.idElemento = idElemento;
        this.idPresenza = idPresenza;
        this.dataConfermaPresenza = dataConfermaPresenza;
        this.idAssenza = idAssenza;
        this.dataConfermaAssenza = dataConfermaAssenza;
        this.statoElemento = statoElemento;
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

    public String getDataConfermaPresenza() {
        return dataConfermaPresenza;
    }

    public void setDataConfermaPresenza(String dataConfermaPresenza) {
        this.dataConfermaPresenza = dataConfermaPresenza;
    }

    public String getStatoElemento() {
        return statoElemento;
    }

    public void setStatoElemento(String statoElemento) {
        this.statoElemento = statoElemento;
    }

    public String getIdAssenza() {
        return idAssenza;
    }

    public void setIdAssenza(String idAssenza) {
        this.idAssenza = idAssenza;
    }

    public String getDataConfermaAssenza() {
        return dataConfermaAssenza;
    }

    public void setDataConfermaAssenza(String dataConfermaAssenza) {
        this.dataConfermaAssenza = dataConfermaAssenza;
    }
}
