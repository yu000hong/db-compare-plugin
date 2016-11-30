package com.github.yu000hong.gradle

interface Differable<T> {

    boolean isDifferent(T o)

}
