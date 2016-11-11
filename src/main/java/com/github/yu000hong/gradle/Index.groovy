package com.github.yu000hong.gradle

public class Index implements Comparable<Index>, Differable<Index> {
    private Map<Integer, String> map = new HashMap<>() //key:1,2,3...;value:column name

    String name
    boolean unique

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
    int compareTo(Index o) {
        if (!o || o.name == 'PRIMARY') {
            return 1
        }
        return this.name <=> o.name
    }

    @Override
    String toString() {
        if ('PRIMARY' == name) {
            return "PRIMARY KEY (${fields().join(',')})"
        } else {
            return "${unique ? 'UNIQUE KEY' : 'KEY'} $name(${fields().join(', ')})"
        }
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
    DiffResult diff(Index o, boolean strict) {
        if (!o || name != o.name || unique != o.unique || map.size() != o.map.size()) {
            return new DiffResult(different: true, message: "index:$name")
        }
        def found = map.find { k, v ->
            return map[k] != o.map[k]
        }
        if (found) {
            return new DiffResult(different: true, message: "index:$name")
        } else {
            return new DiffResult(different: false)
        }
    }
}
