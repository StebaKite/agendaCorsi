package com.example.agendaCorsi.database.table;

import android.provider.BaseColumns;

public class Dashboard implements BaseColumns {

    public static Integer ID_CORSO = 0;
    public static Integer DESCRIZIONE_CORSO = 1;
    public static Integer DESCRIZIONE_FASCIA = 2;
    public static Integer GIORNO_SETTIMANA = 3;
    public static Integer ID_FASCIA = 4;
    public static Integer TOTALE_FASCIA = 5;

    public String idCorso;
    public String descrizioneCorso;
    public String descrizioneFascia;
    public String giornoSettimana;
    public String totaleFascia;
    public String idFascia;

    public Dashboard(String idCorso, String descrizioneCorso, String descrizioneFascia, String giornoSettimana, String idFascia, String totaleFascia) {
        this.idCorso = idCorso;
        this.descrizioneCorso = descrizioneCorso;
        this.descrizioneFascia = descrizioneFascia;
        this.giornoSettimana = giornoSettimana;
        this.idFascia = idFascia;
        this.totaleFascia = totaleFascia;
    }

    public String getIdCorso() {
        return idCorso;
    }

    public void setIdCorso(String idCorso) {
        this.idCorso = idCorso;
    }

    public String getDescrizioneCorso() {
        return descrizioneCorso;
    }

    public void setDescrizioneCorso(String descrizioneCorso) {
        this.descrizioneCorso = descrizioneCorso;
    }

    public String getDescrizioneFascia() {
        return descrizioneFascia;
    }

    public void setDescrizioneFascia(String descrizioneFascia) {
        this.descrizioneFascia = descrizioneFascia;
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

    public String getIdFascia() {
        return idFascia;
    }

    public void setIdFascia(String idFascia) {
        this.idFascia = idFascia;
    }
}
