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
import java.sql.SQLException;

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

    @Override
    public Connection getConnection() {
        
        Connection connection = null;
        
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return connection;
    }

    public void openConnection() {

        HikariConfig config = new HikariConfig();

        config.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database);

        config.setDriverClassName("com.mysql.jdbc.Driver");

        config.setUsername(username);
        config.setPassword(password);

        config.addDataSourceProperty("autoReconnect", "true");

        config.setMaximumPoolSize(20);
        config.setConnectionTimeout(300000);
        config.setConnectionTimeout(120000);
        config.setLeakDetectionThreshold(300000);

        dataSource = new HikariDataSource(config);

    }

    @Override
    public void makeTable() {

        try {
            Connection connection = getConnection();

            PreparedStatement ps = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `Shops` (uuid LONGTEXT, name VARCHAR(16), location LONGTEXT, visits INTEGER, time LONG, maxtime LONG, message LONGTEXT, items LONGTEXT, notes LONGTEXT, priority BOOLEAN)");
            ps.executeUpdate();

            ps = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `Advertising` (uuid LONGTEXT, name VARCHAR(16), message LONGTEXT, time LONG)");
            ps.executeUpdate();

            close(ps, null);
            connection.close();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close(PreparedStatement ps, ResultSet rs) {
        try {
            if (ps != null) ps.close();
            if (rs != null) rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        if (dataSource != null && !dataSource
                .isClosed()) {
            dataSource.close();
        }
    }
}
