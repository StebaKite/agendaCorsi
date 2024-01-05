package com.example.agendaCorsi.database;

import java.util.List;
import java.util.Objects;

public interface Database_itf {

    /*
     * Metodi generici
     */
    List<Object> getAll();
    boolean insert(Object entity);
    boolean update(Object entity);
    boolean updateStato(Object entity);
    Object select(Object entity);
    boolean delete(Object entity);
    boolean isNew(Object entity);

    /*
     * Metodi particolari
     */
    List<Object> getFasceCorso(int idCorsoToRead);
    List<ElementoPortfolio> getContattoElements(int idContattoToRead);

}
