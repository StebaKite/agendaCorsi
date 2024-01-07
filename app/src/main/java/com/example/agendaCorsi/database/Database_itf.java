package com.example.agendaCorsi.database;

import com.example.agendaCorsi.database.table.ElementoPortfolio;

import java.util.List;

public interface Database_itf {

    /*
     * Metodi generici
     */
    List<Object> getAll(String query);
    boolean insert(Object entity, String query);
    boolean update(Object entity, String query);
    boolean updateStato(Object entity, String query);
    Object select(Object entity, String query);
    boolean delete(Object entity, String query);
    boolean isNew(Object entity, String query);

    /*
     * Metodi particolari
     */
    List<Object> getFasceCorso(String idCorsoToRead, String query);
    List<ElementoPortfolio> getContattoElements(String idContattoToRead, String query);

}
