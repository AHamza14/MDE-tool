<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"  xmlns:uFN="uFN" version="2.0">
  <xsl:import href="utilFunctions.xsl"/>
	<xsl:output method="text" encoding="iso-8859-1" indent="yes"/>

<xsl:template match="Project/Models/Package/ModelChildren/Class[not (.//AssociationClass) and .//Stereotype[@Name ='Service']]">
       <xsl:variable name="contextName" select="@Name"/>
	   <xsl:variable name="iDClass" select="@Id"/>
	   <xsl:result-document href="{$projectName}/{$projectName}.Core/Interfaces//I{$contextName}Service.cs">
using System;
using System.Collections.Generic;
using System.Text;
using System.Threading.Tasks;

namespace <xsl:value-of select="$projectName"/>.Core.Interfaces
{
	public interface I<xsl:value-of select="$contextName"/>Service
	{
	
	}
}	
       </xsl:result-document>
</xsl:template>
</xsl:stylesheet>

