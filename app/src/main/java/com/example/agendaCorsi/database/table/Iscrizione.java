package com.example.agendaCorsi.database.table;

import android.provider.BaseColumns;

import java.util.HashMap;
import java.util.Map;

public class Iscrizione implements BaseColumns {

    public static String TABLE_NAME = "iscrizione";
    public static Integer ID_ISCRIZIONE = 0;
    public static Integer ID_FASCIA = 1;
    public static Integer ID_ELEMENTO = 2;
    public static Integer STATO = 3;
    public static Integer DATA_CREAZIONE = 4;
    public static Integer DATA_ULTIMO_AGGIORNAMENTO = 5;

    public static Map<Integer, String> iscrizioneColumns;
    static {
        iscrizioneColumns = new HashMap<>();
        iscrizioneColumns.put(ID_ISCRIZIONE, "id_iscrizione");
        iscrizioneColumns.put(ID_FASCIA, "id_fascia");
        iscrizioneColumns.put(ID_ELEMENTO, "id_elemento");
        iscrizioneColumns.put(STATO, "stato");
        iscrizioneColumns.put(DATA_CREAZIONE, "data_creazione");
        iscrizioneColumns.put(DATA_ULTIMO_AGGIORNAMENTO, "data_ultimo_aggiornamento");
    }

    public String idIscrizione;
    public String idFascia;
    public String idElemento;
    public String stato;
    public String dataCreazione;
    public String dataUltimoAggiornamento;

    public Iscrizione(String idIscrizione, String idFascia, String idElemento, String stato, String dataCreazione, String dataUltimoAggiornamento) {
        this.idIscrizione = idIscrizione;
        this.idFascia = idFascia;
        this.idElemento = idElemento;
        this.stato = stato;
        this.dataCreazione = dataCreazione;
        this.dataUltimoAggiornamento = dataUltimoAggiornamento;
    }

    public String getIdIscrizione() {
        return idIscrizione;
    }

    public void setIdIscrizione(String idIscrizione) {
        this.idIscrizione = idIscrizione;
    }

    public String getIdFascia() {
        return idFascia;
    }

    public void setIdFascia(String idFascia) {
        this.idFascia = idFascia;
    }

    public String getIdElemento() {
        return idElemento;
    }

    public void setIdElemento(String idElemento) {
        this.idElemento = idElemento;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
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
