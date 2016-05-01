package perself

import org.apache.spark._
import org.apache.spark.sql._
import org.apache.spark.sql.types._
import perself.sparkutils.Implicits._

object SparkUtilsTests {
  
	def main(args: Array[String]) {
	  testGetSchemaRDD
	}
	
	def testGetSchemaRDD = {
	  val conf = new SparkConf()
	  implicit val sc = new SparkContext(conf)
	  val sqlContext = new org.apache.spark.sql.SQLContext(sc)
	  val schemaStr = "c1 int, c2 String, c3 String, c4 double, c5 long"
	  val dataFileLoc = "/hadoop/tmp/prqs.txt"
	  val splitChar = ","
	  
	  var tabSchemaRDD = sqlContext.toSchemaRDD(schemaStr, dataFileLoc, splitChar)
	  verifySchemaRDD (tabSchemaRDD)
	  println ("GetSchemaRDD Test1 - Success")
	    
	  tabSchemaRDD = sqlContext.toSchemaRDD(schemaStr, sc.textFile(dataFileLoc), splitChar)
	  verifySchemaRDD (tabSchemaRDD)
	  println ("GetSchemaRDD Test2 - Success")
	  
	  val tabRDD = sc.textFile(dataFileLoc).map(_.split(splitChar))
    				.map(r=>Row(r(0).toInt, r(1).toString(), r(2).toString(), r(3).toDouble, r(4).toLong))
      tabSchemaRDD = sqlContext.toSchemaRDD(schemaStr, tabRDD)
      verifySchemaRDD (tabSchemaRDD)
      println ("GetSchemaRDD Test3 - Success")
      
      sc.stop
	}
	
	def verifySchemaRDD(rdd: SchemaRDD) {
		assert (rdd != null)
		assert (rdd.count > 0)
	}
}