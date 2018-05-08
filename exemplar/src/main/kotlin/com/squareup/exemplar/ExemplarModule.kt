package com.squareup.exemplar

import com.google.inject.AbstractModule
import com.squareup.exemplar.actions.*
import com.squareup.exemplar.datasources.ReadOnly
import com.squareup.exemplar.visitors.Visitor
import misk.hibernate.HibernateModule
import misk.jdbc.DataSourceModule
import misk.web.WebActionModule
import misk.web.actions.DefaultActionsModule

class ExemplarModule : AbstractModule() {
  @Suppress("UNCHECKED_CAST")
  val entities = listOf(
    Visitor::class.java
  ) as List<Class<Any>>

  override fun configure() {
    install(WebActionModule.create<HelloWebAction>())
    install(WebActionModule.create<HelloWebPostAction>())
    install(WebActionModule.create<EchoFormAction>())
    install(WebActionModule.create<GetVisitorAction>())
    install(WebActionModule.create<CreateVisitorAction>())
    install(DefaultActionsModule())
    install(DataSourceModule.create("exemplar"))
    install(DataSourceModule.create("exemplar-readonly", annotatedBy = ReadOnly::class))
    install(HibernateModule.create(entities))
    install(HibernateModule.create(annotatedBy = ReadOnly::class, entities = entities))
  }
}
