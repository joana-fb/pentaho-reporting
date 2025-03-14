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


package org.pentaho.reporting.libraries.css.resolver.values.percentages.text;

import org.pentaho.reporting.libraries.css.StyleSheetUtility;
import org.pentaho.reporting.libraries.css.dom.DocumentContext;
import org.pentaho.reporting.libraries.css.dom.LayoutElement;
import org.pentaho.reporting.libraries.css.dom.LayoutStyle;
import org.pentaho.reporting.libraries.css.keys.font.FontStyleKeys;
import org.pentaho.reporting.libraries.css.keys.text.TextStyleKeys;
import org.pentaho.reporting.libraries.css.model.StyleKey;
import org.pentaho.reporting.libraries.css.resolver.values.ResolveHandler;
import org.pentaho.reporting.libraries.css.values.CSSNumericType;
import org.pentaho.reporting.libraries.css.values.CSSNumericValue;
import org.pentaho.reporting.libraries.css.values.CSSValue;
import org.pentaho.reporting.libraries.fonts.registry.FontMetrics;
import org.pentaho.reporting.libraries.fonts.tools.FontStrictGeomUtility;

/**
 * Creation-Date: 21.12.2005, 15:12:04
 *
 * @author Thomas Morgner
 */
public class LetterSpacingResolveHandler implements ResolveHandler {
  public LetterSpacingResolveHandler() {
  }

  /**
   * This indirectly defines the resolve order. The higher the order, the more dependent is the resolver on other
   * resolvers to be complete.
   *
   * @return
   */
  public StyleKey[] getRequiredStyles() {
    return new StyleKey[] {
      FontStyleKeys.FONT_SIZE,
      FontStyleKeys.FONT_FAMILY,
      FontStyleKeys.FONT_EFFECT,
      FontStyleKeys.FONT_SMOOTH,
      FontStyleKeys.FONT_STRETCH,
      FontStyleKeys.FONT_VARIANT,
      FontStyleKeys.FONT_WEIGHT,
    };
  }

  /**
   * Resolves a single property.
   *
   * @param currentNode
   */
  public void resolve( final DocumentContext process,
                       final LayoutElement currentNode,
                       final StyleKey key ) {
    // Percentages get resolved against the width of a standard space (0x20)
    // character.
    final LayoutStyle layoutContext = currentNode.getLayoutStyle();
    final FontMetrics fm = process.getOutputMetaData().getFontMetrics( layoutContext );
    if ( fm == null ) {
      // we have no font family, so return.
      layoutContext.setValue( TextStyleKeys.X_MIN_LETTER_SPACING, CSSNumericValue.ZERO_LENGTH );
      layoutContext.setValue( TextStyleKeys.X_MAX_LETTER_SPACING, CSSNumericValue.ZERO_LENGTH );
      layoutContext.setValue( TextStyleKeys.X_OPTIMUM_LETTER_SPACING, CSSNumericValue.ZERO_LENGTH );
      return;
    }

    final double width = FontStrictGeomUtility.toExternalValue( fm.getCharWidth( 0x20 ) );
    final CSSNumericValue percentageBase =
      CSSNumericValue.createValue( CSSNumericType.PT, width );
    final CSSNumericValue min = StyleSheetUtility.convertLength
      ( resolveValue( layoutContext, TextStyleKeys.X_MIN_LETTER_SPACING ), percentageBase, currentNode );
    final CSSNumericValue max = StyleSheetUtility.convertLength
      ( resolveValue( layoutContext, TextStyleKeys.X_MAX_LETTER_SPACING ), percentageBase, currentNode );
    final CSSNumericValue opt = StyleSheetUtility.convertLength
      ( resolveValue( layoutContext, TextStyleKeys.X_OPTIMUM_LETTER_SPACING ), percentageBase, currentNode );

    layoutContext.setValue( TextStyleKeys.X_MIN_LETTER_SPACING, min );
    layoutContext.setValue( TextStyleKeys.X_MAX_LETTER_SPACING, max );
    layoutContext.setValue( TextStyleKeys.X_OPTIMUM_LETTER_SPACING, opt );
  }

  private CSSNumericValue resolveValue( final LayoutStyle style, final StyleKey key ) {
    final CSSValue value = style.getValue( key );
    if ( value instanceof CSSNumericValue == false ) {
      // this also covers the valid 'normal' property.
      // it simply means, dont add extra space to the already existing spaces
      return CSSNumericValue.ZERO_LENGTH;
    }

    return (CSSNumericValue) value;
  }

}
