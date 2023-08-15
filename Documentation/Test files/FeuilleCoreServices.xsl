<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"  xmlns:uFN="uFN" version="2.0">
  <xsl:import href="utilFunctions.xsl"/>
	<xsl:output method="text" encoding="iso-8859-1" indent="yes"/>

<xsl:template match="Project/Models/Package/ModelChildren/Class[not (.//AssociationClass) and .//Stereotype[@Name ='Service']]">
       <xsl:variable name="contextName" select="@Name"/>
	   <xsl:variable name="iDClass" select="@Id"/>
	   <xsl:result-document href="{$projectName}/{$projectName}.Core/Services//{$contextName}Service.cs">
using System;
using System.Collections.Generic;
using System.Text;
using System.Threading.Tasks;
using <xsl:value-of select="$projectName"/>.Core.Interfaces;
using <xsl:value-of select="$projectName"/>.Core.Specifications;

namespace <xsl:value-of select="$projectName"/>.Core.Services
{
	public class <xsl:value-of select="$contextName"/>Service : I<xsl:value-of select="$contextName"/>Service
	{
		private readonly I<xsl:value-of select="$contextName"/>Repository _<xsl:value-of select="uFN:first-lower($contextName)"/>Repository;
		
		public <xsl:value-of select="$contextName"/>Service(I<xsl:value-of select="$contextName"/>Repository <xsl:value-of select="uFN:first-lower($contextName)"/>Repository)
		{
			_<xsl:value-of select="uFN:first-lower($contextName)"/>Repository = <xsl:value-of select="uFN:first-lower($contextName)"/>Repository;
		}
	}
}	
       </xsl:result-document>
</xsl:template>
</xsl:stylesheet>

