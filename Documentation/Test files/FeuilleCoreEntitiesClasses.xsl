<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"  xmlns:uFN="uFN" version="2.0">
  <xsl:import href="utilFunctions.xsl"/>
	<!-- <xsl:output method="text" encoding="iso-8859-1" indent="yes"/> -->
	<xsl:output method="text" encoding="UTF-8" indent="yes"/>

<xsl:template match="Project/Models/Package/ModelChildren/Class[not (.//AssociationClass) and .//Stereotype[@Name ='Entity']]">
       <xsl:variable name="contextName" select="@Name"/>
	   <xsl:variable name="iDClass" select="@Id"/>
	   <xsl:result-document href="{$projectName}/{$projectName}.Core/Entities//{$contextName}.cs">
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using <xsl:value-of select="$projectName"/>.SharedKernel.Interfaces;
using <xsl:value-of select="$projectName"/>.SharedKernel;

namespace <xsl:value-of select="$projectName"/>.Core.Entities
{
<xsl:value-of select="uFN:classAnnotations(.)"/>
public class <xsl:value-of select="$contextName"/><xsl:if test="/.//Generalization[@To=$iDClass]"> : <xsl:value-of select="/.//Class[@Id=/.//Generalization[@To=$iDClass]/@From]/@Name"/></xsl:if><xsl:if test="not(/.//Generalization[@To=$iDClass])"> <xsl:value-of select="uFN:classStereotypes(.)"/></xsl:if>
	{
	<!-- Attribute -->
	<xsl:for-each select=".//Attribute">
	    <xsl:variable name="attributeType">
			<xsl:if test="not(./Stereotypes/Stereotype[@Name='Embedded'])">
		        <xsl:value-of select="concat(concat(.//DataType/@Name, ./@Type), ./@TypeModifier)"/>
			</xsl:if>
			<xsl:if test="./Stereotypes/Stereotype[@Name='Embedded']">
		        <xsl:variable name="attributeTypeClassRef" select="./Type/Class/@Idref"/>
				<xsl:variable name="attributeTypeClass" select="/Project/Models/Package/ModelChildren/Class[@Id=$attributeTypeClassRef]"/>
				<xsl:value-of select="$attributeTypeClass/@Name"/>
			</xsl:if>
		</xsl:variable>
		<xsl:text>&#x9;</xsl:text>public <xsl:value-of select="$attributeType"/> <xsl:text>&#x20;</xsl:text> <xsl:value-of select="uFN:first-upper(./@Name)"/> { get; set;}
	</xsl:for-each>

	<!-- Association -->
	<xsl:for-each select="/.//Association[ToEnd/AssociationEnd/@EndModelElement=$iDClass and (FromEnd/AssociationEnd/@Multiplicity='1' or FromEnd/AssociationEnd/@Multiplicity='Unspecified' or FromEnd/AssociationEnd/@Multiplicity='0..1')]">
		<xsl:variable name="fromEndClass" select="uFN:typeUMLElement(FromEnd/AssociationEnd/@EndModelElement)"/>
		<xsl:if test= "ToEnd/AssociationEnd/Stereotypes/Stereotype[@Name ='FK']"><xsl:text>&#x9;</xsl:text>public int<xsl:if test= "FromEnd/AssociationEnd/@Multiplicity='0..1'">?</xsl:if><xsl:text>&#x20;</xsl:text><xsl:value-of select="$fromEndClass/@Name"/>Id { get; set;}</xsl:if>
		<xsl:if test= "$root/Project/Models/Package/ModelChildren/Class[@Name = $fromEndClass/@Name]/Stereotypes/Stereotype[@Name ='NotMapped']">[NotMappedAttribute]</xsl:if>
		public <xsl:value-of select="$fromEndClass/@Name"/><xsl:if test= "FromEnd/AssociationEnd/@Multiplicity='0..1'">?</xsl:if><xsl:text>&#x20;</xsl:text><xsl:value-of select="$fromEndClass/@Name"/> { get; set;}
	</xsl:for-each>

	<!-- Association 2-->
	<xsl:for-each select="/.//Association[FromEnd/AssociationEnd/@EndModelElement=$iDClass and (ToEnd/AssociationEnd/@Multiplicity='1' or ToEnd/AssociationEnd/@Multiplicity='Unspecified' or ToEnd/AssociationEnd/@Multiplicity='0..1')]">
		<xsl:variable name="toEndClass" select="uFN:typeUMLElement(ToEnd/AssociationEnd/@EndModelElement)"/>
		<xsl:if test= "FromEnd/AssociationEnd/Stereotypes/Stereotype[@Name ='FK']"><xsl:text>&#x9;</xsl:text>public int<xsl:if test= "ToEnd/AssociationEnd/@Multiplicity='0..1'">?</xsl:if><xsl:text>&#x20;</xsl:text><xsl:value-of select="$toEndClass/@Name"/>Id { get; set;}</xsl:if>
		<xsl:if test= "$root/Project/Models/Package/ModelChildren/Class[@Name = $toEndClass/@Name]/Stereotypes/Stereotype[@Name ='NotMapped']">[NotMappedAttribute]</xsl:if>
		public <xsl:value-of select="$toEndClass/@Name"/><xsl:if test= "ToEnd/AssociationEnd/@Multiplicity='0..1'">?</xsl:if><xsl:text>&#x20;</xsl:text><xsl:value-of select="$toEndClass/@Name"/> { get; set;}
	</xsl:for-each>
	
	<!-- Association : Aggregation -->
	<!-- <xsl:for-each select="/.//Association[(FromEnd/AssociationEnd/@EndModelElement=$iDClass and FromEnd/AssociationEnd/@AggregationKind='Composited')]"> -->
		<!-- <xsl:variable name="ToEndClass" select="uFN:typeUMLElement(ToEnd/AssociationEnd/@EndModelElement)"/> -->
		<!-- <xsl:text>&#xa;</xsl:text> -->
		<!-- <xsl:text>&#x9;</xsl:text><xsl:if test= "$root/Project/Models/Package/ModelChildren/Class[@Name = $ToEndClass/@Name]/Stereotypes/Stereotype[@Name ='NotMapped']">[NotMappedAttribute]</xsl:if> -->
		<!-- public <xsl:value-of select="$ToEndClass/@Name"/><xsl:if test= "ToEnd/AssociationEnd/@Multiplicity='0..1'">?</xsl:if><xsl:text>&#x20;</xsl:text><xsl:value-of select="$ToEndClass/@Name"/> { get; set;} -->
	<!-- </xsl:for-each> -->

	<!-- Association : Many to many -->
	<xsl:for-each select="/.//Association[FromEnd/AssociationEnd/@EndModelElement=$iDClass and (FromEnd/AssociationEnd/@Multiplicity='*' or FromEnd/AssociationEnd/@Multiplicity='0..*' or FromEnd/AssociationEnd/@Multiplicity='1..*') and (ToEnd/AssociationEnd/@Multiplicity='*' or ToEnd/AssociationEnd/@Multiplicity='0..*' or ToEnd/AssociationEnd/@Multiplicity='1..*')]">
		<xsl:variable name="toEndClass" select="uFN:typeUMLElement(ToEnd/AssociationEnd/@EndModelElement)"/>
    private Dictionary&#60;Guid, <xsl:value-of select="$toEndClass/@Name"/>&#62; ls<xsl:value-of select="$toEndClass/@Name"/> = new Dictionary&#60;Guid, <xsl:value-of select="$toEndClass/@Name"/>&#62;();
		public Dictionary&#60;Guid, <xsl:value-of select="$toEndClass/@Name"/>&#62; List<xsl:value-of select="$toEndClass/@Name"/>
        	{
            		get { return ls<xsl:value-of select="$toEndClass/@Name"/>; }
            		set { ls<xsl:value-of select="$toEndClass/@Name"/> = value; }
        	}
	</xsl:for-each>

	<!-- Constructeurs -->
		public <xsl:value-of select="$contextName"/>()
		{
			// Needed by EF
		}
		
		public <xsl:value-of select="$contextName"/>(<xsl:for-each select=".//Attribute">
	    <xsl:variable name="attributeType">
			<xsl:if test="not(./Stereotypes/Stereotype[@Name='Embedded'])">
		        <xsl:value-of select="concat(concat(.//DataType/@Name, ./@Type), ./@TypeModifier)"/>
			</xsl:if>
			<xsl:if test="./Stereotypes/Stereotype[@Name='Embedded']">
		        <xsl:variable name="attributeTypeClassRef" select="./Type/Class/@Idref"/>
				<xsl:variable name="attributeTypeClass" select="/Project/Models/Package/ModelChildren/Class[@Id=$attributeTypeClassRef]"/>
				<xsl:value-of select="$attributeTypeClass/@Name"/>
			</xsl:if>
		</xsl:variable>
		<xsl:value-of select="$attributeType"/> <xsl:text>&#x20;</xsl:text> <xsl:value-of select="uFN:first-lower(./@Name)"/><xsl:if test="position()!=last()">,<xsl:text>&#x20;</xsl:text></xsl:if>
	</xsl:for-each>)
		{
			<xsl:for-each select=".//Attribute">
			this.<xsl:value-of select="uFN:first-upper(./@Name)"/> = <xsl:value-of select="uFN:first-lower(./@Name)"/>;
			</xsl:for-each>
		}

	<!-- Operations -->
	<xsl:for-each select=".//Operation[@Name!='créer']">
	    <xsl:value-of select="uFN:operationDefinition(.)"/>
	</xsl:for-each>
 
	}
}	
       </xsl:result-document>
</xsl:template>
</xsl:stylesheet>

