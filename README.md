# db-compare-plugin

buildscript {
    repositories {
        mavenCentral()
        mavenLocal()
    }
    dependencies {
//        classpath 'com.yu000hong:db-compare:1.0.2-SNAPSHOT'
        classpath 'mysql:mysql-connector-java:5.1.18'
    }
}

//apply plugin: 'com.yu000hong.dbcompare'
//
//dbCompareConfig {
//    driver = 'com.mysql.jdbc.Driver'
//    testUrl = 'jdbc:mysql://localhost:3306/test?useUnicode=yes&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&autoReconnect=true'
//    testUser = 'root'
//    testPasswd = 'yu000hong'
//    prodUrl = 'jdbc:mysql://localhost:3306/prod?useUnicode=yes&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&autoReconnect=true'
//    prodUser = 'root'
//    prodPasswd = 'yu000hong'
//}

