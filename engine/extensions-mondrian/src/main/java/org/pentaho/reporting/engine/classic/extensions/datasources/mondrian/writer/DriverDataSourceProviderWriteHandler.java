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


package org.pentaho.reporting.engine.classic.extensions.datasources.mondrian.writer;

import org.pentaho.reporting.engine.classic.core.modules.parser.base.PasswordEncryptionService;
import org.pentaho.reporting.engine.classic.core.modules.parser.bundle.writer.BundleWriterException;
import org.pentaho.reporting.engine.classic.core.modules.parser.bundle.writer.BundleWriterState;
import org.pentaho.reporting.engine.classic.core.modules.parser.extwriter.ReportWriterContext;
import org.pentaho.reporting.engine.classic.core.modules.parser.extwriter.ReportWriterException;
import org.pentaho.reporting.engine.classic.extensions.datasources.mondrian.DataSourceProvider;
import org.pentaho.reporting.engine.classic.extensions.datasources.mondrian.DriverDataSourceProvider;
import org.pentaho.reporting.engine.classic.extensions.datasources.mondrian.MondrianDataFactoryModule;
import org.pentaho.reporting.libraries.docbundle.WriteableDocumentBundle;
import org.pentaho.reporting.libraries.xmlns.writer.XmlWriter;
import org.pentaho.reporting.libraries.xmlns.writer.XmlWriterSupport;

import java.io.IOException;

/**
 * Todo: Document me!
 * <p/>
 * Date: 25.08.2009 Time: 18:56:13
 *
 * @author Thomas Morgner.
 */
public class DriverDataSourceProviderWriteHandler
  implements DataSourceProviderBundleWriteHandler, DataSourceProviderWriteHandler {
  /**
   * Writes a data-source into a XML-stream.
   *
   * @param bundle             the document bundle that is produced.
   * @param state              the current writer state.
   * @param xmlWriter          the XML writer that will receive the generated XML data.
   * @param dataSourceProvider the data factory that should be written.
   * @throws java.io.IOException if any error occured
   */
  public void write( final WriteableDocumentBundle bundle,
                     final BundleWriterState state,
                     final XmlWriter xmlWriter,
                     final DataSourceProvider dataSourceProvider ) throws IOException, BundleWriterException {
    if ( dataSourceProvider instanceof DriverDataSourceProvider == false ) {
      throw new BundleWriterException( "This is not a Driver connection" );
    }
    write( xmlWriter, (DriverDataSourceProvider) dataSourceProvider );
  }

  /**
   * Writes a data-source into a XML-stream.
   *
   * @param reportWriter the writer context that holds all factories.
   * @param xmlWriter    the XML writer that will receive the generated XML data.
   * @param dataFactory  the data factory that should be written.
   * @throws java.io.IOException                                                                      if any error
   * occured
   * @throws org.pentaho.reporting.engine.classic.core.modules.parser.extwriter.ReportWriterException if the data
   * factory
   *                                                                                                  cannot be written.
   */
  public void write( final ReportWriterContext reportWriter,
                     final XmlWriter xmlWriter,
                     final DataSourceProvider dataFactory ) throws IOException, ReportWriterException {
    if ( dataFactory instanceof DriverDataSourceProvider == false ) {
      throw new ReportWriterException( "This is not a Driver connection" );
    }
    write( xmlWriter, (DriverDataSourceProvider) dataFactory );
  }

  protected void write( XmlWriter writer, final DriverDataSourceProvider provider ) throws IOException {
    writer.writeTag( MondrianDataFactoryModule.NAMESPACE, "driver", XmlWriter.OPEN );
    writer.writeTag( MondrianDataFactoryModule.NAMESPACE, "driver", XmlWriter.OPEN );
    writer.writeTextNormalized( provider.getDriver(), false );
    writer.writeCloseTag();
    writer.writeTag( MondrianDataFactoryModule.NAMESPACE, "url", XmlWriter.OPEN );
    writer.writeTextNormalized( provider.getUrl(), false );
    writer.writeCloseTag();

    writer.writeTag
      ( MondrianDataFactoryModule.NAMESPACE, "properties", XmlWriterSupport.OPEN );
    final String[] propertyNames = provider.getPropertyNames();
    for ( int i = 0; i < propertyNames.length; i++ ) {
      final String name = propertyNames[ i ];
      final String value = provider.getProperty( name );
      writer.writeTag( MondrianDataFactoryModule.NAMESPACE,
        "property", "name", name, XmlWriterSupport.OPEN );
      if ( name.toLowerCase().contains( "password" ) ) {
        writer.writeTextNormalized( PasswordEncryptionService.getInstance().encrypt( value ), false );
      } else {
        writer.writeTextNormalized( value, false );
      }
      writer.writeCloseTag();
    }
    writer.writeCloseTag();

    writer.writeCloseTag();
  }
}
