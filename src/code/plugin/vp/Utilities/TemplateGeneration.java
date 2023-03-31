package code.plugin.vp.Utilities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.vp.plugin.ApplicationManager;
import com.vp.plugin.ViewManager;

import code.plugin.vp.Structures.Concept;
import code.plugin.vp.Structures.PDM;


public class TemplateGeneration {

    static ViewManager viewManager = ApplicationManager.instance().getViewManager();

    
    public static void CreateTransformationTemplates(List<PDM> pdms) {
        for (PDM pdm : pdms) {
            ArrayList<Concept> concepts =  SortConcepts(pdm.getPdmUmlProfile().getConcepts());
            Map<Concept, ArrayList<Concept>> conceptContainers = createContainers(pdm.getPdmUmlProfile().getConcepts());
            CreateChainTempalet(concepts, conceptContainers);
        }
    }

    private static Map<Concept, ArrayList<Concept>> createContainers(List<Concept> concepts) {
        Map<Concept, ArrayList<Concept>> containerConcepts = new HashMap<Concept, ArrayList<Concept>>();
        for (Concept concept : concepts) {
            if(!concept.getDescription().toUpperCase().startsWith("CONTAINER")){
                ArrayList<Concept> unitConcepts = new ArrayList<Concept>();
                for (Concept unitConcept : concepts) {
                    if(unitConcept.getDescription().contains(concept.getName())){
                        unitConcepts.add(unitConcept);
                    }
                }
                containerConcepts.put(concept, unitConcepts);
            }
        }
        return containerConcepts;
    }

    private static ArrayList<Concept> SortConcepts(List<Concept> concepts) {
        ArrayList<Concept> conceptsHasContainer = new ArrayList<Concept>();
        ArrayList<Concept> containerConcepts = new ArrayList<Concept>();
        for (Concept concept : concepts) {
            if(concept.getDescription().toUpperCase().startsWith("CONTAINER")){
                conceptsHasContainer.add(concept);
            }
            else{
                containerConcepts.add(concept);
            }
        }
        conceptsHasContainer.addAll(containerConcepts);
        return conceptsHasContainer;
    }

    private static void CreateChainTempalet(ArrayList<Concept> concepts, Map<Concept, ArrayList<Concept>> conceptContainers) {
        try {
            
            //TODO: Find a better method
            File file = new File("C:/Users/Hamza/Documents/MDETool/TransformationTemplates/initial_chain_template.xsl");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(file);

            // root element (XSLT header)
            NodeList templateNodes = document.getElementsByTagName("xsl:template");

            Element templateElement = (Element) templateNodes.item(0);

            for (Concept concept : concepts) {

                //Include Elements
                String conceptType = concept.getType().toLowerCase();
                String type = conceptType.contains("(") ? conceptType.substring(conceptType.indexOf("(")+1, conceptType.indexOf(")")) : conceptType;
                String templateType = type + "_template.xsl";

                createIncludeElement(templateType, document);
             
                
                // Create merged containrs
                String mergedNodes = "";
                if(conceptContainers.containsKey(concept)){
                    mergedNodes = createMergedVariable(conceptContainers, concept, document);
                }
                String mergedContainerNodes = conceptContainers.containsKey(concept)? "exsl:node-set($"+mergedNodes+")/node()" : "node()";

                //Variable Elements
                Element variableElement = createVariableElement(concept, mergedContainerNodes, type, document);
                templateElement.appendChild(variableElement);
                
                //Create M2T transformation for interface and class elements
                if(type.equals("interface") || type.equals("class")){
                    templateElement.appendChild(createM2Transformation(concept, type, document));
                }
            }
        
            // MDE Tool Path in Documents
            String documentMDEToolPath = System.getProperty("user.home") + "\\Documents\\MDETool";

            // Project path
            String projectPath = documentMDEToolPath + "\\" + ApplicationManager.instance().getProjectManager().getProject().getName();

            // create the XSLT file
            // transform the DOM Object to an XSLT File
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);

            // Docuemnts save XSLT file
            StreamResult streamResultDoc = new StreamResult(new File(projectPath + "\\Transformation templates/chain_template.xsl").getPath());
            transformer.transform(domSource, streamResultDoc);
            
        } catch (ParserConfigurationException pce) {
            viewManager.showMessage(pce.getMessage(), "Exception");
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            viewManager.showMessage(tfe.getMessageAndLocation(), "Exception");
            tfe.printStackTrace();
        } catch (SAXException e) {
            viewManager.showMessage(e.getMessage(), "Exception");
            e.printStackTrace();
        } catch (IOException e) {
            viewManager.showMessage(e.getMessage(), "Exception");
            e.printStackTrace();
        }
    }

    private static Element createM2Transformation(Concept concept, String type, Document document) {
        String templateMode = type + "_m2t_template";
        String variableName = type + "_" + concept.getName().toLowerCase();

        //Add M2T transformation include
        createIncludeElement(templateMode+".xsl", document);

        //Apply-template Element
        Element applyTemplateElement = document.createElement("xsl:apply-templates");

        // Mode attribute
        Attr modeAttribute = document.createAttribute("mode");
        modeAttribute.setValue(templateMode);
        applyTemplateElement.setAttributeNode(modeAttribute);

        // Select attribute
        Attr selectAttribute = document.createAttribute("select");
        selectAttribute.setValue("exsl:node-set($"+variableName+")/node()");
        applyTemplateElement.setAttributeNode(selectAttribute);

        return applyTemplateElement;
    }

    private static void createIncludeElement(String template_type, Document document) {
        NodeList includeNodes = document.getElementsByTagName("xsl:include");

        NodeList templateNodes = document.getElementsByTagName("xsl:template");
        Element templateElement = (Element) templateNodes.item(0);
        
        boolean includeExists = false;
        if(includeNodes.getLength()>0){
            for (int i = 0; i < includeNodes.getLength(); i++) {
                String includeType = includeNodes.item(i).getAttributes().getNamedItem("href").getNodeValue();
                
                if(includeType.equals(template_type)){
                    includeExists = true;
                    break;
                }
            }
        }
        if(!includeExists){
            //Include Element
            Element includeElement = document.createElement("xsl:include");
            // Include href
            Attr includeHref = document.createAttribute("href");
            includeHref.setValue(template_type);
            includeElement.setAttributeNode(includeHref);
            templateElement.getParentNode().insertBefore(includeElement, templateElement);
        }
    }

    private static Element createVariableElement(Concept concept, String mergedContainerNodes, String conceptType, Document document) {
        
        // Variable element
        String conceptName = concept.getName();
        Element variableElement = document.createElement("xsl:variable");

        // Template match
        Attr variableName = document.createAttribute("name");
        variableName.setValue(conceptType+"_"+conceptName.toLowerCase());
        variableElement.setAttributeNode(variableName);

        //apply-templates element
        Element applyTemplateElement = document.createElement("xsl:apply-templates");
        variableElement.appendChild(applyTemplateElement);

        // Apply-templates Mode
        Attr applyTemplateMode = document.createAttribute("mode");
        applyTemplateMode.setValue(conceptType+"_template");
        applyTemplateElement.setAttributeNode(applyTemplateMode);

        // Apply-templates select
        Attr applyTemplateSelect = document.createAttribute("select");
        applyTemplateSelect.setValue(mergedContainerNodes);
        applyTemplateElement.setAttributeNode(applyTemplateSelect);

        //Create template parameters
        createTemplateParameters(concept, conceptName, conceptType, "name", conceptName, applyTemplateElement, document);

        if(conceptType.equals("operation")){
            createTemplateParameters(concept, conceptName, conceptType, "return_type", "void", applyTemplateElement, document);
        }

        if(conceptType.equals("attribute")){
            createTemplateParameters(concept, conceptName, conceptType, "type", "string", applyTemplateElement, document);
        }

        return variableElement;
    }

    private static void createTemplateParameters(Concept concept, String conceptName, String conceptType, String parameterName, String parameterValue, Element applyTemplateElement, Document document) {
        //Parameter element

        Element parameterElement = document.createElement("xsl:with-param");
        applyTemplateElement.appendChild(parameterElement);

        // Parameter name
        Attr paramName = document.createAttribute("name");
        paramName.setValue(conceptType+"_"+parameterName);
        parameterElement.setAttributeNode(paramName);

        //Parameter Value
        String paramValue = parameterValue;
        if(paramName.equals("name")){
            paramValue = parameterValue.substring(0, 1).toUpperCase() + parameterValue.substring(1);
        }
        parameterElement.appendChild(document.createTextNode(paramValue));

    }

    private static String createMergedVariable(Map<Concept, ArrayList<Concept>> conceptContainers, Concept concept, Document document) {
        NodeList templateNodes = document.getElementsByTagName("xsl:template");
        Element templateElement = (Element) templateNodes.item(0);

        //Variable Element
        Element variableElement = document.createElement("xsl:variable");
        
        Element rootElement = document.createElement("xsl:element");
        //root name
        Attr rootName = document.createAttribute("name");
        rootName.setValue("root");
        rootElement.setAttributeNode(rootName);

        String variableNameValue = concept.getName()+"_";
        for (Concept containerConcept : conceptContainers.get(concept)) {
            variableNameValue += containerConcept.getName(); 

            Element copyOfelement = document.createElement("xsl:copy-of");
            //copyOf select
            String selectValue = "$"+containerConcept.getType().toLowerCase() +"_"+ containerConcept.getName().toLowerCase();
            Attr copyOfSelect = document.createAttribute("select");
            copyOfSelect.setValue(selectValue);
            copyOfelement.setAttributeNode(copyOfSelect);
            rootElement.appendChild(copyOfelement);
        }

        Attr variableName = document.createAttribute("name");
        variableName.setValue(variableNameValue);
        variableElement.setAttributeNode(variableName);

        variableElement.appendChild(rootElement);
        templateElement.appendChild(variableElement);
        return variableNameValue;
    }
}
