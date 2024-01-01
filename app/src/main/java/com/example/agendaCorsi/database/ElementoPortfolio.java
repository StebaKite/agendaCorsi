package com.example.agendaCorsi.database;

import android.provider.BaseColumns;

public class ElementoPortfolio implements BaseColumns {

    public static String TABLE_NAME = "elemento_portfolio";
    public static Integer ID_ELEMENTO = 0;
    public static Integer ID_CONTATTO = 1;
    public static Integer DESCRIZIONE = 2;
    public static Integer SPORT = 3;
    public static Integer NUMERO_LEZIONI = 4;
    public static Integer DATA_ULTIMA_RICARICA = 5;
    public static Integer STATO = 6;
    public static Integer DATA_CREAZIONE = 7;
    public static Integer DATA_ULTIMO_AGGIORNAMENTO = 8;

    public String idElemento;
    public String idContatto;
    public String descrizione;
    public String sport;
    public String numeroLezioni;
    public String dataUltimaRicarica;
    public String stato;
    public String dataCreazione;
    public String dataUltimoAggiornamento;

    public ElementoPortfolio(String idElemento, String idContatto, String descrizione, String sport, String numeroLezioni, String dataUltimaRicarica, String stato) {
        this.idElemento = idElemento;
        this.idContatto = idContatto;
        this.descrizione = descrizione;
        this.sport = sport;
        this.numeroLezioni = numeroLezioni;
        this.dataUltimaRicarica = dataUltimaRicarica;
        this.stato = stato;
        this.dataCreazione = "";
        this.dataUltimoAggiornamento = "";
    }

    public String getIdElemento() {
        return idElemento;
    }

    public void setIdElemento(String idElemento) {
        this.idElemento = idElemento;
    }

    public String getIdContatto() {
        return idContatto;
    }

    public void setIdContatto(String idContatto) {
        this.idContatto = idContatto;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getNumeroLezioni() {
        return numeroLezioni;
    }

    public void setNumeroLezioni(String numeroLezioni) {
        this.numeroLezioni = numeroLezioni;
    }

    public String getDataUltimaRicarica() {
        return dataUltimaRicarica;
    }

    public void setDataUltimaRicarica(String dataUltimaRicarica) {
        this.dataUltimaRicarica = dataUltimaRicarica;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
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

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }
}
