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


package org.pentaho.reporting.libraries.pixie.wmf.records;

import org.pentaho.reporting.libraries.pixie.wmf.MfDcState;
import org.pentaho.reporting.libraries.pixie.wmf.MfRecord;
import org.pentaho.reporting.libraries.pixie.wmf.MfType;
import org.pentaho.reporting.libraries.pixie.wmf.WmfFile;

import java.awt.*;

/**
 * Moves the current Clipping Region (@see CreateRegion) to the specified position, starting with the current region.
 * This will result in a relative move operation. The specified values are no absolute values.
 */
public class MfCmdOffsetClipRgn extends MfCmd {
  private static final int RECORD_SIZE = 2;
  private static final int POS_X = 1;
  private static final int POS_Y = 0;

  private int x;
  private int y;
  private int scaled_x;
  private int scaled_y;

  public MfCmdOffsetClipRgn() {
  }

  /**
   * Replays the command on the given WmfFile.
   *
   * @param file the meta file.
   */
  public void replay( final WmfFile file ) {
    final MfDcState state = file.getCurrentState();
    final Rectangle clipRect = state.getClipRegion();
    final Point p = getScaledDestination();
    clipRect.x += p.x;
    clipRect.y += p.y;
    state.setClipRegion( clipRect );
  }

  /**
   * Creates a empty unintialized copy of this command implementation.
   *
   * @return a new instance of the command.
   */
  public MfCmd getInstance() {
    return new MfCmdOffsetClipRgn();
  }

  public Point getDestination() {
    return new Point( x, y );
  }

  public String toString() {
    final StringBuffer b = new StringBuffer();
    b.append( "[OFFSET_CLIP_RECT] destination=" );
    b.append( getDestination() );
    return b.toString();
  }

  public void setDestination( final Point p ) {
    setDestination( p.x, p.y );
  }

  public void setDestination( final int x, final int y ) {
    this.x = x;
    this.y = y;
    scaleXChanged();
    scaleYChanged();

  }

  /**
   * Reads the function identifier. Every record type is identified by a function number corresponding to one of the
   * Windows GDI functions used.
   *
   * @return the function identifier.
   */
  public int getFunction() {
    return MfType.OFFSET_CLIP_RGN;
  }


  /**
   * Reads the command data from the given record and adjusts the internal parameters according to the data parsed.
   * <p/>
   * After the raw record was read from the datasource, the record is parsed by the concrete implementation.
   *
   * @param record the raw data that makes up the record.
   */
  public void setRecord( final MfRecord record ) {
    final int y = record.getParam( POS_Y );
    final int x = record.getParam( POS_X );
    setDestination( x, y );
  }

  /**
   * Creates a new record based on the data stored in the MfCommand.
   *
   * @return the created record.
   */
  public MfRecord getRecord() {
    final Point dest = getDestination();
    final MfRecord record = new MfRecord( RECORD_SIZE );
    record.setParam( POS_Y, dest.y );
    record.setParam( POS_X, dest.x );
    return record;
  }

  public Point getScaledDestination() {
    return new Point( scaled_x, scaled_y );
  }

  /**
   * A callback function to inform the object, that the x scale has changed and the internal coordinate values have to
   * be adjusted.
   */
  protected void scaleXChanged() {
    scaled_x = getScaledX( x );
  }

  /**
   * A callback function to inform the object, that the y scale has changed and the internal coordinate values have to
   * be adjusted.
   */
  protected void scaleYChanged() {
    scaled_y = getScaledY( y );
  }
}
