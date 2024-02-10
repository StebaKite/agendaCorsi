package com.example.agendaCorsi.database.table;

import android.provider.BaseColumns;

import java.util.HashMap;
import java.util.Map;

public class Contatto implements BaseColumns {

    public static String TABLE_NAME = "contatto";
    public static String VIEW_ALL_CONTATTI = "all_contatti_view";

    public static Integer ID_CONTATTO = 0;
    public static Integer NOME = 1;
    public static Integer DATA_NASCITA = 2;
    public static Integer INDIRIZZO = 3;
    public static Integer TELEFONO = 4;
    public static Integer EMAIL = 5;
    public static Integer STATO_ELEMENTO = 6;

    public static Map<Integer, String> contattoColumns;
    static {
        contattoColumns = new HashMap<>();
        contattoColumns.put(ID_CONTATTO, "id_contatto");
        contattoColumns.put(NOME, "nome");
        contattoColumns.put(DATA_NASCITA, "data_nascita");
        contattoColumns.put(INDIRIZZO, "indirizzo");
        contattoColumns.put(TELEFONO, "telefono");
        contattoColumns.put(EMAIL, "email");
        contattoColumns.put(STATO_ELEMENTO, "stato_elemento");
    }

    public String id;
    public String nome;
    public String dataNascita;
    public String indirizzo;
    public String telefono;
    public String email;
    public String statoElemento;
    public String dataCreazione;
    public String dataUltimoAggiornamento;

    public Contatto(String id, String nome, String dataNascita, String indirizzo, String telefono, String email, String statoElemento) {
        this.id = id;
        this.nome = nome;
        this.dataNascita = dataNascita;
        this.indirizzo = indirizzo;
        this.telefono = telefono;
        this.email = email;
        this.statoElemento = statoElemento;
        this.dataCreazione = "";
        this.dataUltimoAggiornamento = "";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getDataNascita() {
        return dataNascita;
    }

    public void setDataNascita(String dataNascita) {
        this.dataNascita = dataNascita;
    }

    public String getStatoElemento() {
        return statoElemento;
    }

    public void setStatoElemento(String statoElemento) {
        this.statoElemento = statoElemento;
    }
}
