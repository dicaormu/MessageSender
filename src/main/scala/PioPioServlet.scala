import java.util
import java.util.NoSuchElementException
import org.scalatra.swagger._

import fr.ecp.piopio.dao._
//import org.json4s.jackson.Json
//import org.neo4j.cypher.internal.data.SimpleVal.fromStr
import org.scalatra._
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.json._
import scala.collection.JavaConversions._
import org.scalatra.json._



class PioPioServlet(implicit val swagger: Swagger) extends ScalatraServlet
with JacksonJsonSupport with SwaggerSupport{

  override protected val applicationName = Some("PioPio")
  protected val applicationDescription = "The PioPio Message Federator. This application exposes operation " +
    "for sending messages (Pios or Tweets) as a tweeter-like application."

  protected implicit val jsonFormats: Formats = DefaultFormats

  /**
   * Create user
   */

  val postUser =
    (apiOperation[List[UserC]]("postUser")
      summary "Creates an user in the application "
      notes "Creates a new user of the application. Return the user created "
      parameters (
        pathParam[String]("image").description("url of the profile image selected for the user")

      ))

  post("/users",operation(postUser)) {
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


  val getTweets =
    (apiOperation[List[UserC]]("getTweets")
      summary "Show all Pios of an user"
      notes "Shows all the Pios (tweets) of an user. "
      parameter queryParam[Option[String]]("handle").description("the handle of te user to find the Pios"))
  /**
   * :handle tweets
   */
  get("/:handle/tweets",operation(getTweets)) {
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

  val postTweet =
    (apiOperation[String]("postTweet")
      summary "Post a Pio (tweet)"
      notes "Post a Pio for the user with the handle in :handle"
      parameter queryParam[Option[String]]("handle").description("handle of the user"))
  /**
   * Add tweet
   */
  post("/:handle/tweets",operation(postTweet)) {
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


  val getFollowers =
    (apiOperation[List[UserC]]("getFollowers")
      summary "Show all followers of an user"
      notes "Shows all the follower of an user. "
      parameter queryParam[Option[String]]("handle").description("A handle of the user to search for his followers"))

  get("/:handle/followers",operation(getFollowers)) {
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

  //////////////////////////
  val getUsers =
    (apiOperation[List[UserC]]("getFollowers")
      summary "Show all followers of an user"
      notes "Shows all the follower of an user. "
      parameter queryParam[Option[String]]("handle").description("A handle of the user to search for his followers"))

  get("/:handle/users",operation(getUsers)) {
    contentType = formats("json")
    try {

      val handle = params("handle")
      val paramTest = params("ipUser")
      println(s"invoque el servicio $paramTest")
      val resp = new PioDaoFollowers(handle, null).getUserList()

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


  val getFollowings =
    (apiOperation[List[UserC]]("getFollowers")
      summary "Show all followers of an user"
      notes "Shows all the follower of an user. "
      parameter queryParam[Option[String]]("handle").description("A handle of the user to search for his followers"))

  get("/:handle/followings",operation(getFollowings)) {
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
  val follow =
    (apiOperation[List[UserC]]("getFollowers")
      summary "Show all followers of an user"
      notes "Shows all the follower of an user. "
      parameter queryParam[Option[String]]("handle").description("A handle of the user to search for his followers"))

  post("/:handle/followings",operation(follow)) {
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
  val stopFollowing =
    (apiOperation[List[UserC]]("getFollowers")
      summary "Show all followers of an user"
      notes "Shows all the follower of an user. "
      parameter queryParam[Option[String]]("handle").description("A handle of the user to search for his followers"))

  delete("/:handleFrom/followings/:handleTo",operation(stopFollowing)) {
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
  val signin =
    (apiOperation[List[UserC]]("getFollowers")
      summary "Show all followers of an user"
      notes "Shows all the follower of an user. "
      parameter queryParam[Option[String]]("handle").description("A handle of the user to search for his followers"))

  post("/sessions",operation(signin)) {
    contentType = formats("json")
    Message("user", "571505714")
  }

  /**
   * Local followings tweets
   */
  val readin_list =
    (apiOperation[List[UserC]]("getFollowers")
      summary "Show all followers of an user"
      notes "Shows all the follower of an user. "
      parameter queryParam[Option[String]]("handle").description("A handle of the user to search for his followers"))

  get("/:handle/reading_list",operation(readin_list)) {
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