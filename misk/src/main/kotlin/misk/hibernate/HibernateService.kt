package misk.hibernate

import com.google.common.util.concurrent.AbstractIdleService
import misk.logging.getLogger
import org.hibernate.SessionFactory
import javax.inject.Provider

private val logger = getLogger<HibernateService>()

internal class HibernateService(
  private val sessionFactory: SessionFactory
) : AbstractIdleService() {

  override fun startUp() {
    logger.info("starting up Hibernate")
  }

  override fun shutDown() {
    logger.info("shutting down Hibernate")
    sessionFactory.close()
  }

  internal class HibernateServiceProvider(
    private val sessionFactoryProvider: Provider<SessionFactory>
  ) : Provider<HibernateService> {
    override fun get(): HibernateService = HibernateService(sessionFactoryProvider.get())
  }
}
