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


package org.pentaho.reporting.engine.classic.core.layout;

import junit.framework.TestCase;
import org.pentaho.reporting.engine.classic.core.ClassicEngineBoot;
import org.pentaho.reporting.engine.classic.core.ClassicEngineCoreModule;
import org.pentaho.reporting.engine.classic.core.MasterReport;
import org.pentaho.reporting.engine.classic.core.SimplePageDefinition;
import org.pentaho.reporting.engine.classic.core.layout.model.CanvasRenderBox;
import org.pentaho.reporting.engine.classic.core.layout.model.LogicalPageBox;
import org.pentaho.reporting.engine.classic.core.layout.process.IterateStructuralProcessStep;
import org.pentaho.reporting.engine.classic.core.testsupport.DebugReportRunner;
import org.pentaho.reporting.engine.classic.core.util.geom.StrictGeomUtility;
import org.pentaho.reporting.libraries.resourceloader.Resource;
import org.pentaho.reporting.libraries.resourceloader.ResourceManager;

import java.awt.print.PageFormat;
import java.net.URL;

public class NestedBandsIT extends TestCase {
  public NestedBandsIT() {
  }

  public NestedBandsIT( final String s ) {
    super( s );
  }

  protected void setUp() throws Exception {
    ClassicEngineBoot.getInstance().start();
  }

  public void testNestedRows() throws Exception {
    final MasterReport basereport = new MasterReport();
    basereport.setPageDefinition( new SimplePageDefinition( new PageFormat() ) );
    basereport.setCompatibilityLevel( null );
    basereport.getReportConfiguration().setConfigProperty( ClassicEngineCoreModule.COMPLEX_TEXT_CONFIG_OVERRIDE_KEY,
        "false" );

    final URL target = LayoutIT.class.getResource( "nested-bands.xml" );
    final ResourceManager rm = new ResourceManager();
    rm.registerDefaults();
    final Resource directly = rm.createDirectly( target, MasterReport.class );
    final MasterReport report = (MasterReport) directly.getResource();

    final LogicalPageBox logicalPageBox =
        DebugReportRunner.layoutSingleBand( basereport, report.getReportHeader(), false, false );
    // simple test, we assert that all paragraph-poolboxes are on either 485000 or 400000
    // and that only two lines exist for each
    new ValidateRunner().startValidation( logicalPageBox );
  }

  private static class ValidateRunner extends IterateStructuralProcessStep {
    protected boolean startCanvasBox( final CanvasRenderBox box ) {
      if ( "canvas".equals( box.getName() ) ) {
        assertEquals( "Width = 200", StrictGeomUtility.toInternalValue( 200 ), box.getWidth() );
        assertEquals( "Height = 210", StrictGeomUtility.toInternalValue( 110 ), box.getHeight() );
        assertEquals( "X = 100", StrictGeomUtility.toInternalValue( 100 ), box.getX() );
        assertEquals( "Y = 100", StrictGeomUtility.toInternalValue( 100 ), box.getY() );
      }
      if ( "label4".equals( box.getName() ) ) {
        assertEquals( "Width = 100", StrictGeomUtility.toInternalValue( 100 ), box.getWidth() );
        assertEquals( "Height = 10", StrictGeomUtility.toInternalValue( 10 ), box.getHeight() );
        assertEquals( "X = 200", StrictGeomUtility.toInternalValue( 200 ), box.getX() );
        assertEquals( "Y = 200", StrictGeomUtility.toInternalValue( 200 ), box.getY() );
      }
      return true;
    }

    public void startValidation( final LogicalPageBox logicalPageBox ) {
      startProcessing( logicalPageBox );
    }
  }

}
