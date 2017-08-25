package com.bobocode.util;

import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;

public class DbUtil {
    //todo: don't forget to specify your db properties
    static String url = "jdbc:postgresql://localhost:5432/postgres";
    static String user = "postgres";
    static String pass = "admin";

    private static DataSource dataSource;

    public static DataSource getDataSource() {
        if (dataSource == null) {
            dataSource = createDataSource(url, user, pass);
        }
        return dataSource;
    }

    private static DataSource createDataSource(String url, String username, String pass) {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl(url);
        dataSource.setUser(username);
        dataSource.setPassword(pass);
        return dataSource;
    }
}
