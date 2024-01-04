package com.example.agendaCorsi.database;

import java.util.List;

public abstract class DatabaseFacade implements Database_itf {

    @Override
    public List<Object> getAll() {
        return null;
    }

    @Override
    public boolean insert(Object entity) {
        return false;
    }

    @Override
    public boolean update(Object entity) {
        return false;
    }

    @Override
    public Object select(Object entity) {
        return null;
    }

    @Override
    public Boolean delete(Object entity) {
        return null;
    }
}
