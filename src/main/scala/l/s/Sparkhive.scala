package l.s

import java.lang

import org.apache.spark.SparkConf
import org.apache.spark.sql.{Dataset, SparkSession}

object Sparkhive {
    def main(args: Array[String]): Unit = {
        val conf = new SparkConf().setAppName(this.getClass.getSimpleName)
            .setMaster("local[*]")
        val spark = SparkSession.builder()
            .config(conf)
            // 指定hive的metastore的端口  默认为9083 在hive-site.xml中查看
            .config("hive.metastore.uris", "thrift://bigdata04:9083")
            //指定hive的warehouse目录
            .config("spark.sql.warehouse.dir", "hdfs://bigdata00:8020/user/hive/warehouse")
            //直接连接hive
            .enableHiveSupport()
            .getOrCreate()

        val sql =
            s"CREATE TABLE btoutiao_label ( " +
                s"category varchar(50) ," +
                s"label1 varchar(50) ," +
                s"impression_count bigint ," +
                s"impression_avg double ," +
                s"count bigint ," +
                s"relate_labels varchar(255) ," +
                s"date date  )"

        spark.sql("use test")
        spark.sql(sql)
        val frame = spark.sql("show TABLES")
        frame.show()
//        val dsLong: Dataset[lang.Long] = spark.range(10)
//        dsLong.createOrReplaceTempView("t")
//        val frame = spark.sql(s"select cast(id as varchar(50)) from t")
//        frame.printSchema()
//        frame.show(10)

    }
}
