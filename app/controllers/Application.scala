package controllers

import play.api._
import play.api.mvc._
import models._
import akka.actor._
import play.api.cache._
import play.api.Play.current
import sjson.json._

object Application extends Controller {
  
  def index = Action {
    Ok(views.html.index())
  }

  def timeseries = Action { request =>
	  val start = request.queryString.get("start").flatMap(_.headOption)
	  val end = request.queryString.get("end").flatMap(_.headOption)
	  Ok(trafficFor(start.getOrElse("0").toLong, end.getOrElse("0").toLong))
  }

	private def trafficFor(start: Long, end: Long) = {
		Cache.get[Map[Long, List[RedisLog]]]("timeseries") match {
			case Some(timeseries) => 
				val delta = timeseries filter {kv =>  kv._1 >= start && kv._1 <= end }
				new String(Serializer.SJSON.out(delta))
			case _ =>  new String(Serializer.SJSON.out(""))				
		}
	}

  
}