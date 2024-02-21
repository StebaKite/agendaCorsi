package com.example.agendaCorsi.database.table;

import android.provider.BaseColumns;
import java.util.HashMap;
import java.util.Map;

public class GiornoSettimana implements BaseColumns {

    public static String TABLE_NAME = "giorno_settimana";

    public static Integer NUMERO_GIORNO = 0;
    public static Integer NOME_GIORNO_ABBREVIATO = 1;
    public static Integer NOME_GIORNO_ESTESO = 2;

    public static Map<Integer, String> giornoSettimanaColumns;
    static {
        giornoSettimanaColumns = new HashMap<>();
        giornoSettimanaColumns.put(NUMERO_GIORNO, "numero_giorno");
        giornoSettimanaColumns.put(NOME_GIORNO_ABBREVIATO, "nome_giorno_abbreviato");
        giornoSettimanaColumns.put(NOME_GIORNO_ESTESO, "nome_giorno_esteso");
    }

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
