package l.s
import org.apache.spark._
import org.apache.spark.graphx._
// To make some of the examples work we will also need RDD
import org.apache.spark.rdd.RDD

object GraphXTest {
    def main(args: Array[String]): Unit = {
        // Assume the SparkContext has already been constructed
        val conf = new SparkConf().setAppName(GraphXTest.getClass.getSimpleName).setMaster("local[*]")
        val sc: SparkContext = new SparkContext(conf)
        sc.setLogLevel("WARN")
//        // Create an RDD for the vertices
//        val users: RDD[(VertexId, (String, String))] =
//            sc.parallelize(Array((3L, ("rxin", "student")), (7L, ("jgonzal", "postdoc")),
//                (5L, ("franklin", "prof")), (2L, ("istoica", "prof"))))
//        // Create an RDD for edges
//        val relationships: RDD[Edge[String]] =
//            sc.parallelize(Array(Edge(3L, 7L, "collab"),    Edge(5L, 3L, "advisor"),
//                Edge(2L, 5L, "colleague"), Edge(5L, 7L, "pi")))
//        // Define a default user in case there are relationship with missing user
//        val defaultUser = ("John Doe", "Missing")
//        // Build the initial Graph
//        val graph = Graph(users, relationships, defaultUser)
//
//        val count1 = graph.vertices.filter { case (id, (name, pos)) => pos == "postdoc" }.count
//        println(count1)
//        // Count all the edges where src > dst
//        val count2 = graph.edges.filter(e => e.srcId > e.dstId).count
//        println(count2)

//        graph.aggregateMessages[Double](x => x.sendToDst())

//        graph.triplets.foreach(x=>{
//            println(s"srcAttr ${x.srcAttr}")
//            println(s"dstAttr ${x.dstAttr}")
//            println(s"edgeAttr ${x.attr}")
//            println("------------------------------")
//        })

//        graph.inDegrees.foreach(x => {
//            println(s"${x._1} ${x._2}")
//        })

//        val setA: VertexRDD[Int] = VertexRDD(sc.parallelize(0L until 100L).map(id => (id, 2)))
//        val rddB: RDD[(VertexId, Double)] = sc.parallelize(0L until 100L).flatMap(id => List((id, 5.0), (id, 2.0)))
////         There should be 200 entries in rddB
//        println(rddB.count)
//        val setB: VertexRDD[Double] = setA.aggregateUsingIndex(rddB, _ + _)
//        setB.foreach(x => {
//            println(x)
//        })
////         There should be 100 entries in setB
//        println(setB.count)
////         Joining A and B should now be fast!
//        val setC: VertexRDD[Double] = setA.innerJoin(setB)((id, a, b) => a + b)
//        setC.foreach(x => {
//            println(x)
//        })



        val nonUniqueCosts: RDD[(VertexId, Double)] =
            sc.parallelize(Array((3L, 1.0), (7L, 1.0),
                (5L, 1.0), (2L, 1.0)))


    }
}
