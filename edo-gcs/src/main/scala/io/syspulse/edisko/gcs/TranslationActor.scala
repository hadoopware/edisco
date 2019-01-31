package io.syspulse.edisko.gcs

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import com.typesafe.config.ConfigFactory
import java.text.SimpleDateFormat
import java.util.Date

import com.google.cloud.translate.Translate.TranslateOption
import com.google.cloud.translate.{TranslateOptions, Translation}

import scala.concurrent._
import ExecutionContext.Implicits.global
import com.typesafe.scalalogging.Logger

case class TranslationRequest(text:String)
case class TranslationRequestInProgress(status:String)
case class TranslationResponse(text:String,output:String)
case class TranslationHealth()

object TranslationService {
  val translator = TranslateOptions.getDefaultInstance().getService();

  def translate(text:String,fromLang:String,toLang:String):String = {
    TranslationService.translator.translate(
      text,
      TranslateOption.sourceLanguage(fromLang),
      TranslateOption.targetLanguage(toLang)).getTranslatedText
  }
}

object TranslationActor {
  def props(): Props = Props(new TranslationActor)
}

class TranslationActor extends Actor {

  val logger = Logger("Translator")

  def receive = {
    case TranslationRequest(text) =>
      logger.info(s"text='${text}")
      val sender0 = sender()

      val f = future {
        logger.info(s"text='${text}: started")

        TranslationService.translator.translate(
          text,
          TranslateOption.sourceLanguage("en"),
          TranslateOption.targetLanguage("ja"));

      }

      for(t <-f ) {
        val result = t.getTranslatedText()
        logger.info(s"text='${text}: finished='${result}")
        sender0 ! TranslationResponse(text,result)
      }

    case TranslationHealth =>
      logger.info(s"worker: ${this}")
  }
}