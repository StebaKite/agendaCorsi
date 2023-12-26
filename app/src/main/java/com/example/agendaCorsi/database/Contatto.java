package com.example.agendaCorsi.database;

import android.provider.BaseColumns;

public class Contatto implements BaseColumns {

    public static String TABLE_NAME = "contatti";
    public static Integer ID = 0;
    public static Integer NOME = 1;
    public static Integer INDIRIZZO = 2;
    public static Integer TELEFONO = 3;
    public static Integer EMAIL = 4;

    public String id;
    public String nome;
    public String indirizzo;
    public String telefono;
    public String email;

    public Contatto(String id, String nome, String indirizzo, String telefono, String email) {
        this.id = id;
        this.nome = nome;
        this.indirizzo = indirizzo;
        this.telefono = telefono;
        this.email = email;
    }

    public static String getTableName() {
        return TABLE_NAME;
    }

    public static void setTableName(String tableName) {
        TABLE_NAME = tableName;
    }

    public static Integer getID() {
        return ID;
    }

    public static void setID(Integer ID) {
        Contatto.ID = ID;
    }

    public static Integer getNOME() {
        return NOME;
    }

    public static void setNOME(Integer NOME) {
        Contatto.NOME = NOME;
    }

    public static Integer getINDIRIZZO() {
        return INDIRIZZO;
    }

    public static void setINDIRIZZO(Integer INDIRIZZO) {
        Contatto.INDIRIZZO = INDIRIZZO;
    }

    public static Integer getTELEFONO() {
        return TELEFONO;
    }

    public static void setTELEFONO(Integer TELEFONO) {
        Contatto.TELEFONO = TELEFONO;
    }

    public static Integer getEMAIL() {
        return EMAIL;
    }

    public static void setEMAIL(Integer EMAIL) {
        Contatto.EMAIL = EMAIL;
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
}
