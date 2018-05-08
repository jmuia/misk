package com.squareup.exemplar.datasources

import javax.inject.Qualifier

@Qualifier
@Target(
  AnnotationTarget.FIELD,
  AnnotationTarget.PROPERTY,
  AnnotationTarget.FUNCTION,
  AnnotationTarget.VALUE_PARAMETER
)
annotation class ReadOnly
