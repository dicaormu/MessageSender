import org.scalatra._
import javax.servlet.ServletContext

class ScalatraBootstrap extends LifeCycle {

  implicit val swagger = new PioPioSwagger

  override def init(context: ServletContext) {
    context.mount(new PioPioServlet, "/PioPio","PioPio")
    context.mount (new ResourcesApp, "/api-docs")

  }
}
