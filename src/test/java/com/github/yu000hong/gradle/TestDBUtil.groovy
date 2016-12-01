package com.github.yu000hong.gradle

import com.github.yu000hong.spring.common.test.DBUnitUtil
import com.github.yu000hong.spring.common.util.JsonUtil
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.testng.Assert
import org.testng.annotations.AfterClass
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test

import javax.annotation.Resource
import javax.sql.DataSource
import java.sql.DatabaseMetaData

@ContextConfiguration(locations = 'classpath:db.xml')
class TestDBUtil extends AbstractTestNGSpringContextTests {
    private DBUnitUtil dbUnitUtil
    private DatabaseMetaData meta

    @Resource(name = 'testDataSourceH2')
    private DataSource dataSource

    @BeforeClass
    public void setup() {
        dbUnitUtil = new DBUnitUtil(dataSource, [])
        dbUnitUtil.setType(DBUnitUtil.DBType.H2)
        dbUnitUtil.setSqlDir("./src/test/resources/test/h2")
        println(JsonUtil.toJson(DBUtil.getTableNames(dataSource.getConnection().getMetaData())))
        dbUnitUtil.assertEmpty()
        meta = dataSource.getConnection().getMetaData()
    }

    @AfterClass
    public void tearDown() {
        dbUnitUtil.dropTable('account')
        dbUnitUtil.dropTable('comment')
        dbUnitUtil.dropTable('message')
    }

    @Test
    public void testGetTableNames() {
        try {
            def tables = DBUtil.getTableNames(meta)
            Assert.assertEquals(tables.size(), 0)

            dbUnitUtil.createTable('account')
            dbUnitUtil.createTable('comment')
            dbUnitUtil.createTable('message')
            tables = DBUtil.getTableNames(meta)
            Assert.assertEquals(tables.size(), 3)
            Assert.assertTrue(tables.contains('account'))
            Assert.assertTrue(tables.contains('comment'))
            Assert.assertTrue(tables.contains('message'))
        } finally {
            dbUnitUtil.dropTable('account')
            dbUnitUtil.dropTable('comment')
            dbUnitUtil.dropTable('message')
        }
    }

    @Test
    public void testGetFields() {
        def tableName = 'account'
        try {
            dbUnitUtil.createTable(tableName)
            def fields = DBUtil.getFields(meta, tableName)
            Assert.assertEquals(fields.size(), 6)
        } finally {
            dbUnitUtil.dropTable(tableName)
        }
    }

    @Test
    public void testGetIndexes() {
        def tableName = 'account'
        try {
            dbUnitUtil.createTable(tableName)
            def indexes = DBUtil.getIndexes(meta, tableName)
            Assert.assertEquals(indexes.size(), 3)
        } finally {
            dbUnitUtil.dropTable(tableName)
        }
    }

    @Test
    public void testGetTable() {
        def tableName = 'account'
        try {
            dbUnitUtil.createTable(tableName)
            def table = DBUtil.getTable(meta, 'xxx')
            Assert.assertNull(table)
            table = DBUtil.getTable(meta, tableName)
            Assert.assertNotNull(table)
            Assert.assertEquals(table.fieldNames.size(), 6)
            Assert.assertEquals(table.indexNames.size(), 3)
            Assert.assertNotNull(table.getField('UID'))
            Assert.assertNotNull(table.getField('NICKNAME'))
            Assert.assertNotNull(table.getField('CODE'))
            Assert.assertNotNull(table.getField('IMAGE'))
            Assert.assertNotNull(table.getField('INTRO'))
            Assert.assertNotNull(table.getField('CREATE_TIME'))
//            Assert.assertNotNull(table.getIndex('PRIMARY'))//不同数据库主键名不一样
            Assert.assertNotNull(table.getIndex('IDX_NICKNAME'))
            Assert.assertNotNull(table.getIndex('IDX_CODE'))
        } finally {
            dbUnitUtil.dropTable(tableName)
        }
    }

}
