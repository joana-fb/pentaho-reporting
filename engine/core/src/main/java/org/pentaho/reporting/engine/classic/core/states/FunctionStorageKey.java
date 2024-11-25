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


package org.pentaho.reporting.engine.classic.core.states;

import org.pentaho.reporting.engine.classic.core.ReportDefinition;
import org.pentaho.reporting.engine.classic.core.util.InstanceID;

public class FunctionStorageKey {
  private ReportStateKey parentKey;
  private InstanceID reportId;
  private String reportName;

  protected FunctionStorageKey( final ReportStateKey parentKey, final InstanceID reportId, final String reportName ) {
    this.parentKey = parentKey;
    this.reportId = reportId;
    this.reportName = reportName;
  }

  public String getReportName() {
    return reportName;
  }

  public InstanceID getReportId() {
    return reportId;
  }

  public boolean equals( final Object o ) {
    if ( this == o ) {
      return true;
    }
    if ( o == null || getClass() != o.getClass() ) {
      return false;
    }

    final FunctionStorageKey that = (FunctionStorageKey) o;

    if ( reportId != that.reportId ) {
      return false;
    }
    if ( parentKey != null ) {
      if ( !parentKey.equals( that.parentKey ) ) {
        return false;
      }
    } else {
      if ( that.parentKey != null ) {
        return false;
      }
    }

    return true;
  }

  public int hashCode() {
    int result = ( parentKey != null ? parentKey.hashCode() : 0 );
    result = 31 * result + reportId.hashCode();
    return result;
  }

  public String toString() {
    return "FunctionStorageKey{reportId=" + reportId + ", reportName=" + reportName + " ,parentKey=" + parentKey + '}';
  }

  public static FunctionStorageKey createKey( final ReportStateKey parent, final ReportDefinition reportDefinition ) {
    final String name = reportDefinition.getName();
    return new FunctionStorageKey( parent, reportDefinition.getObjectID(), name );
  }
}
