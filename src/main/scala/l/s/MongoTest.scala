package l.s

import java.util.Date

import com.alibaba.fastjson.{JSON, JSONObject}
import org.apache.spark.sql._
import org.apache.spark.sql.catalyst.encoders.ExpressionEncoder


object MongoTest {

    def main(args: Array[String]): Unit = {

        val spark = SparkSession.builder()
                .master("local[2]")
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

        val endTime = new Date().getTime / 1000
        val startDate = endTime - 1 * 86400
        val readConfig = ReadConfig(Map("collection" -> "toutiaoIncrement", "readPreference.name" -> "secondaryPreferred"), Some(ReadConfig(sc)))
        val toutiaoIncrement = MongoSpark.load(spark, readConfig)

        toutiaoIncrement.createOrReplaceTempView("toutiaoIncrement")

        val df = spark.sql(s"select category,impression_count,label  from toutiaoIncrement where create_time >= $startDate and create_time < $endTime")
        df.createOrReplaceTempView("toutiaoIncrement1")



        val str2arr = (s: String) => List[String] = {
            import collection.JavaConverters._
            JSON.parseArray(s, classOf[String]).asScala
        }


        spark.udf.register("str2arr",udf(str2arr))

        val frame = spark.sql("select category,impression_count,str2arr(label) as label from toutiaoIncrement1")
        frame.show(10)

//        def field(category: String): String = category match {
//            case "news_entertainment" => "entertainment"
//            case "news_entertainme" => "entertainment"
//            case "movie" => "entertainment"
//            case "news_tech" => "technology"
//            case "digital" => "technology"
//            case "news_game" => "game"
//            case _ => "other"
//        }
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
