package com.example.agendaCorsi.database.table;

import android.provider.BaseColumns;

public class ContattoIscrivibile implements BaseColumns {

    public static Integer NOME_CONTATTO = 0;
    public static Integer ID_ELEMENTO = 1;
    public static Integer EMAIL_CONTATTO = 2;
    public static Integer DATA_NASCITA = 3;

    public String nomeContatto;
    public String idElemento;
    public String emailContatto;
    public String idFascia;
    public String idCorso;
    public String sport;
    public String dataNascita;

    public ContattoIscrivibile(String nomeContatto, String idElemento, String emailContatto, String idFascia, String idCorso, String sport, String dataNascita) {
        this.nomeContatto = nomeContatto;
        this.idElemento = idElemento;
        this.emailContatto = emailContatto;
        this.dataNascita = dataNascita;
    }

    public String getNomeContatto() {
        return nomeContatto;
    }

    public void setNomeContatto(String nomeContatto) {
        this.nomeContatto = nomeContatto;
    }

    public String getIdElemento() {
        return idElemento;
    }

    public void setIdElemento(String idElemento) {
        this.idElemento = idElemento;
    }

    public String getIdFascia() {
        return idFascia;
    }

    public void setIdFascia(String idFascia) {
        this.idFascia = idFascia;
    }

    public String getIdCorso() {
        return idCorso;
    }

    public void setIdCorso(String idCorso) {
        this.idCorso = idCorso;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public String getEmailContatto() {
        return emailContatto;
    }

    public void setEmailContatto(String emailContatto) {
        this.emailContatto = emailContatto;
    }

    public String getDataNascita() {
        return dataNascita;
    }

    public void setDataNascita(String dataNascita) {
        this.dataNascita = dataNascita;
    }
}
