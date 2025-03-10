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


package org.pentaho.reporting.libraries.formula.function.text;

import org.pentaho.reporting.libraries.formula.EvaluationException;
import org.pentaho.reporting.libraries.formula.FormulaContext;
import org.pentaho.reporting.libraries.formula.LibFormulaErrorValue;
import org.pentaho.reporting.libraries.formula.function.Function;
import org.pentaho.reporting.libraries.formula.function.ParameterCallback;
import org.pentaho.reporting.libraries.formula.lvalues.LValue;
import org.pentaho.reporting.libraries.formula.lvalues.TypeValuePair;
import org.pentaho.reporting.libraries.formula.typing.Sequence;
import org.pentaho.reporting.libraries.formula.typing.Type;
import org.pentaho.reporting.libraries.formula.typing.TypeRegistry;
import org.pentaho.reporting.libraries.formula.typing.coretypes.TextType;

/**
 * This function returns the given value as text.
 *
 * @author Cedric Pronzato
 */
public class ConcatenateFunction implements Function {
  private static final long serialVersionUID = 3505313019941429911L;

  public ConcatenateFunction() {
  }

  public TypeValuePair evaluate( final FormulaContext context,
                                 final ParameterCallback parameters ) throws EvaluationException {
    final StringBuffer computedResult = new StringBuffer( 512 );
    final int parameterCount = parameters.getParameterCount();

    if ( parameterCount == 0 ) {
      throw EvaluationException.getInstance( LibFormulaErrorValue.ERROR_ARGUMENTS_VALUE );
    }

    final TypeRegistry typeRegistry = context.getTypeRegistry();
    for ( int paramIdx = 0; paramIdx < parameterCount; paramIdx++ ) {
      final Type type = parameters.getType( paramIdx );
      final Object value = parameters.getValue( paramIdx );
      final Sequence sequence = typeRegistry.convertToSequence( type, value );

      while ( sequence.hasNext() ) {
        final LValue lValue = sequence.nextRawValue();
        final TypeValuePair pair = lValue.evaluate();
        final Type type1 = pair.getType();
        final Object o = pair.getValue();
        final String str = typeRegistry.convertToText( type1, o );
        computedResult.append( str );
      }
    }

    return new TypeValuePair( TextType.TYPE, computedResult.toString() );
  }

  public String getCanonicalName() {
    return "CONCATENATE";
  }

}
