package com.yu000hong.gradle


public class Field implements Comparable<Field>, Differable<Field> {
    String name
    String type
    int size
    boolean nullable
    String defaultValue
    boolean autoIncrement
    String comment

    @Override
    int hashCode() {
        name.hashCode()
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
        def text = "`$name` $type($size)"
        if (!nullable) {
            text = "$text NOT NULL"
        }
        if (defaultValue != null) {
            text = "$text DEFAULT '$defaultValue'"
        }
        if (autoIncrement) {
            text = "$text AUTO_INCREMENT"
        }
        if (comment != null && !comment.isAllWhitespace()) {
            text = "$text COMMENT '$comment'"
        }
        text
    }

    @Override
    int compareTo(Field o) {
        if (o) {
            return this.name <=> o.name
        } else {
            return 1
        }
    }

    @Override
    DiffResult diff(Field o, boolean strict) {
        if (o != null && name == o.name && type == o.type
                && size == o.size && nullable == o.nullable
                && defaultValue == o.defaultValue && autoIncrement == o.autoIncrement) {
            if (!strict || comment == o.comment) {
                return new DiffResult(different: false)
            }
        }
        return new DiffResult(different: true, message: "field:$name")
    }
}