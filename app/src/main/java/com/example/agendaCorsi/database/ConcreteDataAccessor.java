package com.example.agendaCorsi.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.agendaCorsi.AgendaCorsiApp;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ConcreteDataAccessor implements DataAccessor {

    private static ConcreteDataAccessor INSTANCE = null;
    private static DatabaseHelper databaseHelper;
    private static SQLiteDatabase sqLiteDatabase;

    public static ConcreteDataAccessor getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ConcreteDataAccessor();
            databaseHelper = new DatabaseHelper(AgendaCorsiApp.getContext());
            sqLiteDatabase = databaseHelper.getReadableDatabase();
        }
        return INSTANCE;
    }

    @Override
    public List read(String table, String[] columns, Row selectionRow, String[] sortColumns) throws Exception {
        StringBuffer buffer = new StringBuffer();
        buffer.append("select ");

        if (columns != null) {
            for (int i = 0; i < columns.length; i++) {
                if (i > 0) {
                    buffer.append(", ");
                }
                buffer.append(columns[i]);
            }
        } else {
            buffer.append(" * ");
        }

        buffer.append(" from ");
        buffer.append(table);

        if (selectionRow != null) {
            buffer.append(generateWhereClause(selectionRow));
        }

        if (sortColumns != null) {
            buffer.append(" order by ");
            for (int i = 0; i < sortColumns.length; i++) {
                if (i > 0) {
                    buffer.append(", ");
                }
                buffer.append(sortColumns[i]);
                buffer.append(" asc");
            }
        }

        Cursor cursor = sqLiteDatabase.rawQuery(buffer.toString(), null);

        List resultRows = new LinkedList();
        String[] columnsName = cursor.getColumnNames();
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Row row = new Row();
            for (int i = 0; i <= cursor.getCount() - 1; i++) {
                row.addColumn(columnsName[i], cursor.getColumnIndex(columnsName[i]));
            }
            resultRows.add(row);
            cursor.moveToNext();
        }
        return resultRows;
    }

    @Override
    public void insert(String table, List rows) throws Exception {


    }

    @Override
    public void update(String table, Row selectionRow, Row updateRow) throws Exception {

    }

    @Override
    public void delete(String table, Row selectionRow) throws Exception {

    }

    private String generateWhereClause(Row selectionRow) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(" where ");
        boolean firstColumn = true;
        for (Iterator i = selectionRow.columns(); i.hasNext();) {
            if (!firstColumn) {
                buffer.append(" and ");
            } else {
                firstColumn = false;
            }
            String column = (String) i.next();
            buffer.append(column);
            buffer.append(" = ");
            Object columnValue = selectionRow.getColumnValue(column);
            buffer.append(generateLiteralValue(columnValue));
        }
        return buffer.toString();
    }

    private String generateLiteralValue(Object literalValue) {
        StringBuffer buffer = new StringBuffer();
        if (!(literalValue instanceof Number)) {
            buffer.append("'");
        }
        buffer.append(literalValue);

        if (!(literalValue instanceof Number)) {
            buffer.append("'");
        }
        return buffer.toString();
    }


}
