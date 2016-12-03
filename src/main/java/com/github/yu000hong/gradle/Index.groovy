package com.github.yu000hong.gradle

/**
 * Index: one of elements of Table
 */
public class Index implements Comparable<Index>, Differable<Index>, Definition {
    private final Map<Integer, String> map = [:] //key:1,2,3...;value:column name

    String name
    boolean unique

    public void setName(String name) {
        this.name = name?.toUpperCase()
    }

    /**
     * 添加字段
     * @param position 字段所在索引位置
     * @param field 字段名
     */
    void addField(int position, String field) {
        map.put(position, field)
    }

    List<String> fields() {
        def list = []
        def size = map.size()
        for (int i = 1; i <= size; i++) {
            list << "`${map[i]}`"
        }
        return list
    }

    @Override
    String getDefinition() {
        if ('PRIMARY' == name) {
            return "PRIMARY KEY (${fields().join(',')})"
        }
        return "${unique ? 'UNIQUE KEY' : 'KEY'} $name(${fields().join(', ')})"
    }

    @Override
    int compareTo(Index o) {
        if (!o || o.name == 'PRIMARY') {
            return 1
        }
        return this.name <=> o.name
    }

    @Override
    String toString() {
        return ('PRIMARY' == name) ? "PRIMARY KEY (${fields().join(',')})"
                : "${unique ? 'UNIQUE KEY' : 'KEY'} $name(${fields().join(', ')})"
    }

    @Override
    boolean equals(Object o) {
        if (!o) {
            return false
        }
        if (this == o) {
            return true
        }
        def other = o as Index
        return this.name == other.name
    }

    @Override
    int hashCode() {
        return name.hashCode()
    }

    @Override
    boolean isDifferent(Index o) {
        if (!o || name != o.name || unique != o.unique || map.size() != o.map.size()) {
            return true
        }
        def found = map.find { k, v ->
            map[k] != o.map[k]
        }
        return found
    }
}
