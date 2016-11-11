package com.github.yu000hong.gradle;

import org.gradle.api.DefaultTask;
import org.gradle.api.plugins.ExtensionContainer;
import org.gradle.api.tasks.TaskAction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Map;

public class DBCompareTask2 extends DefaultTask {

    @TaskAction
    public void compare() throws Exception {
        ExtensionContainer container = getProject().getExtensions();
        DBComparePluginExtension ext = container.findByType(DBComparePluginExtension.class);
        if (ext == null) {
            throw new RuntimeException("please config dbCompare extension");
        }

//        def driver = new Driver()
//        println("driver is null: " + (driver == null))
//        println("driver: " + driver)
//        DriverManager.registerDriver(driver)
//        println("more: " + DriverManager.drivers.hasMoreElements())
//        DriverManager.drivers.each { d ->
//            println(ext.testUrl + ": " + d.acceptsURL(ext.testUrl))
//        }
//        Class.forName(ext.driver)

//        Class.forName(ext.driver, true, this.getActions()[0].getClass().classLoader)
        Class.forName(ext.getDriver());
        Connection testConn = null;
        Connection prodConn = null;
        try {
            testConn = DriverManager.getConnection(ext.getTestUrl(), ext.getTestUser(), ext.getTestPasswd());
            prodConn = DriverManager.getConnection(ext.getProdUrl(), ext.getProdUser(), ext.getProdPasswd());
            Map<String, Table> testTables = DBUtil.getTables(testConn.getMetaData());
            Map<String, Table> prodTables = DBUtil.getTables(prodConn.getMetaData());
            DiffResult result = DBUtil.diff(testTables, prodTables, false);
            if (result.getDifferent()) {
                throw new RuntimeException(result.getMessage());
            }
            System.out.println("The databases have no difference");
        } finally {
            if (testConn != null) {
                try {
                    testConn.close();
                } catch (Exception ignored) {
                }
            }
            if (prodConn != null) {
                try {
                    prodConn.close();
                } catch (Exception ignored) {
                }
            }
        }
    }

}
