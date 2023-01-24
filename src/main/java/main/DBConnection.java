package main;

import java.sql.*;

public class DBConnection {
    private Connection connection;
    private Statement statement;

    public DBConnection() {
        try {
            connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void connect() throws SQLException {
        connection = DriverManager.getConnection("jdbc:h2:mem:testdb");
        statement = connection.createStatement();
        statement.executeUpdate("create table if not exists calculator (\n" +
                "    id bigserial primary key,\n" +
                "    result varchar(255));");
    }

    public void insertRecord(String record) {
        try {
            statement.executeUpdate("insert into calculator(result) values ('" + record + "')");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ResultSet getRecords() {
        try {
            return statement.executeQuery("select * from calculator order by id desc limit 10");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
