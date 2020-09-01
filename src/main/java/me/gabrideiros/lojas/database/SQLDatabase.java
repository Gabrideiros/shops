package me.gabrideiros.lojas.database;

import lombok.SneakyThrows;
import me.gabrideiros.lojas.Main;

import javax.xml.transform.Result;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SQLDatabase implements Storage {

    private Main plugin;

    private Connection connection;

    private final File file;

    public SQLDatabase(Main plugin, String file) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), file);

        openConnection();
        makeTable();

    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    @SneakyThrows
    @Override
    public void openConnection() {

        if(!file.getParentFile().exists()) file.getParentFile().mkdir();

        if(!file.exists()) file.createNewFile();

        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:" + file.getName());

    }

    @SneakyThrows
    @Override
    public void makeTable() {

        Connection connection = getConnection();

        PreparedStatement ps = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `Shops` (name VARCHAR(16), location VARCHAR(100), visits INTEGER, time LONG, message LONGTEXT, items LONGTEXT, notes LONGTEXT, priority BOOLEAN)");
        ps.executeUpdate();

        ps = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `Advertising` (name VARCHAR(16), message LONGTEXT, time LONG, time LONG)");
        ps.executeUpdate();

        close(ps, null);

    }

    @Override
    @SneakyThrows
    public void close(PreparedStatement ps, ResultSet rs) {
        if (ps != null) ps.close();
        if (rs != null) rs.close();
    }

    @SneakyThrows
    public void closeConnection() {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
