package models
import akka.actor._
import play.api.cache._
import play.api.Play.current

@scala.reflect.BeanInfo
case class RedisLog(millis: Long, command: String, queue: Option[String] = None, count : Int = 0)

object RedisLogProcessor extends Function[String, Unit] {
  val aggregator = Actor.actorOf[LogAggregatorActor].start

	def apply(log: String) {
		aggregator ! parse(log)
	}
	
	def parse(log: String): RedisLog = {
		val tokens = log.split(" ")
		if(tokens.size >= 3) { 
			return RedisLog(toMillis(tokens(0)) , tokens(1), Some(tokens(2))) 
		}
		return RedisLog(System.currentTimeMillis, "unknown") 
	}
	
	def toMillis(secondsWithFraction: String) = {
		secondsWithFraction.split("\\.")(0).toLong * 1000
	}
}

class LogAggregatorActor() extends Actor {
	var timeseries = Map.empty[Long, List[RedisLog]]
	val aggregator = new LogAggregator(30 * 60 * 1000)
	
	def receive = {
		case r: RedisLog => 
		  timeseries = aggregator.aggregate(timeseries, r)
		  Cache.set("timeseries", timeseries) 
	}
}

class LogAggregator(timespan: Long = 240) {
  def aggregate(timeseries: Map[Long, List[RedisLog]], r: RedisLog) = {
	  val logs = timeseries.getOrElse(r.millis, Nil) 	
	  val newLogs = if(logs.exists(_.command == r.command)) {
		  logs.map { log =>
			  if(log.command == r.command) log.copy(count = log.count + 1)
			  else log 
			}
	  } else { r :: logs } 
	  (timeseries + (r.millis -> newLogs)).filter(_._1 > (r.millis - timespan))
  }

}
