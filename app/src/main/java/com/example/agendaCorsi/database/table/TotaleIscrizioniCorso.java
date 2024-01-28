package com.example.agendaCorsi.database.table;

import android.provider.BaseColumns;

public class TotaleIscrizioniCorso implements BaseColumns {

    public static Integer DESCRIZIONE_CORSO = 0;
    public static Integer ANNO_SVOLGIMENTO = 1;
    public static Integer TOTALE_ISCRIZIONI = 2;

    public String idCorso;
    public String descrizioneCorso;
    public String annoSvolgimento;
    public String totaleIscrizioni;

    public TotaleIscrizioniCorso(String idCorso, String descrizioneCorso, String annoSvolgimento, String totaleIscrizioni) {
        this.idCorso = idCorso;
        this.descrizioneCorso = descrizioneCorso;
        this.annoSvolgimento = annoSvolgimento;
        this.totaleIscrizioni = totaleIscrizioni;
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

    public String getTotaleIscrizioni() {
        return totaleIscrizioni;
    }

    public void setTotaleIscrizioni(String totaleIscrizioni) {
        this.totaleIscrizioni = totaleIscrizioni;
    }

    public String getIdCorso() {
        return idCorso;
    }

    public void setIdCorso(String idCorso) {
        this.idCorso = idCorso;
    }
}
