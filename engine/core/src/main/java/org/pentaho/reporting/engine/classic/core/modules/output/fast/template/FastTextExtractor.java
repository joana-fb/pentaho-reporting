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


package org.pentaho.reporting.engine.classic.core.modules.output.fast.template;

import org.pentaho.reporting.engine.classic.core.ReportElement;
import org.pentaho.reporting.engine.classic.core.Section;
import org.pentaho.reporting.engine.classic.core.SubReport;
import org.pentaho.reporting.engine.classic.core.filter.DataSource;
import org.pentaho.reporting.engine.classic.core.filter.RawDataSource;
import org.pentaho.reporting.engine.classic.core.function.ExpressionRuntime;
import org.pentaho.reporting.engine.classic.core.layout.build.RichTextStyleResolver;
import org.pentaho.reporting.engine.classic.core.layout.output.ContentProcessingException;
import org.pentaho.reporting.engine.classic.core.style.BandStyleKeys;
import org.pentaho.reporting.engine.classic.core.style.StyleSheet;

public class FastTextExtractor {
  private final StringBuilder textBuffer;
  private ExpressionRuntime runtime;
  private RichTextStyleResolver styleResolver;
  private Object rawResult;
  private int inlineLayout;

  public FastTextExtractor() {
    this.textBuffer = new StringBuilder();
  }

  public Object compute( final ReportElement content, final ExpressionRuntime runtime )
          throws ContentProcessingException {

    if ( styleResolver == null ) {
      styleResolver = new RichTextStyleResolver( runtime.getProcessingContext(), content );
    }

    this.runtime = runtime;
    this.rawResult = null;
    this.textBuffer.delete( 0, this.textBuffer.length() );
    this.inlineLayout = 0;
    try {
      if ( content instanceof Section ) {
        traverseSection( (Section) content );
      } else {
        inspectElement( content, true );
      }
      // A simple result. So there's no need to create a rich-text string.
      if ( rawResult != null ) {
        return rawResult;
      }
      return textBuffer.toString();
    } finally {
      this.runtime = null;
    }
  }

  public void setRuntime( final ExpressionRuntime runtime ) {
    this.runtime = runtime;
  }

  protected void clearText() {
    textBuffer.delete( 0, textBuffer.length() );
  }

  public Object getRawResult() {
    return rawResult;
  }

  public String getText() {
    return textBuffer.toString();
  }

  protected void traverseSection( final Section section ) throws ContentProcessingException {
    boolean inlineSection;
    if ( inlineLayout == 0 ) {
      StyleSheet styleSheet = section.getComputedStyle();
      if ( BandStyleKeys.LAYOUT_INLINE.equals( styleSheet.getStyleProperty( BandStyleKeys.LAYOUT ) ) ) {
        inlineLayout += 1;
        inlineSection = true;
      } else {
        inlineSection = false;
      }
    } else {
      inlineLayout += 1;
      inlineSection = true;
    }

    if ( inspectStartSection( section, inlineSection ) ) {
      final int count = section.getElementCount();
      for ( int i = 0; i < count; i++ ) {
        final ReportElement element = section.getElement( i );
        if ( element instanceof SubReport ) {
          inspectStartSection( element, inlineSection );
          inspectElement( element, inlineSection );
          inspectEndSection( element, inlineSection );
        } else if ( element instanceof Section ) {
          traverseSection( (Section) element );
        } else {
          inspectStartSection( element, inlineSection );
          inspectElement( element, inlineSection );
          inspectEndSection( element, inlineSection );
        }
      }
    }

    inspectEndSection( section, inlineSection );

    if ( inlineLayout > 0 ) {
      inlineLayout -= 1;
    }
  }

  protected void inspectEndSection( final ReportElement section, final boolean inlineSection ) {
  }

  protected boolean inspectStartSection( final ReportElement section, final boolean inlineSection ) {
    return false;
  }

  protected void inspectElement( final ReportElement element, final boolean inlineSection )
          throws ContentProcessingException {
    if ( element instanceof Section ) {
      // subreports and so on ..
      return;
    }

    Object value =
            AbstractFormattedDataBuilder.filterRichText( element, element.getElementType().getValue( runtime, element ) );
    if ( value == null ) {
      return;
    }
    if ( value instanceof Section ) {
      Section section = (Section) value;
      styleResolver.resolveRichTextStyle( section );
      traverseSection( section );
      return;
    }

    handleValueContent( element, value, inlineSection );
  }

  protected void handleValueContent( final ReportElement element, final Object value, final boolean inlineSection )
          throws ContentProcessingException {
    if ( value instanceof String ) {
      textBuffer.append( value );

      final DataSource dataSource = element.getElementType();
      if ( dataSource instanceof RawDataSource ) {
        final RawDataSource rds = (RawDataSource) dataSource;
        rawResult = rds.getRawValue( runtime, element );
      } else {
        rawResult = null;
      }
      return;
    }

    rawResult = value;
  }

  protected StringBuilder getTextBuffer() {
    return textBuffer;
  }

  protected void setRawResult( final Object rawResult ) {
    this.rawResult = rawResult;
  }

  protected int getTextLength() {
    return textBuffer.length();
  }
}
