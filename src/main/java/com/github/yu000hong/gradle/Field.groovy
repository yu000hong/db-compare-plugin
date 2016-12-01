package com.github.yu000hong.gradle


public class Field implements Comparable<Field>, Differable<Field>, Definition {
    String name
    String type
    int size
    boolean nullable
    String defaultValue
    boolean autoIncrement
    String comment

    public void setName(String name) {
        this.name = name?.toUpperCase()
    }

    @Override
    String getDefinition() {
        def definition = "$type($size)"
        if (!nullable) {
            definition = "$definition NOT NULL"
        }
        if (defaultValue != null) {
            definition = "$definition DEFAULT '$defaultValue'"
        }
        if (autoIncrement) {
            definition = "$definition AUTO_INCREMENT"
        }
        return definition
    }

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
        return text
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
    boolean isDifferent(Field o) {
        if (o != null && name == o.name && type == o.type
                && size == o.size && nullable == o.nullable
                && defaultValue == o.defaultValue && autoIncrement == o.autoIncrement) {
            return false
        }
        return true
    }
}