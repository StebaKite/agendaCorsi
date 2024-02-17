package com.example.agendaCorsi.database.table;

import android.provider.BaseColumns;

public class Assenza implements BaseColumns {

    public static String TABLE_NAME = "assenza";
    public static Integer ID_ASSENZA = 0;
    public static Integer ID_ISCRIZIONE = 1;
    public static Integer DATA_CONFERMA = 2;

    public String idAssenza;
    public String idIscrizione;
    public String dataConferma;

    public Assenza(String idAssenza, String idIscrizione, String dataConferma) {
        this.idAssenza = idAssenza;
        this.idIscrizione = idIscrizione;
        this.dataConferma = dataConferma;
    }

    public String getIdAssenza() {
        return idAssenza;
    }

    public void setIdAssenza(String idAssenza) {
        this.idAssenza = idAssenza;
    }

    public String getIdIscrizione() {
        return idIscrizione;
    }

    public void setIdIscrizione(String idIscrizione) {
        this.idIscrizione = idIscrizione;
    }

    public String getDataConferma() {
        return dataConferma;
    }

    public void setDataConferma(String dataConferma) {
        this.dataConferma = dataConferma;
    }
}
