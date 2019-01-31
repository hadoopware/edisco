package io.syspulse.edisko.server.http

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import io.syspulse.edisko.server.core.auth.AuthService
import io.syspulse.edisko.server.core.profiles.UserProfileService
import io.syspulse.edisko.server.core.translate.TranslateService
import io.syspulse.edisko.server.http.routes.{AuthRoute, ProfileRoute, TranslateRoute}

import scala.concurrent.ExecutionContext

class HttpRoute(
    userProfileService: UserProfileService,
    authService: AuthService,
    translateService:TranslateService,
    secretKey: String
)(implicit executionContext: ExecutionContext) {

  private val usersRouter = new ProfileRoute(secretKey, userProfileService)
  private val authRouter  = new AuthRoute(authService)
  private val translateRouter  = new TranslateRoute(secretKey, translateService )

  val route: Route =
    cors() {
      pathPrefix("v1") {
        usersRouter.route ~
        authRouter.route ~
        translateRouter.route
      } ~
      pathPrefix("healthcheck") {
        get {
          complete("OK")
        }
      }
    }

}
