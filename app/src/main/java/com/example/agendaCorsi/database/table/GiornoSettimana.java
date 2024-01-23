package com.example.agendaCorsi.database.table;

import android.provider.BaseColumns;

public class GiornoSettimana implements BaseColumns {

    public static String TABLE_NAME = "giorno_settimana";
    public static Integer NUMERO_GIORNO = 0;
    public static Integer NOME_GIORNO_ABBREVIATO = 1;
    public static Integer NOME_GIORNO_ESTESO = 2;

    public String numeroGiorno;
    public String nomeGiornoAbbreviato;
    public String nomeGiornoEsteso;

    public GiornoSettimana(String numeroGiorno, String nomeGiornoAbbreviato, String nomeGiornoEsteso) {
        this.numeroGiorno = numeroGiorno;
        this.nomeGiornoAbbreviato = nomeGiornoAbbreviato;
        this.nomeGiornoEsteso = nomeGiornoEsteso;
    }

    public String getNumeroGiorno() {
        return numeroGiorno;
    }

    public void setNumeroGiorno(String numeroGiorno) {
        this.numeroGiorno = numeroGiorno;
    }

    public String getNomeGiornoAbbreviato() {
        return nomeGiornoAbbreviato;
    }

    public void setNomeGiornoAbbreviato(String nomeGiornoAbbreviato) {
        this.nomeGiornoAbbreviato = nomeGiornoAbbreviato;
    }

    public String getNomeGiornoEsteso() {
        return nomeGiornoEsteso;
    }

    public void setNomeGiornoEsteso(String nomeGiornoEsteso) {
        this.nomeGiornoEsteso = nomeGiornoEsteso;
    }
}
