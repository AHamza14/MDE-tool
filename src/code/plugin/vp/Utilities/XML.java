package code.plugin.vp.Utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import com.vp.plugin.ApplicationManager;
import com.vp.plugin.ViewManager;
import com.vp.plugin.model.IProject;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import code.plugin.vp.Structures.Concept;
import code.plugin.vp.Structures.Constraint;
import code.plugin.vp.Structures.DesignConcern;
import code.plugin.vp.Structures.PDM;
import code.plugin.vp.Structures.TransformationTemplate;
import code.plugin.vp.Structures.UmlProfile;
import code.plugin.vp.Structures.PIMParameterization.DesignConcernMarking;
import code.plugin.vp.Structures.PIMParameterization.MarkedUmlElement;
import code.plugin.vp.Structures.PIMParameterization.VPProject;

public class XML {
    static ViewManager viewManager = ApplicationManager.instance().getViewManager();
    public static void ExportPDMs(List<PDM> pdms){
        try {
 
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
 
            // root element
            Element root = document.createElement("PDMs");
            document.appendChild(root);

            //Pdms
            for(PDM pdm: pdms){
                // PDM element
                Element pdmElement = document.createElement("PDM");
                root.appendChild(pdmElement);

                // PDM Id Attribute
                Attr pdmIdAttr = document.createAttribute("Id");
                pdmIdAttr.setValue(String.valueOf(pdm.getId()));
                pdmElement.setAttributeNode(pdmIdAttr);

                // PDM Name Element
                Element pdmNameElement = document.createElement("Name");
                pdmNameElement.appendChild(document.createTextNode(pdm.getName()));
                pdmElement.appendChild(pdmNameElement);

                // PDM Description Element
                Element pdmDescriptionElement = document.createElement("Description");
                pdmDescriptionElement.appendChild(document.createTextNode(pdm.getDescription()));
                pdmElement.appendChild(pdmDescriptionElement);

                // PDM Date Attribute
                Attr pdmDateAttr = document.createAttribute("CreatingDate");
                pdmDateAttr.setValue(pdm.getCreatingDate());
                pdmElement.setAttributeNode(pdmDateAttr);

                //PDM : Uml Profile Element
                Element umlProfileElement = document.createElement("UMLProfile");
                pdmElement.appendChild(umlProfileElement);

                //PDM : Uml Profile : Cocnepts Element
                Element conceptsElement = document.createElement("Concepts");
                umlProfileElement.appendChild(conceptsElement);

                //PDM : Uml Profile : Cocnepts : Concept Element
                for(Concept concept : pdm.getPdmUmlProfile().getConcepts()){
                    //Concept Element
                    Element conceptElement = document.createElement("Concept");
                    conceptsElement.appendChild(conceptElement);

                    // Concept Id Attribute
                    Attr conceptIdAttr = document.createAttribute("Id");
                    conceptIdAttr.setValue(String.valueOf(concept.getId()));
                    conceptElement.setAttributeNode(conceptIdAttr);

                    //Concept Name Element
                    Element conceptName = document.createElement("Name");
                    conceptName.appendChild(document.createTextNode(concept.getName()));
                    conceptElement.appendChild(conceptName);

                    //Concept Type Element
                    Element conceptType = document.createElement("Type");
                    conceptType.appendChild(document.createTextNode(concept.getType()));
                    conceptElement.appendChild(conceptType);

                    //Concept Description Element
                    Element conceptDescription = document.createElement("Description");
                    conceptDescription.appendChild(document.createTextNode(concept.getDescription()));
                    conceptElement.appendChild(conceptDescription);

                    //Concept Design Concerns Element
                    Element DesignConcernsElement = document.createElement("DesignConcerns");
                    conceptElement.appendChild(DesignConcernsElement);
                    for(DesignConcern designConcern: concept.getDesignConcerns()){
                        //Design Concern Element
                        Element designConcernElement = document.createElement("DesignConcern");
                        DesignConcernsElement.appendChild(designConcernElement);

                        // Design Concern Id Attribute
                        Attr ddIdAttr = document.createAttribute("Id");
                        ddIdAttr.setValue(String.valueOf(designConcern.getId()));
                        designConcernElement.setAttributeNode(ddIdAttr);

                        //Design Concern Name 
                        Element ddName = document.createElement("Name");
                        ddName.appendChild(document.createTextNode(designConcern.getName()));
                        designConcernElement.appendChild(ddName);

                        //Design Concern Type 
                        Element ddType = document.createElement("Type");
                        ddType.appendChild(document.createTextNode(designConcern.getType()));
                        designConcernElement.appendChild(ddType);

                        //Design Concern Description
                        Element ddDescription = document.createElement("Description");
                        ddDescription.appendChild(document.createTextNode(designConcern.getDescription()));
                        designConcernElement.appendChild(ddDescription);

                        //Design Concern UML Element Types
                        Element UmlElementTypes = document.createElement("UMLElementTypes");
                        designConcernElement.appendChild(UmlElementTypes);
                        for(String umlElemnt: designConcern.getUmlElements()){
                            //UML Element Type
                            Element UmlElement = document.createElement("UMLElementType");
                            UmlElement.appendChild(document.createTextNode(umlElemnt));
                            UmlElementTypes.appendChild(UmlElement);
                        }

                    }
                }

                //PDM : Uml Profile : Constraints Element
                Element constraintsElement = document.createElement("Constraints");
                umlProfileElement.appendChild(constraintsElement);
                for(Constraint constraint: pdm.getPdmUmlProfile().getConstraints()){
                    //Constraint Element
                    Element constraintElement = document.createElement("Constraint");
                    constraintsElement.appendChild(constraintElement);

                    // Constraint Id Attribute
                    Attr constraintIdAttr = document.createAttribute("Id");
                    constraintIdAttr.setValue(String.valueOf(constraint.getId()));
                    constraintElement.setAttributeNode(constraintIdAttr);

                    //Constraint Name Element
                    Element constraintName = document.createElement("Name");
                    constraintName.appendChild(document.createTextNode(constraint.getName()));
                    constraintElement.appendChild(constraintName);

                    //Constraint Concept A Element
                    Element constraintAConcept = document.createElement("ConceptA");
                    constraintAConcept.appendChild(document.createTextNode(constraint.getConceptA().getId().toString()));
                    constraintElement.appendChild(constraintAConcept);

                    //Constraint Concept B Element
                    Element constraintBConcept = document.createElement("ConceptB");
                    constraintBConcept.appendChild(document.createTextNode(constraint.getConceptB().getId().toString()));
                    constraintElement.appendChild(constraintBConcept);

                    //Constraint Type Element
                    Element constraintType = document.createElement("Type");
                    constraintType.appendChild(document.createTextNode(constraint.getType()));
                    constraintElement.appendChild(constraintType);

                    //Constraint Description Element
                    Element constraintDescription = document.createElement("Description");
                    constraintDescription.appendChild(document.createTextNode(constraint.getDescription()));
                    constraintElement.appendChild(constraintDescription);
                }

                //PDM : Transformation Template Element
                Element TransformationTemplatesElement = document.createElement("TransformationTemplates");
                pdmElement.appendChild(TransformationTemplatesElement);
                for(TransformationTemplate tt: pdm.getPdmTransformationTemplate()){
                    //Transformation Template Element
                    Element ttElement = document.createElement("TransformationTemplate");
                    TransformationTemplatesElement.appendChild(ttElement);

                    // Transformation Template Id Attribute
                    Attr ttIdAttr = document.createAttribute("Id");
                    ttIdAttr.setValue(String.valueOf(tt.getId()));
                    ttElement.setAttributeNode(ttIdAttr);

                    //Transformation Template Name Element
                    Element ttName = document.createElement("Name");
                    ttName.appendChild(document.createTextNode(tt.getName()));
                    ttElement.appendChild(ttName);

                    //Transformation Template Type Element
                    Element ttType = document.createElement("Type");
                    ttType.appendChild(document.createTextNode(tt.getType()));
                    ttElement.appendChild(ttType);

                    //Transformation Template Description Element
                    Element ttDescription = document.createElement("Description");
                    ttDescription.appendChild(document.createTextNode(tt.getDescription()));
                    ttElement.appendChild(ttDescription);

                    //Transformation Template File URI 
                    Element ttFile = document.createElement("FileURI");
                    ttFile.appendChild(document.createTextNode(tt.getFileUri()));
                    ttElement.appendChild(ttFile);

                    //Transformation Template PrimaryImplementedConceptID Element
                    Element ttPrimaryConcept = document.createElement("PrimaryImplementedConceptID");
                    ttPrimaryConcept.appendChild(document.createTextNode(tt.getPrimaryImplementedConcept().getId().toString()));
                    ttElement.appendChild(ttPrimaryConcept);

                    //Transformation Template ImplmementedConceptsForVariation Element
                    Element ttVariationConcepts = document.createElement("ImplmementedConceptsForVariation");
                    ttElement.appendChild(ttVariationConcepts);
                    for(Concept varConcept: tt.getImplmementedConceptsForVariation()){
                        // Variation Concept Element
                        Element ttVariationConcept = document.createElement("ImplementedConceptForVariationID");
                        ttVariationConcept.appendChild(document.createTextNode(varConcept.getId().toString()));
                        ttVariationConcepts.appendChild(ttVariationConcept);
                    }
                }

            }
 
            //Create the MDE Tool save Path in Documents
            String documentMDEToolPath = System.getProperty("user.home") + "\\Documents\\MDETool";
            UserInterfaceUtil.createFolder(documentMDEToolPath);

            //Create the project folder
            String projectPath = documentMDEToolPath +"\\"+ ApplicationManager.instance().getProjectManager().getProject().getName();
            UserInterfaceUtil.createFolder(projectPath);
            
            // create the xml file
            //transform the DOM Object to an XML File
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            // StreamResult streamResult = new StreamResult(new File(saveFolder +"\\UQARMDE_PDMs.xml"));
            // transformer.transform(domSource, streamResult);


           
            //Docuemnts save xml file
            StreamResult streamResultDoc = new StreamResult(new File(projectPath +"\\UQARMDE_PDMs.xml"));
            transformer.transform(domSource, streamResultDoc);
 
 
        } catch (ParserConfigurationException pce) {
            viewManager.showMessage(pce.getMessage(), "Exception");
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            viewManager.showMessage(tfe.getMessage(), "Exception");
            tfe.printStackTrace();
        }
    }

    public static List<PDM> ImportPDMs(String xmlFile){

        List<PDM> PDMs = new ArrayList<PDM>();

        try{

            File file = new File(xmlFile);
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);
            document.getDocumentElement().normalize();

            NodeList pdmList = document.getElementsByTagName("PDM");

            for (int i = 0; i < pdmList.getLength(); i++) {

                Node pdmNode = pdmList.item(i);

                if (pdmNode.getNodeType() == Node.ELEMENT_NODE) {
                    
                    Element pdmElement = (Element) pdmNode;
                    
                    UUID  pdmId = UUID.fromString(pdmElement.getAttribute("Id").toString());
                    String  pdmDate = pdmElement.getAttribute("CreatingDate").toString();
                    String pdmName = pdmElement.getElementsByTagName("Name").item(0).getTextContent();
                    String pdmDescription = pdmElement.getElementsByTagName("Description").item(0).getTextContent();

                    //PDM UML Profile
                    Node pdmUmlProfile = pdmElement.getElementsByTagName("UMLProfile").item(0);
                    Element pdmUmlProfileElement = (Element) pdmUmlProfile;

                    //PDM UML Profile : Concepts
                    Node conceptsNode = pdmUmlProfileElement.getElementsByTagName("Concepts").item(0);
                    Element conceptsElement = (Element) conceptsNode;

                    // PDM UML Profile : Concepts : Concept
                    NodeList conceptList = conceptsElement.getElementsByTagName("Concept");

                    List<Concept> concepts = new ArrayList<Concept>();
                    for (int j = 0; j < conceptList.getLength(); j++) {
                        Node conceptNode = conceptList.item(j);

                        Element conceptElement = (Element) conceptNode;

                        UUID conceptId = UUID.fromString(conceptElement.getAttribute("Id").toString());
                        String conceptName = conceptElement.getElementsByTagName("Name").item(0).getTextContent();
                        String conceptType = conceptElement.getElementsByTagName("Type").item(0).getTextContent();
                        String conceptDescription = conceptElement.getElementsByTagName("Description").item(0).getTextContent();

                        // PDM UML Profile : Concepts : Concept : Design Concerns
                        Node designConcernsNode = conceptElement.getElementsByTagName("DesignConcerns").item(0);
                        Element designConcernsElement = (Element) designConcernsNode;

                        // PDM UML Profile : Concepts : Concept : Design Concerns :Design Concern
                        NodeList designConcernList = designConcernsElement.getElementsByTagName("DesignConcern");

                        List<DesignConcern> designConcerns = new ArrayList<DesignConcern>();
                        for (int k = 0; k < designConcernList.getLength(); k++) {
                            Node designConcernNode = designConcernList.item(k);
                            Element designConcernElement = (Element) designConcernNode;

                            UUID designConcernId = UUID.fromString(designConcernElement.getAttribute("Id").toString());
                            String designConcernName = designConcernElement.getElementsByTagName("Name").item(0).getTextContent();
                            String designConcernType = designConcernElement.getElementsByTagName("Type").item(0).getTextContent();
                            String designConcernDescription = designConcernElement.getElementsByTagName("Description").item(0).getTextContent();

                            // PDM UML Profile : Concepts : Concept : Design Concern : UMLElementTypes
                            Node umlElementTypesNode = designConcernElement.getElementsByTagName("UMLElementTypes").item(0);
                            Element umlElementTypesElement = (Element) umlElementTypesNode;

                            // PDM UML Profile : Concepts : Concept : Design Concern : UMLElementType
                            NodeList umlElementTypesList = umlElementTypesElement.getElementsByTagName("UMLElementType");
                            
                            List<String> umlElementsTypes = new ArrayList<String>();
                            for (int l = 0; l < umlElementTypesList.getLength(); l++) {
                                Node umlElementNode = umlElementTypesList.item(l);
                                Element umlElementElement = (Element) umlElementNode;
                                String umlElementTypeName = umlElementElement.getTextContent();
                                umlElementsTypes.add(umlElementTypeName);
                            }

                            //Create and add the design concern to the list of Design Concerns
                            designConcerns.add(new DesignConcern(designConcernId, designConcernName, designConcernType, designConcernDescription, umlElementsTypes));
                        }

                        concepts.add(new Concept(conceptId, conceptName, conceptType, conceptDescription, designConcerns));
                    }

                    //PDM UML Profile : Constraints
                    Node constraintsNode = pdmUmlProfileElement.getElementsByTagName("Constraints").item(0);
                    Element constraintsElement = (Element) constraintsNode;

                    // PDM UML Profile : Constraints : Constraint
                    NodeList constraintList = constraintsElement.getElementsByTagName("Constraint");

                    List<Constraint> constraints= new ArrayList<Constraint>();
                    for (int x = 0; x < constraintList.getLength(); x++) {
                        Node constraintNode = constraintList.item(x);

                        Element constraintElement = (Element) constraintNode;

                        UUID constraintId = UUID.fromString(constraintElement.getAttribute("Id").toString());
                        String constraintName = constraintElement.getElementsByTagName("Name").item(0).getTextContent();

                        //Concept A and B need be stored as concept not UUID
                        UUID constraintConceptA = UUID.fromString(constraintElement.getElementsByTagName("ConceptA").item(0).getTextContent().toString());
                        UUID constraintConceptB = UUID.fromString(constraintElement.getElementsByTagName("ConceptB").item(0).getTextContent().toString());

                        String constraintType = constraintElement.getElementsByTagName("Type").item(0).getTextContent();
                        String constraintDescription = constraintElement.getElementsByTagName("Description").item(0).getTextContent();

                        Concept concepta = new Concept();
                        Concept conceptb = new Concept();
                        for (Concept con : concepts) {
                            if(con.getId().compareTo(constraintConceptA) == 0){
                                concepta = con;
                            }

                            if(con.getId().compareTo(constraintConceptB) == 0){
                                conceptb = con;
                            }
                        }
                        constraints.add(new Constraint(constraintId, constraintName, constraintType, constraintDescription, concepta, conceptb));
                    }

                    //Crreate the UML Profile object
                    UmlProfile umlProfile = new UmlProfile(concepts, constraints);

                    //PDM : Transformation Templates
                    Node pdmTransformationTemplates = pdmElement.getElementsByTagName("TransformationTemplates").item(0);
                    Element pdmTransformationTemplatesElement = (Element) pdmTransformationTemplates;

                    //PDM : Transformation Templates : Transformation Template
                    NodeList TransformationTemplateList = pdmTransformationTemplatesElement.getElementsByTagName("TransformationTemplate");

                    List<TransformationTemplate> transformationTemplates = new ArrayList<TransformationTemplate>();
                    for (int j = 0; j < TransformationTemplateList.getLength(); j++) {
                        Node ttNode = TransformationTemplateList.item(j);
                        Element ttElement = (Element) ttNode;

                        UUID ttId = UUID.fromString(ttElement.getAttribute("Id").toString());
                        String ttName = ttElement.getElementsByTagName("Name").item(0).getTextContent();
                        String ttType = ttElement.getElementsByTagName("Type").item(0).getTextContent();
                        String ttDescription = ttElement.getElementsByTagName("Description").item(0).getTextContent();
                        String ttFileUri = ttElement.getElementsByTagName("FileURI").item(0).getTextContent();
                        UUID ttPrimaryConceptID = UUID.fromString(ttElement.getElementsByTagName("PrimaryImplementedConceptID").item(0).getTextContent());

                        //PDM : Transformation Templates : Transformation Template : Concepts Variation
                        Node conceptVariationsNode = ttElement.getElementsByTagName("ImplmementedConceptsForVariation").item(0);
                        Element conceptVariationsElement = (Element) conceptVariationsNode;

                        //PDM : Transformation Templates : Transformation Template : Concept Variation
                        NodeList conceptVariationList = conceptVariationsElement.getElementsByTagName("ImplementedConceptForVariationID");

                        List<UUID> conceptsVartiation = new ArrayList<UUID>();
                        for (int k = 0; k < conceptVariationList.getLength(); k++) {
                            Node conceptNode = conceptVariationList.item(k);
                            Element conceptElement = (Element) conceptNode;
                            UUID ttVariationConceptID = UUID.fromString(conceptElement.getTextContent());
                            
                            conceptsVartiation.add(ttVariationConceptID);
                        }

                        Concept primaryConcept  = null;
                        List<Concept> variationConcepts = new ArrayList<Concept>();
                        for (Concept con : concepts) {
                            if(conceptsVartiation.contains(con.getId())){
                                variationConcepts.add(con);
                            }
                            if(con.getId().compareTo(ttPrimaryConceptID) == 0){
                                primaryConcept = con;
                            }
                        }

                        transformationTemplates.add(new TransformationTemplate(ttId, ttName, ttType, ttDescription, ttFileUri, primaryConcept, variationConcepts));

                    }

                    PDMs.add(new PDM(pdmId, pdmName, pdmDescription, pdmDate, umlProfile, transformationTemplates));
                }

                
            }
            return PDMs;

        } catch (Exception e) {
            viewManager.showMessage(e.getMessage(), "Exception");
            e.printStackTrace();
        }
        return null;
    }

    public static void ExportParameterizedPIM(List<VPProject> projects){
        try {
 
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
 
            // root element
            Element root = document.createElement("PIMParameterization");
            document.appendChild(root);

            //Projects
            for(VPProject project: projects){
                // Project element
                Element projectElement = document.createElement("Project");
                root.appendChild(projectElement);

                // Project Id Attribute
                Attr projectIdAttr = document.createAttribute("Id");
                projectIdAttr.setValue(String.valueOf(project.getId()));
                projectElement.setAttributeNode(projectIdAttr);

                // Project Name Element
                Element projectNameElement = document.createElement("Name");
                projectNameElement.appendChild(document.createTextNode(project.getName()));
                projectElement.appendChild(projectNameElement);

                // Project Date Attribute
                Attr projectDateAttr = document.createAttribute("CreatingDate");
                projectDateAttr.setValue(project.getCreatingDate());
                projectElement.setAttributeNode(projectDateAttr);

                //Project : Uml Elements
                Element umlElements = document.createElement("UMLElements");
                projectElement.appendChild(umlElements);

                //Project : Uml Elements : Uml Element
                for(MarkedUmlElement markedUmlElement : project.getMarkedUmlElements()){

                    //Marked Uml Element
                    Element umlElement = document.createElement("UMLElement");
                    umlElements.appendChild(umlElement);

                    // Marked Uml Element Id Attribute
                    Attr umlElementIdAttr = document.createAttribute("Id");
                    umlElementIdAttr.setValue(markedUmlElement.getId());
                    umlElement.setAttributeNode(umlElementIdAttr);

                    //Marked Uml Element Name Element
                    Element umlElementName = document.createElement("FullQualifiedName");
                    umlElementName.appendChild(document.createTextNode(markedUmlElement.getFullQualifiedName()));
                    umlElement.appendChild(umlElementName);

                    //Marked Uml Element Type Element
                    Element umlElementType = document.createElement("Type");
                    umlElementType.appendChild(document.createTextNode(markedUmlElement.getType()));
                    umlElement.appendChild(umlElementType);

                    //Project : Uml Element : Design Concerns
                    Element DesignConcernsElement = document.createElement("DesignConcerns");
                    umlElement.appendChild(DesignConcernsElement);

                    for(DesignConcernMarking designConcern: markedUmlElement.getDesignConcerns()){
                        //Design Concern Element
                        Element designConcernElement = document.createElement("DesignConcern");
                        DesignConcernsElement.appendChild(designConcernElement);

                        // Design Concern Id Attribute
                        Attr ddIdAttr = document.createAttribute("Id");
                        ddIdAttr.setValue(String.valueOf(designConcern.getDesignConcern().getId()));
                        designConcernElement.setAttributeNode(ddIdAttr);

                        //Design Concern Name 
                        Element ddName = document.createElement("Name");
                        ddName.appendChild(document.createTextNode(designConcern.getDesignConcern().getName()));
                        designConcernElement.appendChild(ddName);

                        //Design Concern Type 
                        Element ddType = document.createElement("Type");
                        ddType.appendChild(document.createTextNode(designConcern.getDesignConcern().getType()));
                        designConcernElement.appendChild(ddType);

                        //Design Concern Value
                        Element ddValue = document.createElement("Value");
                        ddValue.appendChild(document.createTextNode(designConcern.getValue()));
                        designConcernElement.appendChild(ddValue);

                        //Design Concern Pdm
                        Element ddPdm = document.createElement("Pdm");
                        ddPdm.appendChild(document.createTextNode(designConcern.getPdm()));
                        designConcernElement.appendChild(ddPdm);
                    }
                }

            }
            
 
            //Create the MDE Tool save Path in Documents
            String documentPath = System.getProperty("user.home") + "\\Documents";
            documentPath = documentPath + "\\MDETool";
            UserInterfaceUtil.createFolder(documentPath);

            //Create the project folder
            IProject project = ApplicationManager.instance().getProjectManager().getProject();
            String projectPath = documentPath +"\\"+ project.getName();
            UserInterfaceUtil.createFolder(projectPath);

            // create the xml file
            //transform the DOM Object to an XML File
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
           
            StreamResult streamResultDoc = new StreamResult(new File(projectPath +"\\"+ project.getName() + "_PIMParametrization.xml"));
            
            transformer.transform(domSource, streamResultDoc);

        } catch (ParserConfigurationException pce) {            
            viewManager.showMessage(pce.getMessage(), "Exception");
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            viewManager.showMessage(tfe.getMessage(), "Exception");
            tfe.printStackTrace();
        }
    }

    public static Map<String, String> getUmlElementDesignConcern(String umlElementId) throws IllegalArgumentException{

        Map<String, String> designConcerns = new HashMap<String, String>();

        try{

            // String xmlFile = saveFolderFile +"\\"+ projectName + "_PIMParametrization.xml";
            IProject project = ApplicationManager.instance().getProjectManager().getProject();

            String projectName = project.getName();

            String saveFolder = System.getProperty("user.home") + "\\Documents\\MDETool\\"+projectName;

            File saveFolderFile = new File(saveFolder); 

            String xmlFile = saveFolderFile +"\\"+ projectName + "_PIMParametrization.xml";

            File file = new File(xmlFile);

            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);

            document.getDocumentElement().normalize();

            NodeList umlElementsList = document.getElementsByTagName("UMLElement");

            for (int i = 0; i < umlElementsList.getLength(); i++) {

                Node umlElementNode = umlElementsList.item(i);

                if (umlElementNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element pdmElement = (Element) umlElementNode;
                    if (pdmElement.getAttribute("Id").equals(umlElementId)) {
                        Element umlEleElement = (Element) umlElementNode;
                        NodeList designConcernsList = umlEleElement.getElementsByTagName("DesignConcern");
                        for (int j = 0; j < designConcernsList.getLength(); j++) {

                            Node designConcernNode = designConcernsList.item(j);
                            Element designConcernElement = (Element) designConcernNode;

                            designConcerns.put(designConcernElement.getAttribute("Id"), designConcernElement.getElementsByTagName("Value").item(0).getTextContent());
                        }
                    }
                }

                
            }
            return designConcerns;

        } catch (Exception e) {
            viewManager.showMessage(e.getMessage(), "Exception");
            e.printStackTrace();
            return null;
        }
    }

    public static List<MarkedUmlElement> getParameterizedUmlElements() throws IllegalArgumentException {
        List<MarkedUmlElement> markedUmlElements = new ArrayList<MarkedUmlElement>();

        try{

           //Get the PIM Parameterization file Path 
            IProject project = ApplicationManager.instance().getProjectManager().getProject();

            String projectName = project.getName();

            String saveFolder = System.getProperty("user.home") + "\\Documents\\MDETool\\"+projectName;

            File saveFolderFile = new File(saveFolder); 

            String xmlFile = saveFolderFile +"\\"+ projectName + "_PIMParametrization.xml";

           File file = new File(xmlFile);

           DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
           DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
           Document document = documentBuilder.parse(file);
           
           document.getDocumentElement().normalize();

            NodeList umlElementList = document.getElementsByTagName("UMLElement");

            for (int i = 0; i < umlElementList.getLength(); i++) {

                Node umlElementNod = umlElementList.item(i);

                if (umlElementNod.getNodeType() == Node.ELEMENT_NODE) {
                    
                    Element umlElementEle = (Element) umlElementNod;
                    
                    String  umlElementId = umlElementEle.getAttribute("Id").toString();
                    String umlElementFullQualifiedName = umlElementEle.getElementsByTagName("FullQualifiedName").item(0).getTextContent();
                    String umlElementType = umlElementEle.getElementsByTagName("Type").item(0).getTextContent();

                    //UML element design concerns
                    Node designConcernsNode = umlElementEle.getElementsByTagName("DesignConcerns").item(0);
                    Element designConcernsElement = (Element) designConcernsNode;

                    // UML element design concern
                    NodeList designConcernList = designConcernsElement.getElementsByTagName("DesignConcern");

                    List<DesignConcernMarking> designConcerns = new ArrayList<DesignConcernMarking>();
                    for (int j = 0; j < designConcernList.getLength(); j++) {
                        Node designConcernNode = designConcernList.item(j);

                        Element designConcernElement = (Element) designConcernNode;

                        UUID designConcernId = UUID.fromString(designConcernElement.getAttribute("Id").toString());
                        String designConcernName = designConcernElement.getElementsByTagName("Name").item(0).getTextContent();
                        String designConcernType = designConcernElement.getElementsByTagName("Type").item(0).getTextContent();
                        String designConcernValue = designConcernElement.getElementsByTagName("Value").item(0).getTextContent();
                        String designConcernPdm = designConcernElement.getElementsByTagName("Pdm").item(0).getTextContent();

                        designConcerns.add(new DesignConcernMarking(new DesignConcern(designConcernId, designConcernName, designConcernType, "", null), designConcernValue, designConcernPdm));
                    }
                    markedUmlElements.add(new MarkedUmlElement(umlElementId, umlElementFullQualifiedName, umlElementFullQualifiedName, umlElementType, designConcerns));
                }

                
            }
            return markedUmlElements;

        } catch (Exception e) {
            viewManager.showMessage(e.getMessage(), "Exception");
            e.printStackTrace();
            return null;
        }
    }
}