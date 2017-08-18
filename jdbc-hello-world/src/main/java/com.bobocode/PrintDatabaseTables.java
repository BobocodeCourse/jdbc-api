package com.bobocode;

import com.bobocode.util.DbUtil;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Stream;

/**
 * Basic JDBC API example. This is the simples example of using JDBC API. Please note that Java SE includes JDBC API,
 * but you have to add appropriate JDBC Driver to run this code.
 *
 * Minimal required properties
 * 1. Database url
 * 2. Username
 * 3. Password
 *
 * The shortest flow of calling database using JDBC API
 * - Create DataSource
 * - Get connection
 * - Create statement
 * - Execute statement
 */
public class PrintDatabaseTables {

    public static void main(String[] args) throws SQLException {

        DataSource dataSource = DbUtil.getDataSource();

        printPublicTables(dataSource);
    }

    private static void printPublicTables(DataSource dataSource) throws SQLException {
        //try-with-resource will automatically close Connection resource
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            String sql = "SELECT table_name FROM information_schema.tables WHERE table_schema='public' AND table_type='BASE TABLE';";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                System.out.println(resultSet.getString(1));
            }
        }
    }
}
