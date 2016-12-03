package com.github.yu000hong.gradle

/**
 *
 * 比较结果最终展现形式:
 *
 * ----------------------------------------------------------------------
 * Table                       Test                    Prod
 * ----------------------------------------------------------------------
 * account                     -                       +
 * ----------------------------------------------------------------------
 * article                     -                       +
 * ----------------------------------------------------------------------
 * comment                     +                       -
 * ----------------------------------------------------------------------
 * message
 *   F:xcid                    +                       -
 *   F:commentId               VARCHAR(30) NOT NULL    VARCHAR(30) NULL
 *   I:index_xcid_articleId    -                       +
 * ----------------------------------------------------------------------
 *
 */
class TableResult {
    String tableName
    Table testTable
    Table prodTable

    private int columnTitle
    private int columnTest
    private int columnProd

    private boolean different
    private final List<String> diffFieldNames = []
    private final List<String> diffIndexNames = []

    public TableResult(String tableName, Table testTable, Table prodTable) {
        this.tableName = tableName
        this.testTable = testTable
        this.prodTable = prodTable

        diff()
    }

    public String getTableName() {
        return tableName
    }

    public Table getTestTable() {
        return testTable
    }

    public Table getProdTable() {
        return prodTable
    }

    public int getColumnTitle() {
        return columnTitle
    }

    public int getColumnTest() {
        return columnTest
    }

    public int getColumnProd() {
        return columnProd
    }

    public boolean isDifferent() {
        return different
    }

    public List<String> getDiffFieldNames() {
        return diffFieldNames
    }

    public List<String> getDiffIndexNames() {
        return diffIndexNames
    }

    private void diff() {
        //设置默认值
        columnTitle = tableName.length()
        columnTest = 1
        columnProd = 1
        different = true
        //如果其中一个为null, 那么直接返回, 不再进行比较
        if (testTable == null || prodTable == null) {
            return
        }
        //比较字段和索引
        def filedNameSet = testTable.getFieldNames() + prodTable.getFieldNames()
        def indexNameSet = testTable.getIndexNames() + prodTable.getIndexNames()
        filedNameSet.each { fieldName ->
            Field testField = testTable.getField(fieldName)
            Field prodField = prodTable.getField(fieldName)
            if (testField == null || prodField == null) {
                diffFieldNames << fieldName
                return
            }
            if (testField.isDifferent(prodField)) {
                diffFieldNames << fieldName
                if (fieldName.length() + 4 > columnTitle) {
                    columnTitle = fieldName.length() + 4
                }
                def testDefinition = testField.getDefinition()
                if (testDefinition.length() > columnTest) {
                    columnTest = testDefinition.length()
                }
                def prodDefinition = prodField.getDefinition()
                if (prodDefinition.length() > columnProd) {
                    columnProd = prodDefinition.length()
                }
            }
        }
        indexNameSet.each { indexName ->
            Index testIndex = testTable.getIndex(indexName)
            Index prodIndex = prodTable.getIndex(indexName)
            if (testIndex == null || prodIndex == null) {
                diffIndexNames << indexName
                return
            }
            if (testIndex.isDifferent(prodIndex)) {
                diffIndexNames << indexName
                if (indexName.length() + 4 > columnTitle) {
                    columnTitle = indexName.length() + 4
                }
                def testDefinition = testIndex.getDefinition()
                if (testDefinition.length() > columnTest) {
                    columnTest = testDefinition.length()
                }
                def prodDefinition = prodIndex.getDefinition()
                if (prodDefinition.length() > columnProd) {
                    columnProd = prodDefinition.length()
                }
            }
        }
        //完全相同
        if (!diffFieldNames && !diffIndexNames) {
            different = false
            columnTitle = tableName.length()
            columnTest = 0
            columnProd = 0
        }
    }

}
