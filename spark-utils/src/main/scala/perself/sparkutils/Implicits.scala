package perself.sparkutils

import org.apache.spark.sql.SQLContext
import org.apache.spark.SparkContext

object Implicits extends Serializable {
	implicit def getSchemaRDD(sqlContext: SQLContext) = new GetSchemaRDD(sqlContext)
}