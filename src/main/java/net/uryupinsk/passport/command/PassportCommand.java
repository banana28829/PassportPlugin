package net.uryupinsk.passport.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import net.uryupinsk.passport.PassportPlugin;
import net.uryupinsk.passport.book.PassportBookBuilder;
import net.uryupinsk.passport.model.PassportData;
import net.uryupinsk.passport.storage.PassportStorage;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

/**
 * Команда /passport для получения и просмотра паспорта
 */
public class PassportCommand implements CommandExecutor {
    
    private final PassportPlugin plugin;
    private final PassportStorage storage;
    private final PassportBookBuilder bookBuilder;
    
    public PassportCommand(PassportPlugin plugin, PassportStorage storage) {
        this.plugin = plugin;
        this.storage = storage;
        this.bookBuilder = new PassportBookBuilder(plugin);
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Команда доступна только игрокам
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cЭту команду может использовать только игрок!");
            return true;
        }
        
        // Получаем или создаем паспорт
        PassportData passportData = storage.getOrCreatePassport(
            player.getUniqueId(),
            player.getName()
        );
        
        // Строим книгу паспорта
        var passportBook = bookBuilder.buildPassportBook(passportData);
        
        // Проверяем, есть ли свободное место в инвентаре
        if (player.getInventory().firstEmpty() == -1) {
            player.sendMessage(
                Component.text("❌ Инвентарь полон! Освободите место для паспорта.")
                    .color(TextColor.color(255, 0, 0))
            );
            return true;
        }
        
        // Добавляем паспорт в инвентарь
        player.getInventory().addItem(passportBook);
        
        // Отправляем подтверждение
        player.sendMessage(
            Component.text("✓ Паспорт выдан! ").color(TextColor.color(0, 255, 0))
                .append(Component.text("Откройте книгу для просмотра.")
                    .color(TextColor.color(200, 200, 200)))
        );
        
        plugin.getLogger().info("Игрок " + player.getName() + " получил паспорт");
        
        return true;
    }
}
