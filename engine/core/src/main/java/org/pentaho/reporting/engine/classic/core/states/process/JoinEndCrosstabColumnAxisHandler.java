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


package org.pentaho.reporting.engine.classic.core.states.process;

import org.pentaho.reporting.engine.classic.core.CrosstabColumnGroup;
import org.pentaho.reporting.engine.classic.core.CrosstabRowGroup;
import org.pentaho.reporting.engine.classic.core.Group;
import org.pentaho.reporting.engine.classic.core.ReportProcessingException;
import org.pentaho.reporting.engine.classic.core.event.ReportEvent;
import org.pentaho.reporting.engine.classic.core.states.ReportState;
import org.pentaho.reporting.engine.classic.core.states.datarow.DefaultFlowController;

public class JoinEndCrosstabColumnAxisHandler implements AdvanceHandler {
  public static final AdvanceHandler HANDLER = new JoinEndCrosstabColumnAxisHandler();

  public JoinEndCrosstabColumnAxisHandler() {
  }

  public ProcessState advance( final ProcessState state ) throws ReportProcessingException {
    return state.deriveForAdvance();
  }

  /**
   * Checks whether there are more groups active.
   *
   * @return true if this is the last (outer-most) group.
   */
  private boolean isRootGroup( final ProcessState state ) {
    return state.getCurrentGroupIndex() == ReportState.BEFORE_FIRST_GROUP;
  }

  public ProcessState commit( final ProcessState next ) throws ReportProcessingException {
    next.leaveGroup();
    final DefaultFlowController fc = next.getFlowController();
    final boolean advanceRequested = fc.isAdvanceRequested();
    final boolean advanceable = fc.getMasterRow().isAdvanceable();
    if ( isRootGroup( next ) ) {
      throw new ReportProcessingException( "This report is invalid. A CR-Col-Group cannot be a root group." );
    }

    final Group parentGroup = next.getReport().getGroup( next.getCurrentGroupIndex() );
    if ( advanceRequested == false || advanceable == false ) {
      // This happens for empty - reports. Empty-Reports are never advanceable, therefore we can
      // reach an non-advance state where inner group-footers are printed.
      if ( parentGroup instanceof CrosstabRowGroup ) {
        next.setAdvanceHandler( EndCrosstabRowBodyHandler.HANDLER );
      } else if ( parentGroup instanceof CrosstabColumnGroup ) {
        next.setAdvanceHandler( EndCrosstabColumnBodyHandler.HANDLER );
      } else {
        throw new ReportProcessingException( "This report is invalid." );
      }
      return next;
    }

    // This group is not the outer-most group ..
    final DefaultFlowController cfc = fc.performCommit();
    if ( ProcessState.isLastItemInGroup( parentGroup, fc.getMasterRow(), cfc.getMasterRow() ) ) {
      // continue with an other EndGroup-State ...
      if ( parentGroup instanceof CrosstabRowGroup ) {
        next.setAdvanceHandler( EndCrosstabRowBodyHandler.HANDLER );
      } else if ( parentGroup instanceof CrosstabColumnGroup ) {
        next.setAdvanceHandler( EndCrosstabColumnBodyHandler.HANDLER );
      } else {
        throw new ReportProcessingException( "This report is invalid." );
      }
      return next;
    } else {
      // The parent group is not finished, so finalize the createRollbackInformation.
      // more data in parent group, print the next header
      next.setFlowController( cfc );
      next.setAdvanceHandler( BeginCrosstabColumnAxisHandler.HANDLER );
      return next;
    }
  }

  public boolean isFinish() {
    return false;
  }

  public int getEventCode() {
    return ReportEvent.GROUP_FINISHED | ProcessState.ARTIFICIAL_EVENT_CODE | ReportEvent.CROSSTABBING;
  }

  public boolean isRestoreHandler() {
    return false;
  }
}
