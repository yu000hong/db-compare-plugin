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

@ContextConfiguration(locations = 'classpath:db.xml')
class TestDBUtil extends AbstractTestNGSpringContextTests {
    private DBUnitUtil dbUnitUtil

    @Resource(name = 'testDataSource')
    private DataSource dataSource

    @BeforeClass
    public void setup() {
        dbUnitUtil = new DBUnitUtil(dataSource, [])
        dbUnitUtil.assertEmpty()
    }

    @AfterClass
    public void tearDown() {
        dbUnitUtil.dropTable('account')
        dbUnitUtil.dropTable('comment')
        dbUnitUtil.dropTable('message')
    }

    @Test
    public void testGetTableNames() {
        def meta = dataSource.getConnection().getMetaData()
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
        dbUnitUtil.dropTable('account')
        dbUnitUtil.dropTable('comment')
        dbUnitUtil.dropTable('message')
    }

}
