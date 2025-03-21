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
package org.pentaho.openformula.ui;

import javax.swing.*;

public class DefaultFieldDefinition implements FieldDefinition {
  private final String name;
  private final String displayName;
  private final Icon icon;
  private final Class<?> fieldType;

  public DefaultFieldDefinition( final String name ) {
    this( name, name, null, Object.class );
  }

  public DefaultFieldDefinition( final String name, final String displayName ) {
    this( name, displayName, null, Object.class );
  }

  public DefaultFieldDefinition( String name, String displayName, Icon icon, Class<?> fieldType ) {
    this.name = name;
    this.displayName = displayName;
    this.icon = icon;
    this.fieldType = fieldType;
  }

  public String getName() {
    return name;
  }

  public String getDisplayName() {
    return displayName;
  }

  public Icon getIcon() {
    return icon;
  }

  public Class<?> getFieldType() {
    return fieldType;
  }
}
