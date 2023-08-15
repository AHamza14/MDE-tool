<?xml version = '1.0' encoding = 'utf-8' ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:uFN="uFN" version="2.0">

<xsl:variable name="root" select="/"/>
<xsl:variable name="projectName" select="/Project/@Name"/>
<xsl:key name="UMLElement" match="node()[@Id]" use="@Id"/>
<xsl:key name="UMLReference" match="node()[@Idref]" use="@Idref"/>

<xsl:function name="uFN:name">
  <xsl:param name="context"/>
  <xsl:variable name="contextName" select="name($context)"/>
  <xsl:choose>
    <xsl:when test="$contextName = 'Project'">
      <xsl:sequence select="$context/@Name"/>
    </xsl:when>
    <xsl:when test="$contextName = 'Class'">
      <xsl:sequence select="$context/@Name"/>
    </xsl:when>
    <xsl:when test="$contextName = 'DataType'">
      <xsl:sequence select="$context/@Name"/>
    </xsl:when>
    <xsl:when test="$contextName = 'Attribute'">
      <xsl:sequence select="$context/@Name"/>
    </xsl:when>
    <xsl:when test="$contextName = 'Association'">
      <xsl:sequence select="$context/@Name"/>
    </xsl:when>
    <xsl:when test="$contextName = 'AssociationEnd'">
      <xsl:sequence select="$context/@Name"/>
    </xsl:when>
    <xsl:when test="$contextName = 'Stereotype'">
      <xsl:sequence select="$context/@Name"/>
    </xsl:when>
  </xsl:choose>
</xsl:function>

<xsl:function name="uFN:parent">
  <xsl:param name="context"/>
  <xsl:variable name="toGenralizationRef" select="$context/ToSimpleRelationships/Generalization/@Idref"/>
  <xsl:sequence select="$root/Project/Models/Package/ModelChildren/Class[FromSimpleRelationships/Generalization/@Idref=$toGenralizationRef]"/>
</xsl:function>

<xsl:function name="uFN:all-parents">
  <xsl:param name="context"/>
  <xsl:for-each select="$context">
    <xsl:variable name="single-context" select="."/>
    <xsl:variable name="contextName" select="name($single-context)"/>
    <xsl:choose>
      <xsl:when test="$contextName = 'Class'">
        <xsl:sequence select="uFN:all-parents(uFN:parent($single-context)) | uFN:parent($single-context)"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:message><xsl:value-of select="concat('Pas de parent pour ', $contextName)"/></xsl:message>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:for-each>
</xsl:function>

<xsl:function name="uFN:Attribute">
  <xsl:param name="context"/>
  <xsl:sequence select="$context//Attribute"/>
</xsl:function>

<xsl:function name="uFN:AttributeInherit">
  <xsl:param name="context"/>
  <xsl:variable name="contextName" select="name($context)"/>
  <xsl:choose>
    <xsl:when test="not($context)">
      <xsl:sequence select="()"/>
    </xsl:when>
    <xsl:when test="$contextName = 'Class'">
      <xsl:sequence select="uFN:AttributeInherit(uFN:parent($context)) | uFN:Attribute($context)"/>
    </xsl:when>
  </xsl:choose>
</xsl:function>

<xsl:function name="uFN:Association">
  <xsl:param name="context"/>
  <xsl:variable name="iDClass" select="$context/@Id"/>
  <xsl:sequence select="$root/.//Association[ToEnd/AssociationEnd/@EndModelElement=$iDClass or FromEnd/AssociationEnd/@EndModelElement=$iDClass]"/>
</xsl:function>

<xsl:function name="uFN:AssociationInherit">
  <xsl:param name="context"/>
  <xsl:variable name="contextName" select="name($context)"/>
  <xsl:choose>
    <xsl:when test="not($context)">
      <xsl:sequence select="()"/>
    </xsl:when>
    <xsl:when test="$contextName = 'Class'">
      <xsl:sequence select="uFN:AssociationInherit(uFN:parent($context)) | uFN:Association($context)"/>
    </xsl:when>
    <xsl:otherwise>
      <xsl:message><xsl:value-of select="concat('Pas de Association pour ', $contextName)"/></xsl:message>
    </xsl:otherwise>
  </xsl:choose>
</xsl:function>


<xsl:function name="uFN:typeUMLElement">
    <xsl:param name="context"/>
    <xsl:variable name="contextName" select="name($context)"/>
    <xsl:choose>
      <xsl:when test="$contextName = 'AssociationClass'">
          <xsl:sequence select="$root/key('UMLElement', $context/@Idref)"/>
      </xsl:when>
      <xsl:when test="$contextName = 'Operation'">
        <xsl:if test="$context[ReturnType/Class]">
          <xsl:sequence select="$root/key('UMLElement', $context/ReturnType/Class/@Idref)"/>
        </xsl:if>
        <xsl:if test="$context[ReturnType/DataType]">
          <xsl:sequence select="$root/key('UMLElement', $context/ReturnType/DataType/@Idref)"/>
        </xsl:if>
        <xsl:if test="not($context[ReturnType])">
          <xsl:value-of select="()"/>
        </xsl:if>
      </xsl:when>
      <xsl:when test="$contextName = 'Parameter'">
        <xsl:if test="$context[Type/Class]">
          <xsl:sequence select="$root/key('UMLElement', $context/Type/Class/@Idref)"/>
        </xsl:if>
        <xsl:if test="$context[Type/DataType]">
          <xsl:sequence select="$root/key('UMLElement', $context/Type/DataType/@Idref)"/>
        </xsl:if>
        <xsl:if test="not($context[Type])">
          <xsl:value-of select="()"/>
        </xsl:if>
      </xsl:when>
      <xsl:otherwise>
          <xsl:sequence select="$root/key('UMLElement', $context)"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:function>

  <xsl:function name="uFN:typeUMLElementName">
    <xsl:param name="context"/>
    <xsl:variable name="contextName" select="name($context)"/>
    <xsl:choose>
      <xsl:when test="$contextName = 'Operation'">
        <xsl:if test="$context[ReturnType]">
          <xsl:variable name="returnTypeElement" select="uFN:typeUMLElement($context)"/>
          <xsl:value-of select="$returnTypeElement/@Name"/>
        </xsl:if>
        <xsl:if test="not($context[ReturnType])">
          <xsl:value-of select="$context/@ReturnType"/>
        </xsl:if>
      </xsl:when>
      <xsl:when test="$contextName = 'Parameter'">
        <xsl:if test="$context[Type]">
          <xsl:variable name="typeElement" select="uFN:typeUMLElement($context)"/>
          <xsl:value-of select="$typeElement/@Name"/>
        </xsl:if>
        <xsl:if test="not($context[Type])">
          <xsl:value-of select="$context/@Type"/>
        </xsl:if>
      </xsl:when>
    </xsl:choose>
  </xsl:function>

  <xsl:function name="uFN:operationDefinition">
    <xsl:param name="context"/>
    <xsl:value-of select="$context/@Visibility"/><xsl:text>&#032;</xsl:text><xsl:if test="$context/@Abstract = 'true'">abstract<xsl:text>&#032;</xsl:text></xsl:if><xsl:if test="$context/@Static = 'true'">static<xsl:text>&#032;</xsl:text></xsl:if><xsl:value-of select="uFN:typeUMLElementName($context)"/><xsl:value-of select="$context/@TypeModifier"/><xsl:text>&#032;</xsl:text><xsl:value-of select="$context/@Name"/>(<xsl:value-of select="uFN:operationSignature($context)"/>)
    <xsl:variable name="returnTypeElementName" select="uFN:typeUMLElementName($context)"/>
    {
    // Add your business logic code of the operation

    <xsl:if test="$returnTypeElementName != 'void'">return <xsl:value-of select="uFN:default-init($returnTypeElementName)"/>;</xsl:if>
    }
  </xsl:function>
  <xsl:function name="uFN:operationSignature">
    <xsl:param name="context"/>
    <xsl:for-each select="$context/ModelChildren/Parameter">
      <xsl:value-of select="uFN:operationParameterDeclaration(.)"/>
      <xsl:if test="position()!=last()">, </xsl:if>

    </xsl:for-each>
  </xsl:function>
  <xsl:function name="uFN:operationParameterDeclaration">
    <xsl:param name="context"/>
    <xsl:if test="$context/@Direction = 'inout' or $context/@Direction = 'out'">ref<xsl:text>&#032;</xsl:text></xsl:if><xsl:value-of select="uFN:typeUMLElementName($context)"/><xsl:value-of select="$context/@TypeModifier"/><xsl:text>&#032;</xsl:text><xsl:value-of select="$context/@Name"/>
  </xsl:function>

<xsl:function name="uFN:classStereotypes">
    <xsl:param name="context"/>
	<xsl:for-each select="$context/Stereotypes/Stereotype[@Name!='NotMapped' and @Name!='Repository' and @Name!='NotImplemented']">
      <xsl:if test="position()=1"> : </xsl:if><xsl:value-of select="uFN:stereotypeInterface(./@Name)"/><xsl:if test="position()!=last()">, </xsl:if>
    </xsl:for-each>
  </xsl:function>
  
  <xsl:function name="uFN:stereotypeInterface">
  <xsl:param name="value"/>
  <xsl:choose>
    <xsl:when test="$value = 'AggregateRoot'">
      <xsl:sequence select="'IAggregateRoot'"/>
    </xsl:when>
    <xsl:when test="$value = 'Entity'">
      <xsl:sequence select="'BaseEntity'"/>
    </xsl:when>
	<xsl:when test="$value = 'Subject'">
      <xsl:sequence select="'Subject'"/>
    </xsl:when>
	<xsl:when test="$value = 'Observer'">
      <xsl:sequence select="'Observer'"/>
    </xsl:when>
  </xsl:choose>
</xsl:function>

  <xsl:function name="uFN:classAnnotations">
    <xsl:param name="context"/>
	<xsl:for-each select="$context/Stereotypes/Stereotype[@Name!='AggregateRoot' and @Name!='Entity' and @Name!='Repository' and @Name!='Subject' and @Name!='Observer']">
      [<xsl:value-of select="uFN:stereotypeAnnotation(./@Name)"/>]
    </xsl:for-each>
  </xsl:function>
  
  <xsl:function name="uFN:stereotypeAnnotation">
  <xsl:param name="value"/>
  <xsl:choose>
    <xsl:when test="$value = 'NotMapped'">
      <xsl:sequence select="'NotMapped'"/>
    </xsl:when>
    <xsl:when test="$value = 'NotImplemented'">
      <xsl:sequence select="'NotImplemented'"/>
    </xsl:when>
  </xsl:choose>
</xsl:function>

  <xsl:function name="uFN:enumerations">
    <xsl:param name="context"/>
	<xsl:for-each select="$context//EnumerationLiteral">
      <xsl:value-of select="@Name"/> =  <xsl:value-of select="position() - 1"/><xsl:if test="position()!=last()">, </xsl:if>
    </xsl:for-each>
  </xsl:function>

  <xsl:function name="uFN:sqlColumnDeclarationFromAttributes">
    <xsl:param name="context"/>
    <xsl:for-each select="uFN:AttributeInherit($context)">
      <xsl:if test="not(./Stereotypes/Stereotype[@Name='Embedded'])">
        <xsl:variable name="attributeType" select="concat(concat(.//DataType/@Name, ./@Type), ./@TypeModifier)"/>
        <xsl:value-of select="uFN:first-lower(@Name)"/>
        <xsl:text>&#032;</xsl:text>
        <xsl:value-of select="uFN:database-type($attributeType)"/>
        <xsl:text>, </xsl:text>
        <xsl:text>&#xD;</xsl:text>
      </xsl:if>
      <xsl:if test="./Stereotypes/Stereotype[@Name='Embedded']">
        <xsl:variable name="attributeTypeClass" select="uFN:typeUMLElement(.)"/>
        <xsl:for-each select="uFN:AttributeInherit($attributeTypeClass)">
          <xsl:variable name="attributeType" select="concat(concat(.//DataType/@Name, ./@Type), ./@TypeModifier)"/>
          <xsl:value-of select="uFN:first-lower(@Name)"/>
          <xsl:text>&#032;</xsl:text>
          <xsl:value-of select="uFN:database-type($attributeType)"/>
          <xsl:text>, </xsl:text>
          <xsl:text>&#xD;</xsl:text>
        </xsl:for-each>
      </xsl:if>
    </xsl:for-each>
  </xsl:function>
  


  <xsl:function name="uFN:sqlColumnDeclarationFromAssociations">
    <xsl:param name="context"/>
    <xsl:variable name="iDClass" select="$context/@Id"/>
    <xsl:for-each select="$root/.//Association/ToEnd/AssociationEnd[@EndModelElement=$iDClass and (../../FromEnd/AssociationEnd/@Multiplicity='1' or ../../FromEnd/AssociationEnd/@Multiplicity='Unspecified' or../../FromEnd/AssociationEnd/@Multiplicity='0..1')]">
      <xsl:variable name="fromEndId" select="../../FromEnd/AssociationEnd/@EndModelElement"/>
      <xsl:variable name="fromEndClassName" select="$root/.//Class[@Id=$fromEndId]/@Name"/>
      <xsl:value-of select="concat('ID', $fromEndClassName)"/>
      <xsl:text>&#032; uniqueidentifier NOT NULL, </xsl:text>
      <xsl:text>&#xD;</xsl:text>
    </xsl:for-each>
  </xsl:function>
  
    
 <xsl:function name="uFN:fillFieldsInitilizationForAttributes">
    <xsl:param name="context"/>
   <xsl:variable name="contextName" select="$context/@Name"/>
    <xsl:for-each select="uFN:AttributeInherit($context)">
      <xsl:if test="not(./Stereotypes/Stereotype[@Name='Embedded'])">
        <xsl:variable name="attributeType" select="concat(concat(.//DataType/@Name, ./@Type), ./@TypeModifier)"/>
        l<xsl:value-of select="$contextName"/>.<xsl:value-of select="uFN:first-upper(@Name)"/> = (<xsl:value-of select="$attributeType"/>)pDataRow["<xsl:value-of select="uFN:first-upper(@Name)"/>"];
      </xsl:if>
      <xsl:if test="./Stereotypes/Stereotype[@Name='Embedded']">
        <xsl:variable name="embeddedAttributeName" select="./@Name"/>
        <xsl:variable name="attributeTypeClass" select="uFN:typeUMLElement(.)"/>
        <xsl:for-each select="uFN:AttributeInherit($attributeTypeClass)">
          <xsl:variable name="attributeType" select="concat(concat(.//DataType/@Name, ./@Type), ./@TypeModifier)"/>
          l<xsl:value-of select="$contextName"/>.<xsl:value-of select="uFN:first-upper($embeddedAttributeName)"/>.<xsl:value-of select="uFN:first-upper(@Name)"/> = (<xsl:value-of select="$attributeType"/>)pDataRow["<xsl:value-of select="uFN:first-upper(@Name)"/>"];
        </xsl:for-each>
      </xsl:if>
    </xsl:for-each>
  </xsl:function>


  <xsl:function name="uFN:fillFieldsInitilizationForAssociations">
    <xsl:param name="context"/>
    <xsl:variable name="contextName" select="$context/@Name"/>
    <xsl:variable name="inheritedAssociations" select="uFN:AssociationInherit($context)"/>
    <xsl:for-each select="$inheritedAssociations[ToEnd/AssociationEnd/@EndModelElement=$context/@Id and (FromEnd/AssociationEnd/@Multiplicity='1' or FromEnd/AssociationEnd/@Multiplicity='Unspecified' or FromEnd/AssociationEnd/@Multiplicity='0..1')]">
        <xsl:variable name="fromEndClass" select="uFN:typeUMLElement(FromEnd/AssociationEnd/@EndModelElement)"/>
        if (pDataRow["ID<xsl:value-of select="$fromEndClass/@Name"/>"].GetType() != typeof(System.DBNull))
        l<xsl:value-of select="$contextName"/>.ID<xsl:value-of select="$fromEndClass/@Name"/> = new Guid(pDataRow["ID<xsl:value-of select="$fromEndClass/@Name"/>"].ToString());
    </xsl:for-each>
  </xsl:function>


  <xsl:function name="uFN:findAllPourUnMapperForAssociations">
    <xsl:param name="context"/>
    <xsl:variable name="inheritedAssociations" select="uFN:AssociationInherit($context)"/>
    <xsl:for-each select="$inheritedAssociations[ToEnd/AssociationEnd/@EndModelElement=$context/@Id and (FromEnd/AssociationEnd/@Multiplicity='1' or FromEnd/AssociationEnd/@Multiplicity='Unspecified' or FromEnd/AssociationEnd/@Multiplicity='0..1')]">
      <xsl:variable name="fromEndClass" select="uFN:typeUMLElement(FromEnd/AssociationEnd/@EndModelElement)"/>
      <xsl:if test="ToEnd/AssociationEnd/@Multiplicity='*' or ToEnd/AssociationEnd/@Multiplicity='0..*' or ToEnd/AssociationEnd/@Multiplicity='1..*'">
        //--------------------------------------------------------------------
        public DataTable FindAllPourUn<xsl:value-of select="$fromEndClass/@Name"/>(Guid p<xsl:value-of select="$fromEndClass/@Name"/>Id)
        {
        return DataBase.Select("SELECT * FROM tbl<xsl:value-of select="$context/@Name"/> WHERE ID<xsl:value-of select="$fromEndClass/@Name"/>='" + p<xsl:value-of select="$fromEndClass/@Name"/>Id.ToString() + "'");
        }
      </xsl:if>
    </xsl:for-each>
  </xsl:function>


  <xsl:function name="uFN:FindInsertAllMapperForAssociations">
    <xsl:param name="context"/>
    <xsl:variable name="contextName" select="$context/@Name"/>
    <xsl:variable name="inheritedAssociations" select="uFN:AssociationInherit($context)"/>
    <xsl:for-each select="$inheritedAssociations/FromEnd/AssociationEnd[@EndModelElement=$context/@Name and (FromEnd/AssociationEnd/@Multiplicity='*' or FromEnd/AssociationEnd/@Multiplicity='0..*' or FromEnd/AssociationEnd/@Multiplicity='1..*') and (ToEnd/AssociationEnd/@Multiplicity='*' or ToEnd/AssociationEnd/@Multiplicity='0..*' or ToEnd/AssociationEnd/@Multiplicity='1..*')]">
      <xsl:variable name="fromEndClass" select="uFN:typeUMLElement(FromEnd/AssociationEnd/@EndModelElement)"/>
      <xsl:variable name="toEndClass" select="uFN:typeUMLElement(ToEnd/AssociationEnd/@EndModelElement)"/>
      //--------------------------------------------------------------------
      public DataTable FindAll<xsl:value-of select="$toEndClass/@Name"/>Table(<xsl:value-of select="$contextName"/> p<xsl:value-of select="$contextName"/>)
      {
      return DataBase.Select("SELECT * FROM tbl<xsl:value-of select="$contextName"/><xsl:value-of select="$toEndClass/@Name"/> WHERE ID<xsl:value-of select="$contextName"/> = '" + p<xsl:value-of select="$contextName"/>.ID.ToString() + "'");
      }

      //--------------------------------------------------------------------
      public void FindAll<xsl:value-of select="$toEndClass/@Name"/>(ref <xsl:value-of select="$contextName"/> p<xsl:value-of select="$contextName"/>)
      {
      DataTable ldt<xsl:value-of select="$toEndClass/@Name"/> = FindAll<xsl:value-of select="$toEndClass/@Name"/>Table(p<xsl:value-of select="$contextName"/>);
      p<xsl:value-of select="$contextName"/>.List<xsl:value-of select="$toEndClass/@Name"/>.Clear();
      if (Util.isNULL(ldt<xsl:value-of select="$toEndClass/@Name"/>)) return;
      if (ldt<xsl:value-of select="$toEndClass/@Name"/>.Rows.Count == 0) return;
      DataRow ldr<xsl:value-of select="$toEndClass/@Name"/>;
      IEnumerator l<xsl:value-of select="$toEndClass/@Name"/>Enumerator = ldt<xsl:value-of select="$toEndClass/@Name"/>.Rows.GetEnumerator();
      <xsl:value-of select="$toEndClass/@Name"/> l<xsl:value-of select="$toEndClass/@Name"/>;
      <xsl:value-of select="$toEndClass/@Name"/>Mapper l<xsl:value-of select="$toEndClass/@Name"/>Mapper = new <xsl:value-of select="$toEndClass/@Name"/>Mapper();
      while (l<xsl:value-of select="$toEndClass/@Name"/>Enumerator.MoveNext())
      {
      ldr<xsl:value-of select="$toEndClass/@Name"/> = (DataRow)l<xsl:value-of select="$toEndClass/@Name"/>Enumerator.Current;
      l<xsl:value-of select="$toEndClass/@Name"/> = l<xsl:value-of select="$toEndClass/@Name"/>Mapper.Find(new Guid(ldr<xsl:value-of select="$toEndClass/@Name"/>["ID<xsl:value-of select="$toEndClass/@Name"/>"].ToString()));
      p<xsl:value-of select="$contextName"/>.List<xsl:value-of select="$toEndClass/@Name"/>.Add(l<xsl:value-of select="$toEndClass/@Name"/>.ID, l<xsl:value-of select="$toEndClass/@Name"/>);
      }

      }

      //--------------------------------------------------------------------
      public int InsertAll<xsl:value-of select="$toEndClass/@Name"/>(<xsl:value-of select="$contextName"/> p<xsl:value-of select="$contextName"/>)
      {

      <xsl:variable name="tableName" select="concat(tbl, concat($contextName, $toEndClass/@Name))"/>
      int liErreur = DataBase.DeleteRecords("<xsl:value-of select="$tableName"/>", "ID<xsl:value-of select="$contextName"/> = '" + p<xsl:value-of select="$contextName"/>.ID.ToString() + "'");

      if (p<xsl:value-of select="$contextName"/>.List<xsl:value-of select="$toEndClass/@Name"/>.Count == 0) return liErreur;

      IDictionaryEnumerator lDictionaryEnumerator = p<xsl:value-of select="$contextName"/>.List<xsl:value-of select="$toEndClass/@Name"/>.GetEnumerator();
      int liI = 0;
      string[] lsCols = new string[2];
      string[] lsValues = new string[2];

      lsCols[0] = "ID<xsl:value-of select="$contextName"/>";
      lsCols[1] = "ID<xsl:value-of select="$toEndClass/@Name"/>";
      while (lDictionaryEnumerator.MoveNext())
      {
      lsValues[0] = "'" + p<xsl:value-of select="$contextName"/>.ID.ToString() + "'";
      lsValues[1] = "'" + ((<xsl:value-of select="$toEndClass/@Name"/>)lDictionaryEnumerator.Value).ID.ToString() + "'";
      if (DataBase.Insert("<xsl:value-of select="$toEndClass/@Name"/>", lsCols, lsValues) == liErreur) liI = liErreur;
      if (liI != liErreur) liI += 1;
      }
      return liI;
      }
    </xsl:for-each>
  </xsl:function>



  <xsl:function name="uFN:findAllPourUnInterfaceMapperForAssociations">
    <xsl:param name="context"/>
    <xsl:variable name="contextName" select="$context/@Name"/>
    <xsl:variable name="inheritedAssociations" select="uFN:AssociationInherit($context)"/>
    <xsl:for-each select="$inheritedAssociations[ToEnd/AssociationEnd/@EndModelElement=$context/@Id and (FromEnd/AssociationEnd/@Multiplicity='1' or FromEnd/AssociationEnd/@Multiplicity='Unspecified' or FromEnd/AssociationEnd/@Multiplicity='0..1')]">
      <xsl:variable name="fromEndClass" select="uFN:typeUMLElement(FromEnd/AssociationEnd/@EndModelElement)"/>
      <xsl:if test="ToEnd/AssociationEnd/@Multiplicity='*' or ToEnd/AssociationEnd/@Multiplicity='0..*' or ToEnd/AssociationEnd/@Multiplicity='1..*'">
    DataTable FindAllPourUn<xsl:value-of select="$fromEndClass/@Name"/>(Guid p<xsl:value-of select="$fromEndClass/@Name"/>Id);
      </xsl:if>
    </xsl:for-each>
  </xsl:function>

    <xsl:function name="uFN:findInsertAllInterfaceMapperForAssociations">
    <xsl:param name="context"/>
    <xsl:variable name="contextName" select="$context/@Name"/>
    <xsl:variable name="inheritedAssociations" select="uFN:AssociationInherit($context)"/>
    <xsl:for-each select="$inheritedAssociations/FromEnd/AssociationEnd[@EndModelElement=$context/@Name and (FromEnd/AssociationEnd/@Multiplicity='*' or FromEnd/AssociationEnd/@Multiplicity='0..*' or FromEnd/AssociationEnd/@Multiplicity='1..*') and (ToEnd/AssociationEnd/@Multiplicity='*' or ToEnd/AssociationEnd/@Multiplicity='0..*' or ToEnd/AssociationEnd/@Multiplicity='1..*')]">
      <xsl:variable name="fromEndClass" select="uFN:typeUMLElement(FromEnd/AssociationEnd/@EndModelElement)"/>
      <xsl:variable name="toEndClass" select="uFN:typeUMLElement(ToEnd/AssociationEnd/@EndModelElement)"/>
      
      DataTable FindAll<xsl:value-of select="$toEndClass/@Name"/>Table(<xsl:value-of select="$contextName"/> p<xsl:value-of select="$contextName"/>);
      void FindAll<xsl:value-of select="$toEndClass/@Name"/>(ref <xsl:value-of select="$contextName"/> p<xsl:value-of select="$contextName"/>);
      int InsertAll<xsl:value-of select="$toEndClass/@Name"/>(<xsl:value-of select="$contextName"/> p<xsl:value-of select="$contextName"/>);
    </xsl:for-each>
  </xsl:function> 
  <xsl:function name="uFN:fillArrayLiNombreForAttributes">
    <xsl:param name="context"/>
    <xsl:variable name="contextName" select="$context/@Name"/>
    <xsl:variable name="inheritedAttributes" select="uFN:AttributeInherit($context)"/>
    <xsl:variable name="inheritedAssociations" select="uFN:AssociationInherit($context)"/>
  
    int liNombre = <xsl:value-of select="count($inheritedAttributes[not(Stereotypes/Stereotype[@Name='Embedded'])])"/> + <xsl:value-of select="count($inheritedAssociations[ToEnd/AssociationEnd/@EndModelElement=$context/@Id and (FromEnd/AssociationEnd/@Multiplicity='1' or FromEnd/AssociationEnd/@Multiplicity='0..1' or FromEnd/AssociationEnd/@Multiplicity='Unspecified')])"/>;
	  <xsl:for-each select="$inheritedAttributes">
	      <xsl:if test="./Stereotypes/Stereotype[@Name='Embedded']">
	          <xsl:variable name="attributeTypeClass" select="uFN:typeUMLElement(.)"/>
	          <xsl:for-each select="$attributeTypeClass//Attribute">liNombre++;</xsl:for-each>
        </xsl:if>
	  </xsl:for-each> 
  </xsl:function>


  <xsl:function name="uFN:fillArrayForAnAttribute">
    <xsl:param name="context"/>
    <xsl:param name="contextType"/>
    <xsl:param name="parentContextName"/>
    pCols[liCpt] = "<xsl:value-of select="uFN:first-upper($context/@Name)"/>";
    <xsl:choose>
      <xsl:when test="$contextType = 'bool'  or $contextType = 'Boolean' or $contextType = 'int' or $contextType = 'float' or $contextType = 'double' or $contextType = 'decimal' or $contextType = 'DateTime'">
        pValues[liCpt++] = "'" + p<xsl:value-of select="$parentContextName"/>.<xsl:value-of select="uFN:first-upper($context/@Name)"/>.ToString() + "'";
      </xsl:when>
      <xsl:when test="$contextType = 'byte[]'">
        pValues[liCpt++] = "0x" + BitConverter.ToString(p<xsl:value-of select="$parentContextName"/>.<xsl:value-of select="uFN:first-upper($context/@Name)"/>).Replace("-", "");
      </xsl:when>
      <xsl:otherwise>
        pValues[liCpt++] = "'" + Util.DoubleQuote(p<xsl:value-of select="$parentContextName"/>.<xsl:value-of select="uFN:first-upper($context/@Name)"/>) + "'";
      </xsl:otherwise>
    </xsl:choose>
  </xsl:function>
  
  
  <xsl:function name="uFN:fillArrayForAttributes">
    <xsl:param name="context"/>
    <xsl:variable name="contextName" select="$context/@Name"/>
    <xsl:variable name="inheritedAttributes" select="uFN:AttributeInherit($context)"/>

    <xsl:for-each select="$inheritedAttributes">
      <xsl:if test="not(./Stereotypes/Stereotype[@Name='Embedded'])">
        <xsl:variable name="attributeType" select="concat(concat(.//DataType/@Name, ./@Type), ./@TypeModifier)"/>
        <xsl:value-of select="uFN:fillArrayForAnAttribute(., $attributeType, $contextName)"/>
      </xsl:if>
      <xsl:if test="./Stereotypes/Stereotype[@Name='Embedded']">
        <xsl:variable name="embeddedAttributeName" select="./@Name"/>
        <xsl:variable name="attributeTypeClass" select="uFN:typeUMLElement(./Type/Class/@Idref)"/>
        <xsl:for-each select="$attributeTypeClass//Attribute">
          <xsl:variable name="attributeType" select="concat(concat(.//DataType/@Name, ./@Type), ./@TypeModifier)"/>
          <xsl:value-of select="uFN:fillArrayForAnAttribute(., $attributeType, $embeddedAttributeName)"/>
        </xsl:for-each>
      </xsl:if>
    </xsl:for-each>
  </xsl:function>



  <xsl:function name="uFN:fillArrayForAssociations">
    <xsl:param name="context"/>
    <xsl:variable name="contextName" select="$context/@Name"/>
    <xsl:variable name="inheritedAssociations" select="uFN:AssociationInherit($context)"/>
    <xsl:for-each select="$inheritedAssociations[ToEnd/AssociationEnd/@EndModelElement=$context/@Id and (FromEnd/AssociationEnd/@Multiplicity='1' or FromEnd/AssociationEnd/@Multiplicity='0..1' or FromEnd/AssociationEnd/@Multiplicity='Unspecified')]">
      <xsl:variable name="fromEndClass" select="uFN:typeUMLElement(FromEnd/AssociationEnd/@EndModelElement)"/>
      if (!Util.isNULL(p<xsl:value-of select="$contextName"/>.ID<xsl:value-of select="$fromEndClass/@Name"/>))
      {
        pCols[liCpt] = "ID<xsl:value-of select="$fromEndClass/@Name"/>";
        pValues[liCpt++] = "'" + p<xsl:value-of select="$contextName"/>.ID<xsl:value-of select="$fromEndClass/@Name"/>.ToString() + "'";
      }
    </xsl:for-each>
  </xsl:function>



  <xsl:function name="uFN:deleteMapperDefinitionForAssociations">
    <xsl:param name="context"/>
    <xsl:variable name="contextName" select="$context/@Name"/>
    <xsl:variable name="inheritedAssociations" select="uFN:AssociationInherit($context)"/>
    <xsl:for-each select="$inheritedAssociations[FromEnd/AssociationEnd/@EndModelElement=$context/@Id and (FromEnd/AssociationEnd/@Multiplicity='*' or FromEnd/AssociationEnd/@Multiplicity='0..*' or FromEnd/AssociationEnd/@Multiplicity='1..*') and (ToEnd/AssociationEnd/@Multiplicity='*' or ToEnd/AssociationEnd/@Multiplicity='0..*' or ToEnd/AssociationEnd/@Multiplicity='1..*')]">
      <xsl:variable name="fromEndClass" select="uFN:typeUMLElement(FromEnd/AssociationEnd/@EndModelElement)"/>
      <xsl:variable name="toEndClass" select="uFN:typeUMLElement(ToEnd/AssociationEnd/@EndModelElement)"/>
      <xsl:variable name="tableName" select="concat($fromEndClass/@Name, concat(uFN:first-upper(@Name), $toEndClass/@Name))"/>
      liTemp = DataBase.Delete("DELETE FROM tbl<xsl:value-of select="$tableName"/>  WHERE ID<xsl:value-of select="$contextName"/> ='" + p<xsl:value-of select="$contextName"/>.ID + "'");
      if (liTemp == liErreur)
      liI = liErreur;
      if (liI != liErreur)
      liI += liTemp;
    </xsl:for-each>
    <xsl:for-each select="$inheritedAssociations[FromEnd/AssociationEnd/@EndModelElement=$context/@Id and (ToEnd/AssociationEnd/@Multiplicity='*' or ToEnd/AssociationEnd/@Multiplicity='0..*' or ToEnd/AssociationEnd/@Multiplicity='1..*') and (FromEnd/AssociationEnd/@Multiplicity='1' or FromEnd/AssociationEnd/@Multiplicity='0..1' or FromEnd/AssociationEnd/@Multiplicity='unspecified')]">
      <xsl:variable name="toEndClass" select="uFN:typeUMLElement(ToEnd/AssociationEnd/@EndModelElement)"/>
      liTemp = DataBase.Delete("DELETE FROM tbl<xsl:value-of select="$toEndClass/@Name"/> WHERE ID<xsl:value-of select="$contextName"/> ='" + p<xsl:value-of select="$contextName"/>.ID + "'");
      if (liTemp == liErreur)
      liI = liErreur;
      if (liI != liErreur)
      liI += liTemp;
    </xsl:for-each>
  </xsl:function>

<xsl:function name="uFN:first-upper">
  <xsl:param name="value"/>
  <xsl:sequence select="concat(upper-case(substring($value, 1, 1)), substring($value, 2))"/>
</xsl:function>

<xsl:function name="uFN:first-lower">
  <xsl:param name="value"/>
  <xsl:sequence select="concat(lower-case(substring($value, 1, 1)), substring($value, 2))"/>
</xsl:function>

<xsl:function name="uFN:default-init">
  <xsl:param name="value"/>
  <xsl:choose>
    <xsl:when test="$value = 'int'">
      <xsl:sequence select="'0'"/>
    </xsl:when>
    <xsl:when test="$value = 'double'">
      <xsl:sequence select="'0'"/>
    </xsl:when>
    <xsl:when test="$value = 'bool'">
      <xsl:sequence select="'false'"/>
    </xsl:when>
    <xsl:when test="$value = 'boolean'">
      <xsl:sequence select="'false'"/>
    </xsl:when>
    <xsl:when test="$value = 'Boolean'">
      <xsl:sequence select="'false'"/>
    </xsl:when>
    <xsl:when test="$value = 'string'">
      <xsl:sequence select="'string.Empty'"/>
    </xsl:when>
    <xsl:when test="$value = 'DateTime'">
      <xsl:sequence select="'new DateTime(0)'"/>
    </xsl:when>
   <xsl:when test="$value = 'decimal'">
      <xsl:sequence select="'0'"/>
    </xsl:when>
    <xsl:when test="$value = 'void'">
      <xsl:sequence select="''"/>
    </xsl:when>
    <xsl:otherwise>
      <xsl:sequence select="'null'"/>
    </xsl:otherwise>
  </xsl:choose>
</xsl:function>

<xsl:function name="uFN:database-type">
  <xsl:param name="attributeType"/>
  <xsl:choose>
    <xsl:when test="$attributeType = 'DateTime'">
      <xsl:sequence select="'datetime'"/>
    </xsl:when>
    <xsl:when test="$attributeType = 'bool' or $attributeType = 'Boolean'">
      <xsl:sequence select="'bit'"/>
    </xsl:when>
    <xsl:when test="$attributeType = 'float' or $attributeType = 'double'">
      <xsl:sequence select="'float'"/>
    </xsl:when>
    <xsl:when test="$attributeType = 'int'">
      <xsl:sequence select="'int'"/>
    </xsl:when>
    <xsl:when test="$attributeType = 'byte[]'">
      <xsl:sequence select="'varbinary(max)'"/>
    </xsl:when>
    <xsl:when test="$attributeType = 'string' or $attributeType = 'String'">
      <xsl:sequence select="'nvarchar(255)'"/>
    </xsl:when>
    <xsl:when test="$attributeType = 'decimal' or $attributeType = 'Decimal'">
      <xsl:sequence select="'decimal(10,2)'"/>
    </xsl:when>
    <xsl:otherwise>
      <xsl:sequence select="concat('unknown: ', $attributeType)"/>
    </xsl:otherwise>
  </xsl:choose>
</xsl:function>
</xsl:stylesheet>
