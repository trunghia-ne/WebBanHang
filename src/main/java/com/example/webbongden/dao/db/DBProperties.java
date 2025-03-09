package com.example.webbongden.dao.db;
import java.io.IOException;
import java.util.Properties;

public class DBProperties {
    private static Properties prop = new Properties();

    static {
        try {
            prop.load(DBProperties.class.getClassLoader().getResourceAsStream("db.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String host() {
        return prop.get("db.host").toString();
    }

    public static int port() {
        int port = 0;
        try {
            port = Integer.parseInt(prop.get("db.port").toString());
            return port;
        } catch (NumberFormatException e) {
            return 3306;
        }

    }

    public static String username() {
        return prop.get("db.username").toString();
    }

    public static String password() {
        return prop.get("db.password").toString();
    }

    public static String dbname() {
        return prop.get("db.dbname").toString();
    }

    public static String option() {
        return prop.get("db.option").toString();
    }

    public static void main(String[] args) {
        System.out.println(host());
    }
}

