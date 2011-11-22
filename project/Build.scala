import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "carrot"
    val appVersion      = "1.0"

    val appDependencies = Seq(
      // Add your project dependencies here,
      "se.scalablesolutions.akka" % "akka-stm" % "1.2", 
      "net.debasishg"             % "sjson_2.9.1" % "0.15",
 			"org.multiverse" % "multiverse-alpha-unborn" % "0.6.2",
			"org.multiverse" % "multiverse-alpha" % "0.6.2"
    )

    val main = PlayProject(appName, appVersion, appDependencies).settings(defaultScalaSettings:_*).settings(
      // Add your own project settings here      
    )

}
