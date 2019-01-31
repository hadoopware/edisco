package io.syspulse.edisko.server.core.translate

import java.util.concurrent.TimeUnit

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.util.Timeout
import io.syspulse.edisko.gcs.{TranslationActor, TranslationRequest, TranslationResponse, TranslationService}
import io.syspulse.edisko.server.core.UserProfile
import io.syspulse.edisko.server.utils.db.DatabaseConnector
import akka.pattern.ask

import scala.concurrent.{ExecutionContext, Future, future}

sealed trait TranslationEngine {

  def translate(text:String, fromLang:String,toLang:String): Future[String]
}

class EchoTranslationEngine()(implicit executionContext: ExecutionContext)
    extends TranslationEngine {
  override def translate(text: String, fromLang: String, toLang: String): Future[String] = {
    future {
      text
    }
  }
}

class GoogleTranslationEngine(implicit executionContext: ExecutionContext) extends TranslationEngine {

  override def translate(text: String, fromLang: String, toLang: String): Future[String] = {
    val f = Future {
      TranslationService.translate(text,fromLang,toLang)
    }
    f
  }
}
