# Federated Micro Blogging (PIO-PIO), MessageSender

Pio-Pio is a cloud-enabled, mobile-ready,  Scala powered, Federated micro blogging proyect.

### Version
0.0.1

### Tech

Pio-Pio uses a number of open source projects to work properly:

* [Scala] - for the back end!
* [Neo4j] - awesome database
* [jQuery] - duh

### Installation

You need Gulp installed globally:

```sh
$ npm i -g gulp
```

```sh
$ git clone [git-repo-url] Pio-Pio
$ cd Pio-Pio
$ npm i -d
$ mkdir -p public/files/{md,html,pdf}
$ gulp build --prod
$ NODE_ENV=production node app
```

### Plugins

### Development

Want to contribute? Great!

Pio-Pio uses Gulp + Webpack for fast developing.
Make a change in your file and instantanously see your updates!

Open your favorite Terminal and run these commands.

First Tab:
```sh
$ node app
```

Second Tab:
```sh
$ gulp watch
```

(optional) Third:
```sh
$ karma start
```

### Todo's

 - Write Tests
 - Rethink Github Save
 - Add Code Comments
 - Add Night Mode

License
----

MIT


**Free Software, Hell Yeah!**

[Scala]:http://www.scala-lang.org/
[Neo4j]:http://www.neo4j.org/
[AngularJS]:http://angularjs.org
[Gulp]:http://gulpjs.com
[jQuery]:http://jquery.com 
