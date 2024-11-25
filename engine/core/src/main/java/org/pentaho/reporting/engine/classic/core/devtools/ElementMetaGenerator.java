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


package org.pentaho.reporting.engine.classic.core.devtools;

import org.pentaho.reporting.engine.classic.core.ClassicEngineBoot;
import org.pentaho.reporting.engine.classic.core.metadata.AbstractMetaData;
import org.pentaho.reporting.engine.classic.core.metadata.AttributeMetaData;
import org.pentaho.reporting.engine.classic.core.metadata.ElementMetaData;
import org.pentaho.reporting.engine.classic.core.metadata.ElementTypeRegistry;
import org.pentaho.reporting.engine.classic.core.metadata.GroupedMetaDataComparator;
import org.pentaho.reporting.engine.classic.core.metadata.MetaData;
import org.pentaho.reporting.libraries.base.util.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;

public class ElementMetaGenerator {
  private static class AttributeCarrier implements Comparable {
    public AttributeMetaData metaData;
    public String prefix;

    private AttributeCarrier( final String prefix, final AttributeMetaData metaData ) {
      this.prefix = prefix;
      this.metaData = metaData;
    }

    public int compareTo( final Object o ) {
      final AttributeCarrier carrier = (AttributeCarrier) o;
      final int compare = GroupedMetaDataComparator.ENGLISH.compare( this.metaData, carrier.metaData );
      if ( compare == 0 ) {
        return prefix.compareTo( carrier.prefix );
      }
      return compare;
    }
  }

  private static String readMetadataAttribute( final MetaData metaData, final String name, final String defaultValue ) {
    final String metaAttribute = metaData.getMetaAttribute( name, Locale.ENGLISH );
    if ( metaAttribute == null ) {
      return defaultValue;
    }
    return metaAttribute;
  }

  private static void printMetadata( final MetaData metaData, final String prefix, final String name,
      final String defaultValue ) {
    System.out.println( prefix + name + "=" + readMetadataAttribute( metaData, name, defaultValue ) );
  }

  public static void main( final String[] args ) {
    ClassicEngineBoot.getInstance().start();
    final HashMap<String, AttributeCarrier> attributes = new HashMap<String, AttributeCarrier>();

    final ElementMetaData[] allTypes = ElementTypeRegistry.getInstance().getAllElementTypes();
    Arrays.sort( allTypes, GroupedMetaDataComparator.ENGLISH );

    System.out.println( "# Element definitions" );
    for ( int i = 0; i < allTypes.length; i++ ) {
      final ElementMetaData type = allTypes[i];
      final String prefix;
      if ( type instanceof AbstractMetaData ) {
        final AbstractMetaData metaData = (AbstractMetaData) type;
        final String prefixMetadata = metaData.getKeyPrefix();
        if ( StringUtils.isEmpty( prefixMetadata ) ) {
          prefix = "";
        } else {
          prefix = prefixMetadata + type.getName() + ".";
        }
      } else {
        prefix = "element." + type.getName() + ".";
      }

      printMetadata( type, prefix, "display-name", type.getName() );
      printMetadata( type, prefix, "grouping", "" );
      printMetadata( type, prefix, "grouping.ordinal", "0" );
      printMetadata( type, prefix, "ordinal", "0" );
      printMetadata( type, prefix, "description", "" );
      printMetadata( type, prefix, "deprecated", "" );
      printMetadata( type, prefix, "icon", "" );
      System.out.println();

      final AttributeMetaData[] attributeDescriptions = type.getAttributeDescriptions();
      for ( int j = 0; j < attributeDescriptions.length; j++ ) {
        final AttributeMetaData attribute = attributeDescriptions[j];
        final String attrNs = ElementTypeRegistry.getInstance().getNamespacePrefix( attribute.getNameSpace() );
        final String attrPrefix;
        if ( attribute instanceof AbstractMetaData ) {
          final AbstractMetaData metaData = (AbstractMetaData) attribute;
          final String prefixMetadata = metaData.getKeyPrefix();
          if ( StringUtils.isEmpty( prefixMetadata ) ) {
            attrPrefix = "";
          } else {
            attrPrefix = prefixMetadata + attribute.getName() + ".";
          }
        } else {
          attrPrefix = "element." + type.getName() + ".attribute." + attrNs + "." + attribute.getName();
        }
        attributes.put( attrPrefix, new AttributeCarrier( attrPrefix, attribute ) );
      }
    }

    System.out.println( "# Attribute definitions" );
    final AttributeCarrier[] attributeMetadata = attributes.values().toArray( new AttributeCarrier[attributes.size()] );
    Arrays.sort( attributeMetadata );
    for ( int i = 0; i < attributeMetadata.length; i++ ) {
      final AttributeCarrier carrier = attributeMetadata[i];
      final AttributeMetaData attribute = carrier.metaData;
      final String attrNs = ElementTypeRegistry.getInstance().getNamespacePrefix( attribute.getNameSpace() );
      final String attrPrefix = carrier.prefix;
      printMetadata( attribute, attrPrefix, "display-name", attribute.getName() );
      printMetadata( attribute, attrPrefix, "grouping", attrNs );
      printMetadata( attribute, attrPrefix, "grouping.ordinal", "0" );
      printMetadata( attribute, attrPrefix, "ordinal", "0" );
      printMetadata( attribute, attrPrefix, "description", "" );
      printMetadata( attribute, attrPrefix, "deprecated", "" );
      System.out.println();
    }
  }
}
