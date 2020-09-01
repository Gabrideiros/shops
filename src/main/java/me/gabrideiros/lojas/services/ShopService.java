package me.gabrideiros.lojas.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.SneakyThrows;
import me.gabrideiros.lojas.Main;
import me.gabrideiros.lojas.controllers.ShopController;
import me.gabrideiros.lojas.database.Storage;
import me.gabrideiros.lojas.models.Shop;
import me.gabrideiros.lojas.utils.Locations;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class ShopService implements Service<Shop> {

    private final Main plugin;

    private final ShopController controller;
    private final Storage storage;

    private final Gson gson;

    public ShopService(Main plugin, ShopController controller, Storage storage) {

        this.plugin = plugin;

        this.controller = controller;
        this.storage = storage;

        gson = new GsonBuilder().setPrettyPrinting().create();;

        load();

    }

    @SneakyThrows
    public void load() {

        Connection connection = storage.getConnection();

        PreparedStatement ps = connection.prepareStatement("SELECT * FROM `Shops`;");

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            String name = rs.getString("name");
            Location location = Locations.deserialize(rs.getString("location"));
            int visits = rs.getInt("visits");
            long time = rs.getLong("time");
            boolean priority = rs.getBoolean("priority");

            Type listType = new TypeToken<List<String>>(){}.getType();

            List<String> items = gson.fromJson(rs.getString("items"), listType);

            Type mapType = new TypeToken<Map<String, Integer>>(){}.getType();

            Map<String, Integer> note = gson.fromJson(rs.getString("notes"), mapType);

            Shop shop = new Shop(name, location, visits, time, items, note, priority);
            controller.getElements().add(shop);

        }

        plugin.getLogger().log(Level.INFO, "Foram carregados {0} lojas!", controller.getElements().size());

        storage.close(ps, rs);

    }

    @SneakyThrows
    public void insert(Shop shop) {

        Connection connection = storage.getConnection();

        PreparedStatement ps = connection.prepareStatement("INSERT INTO `Shops` (`name`, `location`, `visits`, `time`, `items`, `notes`, `priority`) VALUES (?, ?, ?, ?, ?, ?, ?)");

        ps.setString(1, shop.getName());
        ps.setString(2, Locations.serialize(shop.getLocation()));
        ps.setInt(3, shop.getVisits());
        ps.setLong(4, shop.getTime());
        ps.setString(5, gson.toJson(shop.getItems()));
        ps.setString(6, gson.toJson(shop.getNote()));
        ps.setBoolean(7, shop.isPriority());

        ps.executeUpdate();

        storage.close(ps, null);

        controller.addElement(shop);

    }

    @SneakyThrows
    public void update(Shop shop) {

        Connection connection = storage.getConnection();

        PreparedStatement ps = connection.prepareStatement("UPDATE `Shops` SET location=?, visits=?, time=?, items=?, notes=?, priority=? WHERE name=?");

        ps.setString(1, Locations.serialize(shop.getLocation()));
        ps.setInt(2, shop.getVisits());
        ps.setLong(3, shop.getTime());
        ps.setString(4, gson.toJson(shop.getItems()));
        ps.setString(5, gson.toJson(shop.getNote()));
        ps.setBoolean(6, shop.isPriority());
        ps.setString(7, shop.getName());

        ps.executeUpdate();

        storage.close(ps, null);

    }

    @SneakyThrows
    public void delete(Shop shop) {

        Connection connection = storage.getConnection();

        PreparedStatement ps = connection.prepareStatement("DELETE FROM `Shops` WHERE name=?");

        ps.setString(1, shop.getName());

        ps.executeUpdate();

        storage.close(ps, null);

        controller.getElements().remove(shop);

    }

    public void saveAll() {
        for (Shop shop : controller.getElements()) {
            update(shop);
        }
    }

    public void close() {
        saveAll();
    }
}
