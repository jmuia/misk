package com.squareup.exemplar.actions

import com.squareup.exemplar.datasources.ReadOnly
import com.squareup.exemplar.visitors.Visitor
import misk.web.*
import misk.web.actions.WebAction
import misk.web.mediatype.MediaTypes
import org.hibernate.SessionFactory
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.streams.toList

@Singleton
class GetVisitorAction: WebAction {
  @Inject @ReadOnly lateinit var readOnlySessionFactory: SessionFactory

  @Get("/visitor/")
  @ResponseContentType(MediaTypes.APPLICATION_JSON)
  fun listVisitors(): GetVisitorsResponse {
    val session = readOnlySessionFactory.openSession()
    session.beginTransaction()

    val query = session.criteriaBuilder.createQuery(Visitor::class.java)
    query.select(query.from(Visitor::class.java))
    val visitors = session.createQuery(query).resultStream.map { it.name }.toList()

    session.transaction.commit()
    session.close()
    return GetVisitorsResponse(visitors)
  }
}

data class GetVisitorsResponse(val names: List<String>)
