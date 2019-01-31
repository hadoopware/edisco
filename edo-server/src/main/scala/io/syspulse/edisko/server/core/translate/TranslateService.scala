package io.syspulse.edisko.server.core.translate

import io.syspulse.edisko.server.core.{UserProfile, UserProfileUpdate}

import scala.concurrent.{ExecutionContext, Future}

class TranslateService(
    translationEngine: TranslationEngine
)(implicit executionContext: ExecutionContext) {

  def translate(text:String,fromLang:String,toLang:String): Future[String] =
    translationEngine.translate(text,fromLang,toLang)
}
