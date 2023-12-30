package com.example.agendaCorsi.database;

import android.provider.BaseColumns;

public class ElementoPortfolio implements BaseColumns {

    public static String TABLE_NAME = "elemento_portfolio";
    public static Integer ID_ELEMENTO = 0;
    public static Integer ID_CONTATTO = 1;
    public static Integer DESCRIZIONE = 2;
    public static Integer NUMERO_LEZIONI = 3;
    public static Integer DATA_ULTIMA_RICARICA = 4;
    public static Integer STATO = 5;
    public static Integer DATA_CREAZIONE = 6;
    public static Integer DATA_ULTIMO_AGGIORNAMENTO = 7;

    public String idElemento;
    public String idContatto;
    public String descrizione;
    public String numeroLezioni;
    public String dataUltimaRicarica;
    public String stato;
    public String dataCreazione;
    public String dataUltimoAggiornamento;

    public ElementoPortfolio(String idElemento, String idContatto, String descrizione, String numeroLezioni, String dataUltimaRicarica, String stato) {
        this.idElemento = idElemento;
        this.idContatto = idContatto;
        this.descrizione = descrizione;
        this.numeroLezioni = numeroLezioni;
        this.dataUltimaRicarica = dataUltimaRicarica;
        this.stato = stato;
        this.dataCreazione = "";
        this.dataUltimoAggiornamento = "";
    }

    public static String getTableName() {
        return TABLE_NAME;
    }

    public static void setTableName(String tableName) {
        TABLE_NAME = tableName;
    }

    public static Integer getIdElemento() {
        return ID_ELEMENTO;
    }

    public void setIdElemento(String idElemento) {
        this.idElemento = idElemento;
    }

    public static void setIdElemento(Integer idElemento) {
        ID_ELEMENTO = idElemento;
    }

    public static Integer getIdContatto() {
        return ID_CONTATTO;
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

    public static void setIdContatto(Integer idContatto) {
        ID_CONTATTO = idContatto;
    }

    public static Integer getDESCRIZIONE() {
        return DESCRIZIONE;
    }

    public static void setDESCRIZIONE(Integer DESCRIZIONE) {
        ElementoPortfolio.DESCRIZIONE = DESCRIZIONE;
    }

    public static Integer getNumeroLezioni() {
        return NUMERO_LEZIONI;
    }

    public void setNumeroLezioni(String numeroLezioni) {
        this.numeroLezioni = numeroLezioni;
    }

    public static void setNumeroLezioni(Integer numeroLezioni) {
        NUMERO_LEZIONI = numeroLezioni;
    }

    public static Integer getDataUltimaRicarica() {
        return DATA_ULTIMA_RICARICA;
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

    public static void setDataUltimaRicarica(Integer dataUltimaRicarica) {
        DATA_ULTIMA_RICARICA = dataUltimaRicarica;
    }

    public static Integer getSTATO() {
        return STATO;
    }

    public static void setSTATO(Integer STATO) {
        ElementoPortfolio.STATO = STATO;
    }

    public static Integer getDataCreazione() {
        return DATA_CREAZIONE;
    }

    public void setDataCreazione(String dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public static void setDataCreazione(Integer dataCreazione) {
        DATA_CREAZIONE = dataCreazione;
    }

    public static Integer getDataUltimoAggiornamento() {
        return DATA_ULTIMO_AGGIORNAMENTO;
    }

    public void setDataUltimoAggiornamento(String dataUltimoAggiornamento) {
        this.dataUltimoAggiornamento = dataUltimoAggiornamento;
    }

    public static void setDataUltimoAggiornamento(Integer dataUltimoAggiornamento) {
        DATA_ULTIMO_AGGIORNAMENTO = dataUltimoAggiornamento;
    }
}
