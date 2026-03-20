package com.example.demo.controller;

import org.jooq.DSLContext;
import org.jooq.conf.Settings;
import org.jooq.conf.StatementType;
import org.jooq.tools.jdbc.MockDataProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.*;

@RestController
public class TestController {
    @Autowired
    private DSLContext dsl;

    @Autowired
    private DataSource dataSource;

    @Autowired(required = false)
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private Environment environment;

    @Value("${spring.datasource.url:Не указано}")
    private String datasourceUrl;

    @Value("${spring.datasource.username:Не указано}")
    private String datasourceUsername;

    @Value("${spring.datasource.driver-class-name:Не указано}")
    private String datasourceDriver;

    @GetMapping("/test")
    public Map<String, Object> testConnection() {
        Map<String, Object> debugInfo = new LinkedHashMap<>();

        // Основная информация о приложении
        debugInfo.put("application.status", "RUNNING");
        debugInfo.put("timestamp", new Date().toString());
        debugInfo.put("java.version", System.getProperty("java.version"));
        debugInfo.put("os.name", System.getProperty("os.name"));
        debugInfo.put("os.arch", System.getProperty("os.arch"));

        // Информация о DSLContext
        debugInfo.put("dslContext.present", dsl != null);
        if (dsl != null) {
            try {
                // Информация о конфигурации DSLContext
                debugInfo.put("dslContext.dialect", dsl.configuration().dialect().toString());
                debugInfo.put("dslContext.sqlDialect", dsl.configuration().dialect().getName());

                // Настройки JOOQ
                Settings settings = dsl.configuration().settings();
                if (settings != null) {
                    debugInfo.put("jooq.settings.renderSchema", settings.isRenderSchema());
                    debugInfo.put("jooq.settings.renderFormatted", settings.isRenderFormatted());
                    debugInfo.put("jooq.settings.statementType",
                            settings.getStatementType() != null ? settings.getStatementType().toString() : "default");
                    debugInfo.put("jooq.settings.executeLogging", settings.isExecuteLogging());
                }

                // Пытаемся выполнить простой запрос
                try {
                    Integer result = dsl.selectOne().fetchOne(0, Integer.class);
                    debugInfo.put("database.simpleQuery", "SUCCESS (result: " + result + ")");
                } catch (Exception e) {
                    debugInfo.put("database.simpleQuery", "FAILED: " + e.getMessage());
                    debugInfo.put("database.simpleQuery.error", getStackTraceAsString(e));
                }

            } catch (Exception e) {
                debugInfo.put("dslContext.error", e.getMessage());
                debugInfo.put("dslContext.error.stacktrace", getStackTraceAsString(e));
            }
        }

        // Информация о DataSource
        debugInfo.put("dataSource.present", dataSource != null);
        if (dataSource != null) {
            try {
                debugInfo.put("dataSource.class", dataSource.getClass().getName());

                // Пробуем получить соединение
                try (Connection conn = dataSource.getConnection()) {
                    debugInfo.put("database.connection", "SUCCESS");

                    // Метаданные базы данных
                    DatabaseMetaData metaData = conn.getMetaData();
                    debugInfo.put("database.product", metaData.getDatabaseProductName());
                    debugInfo.put("database.version", metaData.getDatabaseProductVersion());
                    debugInfo.put("database.driver", metaData.getDriverName());
                    debugInfo.put("database.driverVersion", metaData.getDriverVersion());
                    debugInfo.put("database.url", metaData.getURL());
                    debugInfo.put("database.username", metaData.getUserName());

                    // Проверяем поддержку транзакций
                    debugInfo.put("database.supportsTransactions", metaData.supportsTransactions());
                    debugInfo.put("database.supportsBatchUpdates", metaData.supportsBatchUpdates());

                    // Список схем (первые 5 для отладки)
                    List<String> schemas = new ArrayList<>();
                    try (ResultSet rs = metaData.getSchemas()) {
                        int count = 0;
                        while (rs.next() && count < 5) {
                            schemas.add(rs.getString("TABLE_SCHEM"));
                            count++;
                        }
                    }
                    debugInfo.put("database.schemas", schemas);

                } catch (Exception e) {
                    debugInfo.put("database.connection", "FAILED: " + e.getMessage());
                    debugInfo.put("database.connection.error", getStackTraceAsString(e));
                }
            } catch (Exception e) {
                debugInfo.put("dataSource.error", e.getMessage());
            }
        }

        // Информация из конфигурации
        debugInfo.put("config.datasource.url", datasourceUrl);
        debugInfo.put("config.datasource.username", maskPassword(datasourceUsername));
        debugInfo.put("config.datasource.driver", datasourceDriver);

        // Активные профили
        String[] activeProfiles = environment.getActiveProfiles();
        debugInfo.put("spring.activeProfiles", activeProfiles.length > 0 ?
                Arrays.toString(activeProfiles) : "default");

        // Информация о JdbcTemplate
        debugInfo.put("jdbcTemplate.present", jdbcTemplate != null);
        if (jdbcTemplate != null) {
            try {
                debugInfo.put("jdbcTemplate.dataSource",
                        jdbcTemplate.getDataSource() != null ?
                                jdbcTemplate.getDataSource().getClass().getName() : "null");
            } catch (Exception e) {
                debugInfo.put("jdbcTemplate.error", e.getMessage());
            }
        }

        // Проверка зависимостей
        debugInfo.put("dependencies.jooq", checkClass("org.jooq.DSLContext"));
        debugInfo.put("dependencies.postgresql", checkClass("org.postgresql.Driver"));
        debugInfo.put("dependencies.mysql", checkClass("com.mysql.cj.jdbc.Driver"));
        debugInfo.put("dependencies.h2", checkClass("org.h2.Driver"));

        return debugInfo;
    }

    private String checkClass(String className) {
        try {
            Class.forName(className);
            return "✅ Доступен";
        } catch (ClassNotFoundException e) {
            return "❌ Не найден";
        }
    }

    private String getStackTraceAsString(Exception e) {
        StringBuilder sb = new StringBuilder();
        sb.append(e.toString()).append("\n");
        for (StackTraceElement element : e.getStackTrace()) {
            sb.append("  at ").append(element.toString()).append("\n");
            // Ограничим количество строк стека, чтобы не было слишком много
            if (sb.length() > 1000) {
                sb.append("  ...");
                break;
            }
        }
        return sb.toString();
    }

    private String maskPassword(String str) {
        if (str == null || str.isEmpty()) return str;
        // Маскируем пароль, если он есть в строке
        if (str.contains("password=")) {
            return str.replaceAll("password=[^&]*", "password=****");
        }
        return str;
    }
}