import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse}

/**
  * Created by dima on 6/14/2016.
  */
object HandlerPing {
  def Execute(): HttpResponse = {
    HttpResponse(entity = HttpEntity(
      ContentTypes.`text/html(UTF-8)`,"<html><body>Hello world!</body></html>"))
  }
}
