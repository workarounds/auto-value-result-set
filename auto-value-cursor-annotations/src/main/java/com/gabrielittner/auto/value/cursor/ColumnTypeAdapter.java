package com.gabrielittner.auto.value.cursor;


import java.sql.ResultSet;

public interface ColumnTypeAdapter<T> {
    T fromResultSet(ResultSet resultSet, String columnName);
}
