import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpMethods._

import scala.io.StdIn
import akka.http.scaladsl.model._
import akka.stream.ActorMaterializer

object WebServer {
  def main(args: Array[String]) {
    implicit val system = ActorSystem()
    implicit val materializer = ActorMaterializer()

    // needed for the future map / flatmap in the end
    implicit val executionContext = system.dispatcher

    val requestHandler: HttpRequest => HttpResponse = {

      case HttpRequest(GET, Uri.Path("/"), _, _, _) =>
        HttpResponse(entity = HttpEntity(
          ContentTypes.`text/html(UTF-8)`,"<html><body>Hello world!</body></html>"))

      case HttpRequest(GET, Uri.Path("/ping"), _, _, _) =>
        HttpResponse(entity = "PONG!")

      case HttpRequest(GET, Uri.Path("/crash"), _, _, _) =>
        sys.error("BOOM!")

      case _: HttpRequest =>
        HttpResponse(404, entity = "Unknown resource!")
    }

    val bindingFuture = Http().bindAndHandleSync(requestHandler, "localhost", 8080)
    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")

    StdIn.readLine() // let it run until user presses return
    bindingFuture.flatMap(_.unbind()).onComplete(_ => system.terminate())

    // Some comment
  }
}
