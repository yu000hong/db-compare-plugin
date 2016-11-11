package com.github.yu000hong.gradle


public class Table implements Comparable<Table>, Differable<Table> {
    String name
    String comment
    final TreeMap<String, Field> fields = new TreeMap<>()
    final TreeMap<String, Index> indexes = new TreeMap<>()

    @Override
    int hashCode() {
        name.hashCode()
    }

    void addField(Field field) {
        fields.put(field.name, field)
    }

    void addIndex(Index index) {
        indexes.put(index.name, index)
    }

    @Override
    boolean equals(Object o) {
        if (!o) {
            return false
        }
        if (this == o) {
            return true
        }
        def other = o as Table
        return this.name == other.name
    }

    @Override
    String toString() {
        def text = "CREATE TABLE IF NOT EXISTS `$name` ("
        fields.navigableKeySet().each { key ->
            text = "$text\n    ${fields[key]},"
        }
        indexes.navigableKeySet().each { key ->
            text = "$text\n    ${indexes[key]},"
        }
        text = text.substring(0, text.length() - 1)
        text = "$text\n)\n"
        if (comment) {
            text = "${text}COMMENT '$comment'"
        }
        return "$text;"
    }

    @Override
    int compareTo(Table o) {
        if (o) {
            return this.name <=> o.name
        } else {
            return 1
        }
    }

    @Override
    DiffResult diff(Table o, boolean strict) {
        if (!o) {
            return new DiffResult(different: true, message: "======================\ntable:$name\n")
        }
        def fieldKeys = fields.keySet() + o.fields.keySet()
        def indexKeys = indexes.keySet() + o.indexes.keySet()
        def results = new ArrayList<DiffResult>()
        fieldKeys.each { key ->
            if (fields[key] != null) {
                results << fields[key].diff(o.fields[key], strict)
            } else {
                results << o.fields[key].diff(fields[key], strict)
            }
        }
        indexKeys.each { key ->
            if (indexes[key] != null) {
                results << indexes[key].diff(o.indexes[key], strict)
            } else {
                results << o.indexes[key].diff(indexes[key], strict)
            }
        }
        def diffs = results.findAll { result ->
            return result.different
        }
        if (!diffs) {
            return new DiffResult(different: false)
        } else {
            def msg = diffs.collect {
                return it.message
            }.join(',')
            msg = "======================\ntable:$name\n${msg}\n"
            return new DiffResult(different: true, message: msg)
        }
    }
}

