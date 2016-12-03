package com.github.yu000hong.gradle

/**
 * compare field and index
 */
interface Differable<T> {

    boolean isDifferent(T o)

}
