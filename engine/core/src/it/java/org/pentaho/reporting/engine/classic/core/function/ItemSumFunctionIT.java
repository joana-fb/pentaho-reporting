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

import junit.framework.TestCase;
import org.pentaho.reporting.engine.classic.core.ClassicEngineBoot;
import org.pentaho.reporting.engine.classic.core.MasterReport;
import org.pentaho.reporting.engine.classic.core.TableDataFactory;
import org.pentaho.reporting.engine.classic.core.designtime.DesignTimeDataSchemaModel;
import org.pentaho.reporting.engine.classic.core.elementfactory.CrosstabBuilder;
import org.pentaho.reporting.engine.classic.core.testsupport.DebugReportRunner;
import org.pentaho.reporting.engine.classic.core.util.TypedTableModel;

import javax.swing.table.TableModel;

/**
 * @noinspection HardCodedStringLiteral
 */
public class ItemSumFunctionIT extends TestCase {

  public static final String ROW_DIMENSION_A = "Row-Dimension-A";
  public static final String ROW_DIMENSION_B = "Row-Dimension-B";
  public static final String COLUMN_DIMENSION_A = "Column-Dimension-A";
  public static final String COLUMN_DIMENSION_B = "Column-Dimension-B";
  public static final String VALUE = "Value";

  public ItemSumFunctionIT() {
  }

  public ItemSumFunctionIT( final String name ) {
    super( name );
  }

  protected void setUp() throws Exception {
    ClassicEngineBoot.getInstance().start();
  }

  private TableModel createTableModel() {
    final TypedTableModel model = new TypedTableModel();
    model.addColumn( ROW_DIMENSION_A, String.class );
    model.addColumn( ROW_DIMENSION_B, String.class );
    model.addColumn( COLUMN_DIMENSION_A, String.class );
    model.addColumn( COLUMN_DIMENSION_B, String.class );
    model.addColumn( VALUE, String.class );
    model.addColumn( "validate-cell-sum", String.class );
    model.addColumn( "validate-detail-sum", String.class );
    model.addColumn( "validate-row-b-sum", String.class );
    model.addColumn( "validate-row-a-sum", String.class );
    model.addColumn( "validate-column-a-sum", String.class );
    model.addColumn( "validate-column-b-sum", String.class );

    model.addRow( "R1", "r1", "C1", "c1", 1, 1, 1, 1, 1, 1, 1 );
    model.addRow( "R1", "r1", "C1", "c2", 2, 2, 2, 3, 3, 3, 2 );
    model.addRow( "R1", "r1", "C2", "c1", 3, 3, 3, 3, 3, 3, 3 );
    model.addRow( "R1", "r1", "C2", "c2", 4, 4, 4, 7, 7, 7, 4 );
    model.addRow( "R1", "r2", "C1", "c1", 5, 5, 5, 5, 8, 5, 6 ); // *
    model.addRow( "R1", "r2", "C1", "c2", 6, 6, 6, 11, 14, 11, 8 );
    model.addRow( "R1", "r2", "C2", "c1", 7, 7, 7, 7, 14, 7, 10 ); //
    model.addRow( "R1", "r2", "C2", "c2", 8, 8, 8, 15, 22, 15, 12 );
    model.addRow( "R1", "r2", "C2", "c2", 8, 16, 16, 23, 30, 23, 20 );
    model.addRow( "R1", "r2", "C2", "c2", 8, 24, 24, 31, 38, 31, 28 ); //
    model.addRow( "R2", "r1", "C1", "c1", 10, 10, 10, 10, 10, 10, 10 );
    model.addRow( "R2", "r1", "C1", "c2", 11, 11, 11, 21, 21, 21, 11 );
    model.addRow( "R2", "r1", "C2", "c1", 12, 12, 12, 12, 12, 12, 12 );
    model.addRow( "R2", "r1", "C2", "c2", 13, 13, 13, 25, 25, 25, 13 );
    model.addRow( "R2", "r2", "C1", "c1", 14, 14, 14, 14, 35, 14, 24 );
    model.addRow( "R2", "r2", "C1", "c2", 15, 15, 15, 29, 50, 29, 26 );
    model.addRow( "R2", "r2", "C2", "c1", 16, 16, 16, 16, 41, 16, 28 );
    model.addRow( "R2", "r2", "C2", "c2", 17, 17, 17, 33, 58, 33, 30 );
    return model;
  }

  private FieldAggregationFunction create( final String name, final String field, final String filter,
      final String group ) {
    final ItemSumFunction detailsSum = new ItemSumFunction();
    detailsSum.setName( name );
    detailsSum.setField( field );
    detailsSum.setCrosstabFilterGroup( filter );
    detailsSum.setGroup( group );
    return detailsSum;
  }

  public void testReport() throws Exception {
    final TableModel tableModel = createTableModel();
    final MasterReport report = createReport( tableModel );

    report.addExpression( create( "cell-sum", VALUE, null, COLUMN_DIMENSION_B ) );
    report.addExpression( new ValidateFunctionResultExpression( "#cell-sum", true, null ) );

    report.addExpression( create( "detail-sum", VALUE, COLUMN_DIMENSION_B, ROW_DIMENSION_B ) );
    report.addExpression( new ValidateFunctionResultExpression( "#detail-sum", true, COLUMN_DIMENSION_B ) );

    report.addExpression( create( "row-b-sum", VALUE, COLUMN_DIMENSION_A, ROW_DIMENSION_B ) );
    report.addExpression( new ValidateFunctionResultExpression( "#row-b-sum", true, COLUMN_DIMENSION_A ) );

    report.addExpression( create( "row-a-sum", VALUE, COLUMN_DIMENSION_A, ROW_DIMENSION_A ) );
    report.addExpression( new ValidateFunctionResultExpression( "#row-a-sum", true, COLUMN_DIMENSION_A ) );

    report.addExpression( create( "column-a-sum", VALUE, null, COLUMN_DIMENSION_A ) );
    report.addExpression( new ValidateFunctionResultExpression( "#column-a-sum", true, null ) );

    report.addExpression( create( "column-b-sum", VALUE, COLUMN_DIMENSION_B, ROW_DIMENSION_A ) );
    report.addExpression( new ValidateFunctionResultExpression( "#column-b-sum", true, COLUMN_DIMENSION_B ) );

    DebugReportRunner.execGraphics2D( report );
  }

  private MasterReport createReport( final TableModel tableModel ) {
    final MasterReport report = new MasterReport();
    report.setDataFactory( new TableDataFactory( "query", tableModel ) );
    report.setQuery( "query" );
    final DesignTimeDataSchemaModel dataSchemaModel = new DesignTimeDataSchemaModel( report );

    final CrosstabBuilder builder = new CrosstabBuilder( dataSchemaModel );
    builder.addRowDimension( "Row-Dimension-A" );
    builder.addRowDimension( ROW_DIMENSION_B );
    builder.addColumnDimension( COLUMN_DIMENSION_A );
    builder.addColumnDimension( COLUMN_DIMENSION_B );
    builder.addDetails( VALUE, null );
    report.setRootGroup( builder.create() );
    return report;
  }
}
