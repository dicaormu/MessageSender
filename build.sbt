name := "pio-pio"

version := "1.0"

resolvers += "Example Plugin Repository" at "https://oss.sonatype.org/content/repositories/snapshots/"

resolvers += "Neo4j Scala Repo" at "http://m2.neo4j.org/content/groups/everything/"

resolvers += "Sonatype Nexus Releases" at "https://oss.sonatype.org/content/repositories/releases"

resolvers += "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases/"


libraryDependencies += "eu.fakod" %% "neo4j-scala" % "0.3.0"

//libraryDependencies += "org.scalatra" %% "scalatra-commands" % "2.2.2"

//libraryDependencies += "com.sun.jersey" % "jersey-core" % "1.9"

libraryDependencies += "org.scalatra" %% "scalatra-json" % "2.3.0"

libraryDependencies += "org.json4s" %% "json4s-jackson" % "3.2.9" excludeAll(
  ExclusionRule(organization = "org.apache.commons")
  )

libraryDependencies += "org.json4s" %% "json4s-native" % "3.2.9"

libraryDependencies += "org.eclipse.jetty" % "jetty-webapp" % "8.1.7.v20120910" % "container,compile"

libraryDependencies += "org.eclipse.jetty.orbit" % "javax.servlet" % "3.0.0.v201112011016" % "container,compile" artifacts Artifact("javax.servlet", "jar", "jar")

libraryDependencies += "org.scalatra" %% "scalatra-swagger"  % "2.3.0"

assemblyMergeStrategy in assembly := {
 case PathList("javax", "servlet", xs @ _*)         => MergeStrategy.first
  case PathList(ps @ _*) if ps.last endsWith ".html" => MergeStrategy.first
  case "application.conf"                            => MergeStrategy.concat
  case "META-INF/ECLIPSEF.RSA"                            => MergeStrategy.discard
 case "META-INF/ECLIPSEF.SF"                            => MergeStrategy.discard
 case "META-INF/*.DSA"                            => MergeStrategy.discard
  case "META-INF/CHANGES.txt"                                => MergeStrategy.discard
  case "META-INF/LICENSES.txt"                                => MergeStrategy.discard
  case "CHANGES.txt"                                => MergeStrategy.discard
  case "META-INF/MANIFEST.MF"                                => MergeStrategy.discard
  case "MANIFEST.MF"                                => MergeStrategy.discard
  case PathList("META-INF", "MANIFEST.MF") => MergeStrategy.discard
  case _ => MergeStrategy.first
}
//

mainClass in assembly := Some("fr.ecp.piopio.rest.PiopioRest")


//assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = true, includeDependency = true)

seq(webSettings: _*)



