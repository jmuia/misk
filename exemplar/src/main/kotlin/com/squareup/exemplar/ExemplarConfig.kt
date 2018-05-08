package com.squareup.exemplar

import misk.config.Config
import misk.jdbc.DataSourcesConfig
import misk.web.WebConfig

data class ExemplarConfig(
  val web: WebConfig,
  val data_sources: DataSourcesConfig
) : Config
