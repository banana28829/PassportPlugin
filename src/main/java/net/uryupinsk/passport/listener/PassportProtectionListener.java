package net.uryupinsk.passport.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.inventory.ClickEvent;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.Material;
import org.bukkit.event.inventory.CraftItemEvent;
import net.uryupinsk.passport.PassportPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

/**
 * Слушатель событий для защиты паспорта от манипуляций
 * Блокирует: переименование в anvil, крафт, копирование
 */
public class PassportProtectionListener implements Listener {
    
    private final PassportPlugin plugin;
    private final NamespacedKey passportKey;
    
    public PassportProtectionListener(PassportPlugin plugin) {
        this.plugin = plugin;
        this.passportKey = new NamespacedKey(plugin, "passport_uuid");
    }
    
    /**
     * Блокирует переименование паспорта в anvil
     */
    @EventHandler
    public void onPrepareAnvil(PrepareAnvilEvent event) {
        ItemStack firstItem = event.getInventory().getItem(0);
        ItemStack secondItem = event.getInventory().getItem(1);
        
        // Проверяем, не пытается ли игрок манипулировать паспортом
        if (isPassport(firstItem) || isPassport(secondItem)) {
            // Блокируем anvil операцию
            event.setResult(null);
            
            if (event.getViewers().size() > 0) {
                event.getViewers().get(0).sendMessage(
                    Component.text("❌ Паспорт нельзя переименовывать!")
                        .color(TextColor.color(255, 0, 0))
                );
            }
        }
    }
    
    /**
     * Блокирует крафт паспорта
     */
    @EventHandler
    public void onCraftItem(CraftItemEvent event) {
        // Паспорт - Written Book с уникальными свойствами
        // Крафт книг блокируем при попытке использования паспортных компонентов
        
        if (event.getCurrentItem() != null && 
            event.getCurrentItem().getType() == Material.WRITTEN_BOOK) {
            
            // Проверяем ингредиенты
            for (int i = 0; i < event.getInventory().getSize(); i++) {
                ItemStack item = event.getInventory().getItem(i);
                if (isPassport(item)) {
                    event.setCancelled(true);
                    event.getWhoClicked().sendMessage(
                        Component.text("❌ Паспорт нельзя использовать в крафте!")
                            .color(TextColor.color(255, 0, 0))
                    );
                    return;
                }
            }
        }
    }
    
    /**
     * Проверяет, является ли предмет паспортом
     */
    private boolean isPassport(ItemStack item) {
        if (item == null || item.getType() != Material.WRITTEN_BOOK) {
            return false;
        }
        
        BookMeta meta = (BookMeta) item.getItemMeta();
        if (meta == null) {
            return false;
        }
        
        // Проверяем наличие маркера паспорта в PersistentDataContainer
        return meta.getPersistentDataContainer().has(
            passportKey,
            PersistentDataType.STRING
        );
    }
}
