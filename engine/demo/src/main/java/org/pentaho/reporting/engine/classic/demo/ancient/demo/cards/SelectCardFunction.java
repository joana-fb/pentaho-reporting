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


package org.pentaho.reporting.engine.classic.demo.ancient.demo.cards;

import java.io.Serializable;

import org.pentaho.reporting.engine.classic.core.Band;
import org.pentaho.reporting.engine.classic.core.Element;
import org.pentaho.reporting.engine.classic.core.event.ReportEvent;
import org.pentaho.reporting.engine.classic.core.function.AbstractFunction;

/**
 * The SelectCardFunction defines the visiblity of the card bands depending on the current card type.
 * <p/>
 * Valid property names are: <ul> <li>Account <li>Admin <li>User <li>Prepaid <li>Free <li>Empty </ul>
 *
 * @author Thomas Morgner.
 */
public class SelectCardFunction extends AbstractFunction implements Serializable
{
  private String baseCard;
  private String field;
  private String account;
  private String admin;
  private String user;
  private String prepaid;
  private String free;
  private String empty;

  /**
   * Default constructor.
   */
  public SelectCardFunction()
  {
  }

  /**
   * Hides all bands, which are invalid for the current card type. A band declares its validity by having the same
   * element name as the card type name.
   *
   * @param band the band that should be evaluated
   */
  private void selectBand(final Band band)
  {
    CardType type = (CardType) getDataRow().get(getField());
    if (type == null)
    {
      type = CardType.EMPTY;
    }

    final String bandName = getBandForCardType(type);

    // if the special type empty is active, then everything will be hidden ...
    if (type == CardType.EMPTY)
    {
      band.setVisible(false);
    }
    else
    {
      band.setVisible(true);

      final Element[] elements = band.getElementArray();
      for (int i = 0; i < elements.length; i++)
      {
        if (elements[i] instanceof Band)
        {
          final Element e = elements[i];
          e.setVisible(e.getName().equals(bandName));
        }
      }
    }
  }

  /**
   * Receives notification that a row of data is being processed.
   *
   * @param event the event.
   */
  public void itemsAdvanced(final ReportEvent event)
  {
    final Element[] elements = event.getReport().getItemBand().getElementArray();
    final String rootName = getBaseCard();
    // the itemband contains several cards, every card is contained in a single band.
    for (int i = 0; i < elements.length; i++)
    {
      if (elements[i] instanceof Band && elements[i].getName().equals(rootName))
      {
        selectBand((Band) elements[i]);
      }
    }
  }

  /**
   * Return the current expression value. <P> The value depends (obviously) on the expression implementation.
   *
   * @return the value of the function.
   */
  public Object getValue()
  {
    return null;
  }

  public String getBaseCard()
  {
    return baseCard;
  }

  public void setBaseCard(String baseCard)
  {
    this.baseCard = baseCard;
  }

  public String getField()
  {
    return field;
  }

  public void setField(String field)
  {
    this.field = field;
  }

  public String getAccount()
  {
    return account;
  }

  public void setAccount(String account)
  {
    this.account = account;
  }

  public String getAdmin()
  {
    return admin;
  }

  public void setAdmin(String admin)
  {
    this.admin = admin;
  }

  public String getUser()
  {
    return user;
  }

  public void setUser(String user)
  {
    this.user = user;
  }

  public String getPrepaid()
  {
    return prepaid;
  }

  public void setPrepaid(String prepaid)
  {
    this.prepaid = prepaid;
  }

  public String getFree()
  {
    return free;
  }

  public void setFree(String free)
  {
    this.free = free;
  }

  public String getEmpty()
  {
    return empty;
  }

  public void setEmpty(String empty)
  {
    this.empty = empty;
  }

  protected String getBandForCardType(CardType ct)
  {
    if (CardType.ACCOUNT == ct)
    {
      return getAccount();
    }
    if (CardType.ADMIN == ct)
    {
      return getAdmin();
    }
    if (CardType.FREE == ct)
    {
      return getFree();
    }
    if (CardType.PREPAID == ct)
    {
      return getPrepaid();
    }
    if (CardType.USER == ct)
    {
      return getUser();
    }
    return getFree();
  }
}
