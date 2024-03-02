package com.example.agendaCorsi.database.table;

import android.provider.BaseColumns;

import java.util.HashMap;
import java.util.Map;

public class Presenza implements BaseColumns {

    public static String TABLE_NAME = "presenza";

    public static Integer ID_PRESENZA = 0;
    public static Integer ID_ISCRIZIONE = 1;
    public static Integer DATA_CONFERMA = 2;

    public static Map<Integer, String> presenzaColumns;
    static {
        presenzaColumns = new HashMap<>();
        presenzaColumns.put(ID_PRESENZA, "id_presenza");
        presenzaColumns.put(ID_ISCRIZIONE, "id_iscrizione");
        presenzaColumns.put(DATA_CONFERMA, "data_conferma");
    }
    public String idPresenza;
    public String idIscrizione;
    public String dataConferma;

    public Presenza(String idPresenza, String idIscrizione, String dataConferma) {
        this.idPresenza = idPresenza;
        this.idIscrizione = idIscrizione;
        this.dataConferma = dataConferma;
    }

    public String getIdPresenza() {
        return idPresenza;
    }

    public void setIdPresenza(String idPresenza) {
        this.idPresenza = idPresenza;
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
