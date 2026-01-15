# 🛂 PassportPlugin - LanMain Паспорт

Профессиональный плагин паспорта для Paper Minecraft 1.21.10. Реализует систему официальных паспортов игроков в виде красивых Written Books с защитой от манипуляций для сервера LanMain.

## 📋 Особенности

✅ **Written Book Паспорт** - Красивое оформление как официального документа  
✅ **UUID Привязка** - Паспорт привязан к UUID игрока через PersistentDataContainer  
✅ **Автоматическое восстановление** - При потере паспорта можно получить новый через `/passport`  
✅ **Защита от манипуляций**:
- Нельзя переименовать в anvil
- Нельзя использовать в крафте
- Защита от копирования

✅ **Персонализированные данные**:
- Имя игрока
- UUID (сокращенно)
- Рост, вес
- Пол, группа крови
- Дата рождения, раса
- Уникальный номер паспорта
- Дата выдачи и истечения

✅ **YAML Хранилище** - Все данные сохраняются локально в YAML файлах  
✅ **Машиночитаемая зона** - Третья страница с данными в стиле реальных паспортов

## 🎮 Использование

### Команда

```
/passport
```

Выдает или открывает ваш паспорт.

**Что происходит:**
1. Если паспорта нет → создается новый и сохраняется в `plugins/PassportPlugin/data/`
2. Если есть → паспорт добавляется в инвентарь
3. Можно открыть книгу и просмотреть все данные

### Разрешения

Нет специальных permissions - команда доступна всем игрокам.

## 🏗️ Сборка из исходников

### Требования

- **Java 21** или выше
- **Maven 3.8.1** или выше
- **Git**

### Шаги сборки

```bash
# 1. Клонируем репозиторий (или просто открываем папку)
cd PassportPlugin

# 2. Компилируем через Maven
mvn clean package

# 3. JAR файл будет в папке target/
# passport-plugin-1.0.0.jar готов к установке
```

### Установка на сервер

```bash
# Копируем JAR в папку плагинов
cp target/passport-plugin-1.0.0.jar /path/to/server/plugins/

# Перезагружаем сервер
# На консоли: reload
```

## 📁 Структура проекта

```
PassportPlugin/
├── pom.xml                                      # Maven конфигурация
├── src/main/
│   ├── java/net/uryupinsk/passport/
│   │   ├── PassportPlugin.java                 # Главный класс плагина
│   │   ├── command/
│   │   │   └── PassportCommand.java            # Команда /passport
│   │   ├── book/
│   │   │   └── PassportBookBuilder.java        # Построитель Written Book
│   │   ├── model/
│   │   │   └── PassportData.java               # Модель данных паспорта
│   │   ├── storage/
│   │   │   └── PassportStorage.java            # YAML хранилище
│   │   └── listener/
│   │       └── PassportProtectionListener.java # Защита от манипуляций
│   └── resources/
│       ├── plugin.yml                          # Манифест плагина
│       └── config.yml                          # Конфигурация
└── README.md                                   # Этот файл
```

## 📚 Примеры страниц паспорта

### Страница 1 (Обложка)

```
§4§l              
§4§l           УРЮПИНСК           
§4§l         §n§m◎§n          
§4§l           ПАСПОРТ           
§4§l              
§4§l        ДОКУМЕНТ           
§4§l      ГРАЖДАНИНА РФ
```

**Цвета:** §4 (тёмно-красный), §8 (серый)

### Страница 2 (Данные)

```
┏━━━━━━━━━━━━━━━━━━━━━━━━┓
┃ [PHOTO]  NAME: Walker   ┃
┃          UUID:  ...9999 ┃
┃ HEIGHT:  190cm         ┃
┃ WEIGHT:  95kg          ┃
┃ SEX:     M            ┃
┃ BLOOD:   O+           ┃
┃ BORN:    24.03.1990   ┃
┃ RACE:    HUMAN        ┃
┗━━━━━━━━━━━━━━━━━━━━━━━━┛

ISSUED:  2024.01.15
EXPIRE:  2034.01.15

SEAL  URP999999URP  STAMP
```

### Страница 3 (Машиночитаемая зона)

```
<<<<<<<WALKER<<<<<<<
URP999999URP<<<<<<
24031990M<<<<<<<<<
HUMAN0+OO<<<<<
```

## ⚙️ Конфигурация

Файл `config.yml` (создается автоматически):

```yaml
passport:
  issue-date: "2024.01.15"
  expiration-years: 10
  
  player-types:
    - "HUMAN"
    - "HALF-ENDER"
    - "VILLAGER"
    - "ELF"
  
  blood-types:
    - "O+"
    - "O-"
    - "A+"
    # ... и другие

storage:
  format: yaml
  path: "plugins/PassportPlugin/data/"
```

## 📦 Хранение данных

Каждый паспорт сохраняется в отдельный YAML файл:

```
plugins/PassportPlugin/data/
├── 550e8400-e29b-41d4-a716-446655440000.yml
├── 6ba7b810-9dad-11d1-80b4-00c04fd430c8.yml
└── ...
```

**Формат данных:**

```yaml
passport:
  owner-uuid: 550e8400-e29b-41d4-a716-446655440000
  player-name: Walker
  passport-number: URP999999URP
  issue-date: "2024.01.15"
  expiration-date: "2034.01.15"
  height: 190
  weight: 95
  sex: M
  blood-type: "O+"
  birth-date: "1990.03.24"
  race: HUMAN
```

## 🔧 Расширение функционала

### Добавление новых данных

Редактируйте класс `PassportData.java`:

```java
// Добавьте поле
private String newField;

// В toMap()
map.put("new-field", newField);

// В fromMap()
data.newField = (String) map.get("new-field");
```

### Изменение оформления

Изменяйте методы в `PassportBookBuilder.java`:

```java
private Component buildCoverPage() {
    // Здесь регулируйте цвета (TextColor.color(R, G, B))
    // и форматирование обложки
}

private Component buildDataPage(PassportData data) {
    // Здесь оформление страницы с данными
}
```

## 🐛 Решение проблем

**Q: Паспорт не сохраняется**
- Проверьте права на папку `plugins/PassportPlugin/`
- Посмотрите логи сервера на ошибки

**Q: Команда не работает**
- Убедитесь, что плагин загрузился: `/pl`
- Проверьте, что вы игрок, а не консоль

**Q: Паспорт можно переименовать в anvil**
- Слушатель может не зарегистрироваться
- Перезагрузите плагин: `/reload`

## 📝 Лицензия

Открытый исходный код. Используйте и модифицируйте свободно.

## 👨‍💻 Автор

Разработано для сервера **LanMain**.

---

**Версия:** 1.0.0  
**Совместимость:** Paper 1.21.10  
**Java:** 21+  
**Сервер:** LanMain
