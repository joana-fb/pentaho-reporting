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


package org.pentaho.reporting.libraries.formula.function.information;

import org.pentaho.reporting.libraries.formula.function.AbstractFunctionDescription;
import org.pentaho.reporting.libraries.formula.function.FunctionCategory;
import org.pentaho.reporting.libraries.formula.typing.Type;
import org.pentaho.reporting.libraries.formula.typing.coretypes.NumberType;
import org.pentaho.reporting.libraries.formula.typing.coretypes.TextType;

/**
 * Describes ValueFunction function.
 *
 * @author Kurits
 * @see org.pentaho.reporting.libraries.formula.function.information.ValueFunction
 */
public class ValueFunctionDescription extends AbstractFunctionDescription {
  private static final long serialVersionUID = 743117553650008440L;

  public ValueFunctionDescription() {
    super( "VALUE", "org.pentaho.reporting.libraries.formula.function.information.Value-Function" );
  }

  public FunctionCategory getCategory() {
    return InformationFunctionCategory.CATEGORY;
  }

  public int getParameterCount() {
    return 1;
  }

  public Type getParameterType( final int position ) {
    return TextType.TYPE;
  }

  public Type getValueType() {
    return NumberType.GENERIC_NUMBER;
  }

  public boolean isParameterMandatory( final int position ) {
    return true;
  }

}
