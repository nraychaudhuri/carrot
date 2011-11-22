package test

import org.specs2.mutable._
import models._


object LogAggregatorSpec extends Specification {
  "aggregator" should {
    "aggregate new log message with count 1" in {
	     val r = RedisLog(1321834654000L, "\"lpop\"", Some("\"resque:queue:cupcake\""), 1)
			 val a = new LogAggregator
			 val newMap = a.aggregate(Map.empty[Long, List[RedisLog]], r) 
			 newMap.size must equalTo(1) 
			  newMap(r.millis) must equalTo(List(r)) 
     }
     "increment the count of existing log message" in {
	     val r = RedisLog(1321834654000L, "\"lpop\"", Some("\"resque:queue:cupcake\""), 1)
			 val a = new LogAggregator
			 val newMap = a.aggregate(Map(r.millis -> List(r)), r) 
			 newMap.size must equalTo(1)
			 newMap(r.millis) must equalTo(List(r.copy(count = 2))) 
     }
     "increment the count of existing log message matches with the command" in {
	     val r = RedisLog(1321834654000L, "\"lpop\"", Some("\"resque:queue:cupcake\""), 1)
	     val r1 = RedisLog(1321834654000L, "\"del\"", Some("\"resque:queue:cupcake\""), 1)
			 val a = new LogAggregator
			 val newMap = a.aggregate(Map(r.millis -> List(r, r1)), r1) 
			 newMap.size must equalTo(1)
			 newMap(r.millis) must equalTo(List(r, r1.copy(count = 2))) 
     }
     "remove items older than 5 millis when the time span is defined as 5 millis" in {
	     val r = RedisLog(1, "\"lpop\"", Some("\"resque:queue:cupcake\""), 1)
	     val r1 = RedisLog(2, "\"del\"", Some("\"resque:queue:cupcake\""), 1)
	     val r2 = RedisLog(6, "\"sadd\"", Some("\"resque:queue:cupcake\""), 1)
			 val a = new LogAggregator(5)
			 val newMap = a.aggregate(Map(r.millis -> List(r), r1.millis -> List(r1)), r2) 
			 newMap.size must equalTo(2)
			 newMap.get(r.millis) must equalTo(None)	
			 newMap(r1.millis) must equalTo(List(r1))	
			 newMap(r2.millis) must equalTo(List(r2))	
     }
    
     


		 // "parse del log messgae" in {
		 // 	     val redisLog = RedisLogProcessor.parse("""1321834639.445923 "del" "resque:worker:Admins-MacBook-Pro-9.local:7095:cupcake,cupcake_high,cupcake_low"""") 
		 // 	     redisLog must equalTo(RedisLog("1321834639.445923".toFloat.toLong, "\"del\"", Some("\"resque:worker:Admins-MacBook-Pro-9.local:7095:cupcake,cupcake_high,cupcake_low\""), 0))			
		 // }	
		 //      "parse incrby log message" in {
		 // 	     val redisLog = RedisLogProcessor.parse("""1321834639.472652 "incrby" "resque:stat:processed" "1"""")
		 // 	     redisLog must equalTo(RedisLog("1321834639.472652".toFloat.toLong, "\"incrby\"", Some("\"resque:stat:processed\""), 0))			
		 // 	     
		 //      }
   
   }
}