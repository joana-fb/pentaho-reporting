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


package org.pentaho.reporting.engine.classic.extensions.datasources.kettle;

import org.pentaho.reporting.engine.classic.core.designtime.DataSourcePlugin;

public interface EmbeddedKettleDataFactoryEditor extends DataSourcePlugin {
  public void configure( String metaDataId );
}
