package code.plugin.vp.UserInterface.PIMParameterizationDialogs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import com.vp.plugin.ApplicationManager;
import com.vp.plugin.diagram.IDiagramElement;
import com.vp.plugin.diagram.connector.IAssociationUIModel;
import com.vp.plugin.diagram.connector.IDependencyUIModel;
import com.vp.plugin.diagram.connector.IGeneralizationUIModel;
import com.vp.plugin.diagram.shape.IClassUIModel;
import com.vp.plugin.diagram.shape.IPackageUIModel;
import com.vp.plugin.model.*;

import code.plugin.vp.Structures.PDM;

public class UmlElementsTree {

    // Main Method to return the all Tree
    public static DefaultMutableTreeNode getUMLElementsTree(List<PDM> pdms) {
        IDiagramElement[] selectedUmlElements = ApplicationManager.instance().getDiagramManager().getActiveDiagram().getSelectedDiagramElement();
        
        DefaultMutableTreeNode pdmsNode = new DefaultMutableTreeNode("PDM(s)");

        for (PDM pdm : pdms) {
            DefaultMutableTreeNode pdmNode = new UmlModelsTreeNode(pdm);
            for (IDiagramElement element : selectedUmlElements) {

                if (element instanceof IPackageUIModel) {
                    IPackageUIModel packageShape = (IPackageUIModel) element;
                    IPackage packageModel = (IPackage) packageShape.getModelElement();
                    pdmNode.add(getPackageNode(packageModel));
                }

                if (element instanceof IClassUIModel) {
                    IClassUIModel classShape = (IClassUIModel) element;
                    IClass classModel = (IClass) classShape.getModelElement();
                    pdmNode.add(createClassNode(classModel));
                }

                if (element instanceof IAssociationUIModel) {
                    IAssociationUIModel associationShape = (IAssociationUIModel) element;
                    IAssociation associationModel = (IAssociation) associationShape.getModelElement();
                    pdmNode.add(createAssociationNode(associationModel));
                }

                if (element instanceof IGeneralizationUIModel) {
                    IGeneralizationUIModel generalizationShape = (IGeneralizationUIModel) element;
                    IGeneralization generalizationModel = (IGeneralization) generalizationShape.getModelElement();
                    pdmNode.add(createGeneralizationNode(generalizationModel));
                }

                if (element instanceof IDependencyUIModel) {
                    IDependencyUIModel dependencyShape = (IDependencyUIModel) element;
                    IDependency dependencyModel = (IDependency) dependencyShape.getModelElement();
                    pdmNode.add(createDependencyNode(dependencyModel));
                }
            }
            pdmsNode.add(pdmNode);
        }        
        return pdmsNode;
    }

    // Create the Package node 
    private static DefaultMutableTreeNode getPackageNode(IPackage packageElement){

        DefaultMutableTreeNode packageNode = new UmlModelsTreeNode(packageElement);
        
        //Add Class
        List<IClass> packageClasses = new ArrayList<IClass>();
        for (IModelElement packageChild : packageElement.toChildArray()) {
            if(packageChild instanceof IClass){
                IClass childClass = (IClass) packageChild;
                packageNode.add(createClassNode((childClass)));

                packageClasses.add(childClass);
            }
        }

        HashSet<IRelationship> relationships = FindPackageRelationShapes(packageClasses);

        for (IRelationship relationship : relationships) {
            if(relationship instanceof IAssociation){
                packageNode.add(createAssociationNode((IAssociation) relationship));
            }
             
            if(relationship instanceof IGeneralization){
                packageNode.add(createGeneralizationNode((IGeneralization) relationship));
            }

            if(relationship instanceof IDependency){
                packageNode.add(createDependencyNode((IDependency) relationship));
            }
        }

        return packageNode;
    }


    // Create the Class node
    private static DefaultMutableTreeNode createClassNode(IClass classElement){
        
        DefaultMutableTreeNode classNode = new UmlModelsTreeNode(classElement);
                    
        for (IAttribute attribute : classElement.toAttributeArray()) {
            classNode.add(createAttributeNode(attribute));
        }

        for (IOperation operation : classElement.toOperationArray()) {
            DefaultMutableTreeNode operationNode = createOperationNode(operation);
            for (IParameter  parameter : operation.toParameterArray()) {
                operationNode.add(createParameterNode(parameter));
            }

            classNode.add(operationNode);
        }

    
        for (IRelationshipEnd relation : classElement.toFromRelationshipEndArray()) {
            if(relation instanceof IAssociationEnd){
                classNode.add(createAssociationEndNode((IAssociationEnd) relation));
            }
        }

        for (IRelationshipEnd relation : classElement.toToRelationshipEndArray()) {
            if(relation instanceof IAssociationEnd){
                classNode.add(createAssociationEndNode((IAssociationEnd) relation));
            }
        }
        
        return classNode;
    }

    // Create the Attribute node 
    private static DefaultMutableTreeNode createAttributeNode(IAttribute attributeElement){
        return new UmlModelsTreeNode(attributeElement);
    }

    // Create the Operation node 
    private static DefaultMutableTreeNode createOperationNode(IOperation operationElement){
        return new UmlModelsTreeNode(operationElement);
    }

    // Create the Parameter node 
    private static DefaultMutableTreeNode createParameterNode(IParameter parameterElement){
        return new UmlModelsTreeNode(parameterElement);
    }

     // Create Association node 
    private static DefaultMutableTreeNode createAssociationNode(IAssociation associationElement){
        
        return new UmlModelsTreeNode(associationElement);
    }

    
    // Create the Association End node 
    private static DefaultMutableTreeNode createAssociationEndNode(IAssociationEnd assEndElement){
        return new UmlModelsTreeNode(assEndElement);
    }
    

    // Create the Generalization node 
    private static DefaultMutableTreeNode createGeneralizationNode(IGeneralization generalizationElement){
        return new UmlModelsTreeNode(generalizationElement);
    }

    // Create the Dependency node 
    private static DefaultMutableTreeNode createDependencyNode(IDependency dependencyElement){

        return new UmlModelsTreeNode(dependencyElement);
    }
    
    // Find the existing relationships (Association, Generalization, Dependency) in a package
    private static HashSet<IRelationship> FindPackageRelationShapes(List<IClass> packageClasses) {
        HashSet<IRelationship> relationships = new HashSet<IRelationship>();
        for (IClass iClass : packageClasses) {

            for (IRelationship iRelationship : iClass.toFromRelationshipArray()) {
                relationships.add(iRelationship);
            }

            for (IRelationship iRelationship : iClass .toToRelationshipArray()) {
                relationships.add(iRelationship);
            }

            for (IRelationshipEnd iRelationshipEnd : iClass.toFromRelationshipEndArray()) {

                relationships.add(iRelationshipEnd.getEndRelationship());
            }

            for (IRelationshipEnd iRelationshipEnd : iClass.toToRelationshipEndArray()) {

                relationships.add(iRelationshipEnd.getEndRelationship());
            }
        }
        return relationships;
    }

}