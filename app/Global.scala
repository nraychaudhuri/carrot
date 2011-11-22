import play.api._
import scala.sys.process._
import models._

object Global extends GlobalSettings {
  var process: Process = _
  override def onStart(app: Application) {
	  process = startMonitor()
  }  

  override def onStop(app: Application) {
    Logger.info("Application shutdown...")
		process.destroy()
  }

  private def startMonitor() = {
		"redis-cli -h 127.0.0.1 -p 6379 monitor".run(ProcessLogger(RedisLogProcessor))
  }

}
