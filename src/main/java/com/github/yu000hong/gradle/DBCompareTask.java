package com.github.yu000hong.gradle;

import org.gradle.api.DefaultTask;
import org.gradle.api.plugins.ExtensionContainer;
import org.gradle.api.tasks.TaskAction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Map;

/**
 * 这里不能使用Groovy, 只能用Java语言
 * 使用Groovy编写这个类的时候, 会报莫名其妙的错误:
 * - Class.forName()的时候报ClassNotFoundException
 * - There is no suitable driver for jdbc sql url...
 */
public class DBCompareTask extends DefaultTask {

    @TaskAction
    public void compare() throws Exception {
        ExtensionContainer container = getProject().getExtensions();
        DBComparePluginExtension ext = container.findByType(DBComparePluginExtension.class);
        if (ext == null) {
            throw new RuntimeException("please config dbCompare extension");
        }
        Class.forName(ext.getDriver());
        Connection testConn = null;
        Connection prodConn = null;
        try {
            testConn = DriverManager.getConnection(ext.getTestUrl(), ext.getTestUser(), ext.getTestPasswd());
            prodConn = DriverManager.getConnection(ext.getProdUrl(), ext.getProdUser(), ext.getProdPasswd());
            Map<String, Table> testTables = DBUtil.getTables(testConn.getMetaData());
            Map<String, Table> prodTables = DBUtil.getTables(prodConn.getMetaData());
            DiffResult result = DBUtil.diff(testTables, prodTables, ext.getStrict());
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
