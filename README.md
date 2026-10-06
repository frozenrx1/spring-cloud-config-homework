# Spring Cloud Config — домашнее задание

Два отдельных Spring Boot приложения: сервер получает конфигурацию из Git-репозитория, клиент загружает её при запуске и возвращает значение через HTTP.

## Состав проекта

- `config-server` — Config Server на порту **8888**.
- `config-client` — веб-приложение на порту **8080**, endpoint **GET /get**.
- [Репозиторий конфигурации](https://github.com/frozenrx1/spring-cloud-config-repository) — содержит `config-client.properties` со свойством `message=Hello from Config Server`.

Используются Java 17, Spring Boot 4.1.1, Spring Cloud 2025.1.3 и Gradle Wrapper 9.7.1. В клиенте подключён `spring-boot-starter-webmvc` — стартер Spring MVC для Spring Boot 4.

## Требования

- Установленный JDK 17.
- Свободные порты 8888 и 8080.
- Доступ в интернет для загрузки зависимостей и чтения репозитория конфигурации.

Отдельная установка Gradle не требуется: в каждом приложении есть Gradle Wrapper. На Windows вместо `./gradlew` используйте `gradlew.bat`.

## Запуск

Сначала запустите сервер. Из корня репозитория в первом терминале:

```bash
cd config-server
./gradlew bootRun
```

После запуска проверьте конфигурацию:

```bash
curl http://localhost:8888/config-client/default
```

Ответ — JSON, содержащий в `propertySources`:

```json
"source": {
  "message": "Hello from Config Server"
}
```

Во втором терминале, также начиная из корня репозитория, запустите клиент:

```bash
cd config-client
./gradlew bootRun
```

Проверьте метод контроллера:

```bash
curl http://localhost:8080/get
```

Ожидаемый ответ:

```text
Hello from Config Server
```

Эти адреса можно открыть и в браузере. Для остановки приложений нажмите `Ctrl+C` в соответствующих терминалах.

## Сборка и тесты

Из корня репозитория:

```bash
./config-server/gradlew -p config-server clean build
./config-client/gradlew -p config-client clean build
```

Перед сборкой клиента с тестами запустите Config Server на порту 8888. Тест `ConfigClientApplicationTests.contextLoads()` загружает настоящий Spring-контекст и обращается к серверу конфигурации. При остановленном сервере он завершится ошибкой подключения.

## Как это работает

1. Config Server читает `config-client.properties` из отдельного Git-репозитория.
2. Клиент с именем `config-client` при запуске импортирует настройки через `spring.config.import=configserver:http://localhost:8888`.
3. `ConfigController` получает свойство `message` через `@Value` и возвращает его в ответ на `GET /get`.

Используется профиль `default`. Дополнительное задание с отдельными настройками `dev` и `prod` не реализовано.
