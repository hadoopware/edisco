package io.syspulse.edisko.server

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import io.syspulse.edisko.server.http.HttpRoute
import io.syspulse.edisko.server.core.auth.{AuthService, InMemoryAuthDataStorage, JdbcAuthDataStorage}
import io.syspulse.edisko.server.core.profiles.{InMemoryUserProfileStorage, UserProfileService}
import io.syspulse.edisko.server.core.translate.{EchoTranslationEngine, GoogleTranslationEngine, TranslateService}
import io.syspulse.edisko.server.utils.Config
import io.syspulse.edisko.server.utils.db.DatabaseMigrationManager

import scala.concurrent.ExecutionContext
import org.rogach.scallop._

object Boot extends App {

  def startApplication() = {
    implicit val actorSystem                     = ActorSystem()
    implicit val executor: ExecutionContext      = actorSystem.dispatcher
    implicit val materializer: ActorMaterializer = ActorMaterializer()

    val config = Config.load()

    class ArgsConf(arguments: Seq[String]) extends ScallopConf(arguments) {
      val translator = opt[String](short='t',default = Some("gcs"))
      verify()
    }
    val configArgs = new ArgsConf(args)

//    new DatabaseMigrationManager(
//      config.database.jdbcUrl,
//      config.database.username,
//      config.database.password
//    ).migrateDatabaseSchema()

//    val databaseConnector = new DatabaseConnector(
//      config.database.jdbcUrl,
//      config.database.username,
//      config.database.password
//    )

    val userProfileStorage = new InMemoryUserProfileStorage()
    val authDataStorage    = new InMemoryAuthDataStorage()

    val usersService = new UserProfileService(userProfileStorage)
    val authService  = new AuthService(authDataStorage, config.secretKey)
    val translateService  = new TranslateService( (if(configArgs.translator.getOrElse("")=="gcs") new GoogleTranslationEngine() else new EchoTranslationEngine()))

    val httpRoute    = new HttpRoute(usersService, authService, translateService, config.secretKey)

    Console.println(s"Listening on ${config.http.host}:${config.http.port}...")

    Http().bindAndHandle(httpRoute.route, config.http.host, config.http.port)
  }

  startApplication()

}
