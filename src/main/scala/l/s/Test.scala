package l.s

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.serializer.{SerializeFilter, SerializerFeature}

import scala.collection.mutable

object Test {
    def main(args: Array[String]): Unit = {
      val l = Array("1", "2", "3")
      val str = JSON.toJSONString(l, new Array[SerializeFilter](0))
      println(str)
    }

}
