package com.example.agendaCorsi.database.table;

import android.provider.BaseColumns;

import java.util.HashMap;
import java.util.Map;

public class Fascia implements BaseColumns {

    public static String TABLE_NAME = "fascia";
    public static String VIEW_ALL_FASCE_CORSI = "all_fasce_corsi_view";
    public static String VIEW_FASCE_DISPONIBILI = "fasce_disponibili_view";
    public static String VIEW_FASCE_CORSI_RUNNING = "fasce_corsi_running_view";

    public static Integer ID_FASCIA = 0;
    public static Integer ID_CORSO = 1;
    public static Integer DESCRIZIONE = 2;
    public static Integer GIORNO_SETTIMANA = 4;
    public static Integer ORA_INIZIO = 5;
    public static Integer ORA_FINE = 6;
    public static Integer CAPIENZA = 7;
    public static Integer DATA_CREAZIONE = 8;
    public static Integer DATA_ULTIMO_AGGIORNAMENTO = 9;
    public static Integer DESCRIZIONE_FASCIA = 10;
    public static Integer NUMERO_GIORNO = 11;
    public static Integer TOTALE_FASCIA = 12;

    public static Map<Integer, String> fasciaColumns;
    static {
        fasciaColumns = new HashMap<>();
        fasciaColumns.put(ID_FASCIA, "id_fascia");
        fasciaColumns.put(ID_CORSO, "id_corso");
        fasciaColumns.put(DESCRIZIONE, "descrizione");
        fasciaColumns.put(GIORNO_SETTIMANA, "giorno_settimana");
        fasciaColumns.put(ORA_INIZIO, "ora_inizio");
        fasciaColumns.put(ORA_FINE, "ora_fine");
        fasciaColumns.put(CAPIENZA, "capienza");
        fasciaColumns.put(DATA_CREAZIONE, "data_creazione");
        fasciaColumns.put(DATA_ULTIMO_AGGIORNAMENTO, "data_ultimo_aggiornamento");
        fasciaColumns.put(DESCRIZIONE_FASCIA, "descrizione_fascia");
        fasciaColumns.put(NUMERO_GIORNO, "numero_giorno");
        fasciaColumns.put(TOTALE_FASCIA, "totale_fascia");
    }

    public String idFascia;
    public String idCorso;
    public String descrizione;
    public String giornoSettimana;
    public String oraInizio;
    public String oraFine;
    public String capienza;
    public String dataCreazione;
    public String dataUltimoAggiornamento;

    public Fascia(String idFascia, String idCorso, String descrizione, String giornoSettimana, String oraInizio, String oraFine, String capienza, String dataCreazione, String dataUltimoAggiornamento) {
        this.idFascia = idFascia;
        this.idCorso = idCorso;
        this.descrizione = descrizione;
        this.giornoSettimana = giornoSettimana;
        this.oraInizio = oraInizio;
        this.oraFine = oraFine;
        this.capienza = capienza;
        this.dataCreazione = dataCreazione;
        this.dataUltimoAggiornamento = dataUltimoAggiornamento;
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

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getGiornoSettimana() {
        return giornoSettimana;
    }

    public void setGiornoSettimana(String giornoSettimana) {
        this.giornoSettimana = giornoSettimana;
    }

    public String getOraInizio() {
        return oraInizio;
    }

    public void setOraInizio(String oraInizio) {
        this.oraInizio = oraInizio;
    }

    public String getOraFine() {
        return oraFine;
    }

    public void setOraFine(String oraFine) {
        this.oraFine = oraFine;
    }

    public String getCapienza() {
        return capienza;
    }

    public void setCapienza(String capienza) {
        this.capienza = capienza;
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
