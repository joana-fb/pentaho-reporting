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


package org.pentaho.reporting.engine.classic.core.filter.templates;

import org.pentaho.reporting.engine.classic.core.ReportElement;
import org.pentaho.reporting.engine.classic.core.filter.DataRowDataSource;
import org.pentaho.reporting.engine.classic.core.filter.FormatSpecification;
import org.pentaho.reporting.engine.classic.core.filter.RawDataSource;
import org.pentaho.reporting.engine.classic.core.filter.StringFilter;
import org.pentaho.reporting.engine.classic.core.function.ExpressionRuntime;

/**
 * A string field template.
 *
 * @author Thomas Morgner
 */
public class StringFieldTemplate extends AbstractTemplate implements RawDataSource {
  /**
   * The data-row data source.
   */
  private DataRowDataSource dataRowDataSource;

  /**
   * A string filter.
   */
  private StringFilter stringFilter;

  /**
   * Creates a new string field template.
   */
  public StringFieldTemplate() {
    dataRowDataSource = new DataRowDataSource();
    stringFilter = new StringFilter();
    stringFilter.setDataSource( dataRowDataSource );
  }

  /**
   * Returns the field name.
   *
   * @return The field name.
   */
  public String getField() {
    return dataRowDataSource.getDataSourceColumnName();
  }

  /**
   * Sets the field name.
   *
   * @param field
   *          the field name.
   */
  public void setField( final String field ) {
    dataRowDataSource.setDataSourceColumnName( field );
  }

  /**
   * Returns the formula used to compute the value of the data source.
   *
   * @return the formula.
   */
  public String getFormula() {
    return dataRowDataSource.getFormula();
  }

  /**
   * Defines the formula used to compute the value of this data source.
   *
   * @param formula
   *          the formula for the data source.
   */
  public void setFormula( final String formula ) {
    dataRowDataSource.setFormula( formula );
  }

  /**
   * Returns the value displayed by the field when the data source value is <code>null</code>.
   *
   * @return A value to represent <code>null</code>.
   */
  public String getNullValue() {
    return stringFilter.getNullValue();
  }

  /**
   * Sets the value displayed by the field when the data source value is <code>null</code>.
   *
   * @param nullValue
   *          the value that represents <code>null</code>.
   */
  public void setNullValue( final String nullValue ) {
    stringFilter.setNullValue( nullValue );
  }

  /**
   * Returns the current value for the data source.
   *
   * @param runtime
   *          the expression runtime that is used to evaluate formulas and expressions when computing the value of this
   *          filter.
   * @param element
   * @return the value.
   */
  public Object getValue( final ExpressionRuntime runtime, final ReportElement element ) {
    return stringFilter.getValue( runtime, element );
  }

  /**
   * Clones the template.
   *
   * @return the clone.
   * @throws CloneNotSupportedException
   *           this should never happen.
   */
  public StringFieldTemplate clone() throws CloneNotSupportedException {
    final StringFieldTemplate template = (StringFieldTemplate) super.clone();
    template.stringFilter = stringFilter.clone();
    template.dataRowDataSource = (DataRowDataSource) template.stringFilter.getDataSource();
    return template;
  }

  /**
   * Returns the datarow data source used in this template.
   *
   * @return the datarow data source.
   */
  protected DataRowDataSource getDataRowDataSource() {
    return dataRowDataSource;
  }

  public Object getRawValue( final ExpressionRuntime runtime, final ReportElement element ) {
    return stringFilter.getRawValue( runtime, element );
  }

  public FormatSpecification getFormatString( final ExpressionRuntime runtime, final ReportElement element,
      FormatSpecification formatSpecification ) {
    if ( formatSpecification == null ) {
      formatSpecification = new FormatSpecification();
    }
    formatSpecification.redefine( FormatSpecification.TYPE_UNDEFINED, null );
    return formatSpecification;
  }
}
