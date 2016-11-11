import com.yu000hong.gradle.DBUtil

import java.sql.DriverManager


def prodUrl = 'jdbc:mysql://localhost:3306/test?useUnicode=yes&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&autoReconnect=true'
def prodUser = 'root'
def prodPasswd = 'yu000hong'
//Class.forName('com.mysql.jdbc.Driver')
DriverManager.forName('com.mysql.jdbc.Driver')
prodConn = DriverManager.getConnection(prodUrl, prodUser, prodPasswd)

def prodTables = DBUtil.getTables(prodConn.getMetaData())
println(prodTables.size())

