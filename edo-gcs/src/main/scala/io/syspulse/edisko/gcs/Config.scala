package io.syspulse.edisko.gcs

import pureconfig.loadConfig

case class Config(language: LanguageConfig)

object Config {
  def load() =
    loadConfig[Config] match {
      case Right(config) => config
      case Left(error) =>
        throw new RuntimeException("Cannot read config file, errors:\n" + error.toList.mkString("\n"))
    }
}

private[gcs] case class LanguageConfig(output: String)

