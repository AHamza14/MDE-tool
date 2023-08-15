<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"  xmlns:uFN="uFN" version="2.0">
  <xsl:import href="utilFunctions.xsl"/>
	<!-- <xsl:output method="text" encoding="iso-8859-1" indent="yes"/> -->
	<xsl:output method="text" encoding="UTF-8" indent="yes"/>

<xsl:template match="Project/Models/Package/ModelChildren/Class[not (.//AssociationClass) and .//Stereotype[@Name ='Repository']]">
       <xsl:variable name="contextName" select="@Name"/>
	   <xsl:variable name="iDClass" select="@Id"/>
	   <xsl:result-document href="{$projectName}/{$projectName}.Infrastructure/Repositories//{$contextName}Repository.cs">
using System.Text;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.EntityFrameworkCore;
using <xsl:value-of select="$projectName"/>.Core;
using <xsl:value-of select="$projectName"/>.Core.Interfaces;

namespace <xsl:value-of select="$projectName"/>.Infrastructure.Repositories
{
	public class <xsl:value-of select="$contextName"/>Repository : EfRepository&#60;<xsl:value-of select="$contextName"/>&#62;, I<xsl:value-of select="$contextName"/>Repository
	{
		public <xsl:value-of select="$contextName"/>Repository(<xsl:value-of select="$projectName"/>Context <xsl:value-of select="uFN:first-lower($projectName)"/>Context) : base(<xsl:value-of select="uFN:first-lower($projectName)"/>Context)
		{
		}
		
	<!-- GetByIdWith Operation FromEnd -->
	<xsl:for-each select="/.//Association[ToEnd/AssociationEnd/@EndModelElement=$iDClass and (FromEnd/AssociationEnd/@Multiplicity='1' or FromEnd/AssociationEnd/@Multiplicity='Unspecified' or FromEnd/AssociationEnd/@Multiplicity='0..1')]">
		<xsl:variable name="fromEndClass" select="uFN:typeUMLElement(FromEnd/AssociationEnd/@EndModelElement)"/>
		public Task&#60;<xsl:value-of select="$contextName"/>&#62; GetByIdWith<xsl:value-of select="$fromEndClass/@Name"/>Async(int id)
		{
			return _<xsl:value-of select="$projectName"/>Context.<xsl:value-of select="$contextName"/>s
				.Include(c => c.<xsl:value-of select="$fromEndClass/@Name"/>)
				.FirstOrDefaultAsync(c => c.Id == id);
		}
	</xsl:for-each>
	
	<!-- GetByIdWith Operation ToEnd -->
	<xsl:for-each select="/.//Association[FromEnd/AssociationEnd/@EndModelElement=$iDClass and (ToEnd/AssociationEnd/@Multiplicity='1' or ToEnd/AssociationEnd/@Multiplicity='Unspecified' or ToEnd/AssociationEnd/@Multiplicity='0..1')]">
		<xsl:variable name="toEndClass" select="uFN:typeUMLElement(ToEnd/AssociationEnd/@EndModelElement)"/>
		public Task&#60;<xsl:value-of select="$contextName"/>&#62; GetByIdWith<xsl:value-of select="$toEndClass/@Name"/>Async(int id)
		{
			return _<xsl:value-of select="$projectName"/>Context.<xsl:value-of select="$contextName"/>s
				.Include(c => c.<xsl:value-of select="$toEndClass/@Name"/>)
				.FirstOrDefaultAsync(c => c.Id == id);
		}
	</xsl:for-each>
	}
}	
       </xsl:result-document>
</xsl:template>
</xsl:stylesheet>

