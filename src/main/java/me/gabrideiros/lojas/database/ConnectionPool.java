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

public class ConnectionPool {

    private final Main plugin;

    @Getter
    private HikariDataSource dataSource;

    private String username;
    private String password;
    private String host;
    private int port;
    private  String database;

    public ConnectionPool(Main plugin) {
        this.plugin = plugin;

        init();
        openConnection();

    }

    private void init() {

        ConfigurationSection cs = plugin.getConfig().getConfigurationSection("Connection");

        username = cs.getString("Username");
        password = cs.getString("Password");
        host = cs.getString("Host");
        port = cs.getInt("Port");
        database = cs.getString("Database");

    }

    private void openConnection() {

        HikariConfig config = new HikariConfig();

        config.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database);

        config.setDriverClassName("com.mysql.jdbc.Driver");

        config.setUsername(username);
        config.setPassword(password);

        config.setMaximumPoolSize(10);

        config.addDataSourceProperty("autoReconnect", "true");

        dataSource = new HikariDataSource(config);

    }

    @SneakyThrows
    public void close(Connection connection, PreparedStatement ps, ResultSet rs) {
     ﻿   if (connection != null) connection.close();
        if (ps != null) ps.close();
        if (rs != null) rs.close();
    }

    public void closeConnection() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }﻿
    }
}
