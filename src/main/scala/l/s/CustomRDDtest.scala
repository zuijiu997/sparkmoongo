package l.s

import java.util.Date

import com.alicloud.openservices.tablestore.SyncClient
import com.alicloud.openservices.tablestore.model.PrimaryKey
import com.alicloud.openservices.tablestore.model.search.query.BoolQuery
import com.alicloud.openservices.tablestore.model.search.{SearchQuery, SearchRequest}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession
import org.apache.spark.{SparkConf, SparkContext}

object CustomRDDtest {
    def main(args: Array[String]): Unit = {

        val conf = new SparkConf().setAppName(this.getClass.getSimpleName)
                .setMaster("local[1]")
        val spark = SparkSession.builder()
                .config(conf)
                .getOrCreate()

        val sc: SparkContext = spark.sparkContext
        val d: CustomRDD = new CustomRDD("save_time", new Date().getTime - 7 * 86400*1000, new Date().getTime, 86400*1000, sc)
        d.foreach(println(_))
        println("end")
        spark.close()
//        System.exit(0)
//        val unit: RDD[String] = d.map(_.getPrimaryKey.toString)
//        unit.foreach(println(_))
    }

}
