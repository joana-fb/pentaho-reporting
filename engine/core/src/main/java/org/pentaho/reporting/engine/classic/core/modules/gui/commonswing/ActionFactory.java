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


package org.pentaho.reporting.engine.classic.core.modules.gui.commonswing;

/**
 * Creation-Date: 16.11.2006, 16:18:56
 *
 * @author Thomas Morgner
 */
public interface ActionFactory {
  public ActionPlugin[] getActions( SwingGuiContext context, String category );
}
