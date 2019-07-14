package l.s
import java.lang

import org.apache.spark.sql.{Dataset, SparkSession}
object Sparksql {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .master("local")
      .appName("Spark SQL basic example")
//      .config("spark.some.config.option", "some-value")
      .getOrCreate()

    val dsLong: Dataset[lang.Long] = spark.range(10)
    dsLong.createOrReplaceTempView("t")
    val frame = spark.sql(s"select cast(id as varchar(50)) from t")
    frame.printSchema()
    frame.show(10)
//    dsLong.printSchema()
//    import spark.implicits._
  }
}
