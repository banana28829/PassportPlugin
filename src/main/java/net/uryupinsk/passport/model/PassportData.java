package net.uryupinsk.passport.model;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Модель данных паспорта игрока
 * Содержит все информацию о владельце паспорта
 */
public class PassportData {
    
    private UUID ownerUUID;
    private String playerName;
    private String passportNumber;
    private String issueDate;
    private String expirationDate;
    private int height;
    private int weight;
    private char sex;
    private String bloodType;
    private String birthDate;
    private String race;
    
    /**
     * Конструктор для создания нового паспорта
     */
    public PassportData(UUID ownerUUID, String playerName) {
        this.ownerUUID = ownerUUID;
        this.playerName = playerName;
        this.passportNumber = generatePassportNumber(ownerUUID);
        this.issueDate = "2024.01.15";
        this.expirationDate = "2034.01.15";
        
        // Значения по умолчанию
        this.height = 180;
        this.weight = 75;
        this.sex = 'M';
        this.bloodType = "O+";
        this.birthDate = "1990.01.01";
        this.race = "HUMAN";
    }
    
    /**
     * Генерирует уникальный номер паспорта на основе UUID
     */
    private static String generatePassportNumber(UUID uuid) {
        String uuidStr = uuid.toString().replace("-", "").toUpperCase();
        String serialNumber = String.format("URP%s%s", 
            uuidStr.substring(0, 6), 
            uuidStr.substring(uuidStr.length() - 3)
        );
        return serialNumber;
    }
    
    /**
     * Преобразует в Map для сохранения в YAML/JSON
     */
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("owner-uuid", ownerUUID.toString());
        map.put("player-name", playerName);
        map.put("passport-number", passportNumber);
        map.put("issue-date", issueDate);
        map.put("expiration-date", expirationDate);
        map.put("height", height);
        map.put("weight", weight);
        map.put("sex", String.valueOf(sex));
        map.put("blood-type", bloodType);
        map.put("birth-date", birthDate);
        map.put("race", race);
        return map;
    }
    
    /**
     * Создает объект из Map
     */
    public static PassportData fromMap(Map<String, Object> map) {
        UUID uuid = UUID.fromString((String) map.get("owner-uuid"));
        String name = (String) map.get("player-name");
        PassportData data = new PassportData(uuid, name);
        
        data.passportNumber = (String) map.get("passport-number");
        data.issueDate = (String) map.get("issue-date");
        data.expirationDate = (String) map.get("expiration-date");
        data.height = ((Number) map.get("height")).intValue();
        data.weight = ((Number) map.get("weight")).intValue();
        data.sex = ((String) map.get("sex")).charAt(0);
        data.bloodType = (String) map.get("blood-type");
        data.birthDate = (String) map.get("birth-date");
        data.race = (String) map.get("race");
        
        return data;
    }
    
    // Getters and Setters
    public UUID getOwnerUUID() { return ownerUUID; }
    public String getPlayerName() { return playerName; }
    public String getPassportNumber() { return passportNumber; }
    public String getIssueDate() { return issueDate; }
    public String getExpirationDate() { return expirationDate; }
    public int getHeight() { return height; }
    public int getWeight() { return weight; }
    public char getSex() { return sex; }
    public String getBloodType() { return bloodType; }
    public String getBirthDate() { return birthDate; }
    public String getRace() { return race; }
    
    public void setHeight(int height) { this.height = height; }
    public void setWeight(int weight) { this.weight = weight; }
    public void setSex(char sex) { this.sex = sex; }
    public void setBloodType(String bloodType) { this.bloodType = bloodType; }
    public void setBirthDate(String birthDate) { this.birthDate = birthDate; }
    public void setRace(String race) { this.race = race; }
}
