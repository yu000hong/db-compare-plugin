package com.github.yu000hong.gradle

import java.sql.DatabaseMetaData
import java.sql.ResultSetMetaData

class DBUtil {

    /**
     * 获取数据库的数据表名列表
     */
    public static Collection<String> getTableNames(DatabaseMetaData meta) {
        def tables = []
        def rs = meta.getTables(null, null, null, ['TABLE'] as String[])
        while (rs.next()) {
            tables << rs.getString('TABLE_NAME')
        }
        return tables
    }

    /**
     * 打印元数据信息
     */
    public static void print(ResultSetMetaData meta) {
        def count = meta.getColumnCount()
        println("Column count: $count")
        if (count <= 0) {
            return
        }
        (1..count).each { i ->
            println("------------------")
            def columnClassName = meta.getColumnClassName(i)
            def columnName = meta.getColumnName(i)
            def columnLabel = meta.getColumnLabel(i)
            def columnType = meta.getColumnType(i)
            def columnDisplaySize = meta.getColumnDisplaySize(i)
            def scale = meta.getScale(i)
            def precision = meta.getPrecision(i)
            println("\tclass name: $columnClassName")
            println("\tcolumn name: $columnName")
            println("\tcolumn label: $columnLabel")
            println("\tcolumn type: $columnType")
            println("\tdisplay size: $columnDisplaySize")
            println("\tscale: $scale")
            println("\tprecision: $precision")
        }
    }

    /**
     * 获取数据表字段列表
     */
    public static Collection<Field> getFields(DatabaseMetaData meta, String tableName) {
        def fields = []
        def columnsResultSet = meta.getColumns(null, null, tableName, null)
        while (columnsResultSet.next()) {
            def name = columnsResultSet.getString('COLUMN_NAME')
            def typeName = columnsResultSet.getString('TYPE_NAME')
            def size = columnsResultSet.getInt('COLUMN_SIZE')
            def nullable = columnsResultSet.getInt('NULLABLE') ? true : false
            def defValue = columnsResultSet.getString('COLUMN_DEF')
            def comment = columnsResultSet.getString('REMARKS')
            def autoIncr = columnsResultSet.getString('IS_AUTOINCREMENT')
            fields << new Field(
                    name: name,
                    type: typeName,
                    size: size,
                    nullable: nullable,
                    defaultValue: defValue,
                    autoIncrement: autoIncr == 'YES',
                    comment: comment)
        }
        return fields
    }

    /**
     * 获取数据表索引列表
     */
    public static Collection<Index> getIndexes(DatabaseMetaData meta, String tableName) {
        def indexMap = [:] as HashMap<String, Index>
        def rs = meta.getIndexInfo(null, null, tableName, false, true)
        while (rs.next()) {
            def name = rs.getString('INDEX_NAME')
            def unique = !rs.getBoolean('NON_UNIQUE')
            def position = rs.getShort('ORDINAL_POSITION')
            def field = rs.getString('COLUMN_NAME')
            def index = indexMap[name]
            if (!index) {
                index = new Index(name: name, unique: unique)
                indexMap[name] = index
            }
            index.addField(position, field)
        }
        return indexMap.values()
    }

    /**
     * 获取数据表信息
     */
    public static Table getTable(DatabaseMetaData meta, String tableName) {
        def rs = meta.getTables(null, null, tableName, ['TABLE'] as String[])
        if (!rs.next()) {
            return null
        }

        def tableComment = rs.getString('REMARKS')
        def table = new Table(name: tableName, comment: tableComment)
        getFields(meta, tableName).each { field ->
            table.addField(field)
        }
        getIndexes(meta, tableName).each { index ->
            table.addIndex(index)
        }
        return table
    }

}
