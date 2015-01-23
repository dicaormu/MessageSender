package fr.ecp.piopio.rest

import java.net.InetSocketAddress

import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.DefaultServlet
import org.eclipse.jetty.webapp.WebAppContext
import org.scalatra.servlet.ScalatraListener


/**
 * Created by Diana on 01/12/2014.
 */
object PiopioRest  {

  def main(args: Array[String]) {
    val socketAddress = new InetSocketAddress(8080)
    val server = new Server(socketAddress)
    val context = new WebAppContext()
    context.setContextPath("/")
    context.setResourceBase("src/main/webapp")
    context.addEventListener(new ScalatraListener)
    context.addServlet(classOf[DefaultServlet], "/")
    server.setHandler(context)
    server.start()
    server.join()

  }

}
