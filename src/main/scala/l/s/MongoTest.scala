package l.s

import java.util.Date

import com.alibaba.fastjson.{JSON, JSONObject}
import org.apache.spark.sql._
import org.apache.spark.sql.catalyst.encoders.ExpressionEncoder
import org.apache.spark.sql.expressions.{MutableAggregationBuffer, UserDefinedAggregateFunction}
import org.apache.spark.sql.types._

import scala.collection.mutable
import scala.collection.mutable.{ArrayBuffer, ListBuffer, WrappedArray}


object MongoTest {

    def main(args: Array[String]): Unit = {

        val spark = SparkSession.builder()
                .master("local[4]")
                .appName("MongoSparkConnectorIntro")
                .config("spark.mongodb.input.uri", "mongodb://root:zy79117911#@dds-wz9fcbcc474f93e41372-pub.mongodb.rds.aliyuncs.com:3717,dds-wz9fcbcc474f93e42901-pub.mongodb.rds.aliyuncs.com:3717/admin?replicaSet=mgset-10551825")
                .config("spark.mongodb.input.database", "TouTiao")
                .config("spark.mongodb.input.collection", "toutiaoIncrement")
                .config("spark.mongodb.input.partitionerOptions.partitionKey", "create_time")
                .config("spark.mongodb.output.uri", "mongodb://root:zy79117911#@dds-wz9fcbcc474f93e41372-pub.mongodb.rds.aliyuncs.com:3717,dds-wz9fcbcc474f93e42901-pub.mongodb.rds.aliyuncs.com:3717/admin?replicaSet=mgset-10551825")
                .config("spark.mongodb.output.database", "TouTiao")
                .config("spark.mongodb.output.collection", "testSpark")
                .getOrCreate()

        val sc = spark.sparkContext

        import com.mongodb.spark._
        import com.mongodb.spark.config._
        import org.apache.spark.sql.functions._
        val t = new Date().getTime

        val endTime = new Date().getTime / 1000
        val startDate = endTime - 1 * 86400
        val readConfig = ReadConfig(Map("collection" -> "toutiaoIncrement", "readPreference.name" -> "secondaryPreferred"), Some(ReadConfig(sc)))
        val toutiaoIncrement = MongoSpark.load(spark, readConfig)

        toutiaoIncrement.createOrReplaceTempView("toutiaoIncrement")

        var df = spark.sql(s"select category,impression_count,label  from toutiaoIncrement where create_time >= $startDate and create_time < $endTime")
        df.createOrReplaceTempView("toutiaoIncrement1")


        def str2arr(s: String) : Array[String] = {
            import collection.JavaConverters._
            val a: mutable.Buffer[String]  =  JSON.parseArray(s, classOf[String]).asScala
            a.toArray
        }

        def field(category: String): String = category match {
            case "news_entertainment" => "entertainment"
            case "news_entertainme" => "entertainment"
            case "movie" => "entertainment"
            case "news_tech" => "technology"
            case "digital" => "technology"
            case "news_game" => "game"
            case _ => "other"
        }

        spark.udf.register("str2arr",udf(str2arr _))
        spark.udf.register("field", field _)
        spark.udf.register("mergeArray", new MergeArray)

        df = spark.sql("select field(category) as category,impression_count,str2arr(label) as label from toutiaoIncrement1")
        df.createOrReplaceTempView("toutiaoIncrement2")

        df = spark.sql("select category,impression_count,label as relate_labels,explode(label) as label from toutiaoIncrement2")
        df.createOrReplaceTempView("toutiaoIncrement3")
        df = spark.sql("select category,label,sum(impression_count) as impression_count, avg(impression_count), count(impression_count), mergeArray(relate_labels)  from toutiaoIncrement3 group by category,label")
        df.show(10)

        val tt = new Date().getTime
        println(tt-t)


//
//        def group(row: Row) : (String, String) = {
//            (field(row.getAs("category")) , row.getAs("label"))
//        }
//
//        implicit val evidence$3: Encoder[(String, String)] = Encoders.tuple(Encoders.STRING, Encoders.STRING)
//
//        val t1 = df.groupByKey(group).agg(count("category"), new TypedColumn[Row, Long]( sum("impression_count").expr, ExpressionEncoder.apply()), new TypedColumn[Row, Double](avg("impression_count").expr, ExpressionEncoder.apply()))
//        t1.foreach(e=>println(s"${e._1} ${e._2} ${e._3} ${e._4}"))
    }

}


class MergeArray extends UserDefinedAggregateFunction {
    //输入的数据结构
    override def inputSchema: StructType = StructType(Array(StructField("arr", ArrayType(StringType))))

    //中间结果数据结构
    override def bufferSchema: StructType = StructType(Array(StructField("arr1", ArrayType(StringType))))

    //输出的数据类型
    override def dataType: DataType = ArrayType(StringType)


    override def deterministic: Boolean = true

    override def initialize(buffer: MutableAggregationBuffer): Unit = {
        buffer(0) = new mutable.WrappedArray.ofRef[String](Array())
    }

    //局部聚合
    override def update(buffer: MutableAggregationBuffer, input: Row): Unit = {
        val lb = buffer.getAs[mutable.WrappedArray.ofRef[String]](0)
        buffer(0) = lb ++ input.getAs[mutable.WrappedArray.ofRef[String]](0)
    }

    //全局聚合
    override def merge(buffer1: MutableAggregationBuffer, buffer2: Row): Unit = {
        buffer1(0) = buffer1.getAs[mutable.WrappedArray.ofRef[String]](0) ++ buffer2.getAs[mutable.WrappedArray.ofRef[String]](0)
    }

    //返回最终结果
    override def evaluate(buffer: Row): Any = {
        val strings = buffer.getAs[mutable.WrappedArray.ofRef[String]](0)
        strings.map((_,1)).groupBy(_._1).mapValues(_.reduce((a1,a2)=>(a1._1, a1._2 + a2._2))).toList.sortBy(0 - _._2._2).slice(0,1).map(_._1)
    }
}
