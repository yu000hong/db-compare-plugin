package com.github.yu000hong.gradle

import com.github.yu000hong.spring.common.test.DBUnitUtil
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

    @Resource(name = 'testDataSource')
    private DataSource dataSource

    @BeforeClass
    public void setup() {
        dbUnitUtil = new DBUnitUtil(dataSource, [])
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
            Assert.assertNotNull(table.getField('uid'))
            Assert.assertNotNull(table.getField('nickname'))
            Assert.assertNotNull(table.getField('code'))
            Assert.assertNotNull(table.getField('image'))
            Assert.assertNotNull(table.getField('intro'))
            Assert.assertNotNull(table.getField('create_time'))
            Assert.assertNotNull(table.getIndex('PRIMARY'))
            Assert.assertNotNull(table.getIndex('idx_nickname'))
            Assert.assertNotNull(table.getIndex('idx_code'))
        } finally {
            dbUnitUtil.dropTable(tableName)
        }
    }

}
