import com.github.yu000hong.gradle.DBUtil
import com.github.yu000hong.spring.common.util.JsonUtil

import java.sql.DriverManager


def url = 'jdbc:mysql://localhost:3306/test?useUnicode=yes&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&autoReconnect=true'
def user = 'root'
def passwd = 'yu000hong'
//Class.forName('com.mysql.jdbc.Driver')
DriverManager.forName('com.mysql.jdbc.Driver')
def conn = DriverManager.getConnection(url, user, passwd)

def fields = DBUtil.getFields(conn.getMetaData(), 'account')


def set1 = [1, 2, 3, 4] as Set
def set2 = [3, 4, 5] as Set
def collection = set1 + set2
println(JsonUtil.toJson(collection))
println(collection.getClass().name)
