package com.github.yu000hong.gradle

interface Differable<T> {

    DiffResult diff(T o, boolean strict)

}
