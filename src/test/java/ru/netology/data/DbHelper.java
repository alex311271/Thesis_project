package ru.netology.data;

import lombok.*;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.DriverManager;
import java.sql.Timestamp;

public class DbHelper {


    @SneakyThrows
    public static void databaseCleanUp() {
        var deleteDataCredit = "DELETE FROM credit_request_entity;";
        var deleteDataPayment = "DELETE FROM payment_entity;";
        var deleteDataOrder = "DELETE FROM order_entity;";
        var runner = new QueryRunner();
        var connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/base_mysql", "admin", "16jan23");
        runner.execute(connection, deleteDataCredit, new ScalarHandler<>());
        runner.execute(connection, deleteDataPayment, new ScalarHandler<>());
        runner.execute(connection, deleteDataOrder, new ScalarHandler<>());
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaymentEntity {
        private String id;
        private int amount;
        private Timestamp created;
        private String status;
        private String transaction_id;
    }

    @SneakyThrows
    public static PaymentEntity getPaymentInfo() {
        var paymentInfo = "SELECT * FROM payment_entity WHERE created = (SELECT MAX(created) FROM payment_entity);";
        var runner = new QueryRunner();

        try (var connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/base_mysql", "admin", "16jan23")) {
            return runner.query(connection, paymentInfo, new BeanHandler<>(PaymentEntity.class));
        }

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderEntity {
        private String id;
        private Timestamp created;
        private String credit_id;
        private String payment_id;
    }
    @SneakyThrows
    public static OrderEntity getOrderInfo() {
        var orderInfo = "SELECT * FROM order_entity WHERE created = (SELECT MAX(created) FROM order_entity);";
        var runner = new QueryRunner();

        try (var connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/base_mysql", "admin", "16jan23")) {
            return runner.query(connection, orderInfo, new BeanHandler<>(OrderEntity.class));
        }
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreditRequestEntity {
        private String id;
        private String bank_id;
        private Timestamp created;
        private String status;
    }
    @SneakyThrows
    public static CreditRequestEntity getCreditRequestInfo() {
        var creditRequestInfo = "SELECT * FROM credit_request_entity WHERE created = (SELECT MAX(created) FROM credit_request_entity);";
        var runner = new QueryRunner();

        try (var connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/base_mysql", "admin", "16jan23")) {
            return runner.query(connection, creditRequestInfo, new BeanHandler<>(CreditRequestEntity.class));
        }
    }
}

