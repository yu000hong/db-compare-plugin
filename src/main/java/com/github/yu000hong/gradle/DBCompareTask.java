package com.github.yu000hong.gradle;

import org.gradle.api.DefaultTask;
import org.gradle.api.plugins.ExtensionContainer;
import org.gradle.api.tasks.TaskAction;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import static com.github.yu000hong.gradle.FormatUtil.*;

/**
 * The implementation of Task: dbCompare
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
            String result = diff(testConn.getMetaData(), prodConn.getMetaData(), ext.getTables());
            if (result != null) {
                throw new RuntimeException(result);
            }
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

    public static String diff(DatabaseMetaData testMeta, DatabaseMetaData prodMeta, String tables) {
        Collection<String> allTables = new HashSet<String>();
        if (tables.equals("*")) {
            Collection<String> testTables = DBUtil.getTableNames(testMeta);
            Collection<String> prodTables = DBUtil.getTableNames(prodMeta);
            allTables.addAll(testTables);
            allTables.addAll(prodTables);
        } else {
            Collections.addAll(allTables, tables.split(","));
        }
        Collection<TableResult> diffResults = new ArrayList<TableResult>();
        for (String tableName : allTables) {
            Table testTable = DBUtil.getTable(testMeta, tableName);
            Table prodTable = DBUtil.getTable(prodMeta, tableName);
            TableResult result = new TableResult(tableName, testTable, prodTable);
            if (result.isDifferent()) {
                diffResults.add(result);
            }
        }
        if (diffResults.isEmpty()) {
            return null;//completely have no difference
        }
        int columnTitle = 0;
        int columnTest = 0;
        int columnProd = 0;
        for (TableResult result : diffResults) {
            if (result.getColumnTitle() > columnTitle) {
                columnTitle = result.getColumnTitle();
            }
            if (result.getColumnTest() > columnTest) {
                columnTest = result.getColumnTest();
            }
            if (result.getColumnProd() > columnProd) {
                columnProd = result.getColumnProd();
            }
        }
        columnTitle = padWidth(columnTitle) + PAD_WIDTH;
        columnTest = padWidth(columnTest);
        columnProd = padWidth(columnProd);
        int totalWidth = columnTitle + columnTest + columnProd;
        StringBuilder sb = new StringBuilder();
        sb.append(repeat("-", totalWidth))
                .append("\n");
        sb.append(padding(repeat(" ", PAD_WIDTH) + "TABLE", columnTitle))
                .append(padding("TEST", columnTest))
                .append(padding("PROD", columnProd))
                .append("\n");
        sb.append(repeat("-", totalWidth))
                .append("\n");
        for (TableResult result : diffResults) {
            sb.append(padding(repeat(" ", PAD_WIDTH) + result.getTableName(), columnTitle));
            Table testTable = result.getTestTable();
            Table prodTable = result.getProdTable();
            if (testTable == null || prodTable == null) {
                String testDisplay = testTable == null ? "-" : "+";
                String prodDisplay = prodTable == null ? "-" : "+";
                sb.append(padding(testDisplay, columnTest))
                        .append(padding(prodDisplay, columnProd))
                        .append("\n");
            } else {
                sb.append("\n");
                for (String fieldName : result.getDiffFieldNames()) {
                    Field testField = testTable.getField(fieldName);
                    Field prodField = prodTable.getField(fieldName);
                    appendField(sb, fieldName, testField, prodField, columnTitle, columnTest, columnProd);
                }
                for (String indexName : result.getDiffIndexNames()) {
                    Index testIndex = testTable.getIndex(indexName);
                    Index prodIndex = prodTable.getIndex(indexName);
                    appendIndex(sb, indexName, testIndex, prodIndex, columnTitle, columnTest, columnProd);
                }

            }
            sb.append(repeat("-", totalWidth)).append("\n");
        }
        return sb.toString();
    }

    private static void appendField(StringBuilder sb, String fieldName, Field testField,
                                    Field prodField, int columnTitle, int columnTest, int columnProd) {
        sb.append(padding(repeat(" ", PAD_WIDTH) + "  F:" + fieldName, columnTitle));
        appendDefinition(sb, testField, prodField, columnTest, columnProd);
        sb.append("\n");
    }

    private static void appendIndex(StringBuilder sb, String indexName, Index testIndex,
                                    Index prodIndex, int columnTitle, int columnTest, int columnProd) {
        sb.append(padding(repeat(" ", PAD_WIDTH) + "  I:" + indexName, columnTitle));
        appendDefinition(sb, testIndex, prodIndex, columnTest, columnProd);
        sb.append("\n");
    }

    private static void appendDefinition(StringBuilder sb, Definition testDefinition,
                                         Definition prodDefinition, int columnTest, int columnProd) {
        if (testDefinition == null) {
            sb.append(padding("-", columnTest))
                    .append(padding("+", columnProd));
        } else if (prodDefinition == null) {
            sb.append(padding("+", columnTest))
                    .append(padding("-", columnProd));
        } else {
            sb.append(padding(testDefinition.getDefinition(), columnTest))
                    .append(padding(prodDefinition.getDefinition(), columnProd));
        }
    }

}
