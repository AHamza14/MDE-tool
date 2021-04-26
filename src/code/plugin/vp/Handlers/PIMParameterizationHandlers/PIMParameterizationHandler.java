package code.plugin.vp.Handlers.PIMParameterizationHandlers;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.text.SimpleDateFormat;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.event.MouseEvent;

import com.vp.plugin.ApplicationManager;
import com.vp.plugin.ViewManager;
import com.vp.plugin.diagram.IDiagramElement;
import com.vp.plugin.diagram.connector.IAssociationUIModel;
import com.vp.plugin.diagram.shape.IClassUIModel;
import com.vp.plugin.diagram.shape.IPackageUIModel;
import com.vp.plugin.model.*;
import com.vp.plugin.model.factory.IModelElementFactory;
import com.vp.plugin.view.*;

import code.plugin.vp.Utilities.UserInterfaceUtil;
import code.plugin.vp.Utilities.XML;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import code.plugin.vp.Structures.*;
import code.plugin.vp.Structures.PIMParameterization.DesignConcernMarking;
import code.plugin.vp.Structures.PIMParameterization.MarkedUmlElement;
import code.plugin.vp.Structures.PIMParameterization.VPProject;

public class PIMParameterizationHandler implements IDialogHandler {
    List<PDM> PDMs;
    List<PDM> SelectedPdms;
    
    Map<String, Map<PDM, List<DesignConcernMarking>>> markedDesignConcern;
    List<MarkedUmlElement> ParameterizedUmlElements;
    DesignConcernMarkingHandler designConcernsHandler;

    JTree PdmTree = new JTree();
    JButton SaveButton = new JButton("Save");
    JButton CancelButton = new JButton("Cancel");
    JButton CloseButton = new JButton("Close");
    

    public PIMParameterizationHandler(String paraPdmXmlFile) {
        markedDesignConcern = new HashMap<String, Map<PDM, List<DesignConcernMarking>>>();
        PDMs = XML.ImportPDMs(paraPdmXmlFile);

        // get the pdm
        ViewManager vm = ApplicationManager.instance().getViewManager();
        ChoosePDMHandler pdmHandler = new ChoosePDMHandler(PDMs, "Select PDM(s) for parameterization", ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        vm.showDialog(pdmHandler);
        SelectedPdms = pdmHandler.getPdm();

        if(SelectedPdms.isEmpty()){
            
            JDialog dialog = (JDialog) SwingUtilities.getRoot(this.getComponent());
            dialog.dispose();
        }

        designConcernsHandler = null;

        ParameterizedUmlElements = XML.getParameterizedUmlElements();
        if(ParameterizedUmlElements == null){
            ParameterizedUmlElements = new ArrayList<MarkedUmlElement>();
        }
        getComponent();
        
    }

    @Override
    public Component getComponent() {
        JPanel mainPane = new JPanel();

        mainPane.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 2, 2, 2);

        // Title
        gbc = UserInterfaceUtil.setGridBagConstraints(gbc, 0, 0, 2, 10, 30, 0.25, 4);
        mainPane.add(new JLabel("PIM Parameterization", SwingConstants.CENTER), gbc);

        // PDMs tree
        PdmTree = new JTree(UmlElementsTree.getUMLElementsTree(SelectedPdms));
        JScrollPane pdmTreeScroll = new JScrollPane(PdmTree);
        pdmTreeScroll.setPreferredSize(new Dimension(400, 500));
        gbc = UserInterfaceUtil.setGridBagConstraints(gbc, 0, 1, 2, 10, 30, 0.25, 4);
        mainPane.add(pdmTreeScroll, gbc);

        // Save Button
        JPanel mainControls = new JPanel();
        mainControls.setLayout(new FlowLayout());

        SaveButton.setPreferredSize(new Dimension(125, 30));
        mainControls.add(SaveButton);

        CancelButton.setPreferredSize(new Dimension(125, 30));
        mainControls.add(CancelButton);

        CloseButton.setPreferredSize(new Dimension(125, 30));
        mainControls.add(CloseButton);

        gbc = UserInterfaceUtil.setGridBagConstraints(gbc, 0, 2, 2, 10, 0, 0.50, 8);
        mainPane.add(mainControls, gbc);

        return mainPane;
    }

    @Override
    public void prepare(IDialog dialog) {
        dialog.setModal(true);
        dialog.setTitle("MDE - PIM Parameterization");
        dialog.setResizable(true);
        dialog.pack();
    }

    @Override
    public void shown() {

        // Save Button
        SaveButton.addActionListener((ActionListener) new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                IProject project = ApplicationManager.instance().getProjectManager().getProject();
            
                

                VPProject markedProject = new VPProject(project.getId(), project.getName(),
                        new SimpleDateFormat("dd-MM-yyyy").format(new Date()), ParameterizedUmlElements);

                List<VPProject> markedProjects = new ArrayList<VPProject>();
                markedProjects.add(markedProject);

                // Export Project (Parameterzed PIM Model to XML)
                List<VPProject> xmlMarkedProjects = markedProjects;
                for (VPProject vpProject : xmlMarkedProjects) {
                    for (MarkedUmlElement mue : vpProject.getMarkedUmlElements()){
                        if(mue.getDesignConcerns().isEmpty()){
                            vpProject.getMarkedUmlElements().remove(mue);
                        }
                    }
                }

                
                XML.ExportParameterizedPIM(xmlMarkedProjects);
                
                // Apply the design concerns to the PIM
                PIMModelParameterization(markedProject);

                JOptionPane.showMessageDialog(null, "Saved");
                UserInterfaceUtil.CloseDialog(e);
            }
        });

        //Cancel Button
        CancelButton.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to cancel?","Cancel", JOptionPane.YES_NO_OPTION);

            if (result == JOptionPane.YES_OPTION) {
                UserInterfaceUtil.CloseDialog(e);
            }
        });

        //Close Button
        CloseButton.addActionListener(e -> {
            UserInterfaceUtil.CloseDialog(e);
        });

        // Select Uml Element from Tree to mark
        PdmTree.addMouseListener((MouseListener) new MouseInputAdapter() {
            public void mouseClicked(MouseEvent me) {
                TreePath umlElementPath = PdmTree.getPathForLocation(me.getX(), me.getY());

                if (umlElementPath.getParentPath().getParentPath() != null) {
                    DefaultMutableTreeNode pdmNode = (DefaultMutableTreeNode) umlElementPath.getPathComponent(1);
                    PDM selectedUmlElementPdm = (PDM) pdmNode.getUserObject();

                    // Get informations from the selected UML element
                    DefaultMutableTreeNode umlElementNode = (DefaultMutableTreeNode) umlElementPath.getLastPathComponent();
                    IModelElement selectedUmlElement = (IModelElement) umlElementNode.getUserObject();

                    
                    String umlElementType = selectedUmlElement.getModelType();
                    if (umlElementType.equals("Class")) {
                        umlElementType = "Classifier";
                    }

                    // UML element full qualified name
                    String fullQualifiedName = getFullyQualifiedName(selectedUmlElement);
                    
                    // Begin Marking
                    ViewManager vm = ApplicationManager.instance().getViewManager();

                    Map<PDM, List<DesignConcernMarking>> pdmDDsMap =  markedDesignConcern.get(selectedUmlElement.getId());
                    List<DesignConcernMarking> markedDDs = null;
                    if(pdmDDsMap != null){
                        markedDDs = pdmDDsMap.get(selectedUmlElementPdm);
                    }
                    
                    

                    designConcernsHandler = new DesignConcernMarkingHandler(selectedUmlElementPdm, selectedUmlElement.getId(), umlElementType, markedDDs);
                    //designConcernsHandler = new DesignConcernMarkingHandler(selectedUmlElementPdm, selectedUmlElement.getId(), umlElementType, markedDesignConcern.get(selectedUmlElementPdm));
                    vm.showDialog(designConcernsHandler);

                    HashMap<PDM, List<DesignConcernMarking>> pdmDDs = new HashMap<PDM, List<DesignConcernMarking>>();
                    pdmDDs.put(selectedUmlElementPdm, designConcernsHandler.getDesignConcernsMarking());
                    markedDesignConcern.put(selectedUmlElement.getId(), pdmDDs);
                    
                    //markedDesignConcern = designConcernsHandler.getDesignConcernsMarking();

                    if(ParameterizedUmlElements.stream().filter(pue -> pue.getId().equals(selectedUmlElement.getId())).findFirst().isPresent()){
                        MarkedUmlElement existedUmlEmenet = ParameterizedUmlElements.stream().filter(ue -> selectedUmlElement.getId().equals(ue.getId())).findFirst().orElse(null);

                        List<DesignConcernMarking> existedDDs = existedUmlEmenet.getDesignConcerns();
                        
                        existedUmlEmenet.setDesignConcerns(new ArrayList<DesignConcernMarking>());
                        
                        for (PDM pdm : markedDesignConcern.get(selectedUmlElement.getId()).keySet()) {
                            for (DesignConcernMarking ddmarking : markedDesignConcern.get(selectedUmlElement.getId()).get(pdm))
                            {
                                existedUmlEmenet.getDesignConcerns().add(ddmarking);
                            }
                        }

                        for (String existedDDId : existedDDs.stream().map((dd) -> dd.getDesignConcern().getId().toString()).collect(Collectors.toList()))
                        {
                            if(!existedUmlEmenet.getDesignConcerns().stream().map((dd) -> dd.getDesignConcern().getId().toString()).collect(Collectors.toList()).contains(existedDDId)){
                                DesignConcernMarking ddtoAdd = existedDDs.stream().filter(ue -> ue.getDesignConcern().getId().equals(UUID.fromString(existedDDId))).findFirst().orElse(null);
                                if(ddtoAdd != null){
                                    existedUmlEmenet.getDesignConcerns().add(ddtoAdd);
                                }
                            }
                        }
                        
                    }
                    else{
                        ParameterizedUmlElements.add(new MarkedUmlElement(selectedUmlElement.getId(),   
                        fullQualifiedName,
                        selectedUmlElement.getName(),
                        umlElementType,
                        designConcernsHandler.getDesignConcernsMarking()));
                    }
                }
            }
        });
    }

    @Override
    public boolean canClosed() {
        return true;
    }

    // UML element fully qualified name
    public String getFullyQualifiedName(IModelElement model){
        String fullName = model.getName();
        IModelElement parentModel = model.getParent();
        
        if(model instanceof IAssociationEnd){
            IAssociationEnd assoModel = (IAssociationEnd) model;
            parentModel = assoModel.getTypeAsModel();
            if(assoModel.getName() == null || assoModel.getName().isEmpty()){
                fullName = "[NoName]";
            }
        }

        if(model instanceof IRelationship){
            IRelationship relation = (IRelationship) model;
            parentModel = relation.getTo().getParent();
            if(relation.getName() == null || relation.getName().isEmpty()){
                fullName = "[NoName]";
            }
        }

        List<String> parents = new ArrayList<String>();
        parents.add(fullName);

        while(parentModel != null){
            parents.add(parentModel.getName());
            parentModel = parentModel.getParent();
        }

        String[] parentsArray = parents.toArray(new String[0]);
        for(int i = parentsArray.length - 1; i >= 0; i--){

            if(i == parentsArray.length - 1){
                fullName = parentsArray[i]  + '.';
            }
            else if(i == 0){
                fullName += parentsArray[i];
            }
            else{
                fullName += parentsArray[i] + '.';
            }
            
        }
        
        return fullName;
    }
      
     

    // Apply the Stereotypes and Tagged Values
    public void PIMModelParameterization(VPProject project) {
        List<String> yesStereo = new ArrayList<String>();
        yesStereo.add("yes");
        yesStereo.add("Yes");
        
        Map<IModelElement, MarkedUmlElement> markedUmlElements = getMarkedDiagramElements(project);


        for (Map.Entry<IModelElement, MarkedUmlElement> element : markedUmlElements.entrySet()) {
            IModelElement modelElement = element.getKey();
            MarkedUmlElement markedUmlElement = element.getValue();

            ITaggedValueContainer taggedValuesContainer = IModelElementFactory.instance().createTaggedValueContainer();
            

           
           
            if (!markedUmlElement.getDesignConcerns().isEmpty()) {
                for (DesignConcernMarking designConcern : markedUmlElement.getDesignConcerns()) {
                    
                    if (designConcern.getDesignConcern().getType().equals("Stereotype") 
                        && yesStereo.contains(designConcern.getValue())
                        && !modelElement.hasStereotype(designConcern.getDesignConcern().getName())) 
                    {
                        modelElement.addStereotype(createStereoType(designConcern, modelElement));
                    }

                    if (designConcern.getDesignConcern().getType().equals("Tagged Value")) {
                        
                        ITaggedValueContainer existingTVContainer = modelElement.getTaggedValues();
                        // if there is no one => create the first one
                        if (existingTVContainer == null) {
                            taggedValuesContainer.addTaggedValue(createTaggedValues(designConcern));
                            modelElement.setTaggedValues(taggedValuesContainer);
                        }
                        else {
                            // Look if the tagged value already exist and update value
                            ITaggedValue[] existingTaggedValues = existingTVContainer.toTaggedValueArray();

                            boolean found = false;

                            for (int i = 0; i < existingTaggedValues.length; i++) {
                                if (existingTaggedValues[i].getName().equals(designConcern.getDesignConcern().getName())) {
                                    existingTaggedValues[i].setValue(designConcern.getValue());
                                    found = true;
                                }
                            }

                            // If the tagged value is not in the already existing ones => create it
                            if (!found) {
                                taggedValuesContainer.addTaggedValue(createTaggedValues(designConcern));
                                modelElement.setTaggedValues(taggedValuesContainer);
                            }
                        }
                    }

                }

            }
        }
    }

    //Map the diagram UML elements to the created marked uml elements
    private Map<IModelElement, MarkedUmlElement> getMarkedDiagramElements(VPProject project) {
        Map<IModelElement, MarkedUmlElement> markeDiagramElements = new HashMap<IModelElement, MarkedUmlElement>();

        IDiagramElement[] diagramUmlElements = ApplicationManager.instance().getDiagramManager().getActiveDiagram()
                .toDiagramElementArray();

        for (MarkedUmlElement markedumlElement : project.getMarkedUmlElements()) {
            for (IDiagramElement diagramElement : diagramUmlElements) {
                IModelElement modelElement = diagramElement.getModelElement();

                if (modelElement.getId().equals(markedumlElement.getId())) {
                    if(diagramElement instanceof IClassUIModel){
                        IClassUIModel classShape = (IClassUIModel) diagramElement;
                        IModelElement classModel = classShape.getModelElement();
                        markeDiagramElements.put(classModel, markedumlElement);
                    }

                    if(diagramElement instanceof IPackageUIModel){
                        IPackageUIModel packageShape = (IPackageUIModel) diagramElement;
                        IModelElement packageModel = packageShape.getModelElement();
                        markeDiagramElements.put(packageModel, markedumlElement);
                    }

                    if(diagramElement instanceof IAssociationUIModel){
                        IAssociationUIModel associationShape = (IAssociationUIModel) diagramElement;
                        IModelElement associationModel = associationShape.getModelElement();
                        markeDiagramElements.put(associationModel, markedumlElement);
                    }
                    
                }
            }
        }

        // For the class childrens (Attribute, Operation, Parameter and AssociationEnd)
        for (IDiagramElement diagramElement : diagramUmlElements) {
            if (diagramElement instanceof IClassUIModel) {
                IClassUIModel classShape = (IClassUIModel) diagramElement;
                IClass classModel = (IClass) classShape.getModelElement();

                for (IModelElement classChild : classModel.toChildArray()) {
                    for (MarkedUmlElement markedumlElement : project.getMarkedUmlElements()) {
                        if ((markedumlElement.getType().equals("Attribute")
                                || markedumlElement.getType().equals("Operation"))
                                && markedumlElement.getId().equals(classChild.getId())) {
                            markeDiagramElements.put(classChild, markedumlElement);
                            break;

                        }
                    }
                }

                // Parameters
                for (IOperation operation : classModel.toOperationArray()) {
                    for (IModelElement parameter : operation.toParameterArray()) {
                        for (MarkedUmlElement markedumlElement : project.getMarkedUmlElements()) {

                            if (markedumlElement.getType().equals("Parameter")
                                    && markedumlElement.getId().equals(parameter.getId())) {
                                markeDiagramElements.put(parameter, markedumlElement);
                                break;
                            }
                        }
                    }
                }

                //AssociationEnds
                IModelElement[] toAssociation = classModel.toToRelationshipEndArray();         
                IModelElement[] fromAssociation = classModel.toFromRelationshipEndArray();    
                int tol = toAssociation.length;        
                int froml = fromAssociation.length;   
                IModelElement[] allAssociationEnds = new IModelElement[tol + froml];  
                System.arraycopy(toAssociation, 0, allAssociationEnds, 0, tol);  
                System.arraycopy(fromAssociation, 0, allAssociationEnds, tol, froml);
                
                for (IModelElement association : allAssociationEnds) {
                    for (MarkedUmlElement markedumlElement : project.getMarkedUmlElements()) {
                        if (markedumlElement.getType().equals("AssociationEnd")
                            && markedumlElement.getId().equals(association.getId())) {
                            markeDiagramElements.put(association, markedumlElement);
                            break;
                        }
                    }
                }
            }
        }
        

        return markeDiagramElements;
    }

    //Create the Stereotype
    private IStereotype  createStereoType(DesignConcernMarking designConcern, IModelElement modelElement){

        String modelType = "";

        if(modelElement instanceof IPackage){
            modelType = IModelElementFactory.MODEL_TYPE_PACKAGE;
        }

        if(modelElement instanceof IClass){
            modelType = IModelElementFactory.MODEL_TYPE_CLASS;
        }

        if(modelElement instanceof IAttribute){
            modelType = IModelElementFactory.MODEL_TYPE_ATTRIBUTE;
        }

        if(modelElement instanceof IOperation){
            modelType = IModelElementFactory.MODEL_TYPE_OPERATION;
        }

        if(modelElement instanceof IParameter){
            modelType = IModelElementFactory.MODEL_TYPE_PARAMETER;
        }

        if(modelElement instanceof IAssociation){
            modelType = IModelElementFactory.MODEL_TYPE_ASSOCIATION;
        }

        if(modelElement instanceof IAssociationEnd){
            modelType = IModelElementFactory.MODEL_TYPE_ASSOCIATION_END;
        }

        if(modelElement instanceof IGeneralization){
            modelType = IModelElementFactory.MODEL_TYPE_GENERALIZATION;
        }

        if(modelElement instanceof IDependency){
            modelType = IModelElementFactory.MODEL_TYPE_DEPENDENCY;
        }


        IStereotype stereo = IModelElementFactory.instance().createStereotype();
        stereo.setName(designConcern.getDesignConcern().getName());
        stereo.setBaseType(modelType);
        return stereo;
    }

    //Create Tagged Value
    private ITaggedValue createTaggedValues(DesignConcernMarking designConcern){

        ITaggedValue taggedValue = IModelElementFactory.instance().createTaggedValue();
        taggedValue.setName(designConcern.getDesignConcern().getName());
		taggedValue.setType(ITaggedValueDefinition.TYPE_STRING);
        taggedValue.setValue(designConcern.getValue());

        return taggedValue;
    }
}