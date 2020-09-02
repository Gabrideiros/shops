package me.gabrideiros.lojas.services;

import lombok.SneakyThrows;
import me.gabrideiros.lojas.controllers.AdvertisingController;
import me.gabrideiros.lojas.database.Storage;
import me.gabrideiros.lojas.models.Advertising;
import me.gabrideiros.lojas.models.Shop;
import me.gabrideiros.lojas.utils.Locations;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;
import java.util.logging.Level;

public class AdvertisingService implements Service<Advertising> {

    private final Plugin plugin;

    private final AdvertisingController controller;
    private final Storage storage;

    public AdvertisingService(Plugin plugin, AdvertisingController controller, Storage storage) {

        this.plugin = plugin;

        this.controller = controller;
        this.storage = storage;

        load();

    }

    @SneakyThrows
    @Override
    public void insert(Advertising advertising) {

        Connection connection = storage.getConnection();

        PreparedStatement ps = connection.prepareStatement("INSERT INTO `Advertising` (`uuid`, `name`, `message`, `time`) VALUES (?, ?, ?, ?)");

        ps.setString(1, advertising.getUuid().toString());
        ps.setString(2, advertising.getName());
        ps.setString(3, advertising.getMessage());
        ps.setLong(4, advertising.getTime());

        ps.executeUpdate();

        storage.close(ps, null);
        connection.close();

        controller.addElement(advertising);

    }

    @SneakyThrows
    @Override
    public void update(Advertising advertising) {

        Connection connection = storage.getConnection();

        PreparedStatement ps = connection.prepareStatement("UPDATE `Advertising` SET message=? WHERE uuid=?");

        ps.setString(1, advertising.getMessage());
        ps.setString(2, advertising.getUuid().toString());

        ps.executeUpdate();

        storage.close(ps, null);
        connection.close();


    }

    @SneakyThrows
    @Override
    public void delete(Advertising advertising) {

        Connection connection = storage.getConnection();

        PreparedStatement ps = connection.prepareStatement("DELETE FROM `Advertising` WHERE uuid=?");

        ps.setString(1, advertising.getUuid().toString());

        ps.executeUpdate();

        storage.close(ps, null);
        connection.close();

        controller.getElements().remove(advertising);
    }

    @SneakyThrows
    @Override
    public void load() {

        Connection connection = storage.getConnection();

        PreparedStatement ps = connection.prepareStatement("SELECT * FROM `Advertising`;");

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            UUID uuid = UUID.fromString(rs.getString("uuid"));
            String name = rs.getString("name");
            String message = rs.getString("message");
            long time = rs.getLong("time");


            Advertising advertising = new Advertising(uuid, name, message, time);
            controller.getElements().add(advertising);

        }

        plugin.getLogger().log(Level.INFO, "Foram carregados {0} propagandas!", controller.getElements().size());

        storage.close(ps, rs);
        connection.close();
    }

    public void saveAll() {
        for (Advertising advertising : controller.getElements()) {
            update(advertising);
        }
    }

    public void close() {
        saveAll();
    }
}
