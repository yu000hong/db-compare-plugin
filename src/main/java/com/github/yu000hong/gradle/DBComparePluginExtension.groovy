package com.github.yu000hong.gradle

class DBComparePluginExtension {
    String testUrl
    String testUser
    String testPasswd
    String prodUrl
    String prodUser
    String prodPasswd
    String driver = 'com.mysql.Driver'
    String tables = '*'
}
