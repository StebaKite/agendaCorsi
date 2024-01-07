package com.example.agendaCorsi.database.table;

import android.provider.BaseColumns;

public class Corso implements BaseColumns {

    public static String TABLE_NAME = "corso";
    public static Integer ID_CORSO = 0;
    public static Integer DESCRIZIONE = 1;
    public static Integer SPORT = 2;
    public static Integer STATO = 3;
    public static Integer DATA_INIZIO_VALIDITA = 4;
    public static Integer DATA_FINE_VALIDITA = 5;
    public static Integer DATA_CREAZIONE = 6;
    public static Integer DATA_ULTIMO_AGGIORNAMENTO = 7;

    public String idCorso;
    public String descrizione;
    public String sport;
    public String stato;
    public String dataInizioValidita;
    public String dataFineValidita;
    public String dataCreazione;
    public String dataUltimoAggiornamento;

    public Corso(String idCorso, String descrizione, String sport, String stato, String dataInizioValidita, String dataFineValidita, String dataCreazione, String dataUltimoAggiornamento) {
        this.idCorso = idCorso;
        this.descrizione = descrizione;
        this.sport = sport;
        this.stato = stato;
        this.dataInizioValidita = dataInizioValidita;
        this.dataFineValidita = dataFineValidita;
        this.dataCreazione = dataCreazione;
        this.dataUltimoAggiornamento = dataUltimoAggiornamento;
    }

    public String getIdCorso() {
        return idCorso;
    }

    public void setIdCorso(String idCorso) {
        this.idCorso = idCorso;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    public String getDataInizioValidita() {
        return dataInizioValidita;
    }

    public void setDataInizioValidita(String dataInizioValidita) {
        this.dataInizioValidita = dataInizioValidita;
    }

    public String getDataFineValidita() {
        return dataFineValidita;
    }

    public void setDataFineValidita(String dataFineValidita) {
        this.dataFineValidita = dataFineValidita;
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
