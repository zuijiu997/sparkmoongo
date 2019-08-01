package l.s

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.serializer.{SerializeFilter, SerializerFeature}

import scala.collection.mutable
import scala.util.control.Breaks

object Test {
    def main(args: Array[String]): Unit = {
        var a = 0
        val numList = List(1,2,3,4,5,6,7,8,9,10)

        val loop = new Breaks
        loop.breakable {
            while (true) {
                println(1)
                println(2)
                println(3)
                println(4)
            }
        }
    }

}
