package me.gabrideiros.lojas.database;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.SneakyThrows;
import me.gabrideiros.lojas.Main;
import me.gabrideiros.lojas.model.Shop;
import me.gabrideiros.lojas.util.Locations;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class SQLManager {

    private final ConnectionPool pool;

    private final Main plugin;

    private final Gson gson;

    public SQLManager(Main plugin) {

        this.pool = new ConnectionPool(plugin);

        this.plugin = plugin;

        gson = new GsonBuilder().setPrettyPrinting().create();;

        createTable();

        loadShops();

        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this::saveAll, 20 * 60 * 5, 20 * 60 * 5);


    }

    @SneakyThrows
    public void createTable() {

        Connection connection = pool.getDataSource().getConnection();

        PreparedStatement ps = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `Shops` (name VARCHAR(16), location VARCHAR(100), visits INTEGER, time LONG, message LONGTEXT, items LONGTEXT, notes LONGTEXT, priority BOOLEAN)");
        ps.executeUpdate();

        pool.close(connection, ps, null);

    }

    @SneakyThrows
    public void loadShops() {

        Connection connection = pool.getDataSource().getConnection();

        PreparedStatement ps = connection.prepareStatement("SELECT * FROM `Shops`;");

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            String name = rs.getString("name");
            Location location = Locations.deserialize(rs.getString("location"));
            int visits = rs.getInt("visits");
            long time = rs.getLong("time");
            String message = rs.getString("message");
            boolean priority = rs.getBoolean("priority");

            Type listType = new TypeToken<List<String>>(){}.getType();

            List<String> items = gson.fromJson(rs.getString("items"), listType);

            Type mapType = new TypeToken<Map<String, Integer>>(){}.getType();

            Map<String, Integer> note = gson.fromJson(rs.getString("notes"), mapType);

            Shop shop = new Shop(name, location, visits, time, message, items, note, priority);
            plugin.getController().getElements().add(shop);

        }

        plugin.getLogger().log(Level.INFO, "Foram carregados {0} lojas!", plugin.getController().getElements().size());

        pool.close(connection, ps, rs);

    }

    @SneakyThrows
    public void insertShop(Shop shop) {

        Connection connection = pool.getDataSource().getConnection();

        PreparedStatement ps = connection.prepareStatement("INSERT INTO `Shops` (`name`, `location`, `visits`, `time`, `message`, `items`, `notes`, `priority`) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");

        ps.setString(1, shop.getName());
        ps.setString(2, Locations.serialize(shop.getLocation()));
        ps.setInt(3, shop.getVisits());
        ps.setLong(4, shop.getTime());
        ps.setString(5, shop.getMessage());
        ps.setString(6, gson.toJson(shop.getItems()));
        ps.setString(7, gson.toJson(shop.getNote()));
        ps.setBoolean(8, shop.isPriority());

        ps.executeUpdate();

        pool.close(connection, ps, null);

        plugin.getController().getElements().add(shop);

    }

    @SneakyThrows
    public void updateShop(Shop shop) {

        Connection connection = pool.getDataSource().getConnection();

        PreparedStatement ps = connection.prepareStatement("UPDATE `Shops` SET location=?, visits=?, time=?, message=?, items=?, notes=?, priority=? WHERE name=?");

        ps.setString(1, Locations.serialize(shop.getLocation()));
        ps.setInt(2, shop.getVisits());
        ps.setLong(3, shop.getTime());
        ps.setString(4, shop.getMessage());
        ps.setString(5, gson.toJson(shop.getItems()));
        ps.setString(6, gson.toJson(shop.getNote()));
        ps.setBoolean(7, shop.isPriority());
        ps.setString(8, shop.getName());

        ps.executeUpdate();

        pool.close(connection, ps, null);

    }

    @SneakyThrows
    public void delete(Shop shop) {

        Connection connection = pool.getDataSource().getConnection();

        PreparedStatement ps = connection.prepareStatement("DELETE FROM `Shops` WHERE name=?");

        ps.setString(1, shop.getName());

        ps.executeUpdate();

        pool.close(connection, ps, null);

        plugin.getController().getElements().remove(shop);

    }

    public void saveAll() {
        for (Shop shop : plugin.getController().getElements()) {
            updateShop(shop);
        }
    }

    public void close() {
        saveAll();
        pool.closeConnection();
    }

}
