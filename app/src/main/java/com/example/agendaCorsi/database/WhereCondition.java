package com.example.agendaCorsi.database;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class WhereCondition {

    private static final String PLACEHOLDER = "?";
    private StringBuilder _sql = new StringBuilder(80);
    private List _values = new ArrayList();
    private List<String> _names = new ArrayList();
    private NDAO_base _dao;

    public WhereCondition(NDAO_base dao) {
        this._dao = dao;
        this._sql.append(" where ");
    }

    public WhereCondition append(String param) {
        this._sql.append(param);
        return this;
    }

    public WhereCondition appendFieldValue(String name) throws Exception {
        try {
            this._sql.append("?");
            this._values.add(this._dao.getAttribute(name));
            this._names.add(name);
            return this;
        } catch (Throwable var4) {
            Log.e("WhereCondition", "");
            throw var4;
        }
    }

    public String getSQL() {
        return new String(this._sql);
    }

    public List getValues() {
        return this._values;
    }

    public List getNames() {
        return this._names;
    }

    public void clear() {
        this._sql = new StringBuilder(80);
        this._values.clear();
    }

    public String toString() {
        StringBuilder buffer = new StringBuilder(1024);
        buffer.append("WhereCondition[");
        buffer.append("_sql = ").append(this._sql);
        buffer.append(" _values = ").append(this._values);
        buffer.append("]");
        return buffer.toString();
    }

}
