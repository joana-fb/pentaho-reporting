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


package org.pentaho.plugin.jfreereport.reportcharts.collectors;

import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.Dataset;
import org.pentaho.reporting.engine.classic.core.DataRow;
import org.pentaho.reporting.engine.classic.core.function.Expression;

import java.util.ArrayList;
import java.util.Arrays;

public class PivotCategorySetCollector extends AbstractCollectorFunction {
  private ArrayList<String> valueColumns;
  private String categoryColumn;

  public PivotCategorySetCollector() {
    valueColumns = new ArrayList<String>();
  }

  public String getCategoryColumn() {
    return categoryColumn;
  }

  public void setCategoryColumn( final String categoryColumn ) {
    this.categoryColumn = categoryColumn;
  }

  public void setValueColumn( final int index, final String field ) {
    if ( valueColumns.size() == index ) {
      valueColumns.add( field );
    } else {
      valueColumns.set( index, field );
    }
  }

  public String getValueColumn( final int index ) {
    return valueColumns.get( index );
  }

  public int getValueColumnCount() {
    return valueColumns.size();
  }

  public String[] getValueColumn() {
    return valueColumns.toArray( new String[ valueColumns.size() ] );
  }

  public void setValueColumn( final String[] fields ) {
    this.valueColumns.clear();
    this.valueColumns.addAll( Arrays.asList( fields ) );
  }


  protected Dataset createNewDataset() {
    return new DefaultCategoryDataset();
  }


  protected void buildDataset() {
    final DataRow dataRow = getDataRow();
    final Object categoryObject = dataRow.get( getCategoryColumn() );
    if ( categoryObject instanceof Comparable == false ) {
      return;
    }

    final Comparable categoryComparable = (Comparable) categoryObject;

    // I love to be paranoid!
    final DefaultCategoryDataset categoryDataset = (DefaultCategoryDataset) getDataSet();

    final int maxIndex = this.valueColumns.size();
    for ( int i = 0; i < maxIndex; i++ ) {
      final Comparable seriesName = querySeriesValue( i );
      final Object valueObject = dataRow.get( getValueColumn( i ) );
      final Number value = ( valueObject instanceof Number ) ? (Number) valueObject : null;
      final Number existingValue =
        CollectorFunctionUtil.queryExistingValueFromDataSet( categoryDataset, categoryComparable, seriesName );
      if ( existingValue != null ) {
        if ( value != null ) {
          categoryDataset.setValue( CollectorFunctionUtil.add( existingValue, value ), categoryComparable, seriesName );
        }
      } else {
        categoryDataset.setValue( value, categoryComparable, seriesName );
      }
    }
  }


  /**
   * Return a completly separated copy of this function. The copy does no longer share any changeable objects with the
   * original function.
   *
   * @return a copy of this function.
   */
  public Expression getInstance() {
    final PivotCategorySetCollector expression = (PivotCategorySetCollector) super.getInstance();
    expression.valueColumns = (ArrayList) valueColumns.clone();
    return expression;
  }

}
