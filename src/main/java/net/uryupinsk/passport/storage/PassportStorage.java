package net.uryupinsk.passport.storage;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataType;
import org.yaml.snakeyaml.Yaml;
import net.uryupinsk.passport.PassportPlugin;
import net.uryupinsk.passport.model.PassportData;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Система хранения паспортов в YAML файлах
 * Отвечает за сохранение и загрузку данных паспортов
 */
public class PassportStorage {
    
    private final PassportPlugin plugin;
    private final Path dataPath;
    private final Yaml yaml;
    private final NamespacedKey passportKey;
    
    public PassportStorage(PassportPlugin plugin) {
        this.plugin = plugin;
        this.dataPath = Paths.get(plugin.getDataFolder().getAbsolutePath(), "data");
        this.yaml = new Yaml();
        this.passportKey = new NamespacedKey(plugin, "passport_uuid");
    }
    
    /**
     * Создает директорию для данных паспортов
     */
    public void createDataDirectory() {
        try {
            if (!Files.exists(dataPath)) {
                Files.createDirectories(dataPath);
                plugin.getLogger().info("✓ Создана папка хранилища паспортов");
            }
        } catch (IOException e) {
            plugin.getLogger().severe("✗ Ошибка при создании папки данных: " + e.getMessage());
        }
    }
    
    /**
     * Сохраняет паспорт в YAML файл
     */
    public boolean savePassport(PassportData passport) {
        try {
            Path filePath = dataPath.resolve(passport.getOwnerUUID() + ".yml");
            
            Map<String, Object> data = new HashMap<>();
            data.put("passport", passport.toMap());
            
            try (FileWriter writer = new FileWriter(filePath.toFile())) {
                yaml.dump(data, writer);
            }
            
            plugin.getLogger().info("✓ Паспорт сохранен: " + passport.getPlayerName());
            return true;
        } catch (IOException e) {
            plugin.getLogger().severe("✗ Ошибка при сохранении паспорта: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Загружает паспорт из YAML файла
     */
    public PassportData loadPassport(UUID uuid) {
        try {
            Path filePath = dataPath.resolve(uuid + ".yml");
            
            if (!Files.exists(filePath)) {
                return null;
            }
            
            try (FileReader reader = new FileReader(filePath.toFile())) {
                Map<String, Object> data = yaml.load(reader);
                Map<String, Object> passportMap = (Map<String, Object>) data.get("passport");
                return PassportData.fromMap(passportMap);
            }
        } catch (IOException | ClassCastException e) {
            plugin.getLogger().severe("✗ Ошибка при загрузке паспорта: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Проверяет, существует ли паспорт у игрока
     */
    public boolean hasPassport(UUID uuid) {
        return Files.exists(dataPath.resolve(uuid + ".yml"));
    }
    
    /**
     * Получает паспорт по UUID, создает если не существует
     */
    public PassportData getOrCreatePassport(UUID uuid, String playerName) {
        PassportData passport = loadPassport(uuid);
        
        if (passport == null) {
            passport = new PassportData(uuid, playerName);
            savePassport(passport);
        }
        
        return passport;
    }
    
    /**
     * Удаляет паспорт (при необходимости)
     */
    public boolean deletePassport(UUID uuid) {
        try {
            Files.deleteIfExists(dataPath.resolve(uuid + ".yml"));
            return true;
        } catch (IOException e) {
            plugin.getLogger().severe("✗ Ошибка при удалении паспорта: " + e.getMessage());
            return false;
        }
    }
}
