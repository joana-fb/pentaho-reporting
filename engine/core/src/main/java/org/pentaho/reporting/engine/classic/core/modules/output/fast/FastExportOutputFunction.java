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


package org.pentaho.reporting.engine.classic.core.modules.output.fast;

import org.pentaho.reporting.engine.classic.core.DetailsFooter;
import org.pentaho.reporting.engine.classic.core.DetailsHeader;
import org.pentaho.reporting.engine.classic.core.InvalidReportStateException;
import org.pentaho.reporting.engine.classic.core.ItemBand;
import org.pentaho.reporting.engine.classic.core.NoDataBand;
import org.pentaho.reporting.engine.classic.core.RelationalGroup;
import org.pentaho.reporting.engine.classic.core.ReportProcessingException;
import org.pentaho.reporting.engine.classic.core.event.ReportEvent;
import org.pentaho.reporting.engine.classic.core.function.AbstractFunction;
import org.pentaho.reporting.engine.classic.core.function.OutputFunction;
import org.pentaho.reporting.engine.classic.core.layout.InlineSubreportMarker;
import org.pentaho.reporting.engine.classic.core.states.ReportState;
import org.pentaho.reporting.engine.classic.core.states.process.SubReportProcessType;

public class FastExportOutputFunction extends AbstractFunction implements OutputFunction {
  private FastExportTemplate template;

  public FastExportOutputFunction( FastExportTemplate template ) {
    this.template = template;
  }

  public void reportInitialized( final ReportEvent event ) {
    if ( event.getState().isSubReportEvent() == false ) {
      boolean prepareRun = event.getState().isPrepareRun();
      this.template.initialize( event.getReport(), getRuntime(), prepareRun );
    }

  }

  public void reportStarted( final ReportEvent event ) {
    this.template.write( event.getReport().getPageHeader(), getRuntime() );
    this.template.write( event.getReport().getReportHeader(), getRuntime() );
  }

  public void reportFinished( final ReportEvent event ) {
    this.template.write( event.getReport().getReportFooter(), getRuntime() );
    this.template.write( event.getReport().getPageFooter(), getRuntime() );
  }

  public void reportDone( final ReportEvent event ) {
    try {
      if ( event.getState().isSubReportEvent() == false ) {
        this.template.finishReport();
      }
    } catch ( ReportProcessingException pre ) {
      throw new InvalidReportStateException( pre.getMessage(), pre );
    }
  }

  public void groupStarted( final ReportEvent event ) {
    final int gidx = event.getState().getCurrentGroupIndex();
    final RelationalGroup group = (RelationalGroup) event.getReport().getGroup( gidx );
    this.template.write( group.getHeader(), getRuntime() );
  }

  public void groupFinished( final ReportEvent event ) {
    final int gidx = event.getState().getCurrentGroupIndex();
    final RelationalGroup group = (RelationalGroup) event.getReport().getGroup( gidx );
    this.template.write( group.getFooter(), getRuntime() );
  }

  public void itemsAdvanced( final ReportEvent event ) {
    ItemBand itemBand = event.getReport().getItemBand();
    if ( itemBand != null ) {
      this.template.write( itemBand, getRuntime() );
    }
  }

  public void itemsStarted( final ReportEvent event ) {
    final int numberOfRows = event.getState().getNumberOfRows();

    final DetailsHeader detailsHeader = event.getReport().getDetailsHeader();
    if ( detailsHeader != null ) {
      this.template.write( detailsHeader, getRuntime() );
    }

    if ( numberOfRows == 0 ) {
      // ups, we have no data. Lets signal that ...
      final NoDataBand noDataBand = event.getReport().getNoDataBand();
      if ( noDataBand != null ) {
        this.template.write( noDataBand, getRuntime() );
      }
    }
  }

  public void itemsFinished( final ReportEvent event ) {
    final DetailsFooter detailsFooter = event.getReport().getDetailsFooter();
    if ( detailsFooter != null ) {
      this.template.write( detailsFooter, getRuntime() );
    }
  }

  public OutputFunction deriveForStorage() {
    return clone();
  }

  public OutputFunction deriveForPagebreak() {
    return clone();
  }

  public FastExportOutputFunction clone() {
    try {
      return (FastExportOutputFunction) super.clone();
    } catch ( CloneNotSupportedException e ) {
      throw new IllegalStateException();
    }
  }

  public InlineSubreportMarker[] getInlineSubreports() {
    // we do not support inline subreports
    return new InlineSubreportMarker[0];
  }

  public void clearInlineSubreports( final SubReportProcessType processType ) {
  }

  public void restart( final ReportState state ) throws ReportProcessingException {

  }

  public boolean createRollbackInformation() {
    return false;
  }

  public void groupBodyFinished( final ReportEvent event ) {
  }

  public Object getValue() {
    return null;
  }
}
