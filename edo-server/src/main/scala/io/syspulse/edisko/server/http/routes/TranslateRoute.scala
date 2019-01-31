package io.syspulse.edisko.server.http.routes

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import io.circe.generic.auto._
import io.circe.syntax._
import io.syspulse.edisko.server.core.{TranslatePost, TranslateResponse, UserProfileUpdate}
import io.syspulse.edisko.server.core.profiles.UserProfileService
import io.syspulse.edisko.server.core.translate.TranslateService
import io.syspulse.edisko.server.utils.SecurityDirectives

import scala.concurrent.ExecutionContext

class TranslateRoute(
    secretKey: String,
    translateService: TranslateService
)(implicit executionContext: ExecutionContext)
    extends FailFastCirceSupport {

  import SecurityDirectives._
  import StatusCodes._

  val route = pathPrefix("translate") {
    pathEndOrSingleSlash {
      post {
        entity(as[TranslatePost]) { translatePost =>
          complete(translateService.translate(translatePost.text.getOrElse(""),translatePost.fromLang.getOrElse("en"),translatePost.toLang.getOrElse("ja")).map {
            case translatedText =>  OK -> TranslateResponse(translatePost.text.getOrElse(""),translatedText,id=System.nanoTime().toString).asJson
          })
        }
      }
    } ~
    pathPrefix(Segment) { text =>
      pathEndOrSingleSlash {
        get {
          parameters('fromLang.as[String] ? "en", 'toLang.as[String] ? "ja") {
            (fromLang,toLang) =>
              complete(translateService.translate(text,fromLang,toLang).map {
                case translatedText =>
                  OK -> TranslateResponse(text,translatedText,id=System.nanoTime().toString).asJson
                //                case _ =>
                //                  BadRequest -> None.asJson
              })
          }
        }
      }
    }
  }

}
