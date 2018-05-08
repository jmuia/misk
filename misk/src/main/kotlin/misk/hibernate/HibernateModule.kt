package misk.hibernate

import com.google.common.util.concurrent.Service
import com.google.inject.AbstractModule
import com.google.inject.Key
import misk.hibernate.HibernateService.HibernateServiceProvider
import misk.inject.addMultibinderBinding
import org.hibernate.SessionFactory
import org.hibernate.boot.MetadataSources
import org.hibernate.boot.registry.StandardServiceRegistryBuilder
import org.hibernate.cfg.Environment
import javax.inject.Provider
import javax.sql.DataSource
import kotlin.reflect.KClass


class HibernateModule private constructor(
  private val sessionFactoryKey: Key<SessionFactory>,
  private val dataSourceKey: Key<DataSource>,
  private val entities: List<Class<Any>>
) : AbstractModule() {

  override fun configure() {
    bind(sessionFactoryKey)
      .toProvider(SessionFactoryProvider(entities, getProvider(dataSourceKey)))
      .asEagerSingleton()

    binder().addMultibinderBinding<Service>()
      .toProvider(HibernateServiceProvider(getProvider(sessionFactoryKey)))
  }

  companion object {
    fun create(entities: List<Class<Any>>) = HibernateModule(
      Key.get(SessionFactory::class.java),
      Key.get(DataSource::class.java),
      entities
    )

    fun create(annotatedBy: Annotation, entities: List<Class<Any>>) = HibernateModule(
      Key.get(SessionFactory::class.java, annotatedBy),
      Key.get(DataSource::class.java, annotatedBy),
      entities
    )

    fun <A : Annotation> create(annotatedBy: Class<A>, entities: List<Class<Any>>) = HibernateModule(
      Key.get(SessionFactory::class.java, annotatedBy),
      Key.get(DataSource::class.java, annotatedBy),
      entities
    )

    fun <A : Annotation> create(annotatedBy: KClass<A>, entities: List<Class<Any>>) = HibernateModule(
      Key.get(SessionFactory::class.java, annotatedBy.java),
      Key.get(DataSource::class.java, annotatedBy.java),
      entities
    )
  }

  private class SessionFactoryProvider(
    private val entities: List<Class<Any>>,
    private val dataSourceProvider: Provider<DataSource>
  ) : Provider<SessionFactory> {

    override fun get() : SessionFactory {
      val dataSource = dataSourceProvider.get()

      val registry = StandardServiceRegistryBuilder()
        .applySetting(Environment.DATASOURCE, dataSource)
        .build()

      val sources = MetadataSources(registry)
      for (entity in entities) {
        sources.addAnnotatedClass(entity)
      }

      return sources.buildMetadata().buildSessionFactory()
    }
  }
}
