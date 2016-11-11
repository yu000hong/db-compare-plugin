package com.yu000hong.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.StopExecutionException
import org.gradle.api.tasks.TaskAction

import java.sql.Connection
import java.sql.DriverManager

class DBCompareTask extends DefaultTask {

    @TaskAction
    public void compare() {
        def container = getProject().getExtensions()
        def ext = container.findByType(DBComparePluginExtension.class)
        if (ext == null) {
            throw new Exception('please config dbCompare extension')
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
        DriverManager.forName(ext.driver, true, this.getActionClassLoaders()[0])
        Connection testConn = null
        Connection prodConn = null
        try {
            testConn = DriverManager.getConnection(ext.testUrl, ext.testUser, ext.testPasswd)
            prodConn = DriverManager.getConnection(ext.prodUrl, ext.prodUser, ext.prodPasswd)
            def testTables = DBUtil.getTables(testConn.getMetaData())
            def prodTables = DBUtil.getTables(prodConn.getMetaData())
            def result = DBUtil.diff(testTables, prodTables, false)
            if (result.different) {
                println(result.message)
                throw new RuntimeException(result.message)
            }
            println("The databases have no difference")
        } finally {
            if (testConn != null) {
                try {
                    testConn.close()
                } catch (ignored) {
                }
            }
            if (prodConn != null) {
                try {
                    prodConn.close()
                } catch (ignored) {
                }
            }
        }
    }

}