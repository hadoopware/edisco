package io.syspulse.edisko.server

import akka.http.scaladsl.server.Route
import io.syspulse.edisko.server.http.HttpRoute
import io.syspulse.edisko.server.BaseServiceTest
import io.syspulse.edisko.server.core.auth.AuthService
import io.syspulse.edisko.server.core.profiles.UserProfileService

class HttpRouteTest extends BaseServiceTest {

  "HttpRoute" when {

    "GET /healthcheck" should {

      "return 200 OK" in new Context {
        Get("/healthcheck") ~> httpRoute ~> check {
          responseAs[String] shouldBe "OK"
          status.intValue() shouldBe 200
        }
      }

    }

  }

  trait Context {
    val secretKey = "secret"
    val userProfileService: UserProfileService = mock[UserProfileService]
    val authService: AuthService = mock[AuthService]

    val httpRoute: Route = new HttpRoute(userProfileService, authService, secretKey).route
  }

}
