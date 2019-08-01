package l.s

import java.util.Date

import com.alicloud.openservices.tablestore.SyncClient
import com.alicloud.openservices.tablestore.model.search.{SearchQuery, SearchRequest}
import com.alicloud.openservices.tablestore.model.{ColumnValue, Row}
import com.alicloud.openservices.tablestore.model.search.query.{BoolQuery, Query, RangeQuery}
import org.apache.parquet.schema.Types.ListBuilder
import org.apache.spark.internal.Logging
import org.apache.spark.{Partition, SparkContext, TaskContext}
import org.apache.spark.rdd.RDD

import scala.collection.mutable.ListBuffer
import scala.util.control.Breaks

class CustomRDD(shareKey: String, startTime: Long, endTime: Long, priod: Long, sc: SparkContext) extends RDD[Row] (sc, Nil) with Logging{

    //按照分区信息拉取数据
    override def compute(split: Partition, context: TaskContext): Iterator[Row] = {
//        context.addTaskCompletionListener{ context => closeIfNeeded() }
        val tablestorePartition = split.asInstanceOf[TablestorePartition]
        log.info("分区： {}", tablestorePartition.idx)
        val client = new SyncClient("https://Video.cn-shenzhen.ots.aliyuncs.com", "LTAIW9TelZ37ToNd", "lASHlVno6DFpeUtI0942Wlpf9OhTHY", "Video")
        val searchQuery = new SearchQuery
        val boolQuery = new BoolQuery
        val r1 = new RangeQuery()
        r1.setFieldName(shareKey)
        r1.setFrom(ColumnValue.fromLong(tablestorePartition.startTime))
        r1.setTo(ColumnValue.fromLong(tablestorePartition.endTime))
        val queries = new ListBuffer[Query]
        queries.append(r1)
        import scala.collection.JavaConversions
        val java_queries = JavaConversions.bufferAsJavaList(queries)
        boolQuery.setMustQueries(java_queries)
        searchQuery.setQuery(boolQuery)
        searchQuery.setGetTotalCount(true)
        searchQuery.setLimit(100)
        val searchRequest = new SearchRequest("douyin_user", "filter", searchQuery)
        var response = client.search(searchRequest)
        if (!response.isAllSuccess || response.getRows.isEmpty) {
            return new ListBuffer[Row].iterator
        }
        var rows = response.getRows
        val loop = new Breaks
        loop.breakable {
            while (response.getNextToken != null) {
                searchRequest.getSearchQuery.setToken(response.getNextToken)
                response = client.search(searchRequest)
                rows.addAll(response.getRows)
            }
        }

        client.shutdown()
        import scala.collection.JavaConversions
        JavaConversions.asScalaIterator[Row](rows.iterator())

    }

    override protected def getPartitions: Array[Partition] = {
        var time1 = startTime
        var time2 = time1 + priod
        var lb = ListBuffer[Partition]()
        var i = 0
        while (time2 <= endTime) {
            lb.append(new TablestorePartition(i, time1, time2))
            time1 += priod
            time2 += priod
            i += 1
        }

        if (time1 < endTime) {
            lb.append(new TablestorePartition(i, time1, endTime))
        }
        lb.toArray
    }
}

class TablestorePartition(val idx: Int, val startTime: Long, val endTime: Long) extends Partition{
    override def index: Int = idx
}


