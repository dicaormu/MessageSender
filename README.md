# Federated Micro Blogging (PIO-PIO), MessageSender

Pio-Pio is a cloud-enabled, mobile-ready, Scala powered, Federated micro blogging project.

### Version
0.1.0

### Tech

Pio-Pio uses a number of open source projects to work properly:

* [Scala] - for everything !
* [Neo4j] - graph database
* [Scalatra] - for the api REST
* [Swagger] - for the api REST documentation


### Installation

Note: You need sbt installed globally and a Neo4j database.

Go to src/resources/application.conf and change databaseUrl to point to your own neo4j database

```sh
$ git clone [git-repo-url] Pio-Pio
$ cd Pio-Pio
$ sbt clean compile assembly
```
Go to target/scala-XXXX (XXXX = scala version) and execute the generated jar pio-pio-assembly-XX.jar (XX =  PioPio version) using:
```sh
$ java -jar pio-pio-assembly-XX.jar PORT  

```

Where PORT is a number of port of your choice where the application listens.

The REST Api is accesible from url http://yourhost:PORT/PioPio
The Swagger documentation is accesible from url http://yourhost:PORT/api-docs


### Development

Want to contribute? Any suggestions? 

Pio-Pio uses sbt + scala. For executing PioPio in interactive mode, it is necessary to use the container task:
```sh
$ sbt
> clean
> compile
> container:start

```
container starts in default port 8080

Api rest is accesible from url http://yourhost:8080/PioPio

### Todo's

 - Write Tests
 - Add Code Comments

License
----

MIT


**Free Software**

[Scala]:http://www.scala-lang.org/
[Neo4j]:http://www.neo4j.org/
[Scalatra]:http://www.scalatra.org/
[sbt]:http://www.scala-sbt.org/
[git-repo-url]:https://github.com/dicaormu/MessageSender
[Swagger]:http://swagger.io/
