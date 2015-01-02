package fr.ecp.piopio.dao

import java.net.URI
import java.util.Date
import org.neo4j.graphdb._
import org.neo4j.graphdb.index.ReadableIndex
import org.neo4j.tooling.GlobalGraphOperations

import scala.io.Source

import sys.ShutdownHookThread
import eu.fakod.neo4jscala._

//import org.neo4j.scala.{TypedTraverser, SingletonEmbeddedGraphDatabaseServiceProvider, Neo4jWrapper}
/**
 * Created by Diana on 01/12/2014.
 */

trait PioPioBase {
  val date: Date
  val pio: String
}

trait UserBase {
  val id: Int
  val user: String
  val image:String
}



case class PioPio(date: Date, pio: String) extends PioPioBase

case class UserC( id: Int, user: String, image:String) extends UserBase


class PioDaoTweet(handle:Int,pio:String) extends Neo4jWrapper with RestGraphDatabaseServiceProvider with TypedTraverser with Neo4jWrapperImplicits {


  ShutdownHookThread {
    shutdown(ds)
  }

  /**
   * creating nodes and associations
   */
  val nodeMap = withTx {
    implicit us =>
      //val List  = getAllNodes
      val ppp=new PioDaoFindUser(handle.toString)


      val start1 = ppp.findUserByHandle()
      println(s"por qaui pase ${start1}")

      val nodeMap =   createNode(PioPio(new Date(), pio))

      start1  --> "Pia" --> nodeMap

      println("cree la relacon"+nodeMap)

  }

  override def uri: URI = new URI("http://192.168.56.101:7474/db/data/")
}

case class PioDaoFindUser(handle:String) extends Neo4jWrapper with RestGraphDatabaseServiceProvider with TypedTraverser with Cypher {
  override def uri: URI = new URI("http://192.168.56.101:7474/db/data/")
  ShutdownHookThread {
    shutdown(ds)
  }
  def hand=handle

  def findUserByName() : Option[UserC] ={
    val query = "Start user=node:node_auto_index(user='"+hand+"') where user.user='"+hand+"' return user"
    //val query = "Match (user) where user.user='"+user+"' return user"
    println(query)
    val list  = query.execute.asCC[UserC]("user")

    Some(list.next())

  }


  def findUserByHandle() : Node ={
    val query = s"Start user=node:node_auto_index('id:$hand' ) where user.id=$hand return user"
    //val query = "Match (user) where user.user='"+user+"' return user"
    println(query)
    val list  = query.execute.asCC[UserC]("user")
    list.next()
    val autoIndex = indexManager(ds).getNodeAutoIndexer().getAutoIndex();
    val n = autoIndex.get("id",hand).getSingle();
    n
    
   // println("la lista "+list)
    //list.next()
  }



}

class PioDaoCreateUser( id: Int, handle:String, image: String ) extends Neo4jWrapper with RestGraphDatabaseServiceProvider with TypedTraverser  {
  override def uri: URI = new URI("http://192.168.56.101:7474/db/data/")
  ShutdownHookThread {
    shutdown(ds)
  }

  val userEnv = UserC(id,handle,image)
  val nodeMap = withTx {
    implicit us =>
      val nodeMap =   createNode(userEnv)

  }

}



case class PioDaoFollowers(handleFrom:String,handleTo:String) extends Neo4jWrapper with RestGraphDatabaseServiceProvider with TypedTraverser {
  override def uri: URI = new URI("http://192.168.56.101:7474/db/data/")

  ShutdownHookThread {
    shutdown(ds)
  }
  def follows(): Unit = {
    val nodeMap = withTx {
      implicit us =>
        //val List  = getAllNodes
        val from=PioDaoFindUser(handleFrom.toString).findUserByHandle()

        println(s"por qaui pase ${from}")

        val to =   PioDaoFindUser(handleTo.toString).findUserByHandle()

        from  --> "Follows" --> to

    }
  }
}