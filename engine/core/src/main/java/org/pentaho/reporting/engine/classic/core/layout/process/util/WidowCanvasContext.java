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


package org.pentaho.reporting.engine.classic.core.layout.process.util;

import org.pentaho.reporting.engine.classic.core.layout.model.FinishedRenderNode;
import org.pentaho.reporting.engine.classic.core.layout.model.RenderBox;

public class WidowCanvasContext implements WidowContext {
  private StackedObjectPool<WidowCanvasContext> pool;
  private WidowContext parent;

  public WidowCanvasContext() {
  }

  public void init( final StackedObjectPool<WidowCanvasContext> pool, final WidowContext parent ) {
    this.pool = pool;
    this.parent = parent;
  }

  public void startChild( final RenderBox box ) {

  }

  public void registerFinishedNode( final FinishedRenderNode node ) {

  }

  public void registerBreakMark( final RenderBox box ) {

  }

  public void endChild( final RenderBox box ) {

  }

  public WidowContext commit( final RenderBox box ) {
    return parent;
  }

  public void subContextCommitted( final RenderBox contextBox ) {

  }

  public void clearForPooledReuse() {
    parent = null;
    pool.free( this );
  }
}
