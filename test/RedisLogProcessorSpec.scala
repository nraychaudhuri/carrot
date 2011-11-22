package test
import org.specs2.mutable._
import models._

object RedisLogProcessorSpec extends Specification {

  "Log processor" should {
    "parse lpop log message" in {
	     val redisLog = RedisLogProcessor.parse("""1321834654.481620 "lpop" "resque:queue:cupcake"""") 
	     redisLog must equalTo(RedisLog(1321834654000L, "\"lpop\"", Some("\"resque:queue:cupcake\""), 0))
     }
		 "parse del log messgae" in {
	     val redisLog = RedisLogProcessor.parse("""1321834639.445923 "del" "resque:worker:Admins-MacBook-Pro-9.local:7095:cupcake,cupcake_high,cupcake_low"""") 
	     redisLog must equalTo(RedisLog(1321834639000L, "\"del\"", Some("\"resque:worker:Admins-MacBook-Pro-9.local:7095:cupcake,cupcake_high,cupcake_low\""), 0))			
		 }	
     "parse incrby log message" in {
	     val redisLog = RedisLogProcessor.parse("""1321834639.472652 "incrby" "resque:stat:processed" "1"""")
	     redisLog must equalTo(RedisLog(1321834639000L, "\"incrby\"", Some("\"resque:stat:processed\""), 0))			
	     
     }
   
   }
}