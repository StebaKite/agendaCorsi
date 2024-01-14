package com.example.agendaCorsi.database.table;

import android.provider.BaseColumns;

public class FasciaCorso implements BaseColumns {

    public static Integer DESCRIZIONE_CORSO = 0;
    public static Integer STATO = 1;
    public static Integer SPORT = 2;
    public static Integer NUMERO_GIORNO = 3;
    public static Integer DESCRIZIONE_FASCIA = 4;
    public static Integer ID_FASCIA = 5;
    public static Integer CAPIENZA = 6;
    public static Integer GIORNO_SETTIMANA = 7;
    public static Integer TOTALE_FASCIA = 8;
    public static Integer ID_CORSO = 9;
    public static Integer EMAIL_CONTATTO;

    public String descrizioneCorso;
    public String stato;
    public String sport;
    public String numeroGiorno;
    public String descrizioneFascia;
    public String idFascia;
    public String capienza;
    public String giornoSettimana;
    public String totaleFascia;
    public String idCorso;

    public FasciaCorso(String descrizioneCorso, String stato, String sport, String numeroGiorno, String descrizioneFascia, String idFascia, String capienza, String giornoSettimana, String totaleFascia, String idCorso) {
        this.descrizioneCorso = descrizioneCorso;
        this.stato = stato;
        this.sport = sport;
        this.numeroGiorno = numeroGiorno;
        this.descrizioneFascia = descrizioneFascia;
        this.idFascia = idFascia;
        this.capienza = capienza;
        this.giornoSettimana = giornoSettimana;
        this.totaleFascia = totaleFascia;
        this.idCorso = idCorso;
    }

    public String getDescrizioneCorso() {
        return descrizioneCorso;
    }

    public void setDescrizioneCorso(String descrizioneCorso) {
        this.descrizioneCorso = descrizioneCorso;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    public String getSSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public String getNumeroGiorno() {
        return numeroGiorno;
    }

    public void setNumeroGiorno(String numeroGiorno) {
        this.numeroGiorno = numeroGiorno;
    }

    public String getDescrizioneFascia() {
        return descrizioneFascia;
    }

    public void setDescrizioneFascia(String descrizioneFascia) {
        this.descrizioneFascia = descrizioneFascia;
    }

    public String getIdFascia() {
        return idFascia;
    }

    public void setIdFascia(String idFascia) {
        this.idFascia = idFascia;
    }

    public String getCapienza() {
        return capienza;
    }

    public void setCapienza(String capienza) {
        this.capienza = capienza;
    }

    public String getGiornoSettimana() {
        return giornoSettimana;
    }

    public void setGiornoSettimana(String giornoSettimana) {
        this.giornoSettimana = giornoSettimana;
    }

    public String getTotaleFascia() {
        return totaleFascia;
    }

    public void setTotaleFascia(String totaleFascia) {
        this.totaleFascia = totaleFascia;
    }

    public String getIdCorso() {
        return idCorso;
    }

    public void setIdCorso(String idCorso) {
        this.idCorso = idCorso;
    }

}
