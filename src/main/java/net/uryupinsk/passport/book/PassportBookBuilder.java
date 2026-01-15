package net.uryupinsk.passport.book;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataType;
import net.uryupinsk.passport.PassportPlugin;
import net.uryupinsk.passport.model.PassportData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.UUID;

/**
 * Построитель Written Book для паспорта
 * Создает красивую визуализацию паспорта с правильным форматированием
 */
public class PassportBookBuilder {
    
    private final PassportPlugin plugin;
    private final NamespacedKey passportKey;
    
    public PassportBookBuilder(PassportPlugin plugin) {
        this.plugin = plugin;
        this.passportKey = new NamespacedKey(plugin, "passport_uuid");
    }
    
    /**
     * Создает Written Book паспорт из данных
     */
    public ItemStack buildPassportBook(PassportData passportData) {
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) book.getItemMeta();
        
        // Устанавливаем автора и название
        meta.setAuthor("LanMain");
        meta.setTitle("Паспорт");
        
        // Добавляем страницы
        meta.addPages(
            buildCoverPage(),
            buildDataPage(passportData),
            buildMachineReadableZone(passportData)
        );
        
        book.setItemMeta(meta);
        
        // Сохраняем UUID владельца в PersistentDataContainer
        meta = (BookMeta) book.getItemMeta();
        meta.getPersistentDataContainer().set(
            passportKey,
            PersistentDataType.STRING,
            passportData.getOwnerUUID().toString()
        );
        
        // Делаем паспорт не переименовываемым через anvil (используем display name)
        meta.displayName(
            Component.text("Паспорт ").color(TextColor.color(170, 0, 0))
                .append(Component.text(passportData.getPlayerName()).color(TextColor.color(255, 200, 0)))
        );
        
        book.setItemMeta(meta);
        return book;
    }
    
    /**
     * Строит обложку паспорта (левая страница)
     */
    private Component buildCoverPage() {
        return Component.empty()
            .append(Component.text("\n\n\n\n").color(TextColor.color(139, 0, 0)))
            .append(Component.text("LanMain\n").color(TextColor.color(255, 215, 0))
                .decorate(TextDecoration.BOLD))
            .append(Component.text("     ").color(TextColor.color(139, 0, 0)))
            .append(Component.text("◎\n").color(TextColor.color(255, 215, 0))
                .decorate(TextDecoration.BOLD))
            .append(Component.text("ПАСПОРТ\n").color(TextColor.color(255, 215, 0))
                .decorate(TextDecoration.BOLD))
            .append(Component.text("\n\n\n\n").color(TextColor.color(139, 0, 0)))
            .append(Component.text("ДОКУМЕНТ\n").color(TextColor.color(64, 64, 64)))
            .append(Component.text("СЕРВЕРА LanMain").color(TextColor.color(64, 64, 64)));
    }
    
    /**
     * Строит страницу с данными паспорта
     */
    private Component buildDataPage(PassportData data) {
        String shortUUID = data.getOwnerUUID().toString().substring(0, 8).toUpperCase();
        
        StringBuilder sb = new StringBuilder();
        sb.append("┏━━━━━━━━━━━━━━━━━━━━━━━━┓\n");
        sb.append("┃ [PHOTO]  NAME:         ┃\n");
        sb.append(String.format("┃          %s   ┃\n", padRight(data.getPlayerName(), 17)));
        sb.append(String.format("┃ UUID:    %s   ┃\n", padRight(shortUUID + "...", 15)));
        sb.append(String.format("┃ HEIGHT:  %dcm         ┃\n", data.getHeight()));
        sb.append(String.format("┃ WEIGHT:  %dkg         ┃\n", data.getWeight()));
        sb.append(String.format("┃ SEX:     %s            ┃\n", data.getSex()));
        sb.append(String.format("┃ BLOOD:   %s          ┃\n", padRight(data.getBloodType(), 14)));
        sb.append(String.format("┃ BORN:    %s       ┃\n", data.getBirthDate()));
        sb.append(String.format("┃ RACE:    %s         ┃\n", padRight(data.getRace(), 13)));
        sb.append("┗━━━━━━━━━━━━━━━━━━━━━━━━┛\n\n");
        sb.append(String.format("ISSUED:  %s\n", data.getIssueDate()));
        sb.append(String.format("EXPIRE:  %s\n\n", data.getExpirationDate()));
        sb.append(String.format("SEAL  %s  STAMP\n", data.getPassportNumber()));
        
        return Component.text(sb.toString()).color(TextColor.color(0, 0, 0));
    }
    
    /**
     * Строит машиночитаемую зону паспорта
     */
    private Component buildMachineReadableZone(PassportData data) {
        String uuidStr = data.getOwnerUUID().toString().replace("-", "").toUpperCase();
        String serial = data.getPassportNumber();
        String nameFormatted = data.getPlayerName().toUpperCase();
        
        // Форматируем в стиле паспортных зон
        String line1 = String.format("<<<<<<<%-7s<<<<<<<", nameFormatted.length() > 7 ? 
            nameFormatted.substring(0, 7) : nameFormatted);
        String line2 = String.format("%-8sURP<<<<<<", serial.substring(3, 9));
        String line3 = String.format("%-9sM<<<<<<<<<", 
            data.getBirthDate().replace(".", "").substring(0, 8));
        String line4 = String.format("%-14s0+OO<<<<<", 
            data.getRace().length() > 8 ? data.getRace().substring(0, 8) : data.getRace());
        
        return Component.text(
            line1 + "\n" +
            line2 + "\n" +
            line3 + "\n" +
            line4
        ).color(TextColor.color(64, 64, 64));
    }
    
    /**
     * Вспомогательный метод для выравнивания текста
     */
    private String padRight(String s, int n) {
        return String.format("%-" + n + "s", s);
    }
}
