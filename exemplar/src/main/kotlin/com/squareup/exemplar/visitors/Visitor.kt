package com.squareup.exemplar.visitors

import javax.persistence.*

@Entity
@Table(name = "visitors")
class Visitor private constructor() {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id : Long? = null

  @Column
  lateinit var name : String

  companion object {
    fun create(name: String) : Visitor {
      val visitor = Visitor()
      visitor.name = name
      return visitor
    }
  }
}
