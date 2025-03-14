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


package org.pentaho.reporting.libraries.formula.function.database;

import org.pentaho.reporting.libraries.formula.EvaluationException;
import org.pentaho.reporting.libraries.formula.FormulaContext;
import org.pentaho.reporting.libraries.formula.LibFormulaErrorValue;
import org.pentaho.reporting.libraries.formula.function.Function;
import org.pentaho.reporting.libraries.formula.function.ParameterCallback;
import org.pentaho.reporting.libraries.formula.lvalues.TypeValuePair;
import org.pentaho.reporting.libraries.formula.typing.ExtendedComparator;
import org.pentaho.reporting.libraries.formula.typing.Type;
import org.pentaho.reporting.libraries.formula.typing.TypeRegistry;
import org.pentaho.reporting.libraries.formula.typing.coretypes.LogicalType;

public class InFunction implements Function {
  private static final TypeValuePair RETURN_TRUE = new TypeValuePair( LogicalType.TYPE, Boolean.TRUE );
  private static final TypeValuePair RETURN_FALSE = new TypeValuePair( LogicalType.TYPE, Boolean.FALSE );
  private static final long serialVersionUID = -3737884974501253814L;

  public InFunction() {
  }

  public String getCanonicalName() {
    return "IN";
  }

  public TypeValuePair evaluate( final FormulaContext context,
                                 final ParameterCallback parameters )
    throws EvaluationException {
    final int length = parameters.getParameterCount();
    if ( length < 2 ) {
      throw EvaluationException.getInstance( LibFormulaErrorValue.ERROR_ARGUMENTS_VALUE );
    }

    final TypeRegistry typeRegistry = context.getTypeRegistry();
    final Object value1Raw = parameters.getValue( 0 );
    final Type type1 = parameters.getType( 0 );
    for ( int i = 1; i < parameters.getParameterCount(); i++ ) {
      final Object value2Raw = parameters.getValue( i );
      if ( value1Raw == null || value2Raw == null ) {
        throw EvaluationException.getInstance( LibFormulaErrorValue.ERROR_NA_VALUE );
      }

      final Type type2 = parameters.getType( i );
      final ExtendedComparator comparator = typeRegistry.getComparator( type1, type2 );
      final boolean result = comparator.isEqual( type1, value1Raw, type2, value2Raw );
      if ( result ) {
        return RETURN_TRUE;
      }
    }
    return RETURN_FALSE;
  }
}
