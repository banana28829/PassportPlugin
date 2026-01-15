# Инструкция по сборке PassportPlugin

## 🚀 Быстрый старт

### 1. Установка зависимостей

На Windows:

```bash
# Скачайте Java 21 с https://www.oracle.com/java/technologies/downloads/
# Скачайте Maven с https://maven.apache.org/download.cgi

# Проверьте установку:
java -version
mvn -version
```

На Linux:

```bash
sudo apt-get install openjdk-21-jdk maven
```

### 2. Сборка проекта

```bash
# Перейдите в папку проекта
cd PassportPlugin

# Компилируйте
mvn clean package

# Результат в target/passport-plugin-1.0.0.jar
```

### 3. Тестирование на локальном сервере

```bash
# Скачайте Paper Server для 1.21.10
# https://papermc.io/downloads/paper

# Создайте структуру:
server/
├── paper-1.21.10.jar
├── plugins/
│   └── passport-plugin-1.0.0.jar
├── eula.txt
└── ...

# Отредактируйте eula.txt:
eula=true

# Запустите сервер:
java -Xmx1024M -Xms1024M -jar paper-1.21.10.jar nogui

# Дождитесь полной загрузки (Done!)
# В консоли должно появиться:
# ✓ PassportPlugin v1.0.0 ENABLED

# Откройте new-game, выполните:
# /passport
```

### 4. Проверка работы

1. Используйте команду `/passport`
2. Паспорт должен появиться в инвентаре
3. Откройте книгу (правый клик)
4. Проверьте три страницы:
   - Обложка (красная)
   - Данные паспорта (чёрно-белые)
   - Машиночитаемая зона (серая)

## 📦 Структура файлов после сборки

```
PassportPlugin/
├── target/
│   ├── passport-plugin-1.0.0.jar      ← Готовый плагин
│   ├── classes/                        # Скомпилированные классы
│   └── ...
├── src/
├── pom.xml
└── ...
```

## 🔍 Отладка проблем

### Ошибка: "Cannot find symbol"

```
[ERROR] .../PassportPlugin.java:[10,1] cannot find symbol
```

**Решение:** Проверьте, что все зависимости Paper API установлены:

```bash
mvn dependency:tree
```

### Ошибка: "Java 21 не найден"

```bash
# Укажите явно:
mvn clean package -DskipTests -Djava.version=21
```

### JAR файл не загружается на сервер

1. Проверьте права на файл:
```bash
ls -la plugins/passport-plugin-*.jar
```

2. Проверьте логи сервера:
```bash
tail -f logs/latest.log | grep -i passport
```

3. Перезагрузите плагин:
```bash
# На консоли сервера:
reload
```

## 🔗 GitHub Actions CI/CD

Если вы используете GitHub:

```bash
# 1. Создайте .github/workflows/build.yml (уже добавлен)
# 2. Сделайте push:
git add .
git commit -m "Initial commit"
git push origin main

# 3. GitHub автоматически соберет JAR
# 4. Скачайте артефакт в разделе Actions
```

## 📚 Дополнительно

### Maven команды

```bash
# Очистка старых сборок
mvn clean

# Только компиляция (без упаковки)
mvn compile

# Компиляция + тесты
mvn test

# Полная сборка
mvn package

# Установка в локальный репозиторий
mvn install

# Генерация документации
mvn javadoc:javadoc
```

### Работа с исходниками

```bash
# Форматирование кода
mvn formatter:format

# Проверка стиля
mvn checkstyle:check

# Сборка с отключением тестов
mvn package -DskipTests

# Увеличить вывод отладки
mvn -X package
```

## ✅ Контрольный список сборки

- [ ] Установлена Java 21+
- [ ] Установлен Maven 3.8.1+
- [ ] `mvn -version` работает
- [ ] `java -version` показывает 21+
- [ ] `mvn clean package` завершается успешно
- [ ] Файл `target/passport-plugin-1.0.0.jar` существует
- [ ] JAR скопирован в `plugins/` сервера
- [ ] Сервер запущен
- [ ] Плагин загрузился: `/pl`
- [ ] Команда работает: `/passport`

## 🎯 Результат

Если все шаги выполнены правильно, вы должны увидеть:

```
[PluginLoader] PassportPlugin v1.0.0 is enabling PassportPlugin
[PassportPlugin] ╔════════════════════════════════╗
[PassportPlugin] ║     PassportPlugin v1.0.0      ║
[PassportPlugin] ║  УРЮПИНСК - Official Passport  ║
[PassportPlugin] ╚════════════════════════════════╝
[PassportPlugin] ✓ Создана папка хранилища паспортов
```

Теперь можно использовать плагин!
