package com.example.agendaCorsi.database;

import java.util.List;
import java.util.Objects;

public interface Database_itf {

    List<Object> getAll();
    boolean insert(Object entity);
    boolean update(Object entity);
    Object select(Object entity);
    Boolean delete(Object entity);

}
