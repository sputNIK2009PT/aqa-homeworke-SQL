package ru.netology.data;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLHelper {
    private static final QueryRunner runner = new QueryRunner();

    private SQLHelper() {
    }

    private static Connection getConn() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/app", "app", "pass");
    }

    @SneakyThrows
    public static String getVerificationCode() {
        var codeSQL = "SELECT code FROM auth_codes ORDER BY created DESC LIMIT 1";
        try (var conn = getConn()) {
            return runner.query(conn, codeSQL, new ScalarHandler<>());
        }
    }

    @SneakyThrows
    public static String getUserStatus(String login) {
        var sqlQuery = "SELECT status FROM users WHERE login = ?;";
        try (var conn = getConn()) {
            return runner.query(conn, sqlQuery, new ScalarHandler<>(), login);
        }
    }

    @SneakyThrows
    public static void cleanDataBase() {
        try (var connection = getConn()) {
            runner.execute(connection, "DELETE FROM auth_codes");
            runner.execute(connection, "DELETE FROM card_transactions");
            runner.execute(connection, "DELETE FROM cards");
            runner.execute(connection, "DELETE FROM users");
        }
    }

    @SneakyThrows
    public static void cleanAuthCodes() {
        try (var connection = getConn()) {
            runner.execute(connection, "DELETE FROM auth_codes");
        }
    }
}
