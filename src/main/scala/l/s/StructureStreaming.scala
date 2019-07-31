package l.s

import org.apache.spark.sql.streaming.OutputMode
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}

object StructureStreaming {

    def main(args: Array[String]): Unit = {
        val spark: SparkSession = SparkSession
                .builder
                .appName("StructuredNetworkWordCount")
                .master("local[2]")
                .getOrCreate()

        import spark.implicits._
        // Create DataFrame representing the stream of input lines from connection to localhost:9999
        val lines: DataFrame = spark.readStream
                .format("socket")
                .option("host", "localhost")
                .option("port", 9999)
                .load()

        //        lines.printSchema()
        //        val query = lines.writeStream
        //            .outputMode(OutputMode.Append())
        //            .format("console")
        //            .start()


        // Split the lines into words
        val words: Dataset[String] = lines.as[String].flatMap(_.split(" "))

        // Generate running word count
        val wordCounts: DataFrame = words.groupBy("value").count()
        //
        //        val query = wordCounts.writeStream
        //                .outputMode("complete")
        //                .format("console")
        //                .start()
        val query = wordCounts.writeStream.outputMode(OutputMode.Update()).foreachBatch((x, y) => {
            x.printSchema()
            x.show()
            println(y)
        }).start()
        query.awaitTermination()
    }

}
