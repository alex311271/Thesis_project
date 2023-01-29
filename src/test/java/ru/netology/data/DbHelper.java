package ru.netology.data;

import lombok.*;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Timestamp;

public class DbHelper {

    private static Connection connect;

    //При изменении используемой базы данных поменять коментирование
    @SneakyThrows
    public static void setup() {
        // Для автоматического запуска тестов
        connect = DriverManager.getConnection(System.getProperty("dbUrl"), "admin", "16jan23");
        //Для ручного запуска тестов
        //connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/base_mysql", "admin", "16jan23");
        //connect = DriverManager.getConnection("jdbc:postgresql://localhost:5432/base_postgresql", "admin", "16jan23");
    }


    @SneakyThrows
    public static void databaseCleanUp() {

        var deleteDataCredit = "DELETE FROM credit_request_entity;";
        var deleteDataPayment = "DELETE FROM payment_entity;";
        var deleteDataOrder = "DELETE FROM order_entity;";
        setup();
        var runner = new QueryRunner();
        runner.execute(connect, deleteDataCredit, new ScalarHandler<>());
        runner.execute(connect, deleteDataPayment, new ScalarHandler<>());
        runner.execute(connect, deleteDataOrder, new ScalarHandler<>());
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
        setup();
        var paymentInfo = "SELECT * FROM payment_entity WHERE created = (SELECT MAX(created) FROM payment_entity);";

        var runner = new QueryRunner();
        return runner.query(connect, paymentInfo, new BeanHandler<>(PaymentEntity.class));


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
        setup();
        var orderInfo = "SELECT * FROM order_entity WHERE created = (SELECT MAX(created) FROM order_entity);";

        var runner = new QueryRunner();
        return runner.query(connect, orderInfo, new BeanHandler<>(OrderEntity.class));
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
        setup();
        var creditRequestInfo = "SELECT * FROM credit_request_entity WHERE created = (SELECT MAX(created) FROM credit_request_entity);";

        var runner = new QueryRunner();
        return runner.query(connect, creditRequestInfo, new BeanHandler<>(CreditRequestEntity.class));
    }
}

