package fr.ecp.piopio.dao


import org.json4s
import org.json4s.JsonAST.JValue
import org.json4s._
import org.json4s.jackson.Json
import org.json4s.jackson.JsonMethods._
import com.fasterxml.jackson.databind.SerializationFeature
import JsonDSL._
import org.json4s.jackson.Serialization
/**
 * Created by Diana on 03/12/2014.
 */
class PioPioDaoJson(handle:String) {

  implicit lazy val formats =  DefaultFormats

  implicit val format = Serialization.formats(ShortTypeHints(List(classOf[tweet], classOf[tweetss])))


  case class  tweet(content: JString, by: JString, at: JInt, twitterId:JString)

  case class tweetss(tweets:List[tweet])




  def jsonBd = """{"tweets":[{"content":"yess!","by":"test","at":1416915564},{"content":"yo","by":"test","at":1416910135},{"content":"RT @Philae_Ptolemy: It's good luck through bad luck. We've had a number of happy accidents. So how does Philae come back from the dead? hâ€¦","twitterId":"537158868309377024","by":"RosettaMIDAS","at":1416900028},{"content":"RT @ESA_Rosetta: For new followers asking me about the colour of my images of #67P, you might like to read NAVCAM's shades of grey: http:â€¦","twitterId":"537158789695561728","by":"RosettaMIDAS","at":1416900010},{"content":".@donnie_78 yes! I have been to 10 km already - here's a slideshow of pics I took while I was there: http://t.co/C3hZAoTZgj #67P #CometWatch","twitterId":"537158688205963264","by":"ESA_Rosetta","at":1416899985},{"content":"It's good luck through bad luck. We've had a number of happy accidents. So how does Philae come back from the dead? http://t.co/VpZeHY7XMS","twitterId":"537157411736657921","by":"Philae_Ptolemy","at":1416899681},{"content":"For new followers asking me about the colour of my images of #67P, you might like to read NAVCAM's shades of grey: http://t.co/rsujezkzxb","twitterId":"537154191790448640","by":"ESA_Rosetta","at":1416898913},{"content":"RT @oewf: Another chance for sleeping comet probe Philae: http://t.co/a0KFpqaVr1 (via @spacefuture)","twitterId":"537138526622863360","by":"RosettaMIDAS","at":1416895178},{"content":"RT @simonrae: #Philae  - the big experimentn@Philae_MUPUS Hammering &amp; Bangingn(vi)nn{for @roxiethehamste1} http://t.co/3TGuHCALLA","twitterId":"537129628721504256","by":"Philae_MUPUS","at":1416893057},{"content":"RT @simonrae: #Philae  - the big experimentn@Philae_MUPUS Hammering &amp; Bangingn(v) http://t.co/HUaSBmv7O2","twitterId":"536994351747203072","by":"Philae_Ptolemy","at":1416860804}]}"""


  //org.json4s.jackson.JsonMethods.configure(SerializationFeature.CLOSE_CLOSEABLE, false)


  val json=parse(jsonBd)

  /*val name = (json \ "content").extract[String]
  val by = (json \ "by").extract[String]
  val at = (json \ "at").extract[Int]
  val twitterId = (json \ "twitterId").extract[String]*/

     println(json)

  val resp=json.extract[tweetss]

  //def hand=handle

   //println(resp)

  case class Child(name: String, age: Int, birthdate: Option[java.util.Date])
   case class Address(street: String, city: String)
 case class Person(name: String, address: Address, children: List[Child])

  //val json2: json4s.JValue = parse("""{ "name": "joe","address": {"street": "Bulevard", "city": "Helsinki"}, "children": [{"name": "Mary","age": 5 "birthdate": "2004-09-04T18:06:22Z"},{"name": "Mazy", "age": 3}]}""")

  //val renderedjson2 = compact(render(json2))
 //println(renderedjson2)


}
