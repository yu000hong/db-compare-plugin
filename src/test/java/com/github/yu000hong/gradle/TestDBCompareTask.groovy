package com.github.yu000hong.gradle

import com.github.yu000hong.spring.common.test.DBUnitUtil
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.testng.annotations.AfterClass
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test

import javax.annotation.Resource
import javax.sql.DataSource
import java.sql.DatabaseMetaData

@ContextConfiguration(locations = 'classpath:db.xml')
class TestDBCompareTask extends AbstractTestNGSpringContextTests {
    private DBUnitUtil testDbUnitUtil
    private DBUnitUtil prodDbUnitUtil
    private DatabaseMetaData testMeta
    private DatabaseMetaData prodMeta

    @Resource(name = 'testDataSourceH2')
    private DataSource testDataSource
    @Resource(name = 'prodDataSourceH2')
    private DataSource prodDataSource

    @BeforeClass
    public void setup() {
        //设置测试数据库
        testMeta = testDataSource.getConnection().getMetaData()
        testDbUnitUtil = new DBUnitUtil(testDataSource, [])
        testDbUnitUtil.setSqlDir('./src/test/resources/test/h2')
        testDbUnitUtil.setType(DBUnitUtil.DBType.H2)
        testDbUnitUtil.assertEmpty()
        testDbUnitUtil.createTable('account')
        testDbUnitUtil.createTable('comment')
        testDbUnitUtil.createTable('message')
        //设置正式数据库
        prodMeta = prodDataSource.getConnection().getMetaData()
        prodDbUnitUtil = new DBUnitUtil(prodDataSource, [])
        prodDbUnitUtil.setSqlDir('./src/test/resources/prod/h2')
        prodDbUnitUtil.setType(DBUnitUtil.DBType.H2)
        prodDbUnitUtil.assertEmpty()
        prodDbUnitUtil.createTable('account')
        prodDbUnitUtil.createTable('comment')
        prodDbUnitUtil.createTable('user')
    }

    @AfterClass
    public void tearDown() {
        testDbUnitUtil.dropTable('account')
        testDbUnitUtil.dropTable('comment')
        testDbUnitUtil.dropTable('message')
        prodDbUnitUtil.dropTable('account')
        prodDbUnitUtil.dropTable('comment')
        prodDbUnitUtil.dropTable('user')
    }

    @Test
    public void test() {
        String result = DBCompareTask.diff(testMeta, prodMeta, '*')
        println(result)
    }

}
