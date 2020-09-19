package me.gabrideiros.lojas.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import me.gabrideiros.lojas.Main;
import me.gabrideiros.lojas.controllers.ShopController;
import me.gabrideiros.lojas.database.Storage;
import me.gabrideiros.lojas.models.Shop;
import me.gabrideiros.lojas.utils.Locations;
import org.bukkit.Location;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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

    public void load() {

        try {

            if (storage.getConnection().isClosed()) storage.openConnection();

            Connection connection = storage.getConnection();

            PreparedStatement ps = connection.prepareStatement("SELECT * FROM `Shops`;");

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                UUID uuid = UUID.fromString(rs.getString("uuid"));
                String name = rs.getString("name");
                Location location = Locations.deserialize(rs.getString("location"));
                int visits = rs.getInt("visits");
                long time = rs.getLong("time");
                long maxtime = rs.getLong("maxtime");
                boolean priority = rs.getBoolean("priority");

                Type listType = new TypeToken<List<String>>() {
                }.getType();

                List<String> items = gson.fromJson(rs.getString("items"), listType);

                Type mapType = new TypeToken<Map<String, Integer>>() {
                }.getType();

                Map<String, Integer> note = gson.fromJson(rs.getString("notes"), mapType);

                Shop shop = new Shop(uuid, name, location, visits, time, maxtime, items, note, priority);
                controller.getElements().add(shop);

            }

            storage.close(ps, rs);
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            plugin.getLogger().log(Level.INFO, "Foram carregados {0} lojas!", controller.getElements().size());
        }
    }

    public void insert(Shop shop) {

        try {

            if (storage.getConnection().isClosed()) storage.openConnection();

            Connection connection = storage.getConnection();

            PreparedStatement ps = connection.prepareStatement("INSERT INTO `Shops` (`uuid`, `name`, `location`, `visits`, `time`, `maxtime`, `items`, `notes`, `priority`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");

            ps.setString(1, shop.getUuid().toString());
            ps.setString(2, shop.getName());
            ps.setString(3, Locations.serialize(shop.getLocation()));
            ps.setInt(4, shop.getVisits());
            ps.setLong(5, shop.getTime());
            ps.setLong(6, shop.getMaxtime());
            ps.setString(7, gson.toJson(shop.getItems()));
            ps.setString(8, gson.toJson(shop.getNote()));
            ps.setBoolean(9, shop.isPriority());

            ps.executeUpdate();

            storage.close(ps, null);
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            controller.addElement(shop);
        }

    }

    public void update(Shop shop) {

        try {

            if (storage.getConnection().isClosed()) storage.openConnection();

            Connection connection = storage.getConnection();

            PreparedStatement ps = connection.prepareStatement("UPDATE `Shops` SET location=?, visits=?, time=?, maxtime=?, items=?, notes=?, priority=? WHERE uuid=?");

            ps.setString(1, Locations.serialize(shop.getLocation()));
            ps.setInt(2, shop.getVisits());
            ps.setLong(3, shop.getTime());
            ps.setLong(4, shop.getMaxtime());
            ps.setString(5, gson.toJson(shop.getItems()));
            ps.setString(6, gson.toJson(shop.getNote()));
            ps.setBoolean(7, shop.isPriority());
            ps.setString(8, shop.getUuid().toString());

            ps.executeUpdate();

            storage.close(ps, null);
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void delete(Shop shop) {

        try {

            if (storage.getConnection().isClosed()) storage.openConnection();

            Connection connection = storage.getConnection();

            PreparedStatement ps = connection.prepareStatement("DELETE FROM `Shops` WHERE uuid=?");

            ps.setString(1, shop.getUuid().toString());

            ps.executeUpdate();

            storage.close(ps, null);
            connection.close();

        }catch (SQLException e) {
            e.printStackTrace();
        } finally {
            controller.findAndRemove($ -> $.getName().equalsIgnoreCase(shop.getName()));
        }
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
