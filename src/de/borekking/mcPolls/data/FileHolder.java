package de.borekking.mcPolls.data;

import de.borekking.mcPolls.Main;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class FileHolder {

    private final File file;

    protected final YamlConfiguration config;

    public FileHolder(File parent, String fileName) {
        // Try to load parent
        if (parent == null) {
            throw new IllegalArgumentException("No Parent File provided! (null)");
        } else if (!this.loadDictator(parent)) {
            throw new IllegalArgumentException("Parent File could not be created");
        } else if (!parent.isDirectory()) {
            throw new IllegalArgumentException("Parent File is not directory.");
        }

        // Create file
        this.file = new File(parent, fileName);
        // Create YamlConfiguration
        this.config = YamlConfiguration.loadConfiguration(this.file);
        // Create file if not existing
        if (!this.file.exists()) this.save();

        this.load();
    }

    public FileHolder(String fileName) {
        this(Main.getPlugin().getDataFolder(), fileName);
    }

    public void load() {
        try {
            this.config.load(this.file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            System.err.println("ERROR OCCURRED WHILE LOADING FILES");
            Bukkit.shutdown();
        }
    }

    public void save() {
        try {
            this.config.save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("ERROR OCCURRED WHILE SAVING FILES");
            Bukkit.shutdown();
        }
    }

    public void clear() {
        Map<String, Object> values = this.config.getValues(false);

        for (String key : values.keySet()) this.config.set(key, null);
    }

    private boolean loadDictator(File file) {
        // If file doesn't exist create it and return success.
        if (!file.exists()) return file.mkdirs();
        // If file already existed return true.
        return true;
    }
}
