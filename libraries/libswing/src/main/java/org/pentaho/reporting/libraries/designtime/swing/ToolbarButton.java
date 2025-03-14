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


package org.pentaho.reporting.libraries.designtime.swing;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * A standard toolbar button preconfigured and ready to be used with a action..
 *
 * @author Thomas Morgner.
 */
public class ToolbarButton extends JButton {
  private class ActionUpdateHandler implements PropertyChangeListener {
    private ActionUpdateHandler() {
    }

    /**
     * This method gets called when a bound property is changed.
     *
     * @param evt A PropertyChangeEvent object describing the event source and the property that has changed.
     */
    public void propertyChange( final PropertyChangeEvent evt ) {
      revalidateAction();
    }

  }

  /**
   * Creates a button with no set text or icon.
   */
  public ToolbarButton() {
    init();
  }

  /**
   * Creates a button with an icon.
   *
   * @param icon the Icon image to display on the button
   */
  public ToolbarButton( final Icon icon ) {
    super( icon );
    init();
  }

  /**
   * Creates a button with text.
   *
   * @param text the text of the button
   */
  public ToolbarButton( final String text ) {
    super( text );
    init();
  }

  /**
   * Creates a button where properties are taken from the <code>Action</code> supplied.
   *
   * @param a the <code>Action</code> used to specify the new button
   * @since 1.3
   */
  public ToolbarButton( final Action a ) {
    super( a );
    init();
  }

  /**
   * Creates a button with initial text and an icon.
   *
   * @param text the text of the button
   * @param icon the Icon image to display on the button
   */
  public ToolbarButton( final String text, final Icon icon ) {
    super( text, icon );
    init();
  }

  protected void init() {
    addPropertyChangeListener( "action", new ActionUpdateHandler() );
    putClientProperty( "JButton.buttonType", "square" );
    putClientProperty( "JComponent.sizeVariant", "small" );
    setHorizontalTextPosition( JButton.CENTER );
    setVerticalTextPosition( JButton.BOTTOM );
    revalidateAction();
  }

  private void revalidateAction() {
    final Action a = getAction();
    if ( a == null ) {
      putClientProperty( "hideActionText", Boolean.FALSE ); // NON-NLS
    } else {
      if ( a.getValue( Action.SMALL_ICON ) != null || a.getValue( Action.LARGE_ICON_KEY ) != null ) {
        putClientProperty( "hideActionText", Boolean.TRUE ); // NON-NLS
      } else {
        putClientProperty( "hideActionText", Boolean.FALSE );// NON-NLS
      }
    }
  }
}
