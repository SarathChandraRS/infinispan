package org.infinispan.persistence.jdbc.impl.table;

import org.infinispan.persistence.jdbc.configuration.TableManipulationConfiguration;
import org.infinispan.persistence.jdbc.connectionfactory.ConnectionFactory;
import org.infinispan.persistence.jdbc.logging.Log;
import org.infinispan.persistence.spi.InitializationContext;
import org.infinispan.util.logging.LogFactory;

/**
 * @author Ryan Emerson
 */
class GenericTableManager extends AbstractTableManager {

   private static final Log log = LogFactory.getLog(GenericTableManager.class, Log.class);

   GenericTableManager(InitializationContext ctx, ConnectionFactory connectionFactory, TableManipulationConfiguration config, DbMetaData metaData, String cacheName) {
      super(ctx, connectionFactory, config, metaData, cacheName, log);
   }
}
