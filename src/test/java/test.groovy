import com.github.yu000hong.spring.common.util.JsonUtil

def set1 = [1, 2, 3, 4] as Set
def set2 = [3, 4, 5] as Set
def collection = set1 + set2
println(JsonUtil.toJson(collection))
println(collection.getClass().name)
