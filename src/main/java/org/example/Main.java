package org.example;

import org.example.hw3.StudentRepository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {

        try (Connection connection = DriverManager.getConnection("jdbc:h2:mem:testdb", "root", "root")) {
            StudentRepository.acceptConnection(connection);
            System.out.println("____________________________________");
            StudentRepository.selectByGroup(connection, StudentRepository.getUuidGroup2());
        } catch (SQLException e) {
            System.err.println("Неудачное подключение к БД: " + e.getMessage());
        }
    }
}
