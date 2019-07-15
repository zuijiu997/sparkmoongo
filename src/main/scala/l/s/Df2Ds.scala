package l.s

import org.apache.spark.sql.DataFrame

case class Bean(name: String, age: Int)

object Df2Ds {
    def main(args: Array[String]): Unit = {
//        val df: DataFrame;
//        需要隐式转换
//        val ds = df.as[Bean]

        //dataset可以想Rdd那样操作
        //用map flatmap reduceByKey 等算子
    }
}
