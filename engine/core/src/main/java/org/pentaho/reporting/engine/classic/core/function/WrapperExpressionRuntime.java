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


package org.pentaho.reporting.engine.classic.core.function;

import org.pentaho.reporting.engine.classic.core.DataFactory;
import org.pentaho.reporting.engine.classic.core.DataRow;
import org.pentaho.reporting.engine.classic.core.ReportEnvironment;
import org.pentaho.reporting.engine.classic.core.ResourceBundleFactory;
import org.pentaho.reporting.engine.classic.core.layout.output.OutputProcessorMetaData;
import org.pentaho.reporting.engine.classic.core.parameters.CompoundDataRow;
import org.pentaho.reporting.engine.classic.core.wizard.DataSchema;
import org.pentaho.reporting.libraries.base.config.Configuration;
import org.pentaho.reporting.libraries.docbundle.DocumentMetaData;
import org.pentaho.reporting.libraries.formula.FormulaContext;
import org.pentaho.reporting.libraries.resourceloader.ResourceKey;
import org.pentaho.reporting.libraries.resourceloader.ResourceManager;

import javax.swing.table.TableModel;

public class WrapperExpressionRuntime implements ExpressionRuntime {
  private static class WrapperProcessingContext implements ProcessingContext {
    private ProcessingContext context;
    private ExpressionRuntime runtime;

    private WrapperProcessingContext( final ExpressionRuntime runtime ) {
      this.context = runtime.getProcessingContext();
      this.runtime = runtime;
    }

    public int getProgressLevel() {
      return context.getProgressLevel();
    }

    public int getProgressLevelCount() {
      return context.getProgressLevelCount();
    }

    public int getProcessingLevel() {
      return context.getProcessingLevel();
    }

    public FormulaContext getFormulaContext() {
      return new ReportFormulaContext( context.getFormulaContext(), runtime );
    }

    public boolean isPrepareRun() {
      return context.isPrepareRun();
    }

    public String getExportDescriptor() {
      return context.getExportDescriptor();
    }

    public OutputProcessorMetaData getOutputProcessorMetaData() {
      return context.getOutputProcessorMetaData();
    }

    public ResourceBundleFactory getResourceBundleFactory() {
      return context.getResourceBundleFactory();
    }

    public ResourceKey getContentBase() {
      return context.getContentBase();
    }

    public ResourceManager getResourceManager() {
      return context.getResourceManager();
    }

    public Configuration getConfiguration() {
      return context.getConfiguration();
    }

    public DocumentMetaData getDocumentMetaData() {
      return context.getDocumentMetaData();
    }

    public ReportEnvironment getEnvironment() {
      return context.getEnvironment();
    }

    public long getReportProcessingStartTime() {
      return context.getReportProcessingStartTime();
    }

    public int getCompatibilityLevel() {
      return context.getCompatibilityLevel();
    }
  }

  private DataRow dataRow;
  private ExpressionRuntime runtime;
  private WrapperProcessingContext processingContext;

  public WrapperExpressionRuntime() {
  }

  public WrapperExpressionRuntime( final DataRow overrideValues, final ExpressionRuntime runtime ) {
    update( overrideValues, runtime );
  }

  public void update( final DataRow overrideValues, final ExpressionRuntime runtime ) {
    if ( runtime == null ) {
      this.dataRow = null;
      this.runtime = null;
      this.processingContext = null;
      return;
    }

    if ( overrideValues != null ) {
      this.dataRow = new CompoundDataRow( overrideValues, runtime.getDataRow() );
    } else {
      this.dataRow = runtime.getDataRow();
    }
    this.processingContext = new WrapperProcessingContext( runtime );
    this.runtime = runtime;
  }

  public DataRow getDataRow() {
    return dataRow;
  }

  public DataSchema getDataSchema() {
    return runtime.getDataSchema();
  }

  public Configuration getConfiguration() {
    return runtime.getConfiguration();
  }

  public ResourceBundleFactory getResourceBundleFactory() {
    return runtime.getResourceBundleFactory();
  }

  public TableModel getData() {
    return runtime.getData();
  }

  public int getCurrentRow() {
    return runtime.getCurrentRow();
  }

  public int getCurrentDataItem() {
    return runtime.getCurrentDataItem();
  }

  public int getCurrentGroup() {
    return runtime.getCurrentGroup();
  }

  public int getGroupStartRow( final String groupName ) {
    return runtime.getGroupStartRow( groupName );
  }

  public int getGroupStartRow( final int groupIndex ) {
    return runtime.getGroupStartRow( groupIndex );
  }

  public String getExportDescriptor() {
    return runtime.getExportDescriptor();
  }

  public ProcessingContext getProcessingContext() {
    return processingContext;
  }

  public DataFactory getDataFactory() {
    return runtime.getDataFactory();
  }

  public boolean isStructuralComplexReport() {
    return runtime.isStructuralComplexReport();
  }

  public boolean isCrosstabActive() {
    return runtime.isCrosstabActive();
  }
}
