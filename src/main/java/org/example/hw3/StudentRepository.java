package org.example.hw3;

import java.sql.*;
import java.util.UUID;

public class StudentRepository {
    private static String uuidGroup1;
    private static String uuidGroup2;
    private static String uuidGroup3;

    public static String getUuidGroup1() {
        return uuidGroup1;
    }

    public static String getUuidGroup2() {
        return uuidGroup2;
    }

    public static String getUuidGroup3() {
        return uuidGroup3;
    }

    public static void acceptConnection(Connection connection) throws SQLException {


        try (Statement statement = connection.createStatement()) {
            statement.execute("""
                            create table Student_group(
                            id varchar(36) primary key,
                            name varchar(256)
                        )
                    """);
        }

        try (Statement statement = connection.createStatement()) {
            statement.execute("""
                            create table Student(
                            id varchar(36) primary key,
                            first_name varchar(256),
                            second_name varchar(256),
                            student_group varchar(36)
                        )
                    """);
        }

        try (Statement statement = connection.createStatement()) {
            statement.execute("""
                        alter table Student
                        add constraint fk_student_group
                        foreign key (student_group) references Student_group(id)
                    """);
        }


        try (Statement statement = connection.createStatement()) {
            uuidGroup1 = UUID.randomUUID().toString();
            uuidGroup2 = UUID.randomUUID().toString();
            uuidGroup3 = UUID.randomUUID().toString();
            int count = statement.executeUpdate(
                    "insert into Student_group(id, name) values " +
                            "('" + uuidGroup1 + "', 'Первая группа'), " +
                            "('" + uuidGroup2 + "', 'Вторая группа'), " +
                            "('" + uuidGroup3 + "', 'Третья группа')"
            );
        }

        try (Statement statement = connection.createStatement()) {
            String uuid = UUID.randomUUID().toString();
            int count = statement.executeUpdate(
                    "insert into Student(id, first_name, second_name, student_group) values " +
                            "('" + uuid + "', 'Masha', 'Mashina', '" + uuidGroup1 + "'), " +
                            "('" + UUID.randomUUID() + "', 'Sasha', 'Sashin', '" + uuidGroup2 + "'), " +
                            "('" + UUID.randomUUID() + "', 'Kolya', 'Kolin', '" + uuidGroup1 + "'), " +
                            "('" + UUID.randomUUID() + "', 'Vasya', 'Vasin', '" + uuidGroup3 + "'), " +
                            "('" + UUID.randomUUID() + "', 'Valera', 'Valerin', '" + uuidGroup2 + "')"
            );

            System.out.println("Количество добавленных строк: " + count);
        }

        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("""
                    select id, first_name, second_name, student_group
                    from Student
                    """);

            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String firstName = resultSet.getString("first_name");
                String secondName = resultSet.getString("second_name");
                String group = resultSet.getString("student_group");
                System.out.println("Прочитана строка: " + String.format("%s, %s, %s, %s", id, firstName, secondName, group));
            }
        }
    }

    public static void selectByGroup(Connection connection, String group) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("select id, first_name, second_name, student_group " +
                "from Student " +
                "where student_group = ?")) {
            preparedStatement.setString(1, group);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String firstName = resultSet.getString("first_name");
                String secondName = resultSet.getString("second_name");
                String studentGroup = resultSet.getString("student_group");
                System.out.println("Прочитана строка: " + String.format("%s, %s, %s, %s", id, firstName, secondName, studentGroup));
            }
        }
    }
}
