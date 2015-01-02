import java.util.NoSuchElementException

import fr.ecp.piopio.dao._
import org.json4s.jackson.Json
import org.neo4j.cypher.internal.data.SimpleVal.fromStr
import org.scalatra._
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.json._


class HelloServlet extends ScalatraServlet with JacksonJsonSupport {

  protected implicit val jsonFormats: Formats = DefaultFormats


  /**
   * Create user
   */
  post("/users") {
    contentType = formats("json")
    val parseado = parse(request.body)
    val usuario = (parseado \ "user").values
    val image = (parseado \ "image").values
    println(usuario)
    try {
      val resp = new PioDaoFindUser(usuario.toString)
      resp.findUserByName().get

    }
    catch {
      case e: java.util.NoSuchElementException =>
        println("error");
        val resp = new PioDaoCreateUser(usuario.hashCode, usuario.toString, image.toString)
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
      //val tweet =  request.body

      val parseado = parse(request.body)
      val tweet = (parseado \ "tweet").values
      print(tweet)
      val resp = new PioDaoTweet(new Integer(handle), tweet.toString)
      //Message(handle, tweet.toString)
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
  get("/:handle/followers") {
    contentType = formats("json")
    try {
      val handle = params("handle")
      Message(handle, "World")
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
  get("/:handle/followings") {
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

  /**
   * Follow someone
   */
  post("/:handle/followings") {
    contentType = formats("json")
    try {
      val handleFrom = params("handle")
      //val tweet =  request.body

      val parseado = parse(request.body)
      val to = (parseado \ "follow").values
      print(to)
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
  delete("/:handle/followings") {
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

  /**
   * Signin user
   */
  post("/sessions") {
    contentType = formats("json")
    Message("Hello", "World")
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


  private case class Message(greeting: String, to: String)

  private case class Error(technical: String, client: String)

}