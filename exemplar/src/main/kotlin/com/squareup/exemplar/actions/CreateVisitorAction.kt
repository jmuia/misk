package com.squareup.exemplar.actions

import com.squareup.exemplar.visitors.Visitor
import misk.web.PathParam
import misk.web.Post
import misk.web.RequestContentType
import misk.web.ResponseContentType
import misk.web.actions.WebAction
import misk.web.mediatype.MediaTypes
import org.hibernate.SessionFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CreateVisitorAction: WebAction {
  @Inject lateinit var sessionFactory: SessionFactory

  @Post("/visitor/{name}")
  @RequestContentType(MediaTypes.APPLICATION_JSON)
  @ResponseContentType(MediaTypes.APPLICATION_JSON)
  fun createVisitor(@PathParam name: String): CreateVisitorResponse {
    val session = sessionFactory.openSession()
    session.beginTransaction()
    session.save(Visitor.create(name))
    session.getTransaction().commit()
    session.close()
    return CreateVisitorResponse(name)
  }
}

data class CreateVisitorResponse(val name: String)
