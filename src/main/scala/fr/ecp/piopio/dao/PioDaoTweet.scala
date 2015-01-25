package fr.ecp.piopio.dao

import java.net.URI
import java.util
import java.util.Date
import org.neo4j.cypher.ExecutionEngine
import org.neo4j.graphdb._
import org.neo4j.graphdb.index.ReadableIndex
import org.neo4j.rest.graphdb.RestAPIFacade
import org.neo4j.rest.graphdb.query.RestCypherQueryEngine
import org.neo4j.rest.graphdb.services.RequestType
import org.neo4j.tooling.GlobalGraphOperations

import scala.io.Source

import sys.ShutdownHookThread
import eu.fakod.neo4jscala._
import scala.collection.JavaConverters._

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
  val image: String
}


case class PioPio(date: Date, pio: String) extends PioPioBase

case class UserC(id: Int, user: String, image: String) extends UserBase


class PioDaoTweet(handle: Int, pio: String) extends Neo4jWrapper with RestGraphDatabaseServiceProvider with TypedTraverser with Neo4jWrapperImplicits {


  ShutdownHookThread {
    shutdown(ds)
  }


  def saveTweet(): Unit = {

    println(handle)
    ShutdownHookThread {
      shutdown(ds)
    }
    /**
     * creating nodes and associations
     */
    val nodeMap = withTx {
      implicit us =>
        //val List  = getAllNodes

        val start1 = PioDaoFindUser(handle.toString).findUserByHandle()

        val nodeMap = createNode(PioPio(new Date(), pio))

        start1 --> "Pia" --> nodeMap
    }
    ShutdownHookThread {
      shutdown(ds)
    }
  }

  def getTweets(): util.ArrayList[PioPio] = {
    val user = PioDaoFindUser(handle.toString).findUserByHandle()

    val listTweets = user.getRelationships(DynamicRelationshipType.withName("Pia")).asScala.map(_.getOtherNode(user))

    val pioList = new util.ArrayList[PioPio]
    for (element <- listTweets) pioList.add(new PioPio(new Date(new java.lang.Long(element.getProperty("date").toString)), element.getProperty("pio").toString))

    pioList

  }

  override def uri: URI = new URI("http://192.168.56.101:7474/db/data/")
}

case class PioDaoFindUser(handle: String) extends Neo4jWrapper with RestGraphDatabaseServiceProvider with TypedTraverser with Cypher {
  override def uri: URI = new URI("http://192.168.56.101:7474/db/data/")

  ShutdownHookThread {
    shutdown(ds)
  }

  def hand = handle

  def findUserByName(): Option[UserC] = {
    val query = "Start user=node:node_auto_index(user='" + hand + "') where user.user='" + hand + "' return user"

    val list = query.execute.asCC[UserC]("user")

    Some(list.next())

  }


  def findUserByHandle(): Node = {
    val query = s"Start user=node:node_auto_index('id:$hand' ) where user.id=$hand return user"
    //val query = "Match (user) where user.user='"+user+"' return user"
    val list = query.execute.asCC[UserC]("user")
    list.next()
    val autoIndex = indexManager(ds).getNodeAutoIndexer().getAutoIndex();
    val n = autoIndex.get("id", hand).getSingle();
    n
  }


}

class PioDaoCreateUser(id: Int, handle: String, image: String) extends Neo4jWrapper with RestGraphDatabaseServiceProvider with TypedTraverser {
  override def uri: URI = new URI("http://192.168.56.101:7474/db/data/")

  ShutdownHookThread {
    shutdown(ds)
  }

  val userEnv = UserC(id, handle, image)
  val nodeMap = withTx {
    implicit us =>
      val nodeMap = createNode(userEnv)

  }

}


case class PioDaoFollowers(handleFrom: String, handleTo: String) extends Neo4jWrapper with RestGraphDatabaseServiceProvider with TypedTraverser with Cypher {
  override def uri: URI = new URI("http://192.168.56.101:7474/db/data/")

  ShutdownHookThread {
    shutdown(ds)
  }

  def follows(): Unit = {
    val nodeMap = withTx {
      implicit us =>
        //val List  = getAllNodes
        val from = PioDaoFindUser(handleFrom.toString).findUserByHandle()

        val to = PioDaoFindUser(handleTo.toString).findUserByHandle()

        from --> "Follows" --> to

    }
  }

  def getFollowers(): util.ArrayList[UserC] = {
    val user = new PioDaoFindUser(handleFrom.toString).findUserByHandle()

    val listFolowers = user.getRelationships(Direction.INCOMING, DynamicRelationshipType.withName("Follows")).asScala.map(_.getOtherNode(user))

    val followsList = new util.ArrayList[UserC]
    for (element <- listFolowers) followsList.add(new UserC(new Integer(element.getProperty("id").toString), element.getProperty("user").toString, element.getProperty("image").toString))

    followsList

  }

  def getFollowings(): util.ArrayList[UserC] = {
    val user = PioDaoFindUser(handleFrom.toString).findUserByHandle()
    //val listFolowers = user.getRelationships(DynamicRelationshipType.withName("Follows")).asScala.map(_.getOtherNode(user))
    val listFolowers = user.getRelationships(Direction.OUTGOING,DynamicRelationshipType.withName("Follows")).asScala.map(_.getOtherNode(user))
    val followsList = new util.ArrayList[UserC]
    for (element <- listFolowers) followsList.add(new UserC(new Integer(element.getProperty("id").toString), element.getProperty("user").toString, element.getProperty("image").toString))

    followsList

  }

  def getUserList(): util.ArrayList[UserC] = {

    val nodes = getAllNodes(ds)

    //val query = "START user=node:node_auto_index('*:*') where user.__CLASS__ = 'fr.ecp.piopio.dao.UserC' RETURN user"

    val followsList = new util.ArrayList[UserC]
    for (element <- nodes) {
      if (element.getProperty("__CLASS__").equals("fr.ecp.piopio.dao.UserC")) {

        //println("la propiedad es")
          if (element.hasProperty("id")) {
            if (!element.getProperty("id").toString.equals(handleFrom.toString)) {
              val rels=element.getRelationships(Direction.INCOMING,DynamicRelationshipType.withName("Follows")).asScala
              //rels.view.map(v=>)
              val del=rels.find(v=>v.getStartNode.getProperty("id").toString().equals(handleFrom.toString))
              println(del)
              //val del=rels.takeWhile(_.getStartNode.getProperty("id").toString().equals(handleFrom.toString))
              if(del.size ==0)
                followsList.add(new UserC(Math.abs(new Integer(element.getProperty("id").toString)), element.getProperty("user").toString, element.getProperty("image").toString))
            }
          }
      }
    }

    println("pase " + followsList.size)

    followsList

  }

  def deleteFollowing(): Unit = {
    val nodeMap = withTx {
      implicit us =>
        val user = PioDaoFindUser(handleFrom.toString).findUserByHandle()

        val rels = user.getRelationships(Direction.OUTGOING,DynamicRelationshipType.withName("Follows")).asScala

        for(element<-rels){
          if(element.getEndNode.getProperty("id").toString.equals(handleTo.toString))
            element.delete()
        }
        //val del = rels.takeWhile(_.getEndNode.getProperty("id").toString().equals(handleTo.toString)).foreach(_.delete())
          //.equals(handleTo.toString))
        //println(del)
        //del.foreach(_.delete())
    }
  }

}