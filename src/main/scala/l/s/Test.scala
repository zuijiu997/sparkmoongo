package l.s

import scala.collection.mutable

object Test {
    def main(args: Array[String]): Unit = {
        val strings = new mutable.WrappedArray.ofRef[String](Array("a", "b", "a", "a", "b"))
        val stringToInt = strings.map((_, 1)).groupBy(_._1).mapValues(_.reduce((a1,a2)=>(a1._1, a1._2 + a2._2)))
        println(stringToInt)
    }

}
