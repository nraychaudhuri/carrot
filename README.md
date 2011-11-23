carrot: Redis realtime reporting tool inspired by [radish](http://radishapp.com/)
================
Gives you a view into your redis server.The current version only gives you a graph of commands running over time. But as I add new features
I will keep updating this section.  

Things that I want to implement
===============================
* Comparison between read and write commands
* Database size and keys
* Reports on specific queues

Tools used
==========
* [Play 2.0 + Scala](http://www.playframework.org/2.0)
* [Highcharts](http://www.highcharts.com/)


How to Run?
===============

* Download and install play 2.0 
* Run redis server
* Run "play run" command on the project folder. By default it points to localhost and default port.
* Go to http://localhost:9000/

You could run the following code to generate load on redis server to see the reporting tool in action. Here I am using [fyrie-redis](https://github.com/derekjw/fyrie-redis) scala redis client.


    import net.fyrie.redis._
    val r = new RedisClient("localhost", 6379)
    def update(i: Int) = (1 to i).toList.foreach(x => r.set("key" + x, "some value" + x))

    (1 to 3600000).foreach { x =>
        Thread.sleep(1000)
        update((Math.random * 100).toInt)
    } 