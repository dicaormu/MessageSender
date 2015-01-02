name := "pio-pio"

version := "1.0"

resolvers += "Example Plugin Repository" at "http://github.com/fakod/neo4j-scala"

//libraryDependencies += "eu.fakod" %% "neo4j-scala_2.10" % "0.3.0"

resolvers += "Neo4j Scala Repo" at "http://m2.neo4j.org/content/groups/everything/"

//libraryDependencies += "org.neo4j" % "neo4j-rest-graphdb" % "1.7"

resolvers += "Sonatype Nexus Releases" at "https://oss.sonatype.org/content/repositories/releases"

resolvers += "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
  "eu.fakod" %% "neo4j-scala" % "0.3.0",
  "org.neo4j" % "neo4j-rest-graphdb" % "1.7",
 "org.scalatra" %% "scalatra-json" % "2.3.0",
  "org.scalatra" %% "scalatra" % "2.2.2",
  "org.scalatra" %% "scalatra-specs2" % "2.2.2" % "test",
  "org.json4s"   %% "json4s-jackson" % "3.2.9",
  "org.eclipse.jetty" % "jetty-webapp" % "8.1.7.v20120910" % "container,compile",
  "org.eclipse.jetty.orbit" % "javax.servlet" % "3.0.0.v201112011016" % "container,compile" artifacts Artifact( "javax.servlet", "jar", "jar")
)

seq(webSettings :_*)

