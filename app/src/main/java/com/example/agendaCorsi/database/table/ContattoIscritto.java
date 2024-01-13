package com.example.agendaCorsi.database.table;

import android.provider.BaseColumns;

public class ContattoIscritto implements BaseColumns {

    public static Integer NOME_CONTATTO = 0;
    public static Integer ID_ISCRIZIONE = 1;

    public String nomeContatto;
    public String idIscrizione;

    public ContattoIscritto(String nomeContatto, String idIscrizione) {
        this.nomeContatto = nomeContatto;
        this.idIscrizione = idIscrizione;
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
}
