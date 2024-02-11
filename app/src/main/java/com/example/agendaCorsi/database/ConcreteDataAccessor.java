package com.example.agendaCorsi.database;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.agendaCorsi.AgendaCorsiApp;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ConcreteDataAccessor implements DataAccessor {

    private static ConcreteDataAccessor INSTANCE = null;
    private static DatabaseHelper databaseHelper;
    private static SQLiteDatabase sqLiteDatabase;

    /**
     * Singleton pattern of "Gang of four"
     * @return object instance
     */

    public static ConcreteDataAccessor getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ConcreteDataAccessor();
            databaseHelper = new DatabaseHelper(AgendaCorsiApp.getContext());
            sqLiteDatabase = databaseHelper.getReadableDatabase();
        }
        return INSTANCE;
    }

    /**
     * Read data from table
     *
     * @param table         The table
     * @param columns       The columns to read, or null to read all columns in the table
     * @param selectionRow  A set of filter columns and values used to subset the rows, or null
     *                      to read all rows in the table
     * @param sortColumns   The columns to sort, or null to read without sorting
     *
     * @return              The list of rows
     */
    @SuppressLint("Range")
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
            for (int i = 0; i < columnsName.length; i++) {
                row.addColumn(columnsName[i], cursor.getString(cursor.getColumnIndex(columnsName[i])));
            }
            resultRows.add(row);
            cursor.moveToNext();
        }
        return resultRows;
    }

    /**
     * Insert data into table
     *
     * @param table         The table
     * @param rows          The rows to insert
     */
    @Override
    public void insert(String table, List rows) throws Exception {
        try {
            for (Iterator i = rows.iterator(); i.hasNext();) {
                Row row = (Row) i.next();

                StringBuffer buffer = new StringBuffer();
                buffer.append("insert into ");
                buffer.append(table);

                // List the column names

                buffer.append(" (");
                boolean firstColumn = true;
                for (Iterator j = row.columns(); j.hasNext();) {
                    if (!firstColumn) {
                        buffer.append(", ");
                    } else {
                        firstColumn = false;
                    }
                    buffer.append(j.next());
                }

                // List the column values

                buffer.append(") values (");
                firstColumn = true;
                for (Iterator j = row.columns(); j.hasNext();) {
                    if (!firstColumn) {
                        buffer.append(", ");
                    } else {
                        firstColumn = false;
                    }
                    String column = (String) j.next();
                    Object columnValue = row.getColumnValue(column);
                    buffer.append(generateLiteralValue(columnValue));
                }
                buffer.append(")");
                sqLiteDatabase.execSQL(buffer.toString());
            }
        }
        catch (SQLException e) {
            Log.e("Unable to insert into table " + table, e.toString());
            throw new SQLException("Unable to insert into table " + table);
        }
    }

    /**
     * Update data in a table
     *
     * @param table         The table
     * @param selectionRow  A set of filter columns and values used to subset the rows, or null
     *                      to update all of rows in the table.
     * @param updateRow     A set of update columns and values
     */
    @Override
    public void update(String table, Row selectionRow, Row updateRow) throws Exception {
        try {
            StringBuffer buffer = new StringBuffer();
            buffer.append("update ");
            buffer.append(table);

            // Generate the set clause

            buffer.append(" set ");
            boolean firstColumn = true;
            for (Iterator i = updateRow.columns(); i.hasNext();) {
                if (!firstColumn) {
                    buffer.append(", ");
                } else {
                    firstColumn = false;
                }
                String column = (String) i.next();
                buffer.append(column);
                buffer.append(" = ");
                Object columnValue = updateRow.getColumnValue(column);
                buffer.append(generateLiteralValue(columnValue));
            }

            // Generate Where clause if specified

            if (selectionRow != null) {
                buffer.append(generateWhereClause(selectionRow));
            }
            sqLiteDatabase.execSQL(buffer.toString());
        }
        catch (SQLException e) {
            Log.e("Unable to update table " + table, e.toString());
            throw new SQLException("Unable to update table " + table);
        }
    }

    @Override
    public void delete(String table, Row selectionRow) throws Exception {
        try {
            StringBuffer buffer = new StringBuffer();
            buffer.append("delete from ");
            buffer.append(table);

            // Generate Where clause if specified

            if (selectionRow != null) {
                buffer.append(generateWhereClause(selectionRow));
            }
            sqLiteDatabase.execSQL(buffer.toString());
        }
        catch (SQLException e) {
            Log.e("Unable to delete from table " + table, e.toString());
            throw new SQLException("Unable to delete from table " + table);
        }
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
        if (literalValue == null) {
            buffer.append("null");
        }
        else {
            if (!(literalValue instanceof Number)) {
                buffer.append("'");
            }
            buffer.append(literalValue);

            if (!(literalValue instanceof Number)) {
                buffer.append("'");
            }
        }
        return buffer.toString();
    }
}
