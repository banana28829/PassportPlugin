package net.uryupinsk.passport;

import org.bukkit.plugin.java.JavaPlugin;
import net.uryupinsk.passport.command.PassportCommand;
import net.uryupinsk.passport.listener.PassportProtectionListener;
import net.uryupinsk.passport.storage.PassportStorage;

/**
 * Основной класс плагина Passport
 * Отвечает за инициализацию и управление компонентами
 */
public class PassportPlugin extends JavaPlugin {

    private PassportStorage storage;
    
    @Override
    public void onEnable() {
        // Сохраняем конфиг по умолчанию
        saveDefaultConfig();
        
        // Инициализируем хранилище паспортов
        storage = new PassportStorage(this);
        storage.createDataDirectory();
        
        // Регистрируем команду
        getCommand("passport").setExecutor(new PassportCommand(this, storage));
        
        // Регистрируем слушатели для защиты паспорта
        getServer().getPluginManager().registerEvents(new PassportProtectionListener(this), this);
        
        getLogger().info("╔════════════════════════════════╗");
        getLogger().info("║     PassportPlugin v1.0.0       ║");
        getLogger().info("║   LanMain - Official Passport   ║");
        getLogger().info("╚════════════════════════════════╝");
    }
    
    @Override
    public void onDisable() {
        getLogger().info("PassportPlugin disabled");
    }
    
    /**
     * Получить хранилище паспортов
     */
    public PassportStorage getPassportStorage() {
        return storage;
    }
}
