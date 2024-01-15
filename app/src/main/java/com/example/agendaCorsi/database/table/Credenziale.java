package com.example.agendaCorsi.database.table;

import android.provider.BaseColumns;

import java.security.PublicKey;

public class Credenziale implements BaseColumns {

    public static Integer UTENTE = 0;
    public static Integer PASSWORD = 1;
    public static Integer DATA_CREAZIONE = 2;
    public static Integer DATA_ULTIMO_AGGIORNAMENTO = 3;

    public String utente;
    public String password;
    public String dataCreazione;
    public String dataUltimoAggiornamento;

    public Credenziale(String utente, String password, String dataCreazione, String dataUltimoAggiornamento) {
        this.utente = utente;
        this.password = password;
        this.dataCreazione = dataCreazione;
        this.dataUltimoAggiornamento = dataUltimoAggiornamento;
    }

    public String getUtente() {
        return utente;
    }

    public void setUtente(String utente) {
        this.utente = utente;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDataCreazione() {
        return dataCreazione;
    }

    public void setDataCreazione(String dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public String getDataUltimoAggiornamento() {
        return dataUltimoAggiornamento;
    }

    public void setDataUltimoAggiornamento(String dataUltimoAggiornamento) {
        this.dataUltimoAggiornamento = dataUltimoAggiornamento;
    }
}
