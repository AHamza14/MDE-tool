package code.plugin.vp.Handlers.PIMParameterizationHandlers;

import javax.swing.tree.DefaultMutableTreeNode;

import com.vp.plugin.model.IAssociation;
import com.vp.plugin.model.IAssociationEnd;
import com.vp.plugin.model.IAttribute;
import com.vp.plugin.model.IClass;
import com.vp.plugin.model.IDependency;
import com.vp.plugin.model.IGeneralization;
import com.vp.plugin.model.IModelElement;
import com.vp.plugin.model.IOperation;
import com.vp.plugin.model.IPackage;
import com.vp.plugin.model.IParameter;

import code.plugin.vp.Structures.PDM;

public class UmlModelsTreeNode extends DefaultMutableTreeNode {


    UmlModelsTreeNode(String userObject){
        super(userObject);
    }

    UmlModelsTreeNode(IModelElement userObject){
        super(userObject);
    }

    UmlModelsTreeNode(PDM userObject){
        super(userObject);
    }
    
    private static final long serialVersionUID = 1L;
    
    @Override
    public String toString() {
        
        if(userObject instanceof String){
            return (String) userObject;
        }

        if(userObject instanceof PDM){
            PDM pdm = (PDM) userObject;
            return pdm.getName();
        }

        IModelElement model = (IModelElement) userObject;

        if(model instanceof IPackage){
            return model.getName() + " (Package)";
        }

        if(model instanceof IClass){
            return model.getName() + " (Classifier)";
        }

        if(model instanceof IAttribute){
            return model.getName() + " (Attribute)";
        }

        if(model instanceof IOperation){
            return model.getName() + " (Operation)";
        }

        if(model instanceof IParameter){
            return model.getName() + " (Parameter)";   
        }

        if(model instanceof IAssociation){
            IAssociation assModel = (IAssociation) model;
            String direction = " ["+assModel.getFrom().getName()+" -> "+assModel.getTo().getName()+"] ";
            return assModel.getName() == null ||  assModel.getName().isEmpty() ? "NoName" +direction+ " (Association)" : assModel.getName() +direction+ " (Association)";
        }

        if(model instanceof IAssociationEnd){
            
            IAssociationEnd assEnd = (IAssociationEnd) model;

            String connectedTo = assEnd.getEndRelationship().getName();

            return model.getName() == null || model.getName().isEmpty()? "NoName" +" ["+connectedTo+"] "+ " (AssociationEnd)" : model.getName() +" ["+connectedTo+"] "+ " (AssociationEnd)";
        }

        if(model instanceof IGeneralization){
            IGeneralization geneModel = (IGeneralization) model;

            String direction = " [" + geneModel.getFrom().getName() +" -> "+ geneModel.getTo().getName() + "] ";

        return geneModel.getName() == null || geneModel.getName().isEmpty() ? "NoName" +direction+ " (Generalization)" : geneModel.getName() +direction+ " (Generalization)";
        }

        if(model instanceof IDependency){

            IDependency depenModel = (IDependency) model;

            String direction = " [" + depenModel.getFrom().getName() +" -> "+ 
            depenModel.getTo().getName() + "] ";

            return depenModel.getName() == null || depenModel.getName().isEmpty()? "NoName" +direction+ " (Dependency)" : depenModel.getName() +direction+ " (Dependency)";
        }
            
        return model.getName();
    }
}