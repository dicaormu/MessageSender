import java.util
import java.util.NoSuchElementException

import fr.ecp.piopio.dao._
//import org.json4s.jackson.Json
//import org.neo4j.cypher.internal.data.SimpleVal.fromStr
import org.scalatra._
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.json._
import scala.collection.JavaConversions._




class PioPioServlet extends ScalatraServlet with JacksonJsonSupport {

  protected implicit val jsonFormats: Formats = DefaultFormats


  /**
   * Create user
   */
  post("/users") {
    contentType = formats("json")
    val parseado = parse(request.body)
    val usuario = (parseado \ "user").values
    val image = (parseado \ "image").values
    try {
      val resp = new PioDaoFindUser(usuario.toString)
      resp.findUserByName().get

    }
    catch {
      case e: java.util.NoSuchElementException =>
        println("error");
        val resp = new PioDaoCreateUser(usuario.hashCode.abs, usuario.toString, image.toString)
        resp.userEnv
    }

  }

  /**
   * :handle tweets
   */
  get("/:handle/tweets") {
    contentType = formats("json")
    try {
      val handle = params("handle")
      try {
        val piopio = new PioDaoTweet(new Integer(handle),null)
        val resp=piopio.getTweets()

        PioData.addAll(resp.subList(0,resp.size()))
        PioData.all
      }
      //val resp=new PioDao("hola",handle.toString)
      //resp.jsonBd
    } catch {
      case nse: NoSuchElementException =>
        halt(status = 400,
          reason = "Bad Request",
          body = Error("forgot handle parameter", "Invalid request"))
    }
  }

  /**
   * Add tweet
   */
  post("/:handle/tweets") {
    contentType = formats("json")
    try {
      val handle = params("handle")

      val parseado = parse(request.body)
      val tweet = (parseado \ "tweet").values
      println("tweet "+tweet)
      val resp = new PioDaoTweet(new Integer(handle), tweet.toString)

      resp.saveTweet()
      //Message(handle, tweet.toString)
    } catch {
      case nse: NoSuchElementException =>
        halt(status = 400,
          reason = "Bad Request",
          body = Error("forgot handle parameter", "Invalid request"))
      case other : Exception =>
        println(other)
    }
  }

  /**
   *
   */
  get("/:handle/followers") {
    contentType = formats("json")
    try {
      val handle = params("handle")
      val resp = new PioDaoFollowers(handle,null).getFollowers()
      contentType = formats("json")
      val buff=new StringBuffer()

      UserData.addAll(resp.subList(0,resp.size()))
      UserData.all


    } catch {
      case nse: NoSuchElementException =>
        halt(status = 400,
          reason = "Bad Request",
          body = Error("forgot handle parameter", "Invalid request"))
    }
  }

  /**
   *
   */
  get("/users") {
    contentType = formats("json")
    try {

      val resp = new PioDaoFollowers(null, null).getUserList()
      UserData.addAll(resp.toList)

      UserData.all
    }
    catch {
      case nse: NoSuchElementException =>
        println(nse)
        halt(status = 400,
          reason = "Bad Request",
          body = Error("forgot handle parameter", "Invalid request"))
    }
  }

  /**
   *
   */
  get("/:handle/followings") {
    contentType = formats("json")
    try {
      val handle = params("handle")
      val resp = new PioDaoFollowers(handle, null).getFollowings()
      UserData.addAll(resp.subList(0,resp.size()))

      UserData.all
    }
    catch {
      case nse: NoSuchElementException =>
        halt(status = 400,
          reason = "Bad Request",
          body = Error("forgot handle parameter", "Invalid request"))
    }
  }

  /**
   * Follow someone
   */
  post("/:handle/followings") {
    contentType = formats("json")
    try {
      val handleFrom = params("handle")
      val parseado = parse(request.body)
      val to = (parseado \ "follow").values
      val resp = new PioDaoFollowers(handleFrom,to.toString).follows()
    } catch {
      case nse: NoSuchElementException =>
        halt(status = 400,
          reason = "Bad Request",
          body = Error("forgot handle parameter", "Invalid request"))
    }
  }

  /**
   * Stop following someone
   */
  delete("/:handleFrom/followings/:handleTo") {
    contentType = formats("json")
    try {
      val handleFrom = params("handleFrom")
      val to = params("handleTo")
      val resp = new PioDaoFollowers(handleFrom,to.toString).deleteFollowing()
    } catch {
      case nse: NoSuchElementException =>
        halt(status = 400,
          reason = "Bad Request",
          body = Error("forgot handle parameter", "Invalid request"))
    }
  }

  /**
   * Signin user
   */
  post("/sessions") {
    contentType = formats("json")
    Message("user", "571505714")
  }

  /**
   * Local followings tweets
   */
  get("/:handle/reading_list") {
    contentType = formats("json")
    try {
      val handle = params("handle")
      Message("Hello", "World")
    } catch {
      case nse: NoSuchElementException =>
        halt(status = 400,
          reason = "Bad Request",
          body = Error("forgot handle parameter", "Invalid request"))
    }
  }

object UserData{


  var all = List[UserC](
    )


  /** Adds a new user object to the existing list of todos, then sorts the list.
    */
  def add(user: UserC): List[UserC] = {
    all ::= user
    all
  }

  def addAll(user: scala.collection.Seq[UserC]): List[UserC] = {
    all = List[UserC](
    )
    for(element<-user) all::=element
    all
  }
}

  object PioData{


    var all = List[PioPio](
    )


    /** Adds a new user object to the existing list of todos, then sorts the list.
      */
    def add(pio: PioPio): List[PioPio] = {
      all ::= pio
      all
    }

    def addAll(pio: scala.collection.Seq[PioPio]): List[PioPio] = {
      all = List[PioPio](
      )
      for(element<-pio) all::=element
      all
    }
  }

  private case class Message(greeting: String, to: String)

  private case class Error(technical: String, client: String)

}