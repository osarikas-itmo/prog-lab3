# Лабораторная работа №3

Модель фермы Робинзона: посев, рост и сбор урожая, хранение, обмолот и планирование питания.

Требуется JDK 21 или новее. Внешних зависимостей нет.

Сборка и запуск из корня проекта (Bash):

```bash
mkdir -p out
javac -encoding UTF-8 --release 21 -d out $(find src -name '*.java')
java -cp out Main
```

В IntelliJ IDEA откройте папку проекта, выберите JDK 21+ и отметьте `src` как Sources Root. Запустите `Main`.

[Объектная модель](docs/object-model.md)
