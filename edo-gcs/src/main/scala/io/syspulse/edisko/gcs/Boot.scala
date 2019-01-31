package io.syspulse.edisko.gcs

import java.util.concurrent.TimeUnit

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.{Await, ExecutionContext}
import scala.concurrent.duration.Duration


object Boot extends App {

  def startApplication() = {
    implicit val actorSystem                     = ActorSystem()
    implicit val executor: ExecutionContext      = actorSystem.dispatcher
    implicit val materializer: ActorMaterializer = ActorMaterializer()

    val config = Config.load()

    val text = if(Console.in.ready) Console.in.readLine() else "Please, enter (pipe) sentence to Stdin for translation";

    val a = actorSystem.actorOf(TranslationActor.props(),"translator-actor")

    val f = (a ? TranslationRequest(text))(Timeout(5000L,TimeUnit.MILLISECONDS))

    val result = Await.result(f,Duration(1000L,TimeUnit.MILLISECONDS))
    println(result)
    actorSystem.terminate()

  }

  startApplication()

}
