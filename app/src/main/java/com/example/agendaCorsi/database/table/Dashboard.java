package com.example.agendaCorsi.database.table;

import android.provider.BaseColumns;

public class Dashboard implements BaseColumns {

    public static Integer DESCRIZIONE_CORSO = 0;
    public static Integer DESCRIZIONE_FASCIA = 1;
    public static Integer GIORNO_SETTIMANA = 2;
    public static Integer TOTALE_FASCIA = 3;

    public String descrizioneCorso;
    public String descrizioneFascia;
    public String giornoSettimana;
    public String totaleFascia;

    public Dashboard(String descrizioneCorso, String descrizioneFascia, String giornoSettimana, String totaleFascia) {
        this.descrizioneCorso = descrizioneCorso;
        this.descrizioneFascia = descrizioneFascia;
        this.giornoSettimana = giornoSettimana;
        this.totaleFascia = totaleFascia;
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
}
