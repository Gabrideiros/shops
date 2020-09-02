package me.gabrideiros.lojas.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.SneakyThrows;
import me.gabrideiros.lojas.Main;
import org.bukkit.configuration.ConfigurationSection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PoolingDatabase implements Storage {

    private final Main plugin;

    @Getter
    private HikariDataSource dataSource;

    private String username;
    private String password;
    private String host;
    private int port;
    private  String database;

    public PoolingDatabase(Main plugin) {
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
    public Connection getConnection() {
        return dataSource.getConnection();
    }

    public void openConnection() {

        HikariConfig config = new HikariConfig();

        config.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database);

        config.setDriverClassName("com.mysql.jdbc.Driver");

        config.setUsername(username);
        config.setPassword(password);

        config.setMaximumPoolSize(10);
        config.addDataSourceProperty("autoReconnect", "true");

        dataSource = new HikariDataSource(config);

    }

    @Override
    @SneakyThrows
    public void makeTable() {

        Connection connection = getConnection();

        PreparedStatement ps = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `Shops` (uuid LONGTEXT, name VARCHAR(16), location VARCHAR(100), visits INTEGER, time LONG, message LONGTEXT, items LONGTEXT, notes LONGTEXT, priority BOOLEAN)");
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

    public void closeConnection() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}
