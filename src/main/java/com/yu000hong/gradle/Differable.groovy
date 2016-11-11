package com.yu000hong.gradle

interface Differable<T> {

    DiffResult diff(T o, boolean strict)

}
