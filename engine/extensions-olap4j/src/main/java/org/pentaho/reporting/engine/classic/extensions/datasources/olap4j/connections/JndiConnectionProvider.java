/*! ******************************************************************************
 *
 * Pentaho
 *
 * Copyright (C) 2024 by Hitachi Vantara, LLC : http://www.pentaho.com
 *
 * Use of this software is governed by the Business Source License included
 * in the LICENSE.TXT file.
 *
 * Change Date: 2029-07-20
 ******************************************************************************/


package org.pentaho.reporting.engine.classic.extensions.datasources.olap4j.connections;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.olap4j.OlapConnection;
import org.olap4j.OlapWrapper;
import org.pentaho.reporting.engine.classic.core.ClassicEngineBoot;
import org.pentaho.reporting.engine.classic.core.modules.misc.connections.DataSourceService;
import org.pentaho.reporting.engine.classic.core.modules.misc.connections.DatasourceServiceException;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class JndiConnectionProvider implements OlapConnectionProvider {
  private transient DataSourceService dataSourceService;

  private static final Log logger = LogFactory.getLog( JndiConnectionProvider.class );
  private String connectionPath;
  private String username;
  private String password;

  public JndiConnectionProvider() {
    this.dataSourceService = ClassicEngineBoot.getInstance().getObjectFactory().get( DataSourceService.class );
  }

  public String getConnectionPath() {
    return connectionPath;
  }

  public void setConnectionPath( final String connectionPath ) {
    this.connectionPath = connectionPath;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername( final String username ) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword( final String password ) {
    this.password = password;
  }

  public OlapConnection createConnection( final String user, final String password ) throws SQLException {
    try {
      final DataSource ds = dataSourceService.getDataSource( connectionPath );

      final String realUser;
      final String realPassword;
      if ( username != null ) {
        realUser = username;
      } else {
        realUser = user;
      }
      if ( this.password != null ) {
        realPassword = this.password;
      } else {
        realPassword = password;
      }

      if ( realUser == null ) {
        final Connection connection = ds.getConnection();
        if ( connection == null ) {
          throw new SQLException( "JNDI DataSource is invalid; it returned null without throwing a meaningful error." );
        }
        if ( connection instanceof OlapConnection ) {
          return (OlapConnection) connection;
        }
        if ( connection instanceof OlapWrapper ) {
          final OlapWrapper wrapper = (OlapWrapper) connection;
          return wrapper.unwrap( OlapConnection.class );
        }
        throw new SQLException( "Unable to unwrap the connection: " + connectionPath ); //$NON-NLS-1$
      }

      final Connection connection = ds.getConnection( realUser, realPassword );
      if ( connection == null ) {
        throw new SQLException( "JNDI DataSource is invalid; it returned null without throwing a meaningful error." );
      }
      if ( connection instanceof OlapConnection ) {
        return (OlapConnection) connection;
      }
      if ( connection instanceof OlapWrapper ) {
        final OlapWrapper wrapper = (OlapWrapper) connection;
        return wrapper.unwrap( OlapConnection.class );
      }

      return connection.unwrap( OlapConnection.class );
    } catch ( DatasourceServiceException ne ) {
      JndiConnectionProvider.logger.warn( "Failed to access the JDNI-System", ne );
      throw new SQLException( "Failed to access the JNDI system" );
    }
  }

  public Object getConnectionHash() {
    final ArrayList<Object> list = new ArrayList<Object>();
    list.add( getClass().getName() );
    list.add( connectionPath );
    list.add( username );
    return list;
  }

  public boolean equals( final Object o ) {
    if ( this == o ) {
      return true;
    }
    if ( o == null || getClass() != o.getClass() ) {
      return false;
    }

    final JndiConnectionProvider that = (JndiConnectionProvider) o;

    if ( connectionPath != null ? !connectionPath.equals( that.connectionPath ) : that.connectionPath != null ) {
      return false;
    }
    if ( password != null ? !password.equals( that.password ) : that.password != null ) {
      return false;
    }
    if ( username != null ? !username.equals( that.username ) : that.username != null ) {
      return false;
    }

    return true;
  }

  public int hashCode() {
    int result;
    result = ( connectionPath != null ? connectionPath.hashCode() : 0 );
    result = 31 * result + ( username != null ? username.hashCode() : 0 );
    result = 31 * result + ( password != null ? password.hashCode() : 0 );
    return result;
  }

  private void readObject( final ObjectInputStream stream )
    throws IOException, ClassNotFoundException {
    stream.defaultReadObject();
    dataSourceService = ClassicEngineBoot.getInstance().getObjectFactory().get( DataSourceService.class );
  }
}
