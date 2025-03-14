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
 * Sets the size of the viewport. The SetViewportOrgEx function specifies which device point maps to the window origin
 * (0,0).
 * <p/>
 * This functionality is similiar to a TranslateTransform.
 */
public class MfCmdSetViewPortExt extends MfCmd {
  private static final int RECORD_SIZE = 2;
  private static final int POS_HEIGHT = 0;
  private static final int POS_WIDTH = 1;

  private int height;
  private int width;
  private int scaled_width;
  private int scaled_height;

  public MfCmdSetViewPortExt() {
  }

  /**
   * Replays the command on the given WmfFile.
   *
   * @param file the meta file.
   */
  public void replay( final WmfFile file ) {
    final MfDcState state = file.getCurrentState();
    final Dimension dim = getScaledDimension();
    state.setViewportExt( dim.width, dim.height );
  }

  /**
   * Creates a empty unintialized copy of this command implementation.
   *
   * @return a new instance of the command.
   */
  public MfCmd getInstance() {
    return new MfCmdSetViewPortExt();
  }

  /**
   * Reads the command data from the given record and adjusts the internal parameters according to the data parsed.
   * <p/>
   * After the raw record was read from the datasource, the record is parsed by the concrete implementation.
   *
   * @param record the raw data that makes up the record.
   */
  public void setRecord( final MfRecord record ) {
    final int height = record.getParam( POS_HEIGHT );
    final int width = record.getParam( POS_WIDTH );
    setDimension( width, height );
  }

  /**
   * Creates a new record based on the data stored in the MfCommand.
   *
   * @return the created record.
   */
  public MfRecord getRecord()
    throws RecordCreationException {
    final MfRecord record = new MfRecord( RECORD_SIZE );
    final Dimension dim = getDimension();
    record.setParam( POS_HEIGHT, dim.height );
    record.setParam( POS_WIDTH, dim.width );
    return record;
  }

  public String toString() {
    final StringBuffer b = new StringBuffer();
    b.append( "[SET_VIEWPORT_EXT] dimension=" );
    b.append( getDimension() );
    return b.toString();
  }

  public Dimension getDimension() {
    return new Dimension( width, height );
  }

  public Dimension getScaledDimension() {
    return new Dimension( scaled_width, scaled_height );
  }

  public void setDimension( final int w, final int h ) {
    this.width = w;
    this.height = h;
    scaleXChanged();
    scaleYChanged();

  }

  /**
   * A callback function to inform the object, that the x scale has changed and the internal coordinate values have to
   * be adjusted.
   */
  protected void scaleXChanged() {
    scaled_width = getScaledX( width );
  }

  /**
   * A callback function to inform the object, that the y scale has changed and the internal coordinate values have to
   * be adjusted.
   */
  protected void scaleYChanged() {
    scaled_height = getScaledY( height );
  }

  /**
   * Reads the function identifier. Every record type is identified by a function number corresponding to one of the
   * Windows GDI functions used.
   *
   * @return the function identifier.
   */
  public int getFunction() {
    return MfType.SET_VIEWPORT_EXT;
  }

}
