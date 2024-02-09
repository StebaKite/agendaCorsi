package com.example.agendaCorsi.database;

import java.util.List;

public interface DataAccessor {

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
    List read(String table, String[] columns, Row selectionRow, String[] sortColumns) throws Exception;

    /**
     * Insert data into table
     *
     * @param table         The table
     * @param rows          The rows to insert
     */
    void insert(String table, List rows) throws Exception;

    /**
     * Update data in a table
     *
     * @param table         The table
     * @param selectionRow  A set of filter columns and values used to subset the rows, or null
     *                      to update all of rows in the table.
     * @param updateRow     A set of update columns and values
     */
    void update(String table, Row selectionRow, Row updateRow) throws Exception;

    /**
     * Delete data from table
     *
     * @param table         The table
     * @param selectionRow  A set of filter columns and values used to subset the rows, or null
     *                      to delete all of the rows in the table
     */
    void delete(String table, Row selectionRow) throws Exception;

}
