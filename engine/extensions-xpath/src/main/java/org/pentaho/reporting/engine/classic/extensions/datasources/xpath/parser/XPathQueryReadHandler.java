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


package org.pentaho.reporting.engine.classic.extensions.datasources.xpath.parser;

import org.pentaho.reporting.libraries.xmlns.parser.PropertyReadHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class XPathQueryReadHandler extends PropertyReadHandler {
  private boolean legacyMode;

  public XPathQueryReadHandler() {
  }

  /**
   * Starts parsing.
   *
   * @param attrs the attributes.
   * @throws org.xml.sax.SAXException if there is a parsing error.
   */
  protected void startParsing( final Attributes attrs ) throws SAXException {
    super.startParsing( attrs );
    if ( "false".equals( attrs.getValue( getUri(), "legacy-mode" ) ) ) {
      this.legacyMode = false;
    } else {
      this.legacyMode = true;
    }
  }

  public boolean isLegacyMode() {
    return legacyMode;
  }
}
