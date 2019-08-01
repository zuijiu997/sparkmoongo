package l.s


import java.sql.Timestamp
import java.util.Date

import org.apache.spark.sql._
import org.apache.spark.sql.streaming.OutputMode
import org.apache.spark.sql.functions._
case class Message(key: String, value : String, timestamp: Timestamp)
object KafkaStreaming {
    def main(args: Array[String]): Unit = {
        val spark: SparkSession = SparkSession
                .builder
                .appName("StructuredNetworkWordCount")
                .master("local[2]")
                .getOrCreate()

        val sc = spark.sparkContext
        sc.setLogLevel("WARN")

        import spark.implicits._

        // Subscribe to 1 topic
        val df = spark
                .readStream
                .format("kafka")
                .option("kafka.bootstrap.servers", "192.168.1.133:9092")
                .option("subscribe", "test")
                .load()

        val ds = df.selectExpr("CAST(key AS STRING)", "CAST(value AS STRING) as value", "timestamp")
                .as[Message]

        val wc = ds.withWatermark("timestamp", "10 minutes")
                .groupBy(
                    window($"timestamp", "10 minutes", "5 minutes"),
                    $"value")
                .count()

//        val query = wc.writeStream.outputMode(OutputMode.Append()).foreachBatch((x, y) => {
//            x.printSchema()
//            x.show(20, truncate = false)
//            println(y)
//        }).start()

        val query = wc.writeStream.outputMode(OutputMode.Update()).foreach(new ForeachWriter[Row] {
            override def open(partitionId: Long, epochId: Long): Boolean = {
                true
            }

            override def process(record: Row): Unit = {
                println(record)
            }

            override def close(errorOrNull: Throwable): Unit = {

            }
        }).start()
        query.awaitTermination()
    }
}
