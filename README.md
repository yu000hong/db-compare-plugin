# db-compare-plugin

db-compare-plugin is a gradle plugin for database comparison. Because it
compares database metadata through JDBC, so it can compare different databases.
However, it can only compare the common parts of these databases.

### Usage

**build.gradle**

```groovy
buildscript {
    repositories {
        mavenCentral()
        maven {
            url 'https://jitpack.io'
        }
    }
    dependencies {
        classpath 'com.github.yu000hong:db-compare-plugin:1.0.3'
        classpath 'mysql:mysql-connector-java:5.1.18'
    }
}

apply plugin: 'com.github.yu000hong.dbcompare'

dbCompareConfig{
    testUrl = 
    testUser
    testPasswd
    prodUrl
    prodUser
    prodPasswd
    driver = 'com.mysql.Driver'
    tables = '*'
}
```

**Execute**

```bash
$> gradle dbCompare
```

### Output

If there is no difference between two databases, then no output. Or, the output will look like as follow:

```
 ----------------------------------------------------------------------
 Table                       Test                    Prod
 ----------------------------------------------------------------------
 account                     -                       +
 ----------------------------------------------------------------------
 article                     -                       +
 ----------------------------------------------------------------------
 comment                     +                       -
 ----------------------------------------------------------------------
 message
   F:xcid                    +                       -
   F:commentId               VARCHAR(30) NOT NULL    VARCHAR(30) NULL
   I:index_xcid_articleId    -                       +
 ----------------------------------------------------------------------
```