package perself.sparkutils;

import org.apache.spark._
import org.apache.spark.rdd._
import org.apache.spark.sql._
import org.apache.spark.sql.types._


class GetSchemaRDD (sqlContext: SQLContext) extends Serializable {
 
  /**
   * Generate SchemaRDD of given schema string from given RDD of type Row.
   */
  def toSchemaRDD(schemaStr : String, rowRDD : RDD[Row]) : SchemaRDD = {      
    toSchemaRDD(toColumnArr(schemaStr), rowRDD)      
  }  
  
  def toSchemaRDD(colArr: Array[StructField], rowRDD: RDD[Row]): SchemaRDD = {
    sqlContext.createDataFrame(rowRDD, StructType(colArr))
    //sqlContext.applySchema(rowRDD, StructType(colArr))
  }
  
  /**
   * Generate SchemaRDD of given schema string from given RDD of type Row.
   */
  def toSchemaRDD(schemaStr: String, colData: RDD[String], splitChar: String): SchemaRDD = {
    val colArr = toColumnArr(schemaStr)
    toSchemaRDD(colArr, toRowRDD(colArr, colData, splitChar))
  }
  
  /**
   * Generate SchemaRDD of given schema string from given RDD of type Row.
   */
  def toSchemaRDD(schemaStr: String, datFileLoc: String, splitChar: String)(implicit sc: SparkContext): SchemaRDD = {
	toSchemaRDD(schemaStr, sc.textFile(datFileLoc), splitChar)
  }
  
  private def toColumnArr(schemaStr: String): Array[StructField] = {
    val cols = schemaStr.split(",")
    val colArr = new Array[StructField]( cols.length )
    
    var counter = 0
    cols.foreach(col1 => {
      val colval = col1.trim.split(" ")
      colval(1).trim.toLowerCase match {
        case "string" => colArr( counter ) = StructField( colval(0).trim(), StringType, true )
        case "int" => colArr( counter ) = StructField( colval(0).trim(), IntegerType, true )
        case "long" => colArr( counter ) = StructField( colval(0).trim(), LongType, true )
        case "float" => colArr( counter ) = StructField( colval(0).trim(), FloatType, true )
        case "double" => colArr( counter ) = StructField( colval(0).trim(), DoubleType, true )
        }
      counter = counter + 1
    })
    colArr
  }
  
  private def toRowRDD(colArr: Array[StructField], colData: RDD[String], splitChar: String): RDD[Row] = {
    var counter = colArr.length 
    val valArr = new Array[Any](counter)
     
    colData.map( _.split( splitChar ) )
		.map( p => { 
			for(  a <- 0 until counter )  colArr(a).dataType match {    			
			case StringType => valArr( a ) = p( a ).trim
			case IntegerType => valArr( a ) = toInt( p(a) ).getOrElse(0)    				
			case LongType => valArr( a ) = toLong( p(a) ).getOrElse(0)
			case FloatType => valArr( a ) = toFloat( p(a) ).getOrElse(0)
			case DoubleType => valArr( a ) = toDouble( p(a) ).getOrElse(0)
			}    			  
			Row.fromSeq( valArr )   
		})	
  }
  
  
  def toInt(s: String): Option[Int] =  try { Some(s.toInt) } catch { case e: Exception => None }
  def toLong(s: String): Option[Long] =  try { Some(s.toLong) } catch { case e: Exception => None } 
  def toFloat(s: String): Option[Double] =  try { Some(s.toFloat) } catch { case e: Exception => None } 
  def toDouble(s: String): Option[Double] = try { Some(s.toDouble) } catch { case e: Exception => None }
}