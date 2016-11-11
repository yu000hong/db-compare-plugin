package com.yu000hong.gradle

import java.sql.DatabaseMetaData

class DBUtil {

    public static TreeMap<String, Table> getTables(DatabaseMetaData meta) {
        def map = new TreeMap<String, Table>()
        def rs = meta.getTables(null, null, null, ['TABLE'] as String[])
        while (rs.next()) {
            def tableName = rs.getString('TABLE_NAME')
            def tableComment = rs.getString('REMARKS')
            def table = new Table(name: tableName, comment: tableComment)
            //获取字段信息
            def columnsResultSet = meta.getColumns(null, null, tableName, null)
            while (columnsResultSet.next()) {
                def name = columnsResultSet.getString('COLUMN_NAME')
                def typeName = columnsResultSet.getString('TYPE_NAME')
                def size = columnsResultSet.getInt('COLUMN_SIZE')
                def nullable = columnsResultSet.getInt('NULLABLE') ? true : false
                def defValue = columnsResultSet.getString('COLUMN_DEF')
                def comment = columnsResultSet.getString('REMARKS')
                def autoIncr = columnsResultSet.getString('IS_AUTOINCREMENT')

                table.addField(new Field(
                        name: name,
                        type: typeName,
                        size: size,
                        nullable: nullable,
                        defaultValue: defValue,
                        autoIncrement: autoIncr == 'YES',
                        comment: comment))
            }
            //获取索引信息
            def indexMap = [:] as HashMap<String, Index>
            def uniqueIndexResultSet = meta.getIndexInfo(null, null, tableName, true, true)
            while (uniqueIndexResultSet.next()) {
                def name = uniqueIndexResultSet.getString('INDEX_NAME')
                def unique = !uniqueIndexResultSet.getBoolean('NON_UNIQUE')
                def position = uniqueIndexResultSet.getShort('ORDINAL_POSITION')
                def field = uniqueIndexResultSet.getString('COLUMN_NAME')
                def index = indexMap[name]
                if (!index) {
                    index = new Index(name: name, unique: unique)
                    indexMap[name] = index
                }
                index.addField(position, field)
            }
            def nonUniqueIndexResultSet = meta.getIndexInfo(null, null, tableName, false, true)
            while (nonUniqueIndexResultSet.next()) {
                def name = nonUniqueIndexResultSet.getString('INDEX_NAME')
                def unique = !nonUniqueIndexResultSet.getBoolean('NON_UNIQUE')
                def position = nonUniqueIndexResultSet.getShort('ORDINAL_POSITION')
                def field = nonUniqueIndexResultSet.getString('COLUMN_NAME')
                def index = indexMap[name]
                if (!index) {
                    index = new Index(name: name, unique: unique)
                    indexMap[name] = index
                }
                index.addField(position, field)
            }
            indexMap.values().each { index ->
                table.addIndex(index)
            }

            map[tableName] = table
        }
        map
    }

    public static DiffResult diff(Map<String, Table> test, Map<String, Table> prod, boolean strict) {
        def results = new ArrayList<DiffResult>()
        (test.keySet() + prod.keySet()).each { key ->
            if (test[key]) {
                results << test[key].diff(prod[key], strict)
            } else {
                results << prod[key].diff(test[key], strict)
            }
        }
        def diffs = results.findAll { result ->
            return result.different
        }
        if (diffs) {
            def msg = diffs.collect {
                return it.message
            }.join('\n')
            return new DiffResult(different: true, message: msg)
        } else {
            return new DiffResult(different: false)
        }
    }

}
