<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"  xmlns:uFN="uFN" version="2.0">
  <xsl:import href="utilFunctions.xsl"/>
	<xsl:output method="text" encoding="iso-8859-1" indent="yes"/>
<xsl:template match="Project/Models/Package/ModelChildren">
	   <xsl:result-document href="{$projectName}/{$projectName}.Infrastructure/{$projectName}Conetxt.cs">
using System;
using System.Collections.Generic;
using System.Text;
using System.Threading.Tasks;
using Microsoft.EntityFrameworkCore;
using <xsl:value-of select="$projectName"/>.Core;

namespace <xsl:value-of select="$projectName"/>.Infrastructure
{
	public class <xsl:value-of select="$projectName"/>Context : DbContext
	{
	<xsl:for-each select="./Class[.//Stereotype[@Name ='Entity']]">
		<xsl:variable name="entityName" select="./@Name"/>
		public DbSet&#60;<xsl:value-of select="$entityName"/>&#62; <xsl:value-of select="$entityName"/>s { get; set; }
	</xsl:for-each>
	
		public <xsl:value-of select="$projectName"/>Conetxt(DbContextOptions options) : base(options) { }
		
		public <xsl:value-of select="$projectName"/>Context() : base(new DbContextOptionsBuilder&#60;<xsl:value-of select="$projectName"/>Context&#62;()
						.UseSqlServer(@"Server=.;Database=<xsl:value-of select="$projectName"/>DB;Trusted_Connection=True;")
						.Options)
		{ }
	}
}	
       </xsl:result-document>
</xsl:template>
</xsl:stylesheet>

