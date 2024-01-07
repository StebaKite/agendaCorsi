package com.example.agendaCorsi.database.table;

import android.provider.BaseColumns;

public class Fascia implements BaseColumns {

    public static String TABLE_NAME = "fascia";
    public static Integer ID_FASCIA = 0;
    public static Integer ID_CORSO = 1;
    public static Integer DESCRIZIONE = 2;
    public static Integer GIORNO_SETTIMANA = 3;
    public static Integer ORA_INIZIO = 4;
    public static Integer ORA_FINE = 5;
    public static Integer CAPIENZA = 6;
    public static Integer DATA_CREAZIONE = 7;
    public static Integer DATA_ULTIMO_AGGIORNAMENTO = 8;

    public String idFascia;
    public String idCorso;
    public String descrizione;
    public String giornoSettimana;
    public String oraInizio;
    public String oraFine;
    public String capienza;
    public String dataCreazione;
    public String dataUltimoAggiornamento;

    public Fascia(String idFascia, String idCorso, String descrizione, String giornoSettimana, String oraInizio, String oraFine, String capienza, String dataCreazione, String dataUltimoAggiornamento) {
        this.idFascia = idFascia;
        this.idCorso = idCorso;
        this.descrizione = descrizione;
        this.giornoSettimana = giornoSettimana;
        this.oraInizio = oraInizio;
        this.oraFine = oraFine;
        this.capienza = capienza;
        this.dataCreazione = dataCreazione;
        this.dataUltimoAggiornamento = dataUltimoAggiornamento;
    }

    public String getIdFascia() {
        return idFascia;
    }

    public void setIdFascia(String idFascia) {
        this.idFascia = idFascia;
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

    public String getGiornoSettimana() {
        return giornoSettimana;
    }

    public void setGiornoSettimana(String giornoSettimana) {
        this.giornoSettimana = giornoSettimana;
    }

    public String getOraInizio() {
        return oraInizio;
    }

    public void setOraInizio(String oraInizio) {
        this.oraInizio = oraInizio;
    }

    public String getOraFine() {
        return oraFine;
    }

    public void setOraFine(String oraFine) {
        this.oraFine = oraFine;
    }

    public String getCapienza() {
        return capienza;
    }

    public void setCapienza(String capienza) {
        this.capienza = capienza;
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
