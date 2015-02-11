import org.scalatra.ScalatraServlet
import org.scalatra.swagger.{ApiInfo, NativeSwaggerBase, Swagger}


class ResourcesApp(implicit val swagger: Swagger) extends ScalatraServlet with NativeSwaggerBase

object PioPioApiInfo extends ApiInfo(
  "The PioPio API",
  "Docs for the PioPio API",
  "http://scalatra.org",
  "diana.ortega@student.ecp.fr",
  "ECP",
  "http://opensource.org/licenses/MIT")

class PioPioSwagger extends Swagger(Swagger.SpecVersion, "1.0.0",PioPioApiInfo)