package me.gabrideiros.lojas.database;

import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface Storage {

    Connection getConnection() throws SQLException;

    void openConnection();

    void closeConnection();

    void makeTable();

    void close(PreparedStatement ps, ResultSet rs);
}
