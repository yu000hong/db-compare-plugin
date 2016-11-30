package com.github.yu000hong.gradle


public class Table implements Comparable<Table> {
    String name
    String comment
    final TreeMap<String, Field> fields = new TreeMap<>()
    final TreeMap<String, Index> indexes = new TreeMap<>()

    public void addField(Field field) {
        fields.put(field.name, field)
    }

    public void addIndex(Index index) {
        indexes.put(index.name, index)
    }

    public Set<String> getFieldNames() {
        return fields.keySet()
    }

    public Set<String> getIndexNames() {
        return indexes.keySet()
    }

    public Field getField(String fieldName) {
        return fields.get(fieldName)
    }

    public Index getIndex(String indexName) {
        return indexes.get(indexName)
    }

    @Override
    int hashCode() {
        return name.hashCode()
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

}

