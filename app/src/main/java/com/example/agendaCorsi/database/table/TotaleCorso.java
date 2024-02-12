package com.example.agendaCorsi.database.table;

import android.provider.BaseColumns;

import java.util.HashMap;
import java.util.Map;

public class TotaleCorso implements BaseColumns {

    public static String TABLE_NAME = "totale_corso";
    public static String VIEW_TOT_ISCRIZIONI = "tot_iscrizioni_view";

    public static Integer ID_TOTALE = 0;
    public static Integer DESCRIZIONE_CORSO = 1;
    public static Integer ANNO_SVOLGIMENTO = 2;
    public static Integer NOME_TOTALE = 3;
    public static Integer VALORE_TOTALE = 4;

    public static Map<Integer, String> totIscrizioniCorsoColumns;
    static {
        totIscrizioniCorsoColumns = new HashMap<>();
        totIscrizioniCorsoColumns.put(ID_TOTALE, "id_totale");
        totIscrizioniCorsoColumns.put(DESCRIZIONE_CORSO, "descrizione_corso");
        totIscrizioniCorsoColumns.put(ANNO_SVOLGIMENTO, "anno_svolgimento");
        totIscrizioniCorsoColumns.put(NOME_TOTALE, "nome_totale");
        totIscrizioniCorsoColumns.put(VALORE_TOTALE, "valore_totale");
    }


    public String idTotale;
    public String descrizioneCorso;
    public String annoSvolgimento;
    public String nomeTotale;
    public String valoreTotale;

    public TotaleCorso(String idTotale, String descrizioneCorso, String annosvolgimento, String nomeTotale, String valoreTotale) {
        this.idTotale = idTotale;
        this.descrizioneCorso = descrizioneCorso;
        this.annoSvolgimento = annosvolgimento;
        this.nomeTotale = nomeTotale;
        this.valoreTotale = valoreTotale;
    }

    public String getIdTotale() {
        return idTotale;
    }

    public void setIdTotale(String idTotale) {
        this.idTotale = idTotale;
    }

    public String getDescrizioneCorso() {
        return descrizioneCorso;
    }

    public void setDescrizioneCorso(String descrizioneCorso) {
        this.descrizioneCorso = descrizioneCorso;
    }

    public String getAnnoSvolgimento() {
        return annoSvolgimento;
    }

    public void setAnnoSvolgimento(String annoSvolgimento) {
        this.annoSvolgimento = annoSvolgimento;
    }

    public String getNomeTotale() {
        return nomeTotale;
    }

    public void setNomeTotale(String nomeTotale) {
        this.nomeTotale = nomeTotale;
    }

    public String getValoreTotale() {
        return valoreTotale;
    }

    public void setValoreTotale(String valoreTotale) {
        this.valoreTotale = valoreTotale;
    }

}
