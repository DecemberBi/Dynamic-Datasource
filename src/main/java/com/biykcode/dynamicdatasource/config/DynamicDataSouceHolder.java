package com.biykcode.dynamicdatasource.config;

public class DynamicDataSouceHolder {

    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    public static String getDataSource() {
        return contextHolder.get();
    }

    public static void setDataSource(String key) {
        contextHolder.set(key);
    }

    public static void clearDataSource() {
        contextHolder.remove();
    }
}
