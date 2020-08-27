package me.gabrideiros.lojas.database;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.SneakyThrows;
import me.gabrideiros.lojas.Main;
import me.gabrideiros.lojas.controller.ShopController;
import me.gabrideiros.lojas.model.Shop;
import me.gabrideiros.lojas.util.Locations;
import org.bukkit.Location;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.Map;

public class SQLManager {

    private final ConnectionPool pool;
    private final ShopController controller;

    private final Gson gson;

    public SQLManager(ShopController controller, Main plugin) {

        this.pool = new ConnectionPool(plugin);

        this.controller = controller;

        gson = new GsonBuilder().setPrettyPrinting().create();;

        createTable();

    }

    @SneakyThrows
    public void createTable() {

        Connection connection = pool.getDataSource().getConnection();

        PreparedStatement ps = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `Shops` (name VARCHAR(16), location VARCHAR(32), visits INTEGER, time LONG, notes LONGTEXT");
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
            long time = rs.getInt("time");

            Type type = new TypeToken<Map<String, Integer>>(){}.getType();

            Map<String, Integer> note = gson.fromJson(rs.getString("notes"), type);

            Shop shop = new Shop(name, location, visits, time, note);
            controller.getElements().add(shop);

        }

        pool.close(connection, ps, rs);

    }

    @SneakyThrows
    public void insertShop(Shop shop) {

        Connection connection = pool.getDataSource().getConnection();

        PreparedStatement ps = connection.prepareStatement("INSERT INTO `Shops` (`name`, `location`, `visits`, `time`, `notes`) VALUES (?, ?, ?, ?, ?)");

        ps.setString(1, shop.getName());
        ps.setString(2, Locations.serialize(shop.getLocation()));
        ps.setInt(3, shop.getVisits());
        ps.setLong(4, shop.getTime());
        ps.setString(1, gson.toJson(shop.getNote()));

        ps.executeUpdate();

        pool.close(connection, ps, null);

        controller.getElements().add(shop);

    }

    @SneakyThrows
    public void updateShop(Shop shop) {

        Connection connection = pool.getDataSource().getConnection();

        PreparedStatement ps = connection.prepareStatement("UPDATE `Shops` SET visits=?, time=?, notes=? WHERE name=?");

        ps.setInt(1, shop.getVisits());
        ps.setLong(2, shop.getTime());
        ps.setString(3, gson.toJson(shop.getNote()));
        ps.setString(4, shop.getName());

        ps.executeUpdate();

        pool.close(connection, ps, null);

    }

    @SneakyThrows
    public void delete(String key) {

        Connection connection = pool.getDataSource().getConnection();

        PreparedStatement ps = connection.prepareStatement("DELETE FROM `Shops` WHERE name=?");

        ps.setString(1, key);

        ps.executeUpdate();

        pool.close(connection, ps, null);

    }

    public void close() {
        pool.closeConnection();
    }

}
