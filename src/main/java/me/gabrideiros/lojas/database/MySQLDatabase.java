package me.gabrideiros.lojas.database;

import lombok.SneakyThrows;
import me.gabrideiros.lojas.Main;
import org.bukkit.configuration.ConfigurationSection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MySQLDatabase implements Storage {

    private final Main plugin;

    private Connection connection;

    private String username;
    private String password;
    private String host;
    private int port;
    private  String database;

    public MySQLDatabase(Main plugin) {
        this.plugin = plugin;

        init();
        openConnection();
        makeTable();

    }

    private void init() {

        ConfigurationSection cs = plugin.getConfig().getConfigurationSection("Connection");

        username = cs.getString("Username");
        password = cs.getString("Password");
        host = cs.getString("Host");
        port = cs.getInt("Port");
        database = cs.getString("Database");

    }

    @SneakyThrows
    @Override
    public void openConnection() {

        Class.forName("com.mysql.jdbc.Driver");

        connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    @SneakyThrows
    @Override
    public void makeTable() {

        Connection connection = getConnection();

        PreparedStatement ps = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `Shops` (uuid LONGTEXT, name VARCHAR(16), location LONGTEXT, visits INTEGER, time LONG, maxtime LONG, message LONGTEXT, items LONGTEXT, notes LONGTEXT, priority BOOLEAN)");
        ps.executeUpdate();

        ps = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `Advertising` (uuid LONGTEXT, name VARCHAR(16), message LONGTEXT, time LONG)");
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
