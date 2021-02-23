package code.plugin.vp.Handlers;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.vp.plugin.ApplicationManager;
import com.vp.plugin.ViewManager;
import com.vp.plugin.view.*;

import code.plugin.vp.Structures.Concept;
import code.plugin.vp.Structures.Constraint;
import code.plugin.vp.Structures.PDM;
import code.plugin.vp.Structures.TransformationTemplate;
import code.plugin.vp.Structures.UmlProfile;
import code.plugin.vp.Utilities.Constants;
import code.plugin.vp.Utilities.UserInterfaceUtil;


public class PDMDetailsHandler implements IDialogHandler {
    
    //Main Panel
    JTextField PDMId = new JTextField();
    JTextField PDMName = new JTextField();
    JTextArea PDMDescription = new JTextArea(5, 20);
    JButton MainSaveButton = new JButton("Save");
    JButton MainCancelButton = new JButton("Cancel");
    JButton MainCloseButton = new JButton("Close");

    //PDM logic
    PDM Pdm;
    // Uml Profile : Logic
    UmlProfile UmlProfile = new UmlProfile();

    //Uml Profile Panel : Concepts
    JButton UmlProfileConceptAddButton = new JButton("Add");
    JButton UmlProfileConceptRemoveButton = new JButton("Remove");
    JButton UmlProfileConceptEditButton = new JButton("Edit");
    JTable UmlProfileConceptTable = new JTable(new DefaultTableModel(Constants.TableColumns, 0));

    List<Concept> Concepts = new ArrayList<Concept>();
    UUID ConceptUUID = UUID.randomUUID();

    //Uml Profile Panel : Constraint
    JButton UmlProfileConstraintAddButton = new JButton("Add");
    JButton UmlProfileConstraintRemoveButton = new JButton("Remove");
    JButton UmlProfileConstraintEditButton = new JButton("Edit");
    JTable UmlProfileConstraintTable = new JTable(new DefaultTableModel(Constants.TableColumns, 0));

    List<Constraint> Constraints = new ArrayList<Constraint>();
    UUID ConstraintUUID = UUID.randomUUID();

    //Transformation Template Panel
    JButton TransTemplateAddButton = new JButton("Add");
    JButton TransTemplateRemoveButton = new JButton("Remove");
    JButton TransTemplateEditButton = new JButton("Edit");
    JTable TransTemplateTable = new JTable(new DefaultTableModel(Constants.TableColumns, 0));

    List<TransformationTemplate> TransTemplates = new ArrayList<TransformationTemplate>();
    UUID TTUUID = UUID.randomUUID();
    
    //Constructors
    public PDMDetailsHandler(UUID paraId) {

        getComponent();

        PDMId.setText(String.valueOf(paraId));
        Pdm = new PDM();
    }

    public PDMDetailsHandler(PDM pdm) {

        PDMId.setText(String.valueOf(pdm.getId()));
        PDMName.setText(pdm.getName());
        PDMDescription.setText(pdm.getDescription());

        //Concepts
        Concepts = pdm.getPdmUmlProfile().getConcepts();
        UmlProfileConceptTable.getColumnModel().getColumn(0).setMaxWidth(80);
        for(Concept concept: Concepts){
            DefaultTableModel tableModel = (DefaultTableModel) UmlProfileConceptTable.getModel();
            tableModel.addRow(new Object[]{concept.getId(), concept.getName()});
        }

        //Constraints
        Constraints = pdm.getPdmUmlProfile().getConstraints();
        UmlProfileConstraintTable.getColumnModel().getColumn(0).setMaxWidth(80);
        for(Constraint constraint: Constraints){
            DefaultTableModel tableModel = (DefaultTableModel) UmlProfileConstraintTable.getModel();
            tableModel.addRow(new Object[]{constraint.getId(), constraint.getName()});
        }

        //Transformation Templates
        TransTemplates = pdm.getPdmTransformationTemplate();
        TransTemplateTable.getColumnModel().getColumn(0).setMaxWidth(80);
        for(TransformationTemplate tt: TransTemplates){
            DefaultTableModel tableModel = (DefaultTableModel) TransTemplateTable.getModel();
            tableModel.addRow(new Object[]{tt.getId(), tt.getName()});
        }

        getComponent();
        
        
        Pdm = pdm;
    }

    @Override
    public Component getComponent() {


        /*---------------Concepts Panel-------------------*/
        JPanel umlProfileConceptsPane = new JPanel();

        umlProfileConceptsPane.setLayout(new GridBagLayout());
        GridBagConstraints umlProfileConceptgbc = new GridBagConstraints();
        umlProfileConceptgbc.insets = new Insets(2, 2, 2, 2);

        //Concepts Panel : Table
        umlProfileConceptgbc = UserInterfaceUtil.setGridBagConstraints(umlProfileConceptgbc, 0, 1, 2, 10, 10, 0.0, 3);
        UmlProfileConceptTable.getColumnModel().getColumn(0).setMaxWidth(80);
        JScrollPane UmlProfileConceptTableScroll = new JScrollPane(UmlProfileConceptTable);
        UmlProfileConceptTableScroll.setPreferredSize(new Dimension(300, 300));
        umlProfileConceptsPane.add(UmlProfileConceptTableScroll, umlProfileConceptgbc);

        //Concepts Panel : Add Button
        umlProfileConceptgbc = UserInterfaceUtil.setGridBagConstraints(umlProfileConceptgbc, 0, 2, 2, 10, 10, 0.5, 1);
        umlProfileConceptsPane.add(UmlProfileConceptAddButton, umlProfileConceptgbc);

        //Concepts Panel : Remove Button
        umlProfileConceptgbc = UserInterfaceUtil.setGridBagConstraints(umlProfileConceptgbc, 1, 2, 2, 10, 10, 0.5, 1);
        umlProfileConceptsPane.add(UmlProfileConceptRemoveButton, umlProfileConceptgbc);

        //Concepts Panel : Edit Button
        umlProfileConceptgbc = UserInterfaceUtil.setGridBagConstraints(umlProfileConceptgbc, 2, 2, 2, 10, 10, 0.5, 1);
        umlProfileConceptsPane.add(UmlProfileConceptEditButton, umlProfileConceptgbc);


        /*------------------Constraints Panel-------------------*/
        JPanel umlProfileConstraintsPane = new JPanel();

        umlProfileConstraintsPane.setLayout(new GridBagLayout());
        GridBagConstraints umlProfileConstraintgbc = new GridBagConstraints();
        umlProfileConstraintgbc.insets = new Insets(2, 2, 2, 2);

        //Constraints Panel : Table
        umlProfileConstraintgbc = UserInterfaceUtil.setGridBagConstraints(umlProfileConstraintgbc, 0, 4, 2, 10, 10, 0.0, 3);
        UmlProfileConstraintTable.getColumnModel().getColumn(0).setMaxWidth(80);
        JScrollPane ConstraintstableScroll = new JScrollPane(UmlProfileConstraintTable);
        ConstraintstableScroll.setPreferredSize(new Dimension(300, 300));
        umlProfileConstraintsPane.add(ConstraintstableScroll, umlProfileConstraintgbc);

        //Constraints Panel : Add Button
        umlProfileConstraintgbc = UserInterfaceUtil.setGridBagConstraints(umlProfileConstraintgbc, 0, 5, 2, 10, 10, 0.5, 1);
        umlProfileConstraintsPane.add(UmlProfileConstraintAddButton, umlProfileConstraintgbc);

        //Constraints Panel: Remove Button
        umlProfileConstraintgbc = UserInterfaceUtil.setGridBagConstraints(umlProfileConstraintgbc, 1, 5, 2, 10, 10, 0.5, 1);
        umlProfileConstraintsPane.add(UmlProfileConstraintRemoveButton, umlProfileConstraintgbc);

        //Constraints Panel: Edit Button
        umlProfileConstraintgbc = UserInterfaceUtil.setGridBagConstraints(umlProfileConstraintgbc, 2, 5, 2, 10, 10, 0.5, 1);
        umlProfileConstraintsPane.add(UmlProfileConstraintEditButton, umlProfileConstraintgbc);

        /*------------- Uml Profile Panel ----------------*/
        JPanel umlProfilePane = new JPanel();

        umlProfilePane.setLayout(new GridBagLayout());
        GridBagConstraints umlProfilegbc = new GridBagConstraints();
        umlProfilegbc.insets = new Insets(2, 2, 2, 2);

        //Uml Profile Panel : Concepts Panel
        umlProfilegbc = UserInterfaceUtil.setGridBagConstraints(umlProfilegbc, 0, 0, 2, 10, 10, 0.5, 3);
        umlProfileConceptsPane.setBorder(BorderFactory.createTitledBorder("Concepts"));
        umlProfilePane.add(umlProfileConceptsPane, umlProfilegbc);

        //Uml Profile Panel : Constraints Panel
        umlProfilegbc = UserInterfaceUtil.setGridBagConstraints(umlProfilegbc, 4, 0, 2, 10, 10, 0.5, 3);
        umlProfileConstraintsPane.setBorder(BorderFactory.createTitledBorder("Constraints"));
        umlProfilePane.add(umlProfileConstraintsPane, umlProfilegbc);

        /*------------- Transformation Template Panel ----------------*/
        JPanel transTemplatePane = new JPanel();
        
        transTemplatePane.setLayout(new GridBagLayout());
        GridBagConstraints transTemplategbc = new GridBagConstraints();
        transTemplategbc.insets = new Insets(2, 2, 2, 2);

        //Transformation Template Panel : Table
        transTemplategbc = UserInterfaceUtil.setGridBagConstraints(transTemplategbc, 0, 0, 2, 10, 10, 0.0, 3);
        TransTemplateTable.getColumnModel().getColumn(0).setMaxWidth(80);
        JScrollPane TTtableScroll = new JScrollPane(TransTemplateTable);
        TTtableScroll.setPreferredSize(new Dimension(300, 300));
        transTemplatePane.add(TTtableScroll, transTemplategbc);

        //Transformation Template Panel : Add Button
        transTemplategbc = UserInterfaceUtil.setGridBagConstraints(transTemplategbc, 0, 1, 2, 10, 10, 0.5, 1);
        transTemplatePane.add(TransTemplateAddButton, transTemplategbc);

        //Transformation Template Panel : Remove Button
        transTemplategbc = UserInterfaceUtil.setGridBagConstraints(transTemplategbc, 1, 1, 2, 10, 10, 0.5, 1);
        transTemplatePane.add(TransTemplateRemoveButton, transTemplategbc);

        //Transformation Template Panel : Edit Button
        transTemplategbc = UserInterfaceUtil.setGridBagConstraints(transTemplategbc, 2, 1, 2, 10, 10, 0.5, 1);
        transTemplatePane.add(TransTemplateEditButton, transTemplategbc);


        /*-------------Tabbed panel-----------------*/
        JTabbedPane tabbedPane = new JTabbedPane();

        //Tabbed panel : UML Profile
        tabbedPane.addTab("UML Profile", umlProfilePane);

        //Tabbed panel : Transformation Template
        tabbedPane.addTab("Transformation Template", transTemplatePane);

        /*------------- Main Panel ----------------*/
        JPanel mainPane = new JPanel();
        mainPane.setLayout(new GridBagLayout());
        //mainPane.setPreferredSize(new Dimension(640, 480));

        //Main Panel : Grid Bag Constraints
        GridBagConstraints mainGbc = new GridBagConstraints();
        mainGbc.insets = new Insets(2, 2, 2, 2);

        //Main Panel : Id
        mainGbc = UserInterfaceUtil.setGridBagConstraints(mainGbc, 0, 0, 2, 10, 30, 0.25, 2);
        mainPane.add(new JLabel("Id : "), mainGbc);

        mainGbc = UserInterfaceUtil.setGridBagConstraints(mainGbc, 2, 0, 2, 10, 10, 0.75, 5);
        PDMId.setEditable(false);
        mainPane.add(PDMId, mainGbc);

        //Main Panel : Name 
        mainGbc = UserInterfaceUtil.setGridBagConstraints(mainGbc, 0, 1, 2, 10, 30, 0.25, 2);
        mainPane.add(new JLabel("Name : "), mainGbc);

        mainGbc = UserInterfaceUtil.setGridBagConstraints(mainGbc, 2, 1, 2, 10, 10, 0.75, 5);
        mainPane.add(PDMName, mainGbc);

        //Main Panel : Description
        mainGbc = UserInterfaceUtil.setGridBagConstraints(mainGbc, 0, 2, 2, 10, 30, 0.25, 2);
        mainGbc.anchor = GridBagConstraints.NORTHWEST;
        mainPane.add(new JLabel("Description : "), mainGbc);

        mainGbc = UserInterfaceUtil.setGridBagConstraints(mainGbc, 2, 2, 2, 10, 10, 0.75, 5);
        mainPane.add(PDMDescription, mainGbc);

        //Main Panel : Tabbed  panel (Uml profile and transformation template)
        mainGbc = UserInterfaceUtil.setGridBagConstraints(mainGbc, 0, 3, 2, 10, 10, 0.0, 10);
        //tabbedPane.setPreferredSize(new Dimension(500, 500));
        mainPane.add(tabbedPane, mainGbc);

        //Main Panel : Save 
        JPanel mainControls = new JPanel();
        mainControls.setLayout(new FlowLayout());

        MainSaveButton.setPreferredSize(new Dimension(150, 30));
        mainControls.add(MainSaveButton);

        MainCancelButton.setPreferredSize(new Dimension(150, 30));
        mainControls.add(MainCancelButton);

        MainCloseButton.setPreferredSize(new Dimension(150, 30));
        mainControls.add(MainCloseButton);

        mainGbc = UserInterfaceUtil.setGridBagConstraints(mainGbc, 0, 4, 2, 10, 0, 0.50, 8);
        mainPane.add(mainControls, mainGbc);
        // mainGbc = UserInterfaceUtil.setGridBagConstraints(mainGbc, 1, 4, 2, 10, 10, 0.5, 3);
        // mainPane.add(MainSaveButton, mainGbc);

        // //Main Panel : Cancel 
        // mainGbc = UserInterfaceUtil.setGridBagConstraints(mainGbc, 4, 4, 2, 10, 10, 0.5, 3);
        // mainPane.add(MainCancelButton, mainGbc);

        // //Main Panel : Close 
        // mainGbc = UserInterfaceUtil.setGridBagConstraints(mainGbc, 7, 4, 2, 10, 10, 0.5, 3);
        // mainPane.add(MainCloseButton, mainGbc);
        
        return mainPane;
    }

    @Override
    public void prepare(IDialog dialog) {
        dialog.setModal(true);
        dialog.setTitle("MDE - PDM Specification");
        dialog.setResizable(true);
        dialog.pack();
    }

    @Override
    public void shown() {

        // Add a Uml Profile Concept
        UmlProfileConceptAddButton.addActionListener((ActionListener) new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                ViewManager vm = ApplicationManager.instance().getViewManager();
                UmlProfileConceptHandler d = new UmlProfileConceptHandler(ConceptUUID);
                vm.showDialog(d);

                if(d.getConcept().getId() != null){
                    Concepts.add(d.getConcept());

                    DefaultTableModel model = (DefaultTableModel) UmlProfileConceptTable.getModel();
                    model.addRow(new Object[]{d.getConcept().getId(), d.getConcept().getName()});
                    ConceptUUID = UUID.randomUUID();
                }
            }
        });

         //Remove a Uml Profile Concept
         UmlProfileConceptRemoveButton.addActionListener((ActionListener) new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int column = 1;
                int selectedRow = UmlProfileConceptTable.getSelectedRow();

                String conceptName = (String) UmlProfileConceptTable.getModel().getValueAt(selectedRow, column);
                for(Concept con : Concepts) {
                    if(con.getName() == conceptName) {
                        Concepts.remove(con);
                        break;
                    }
                }

                ((DefaultTableModel)UmlProfileConceptTable.getModel()).removeRow(selectedRow);
            }
        });

        //Edit a Uml Profile Concept
        UmlProfileConceptEditButton.addActionListener((ActionListener) new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                int column = 1;
                int selectedRow = UmlProfileConceptTable.getSelectedRow();

                String conceptName = (String) UmlProfileConceptTable.getModel().getValueAt(selectedRow, column);
                for(Concept concept : Concepts) {
                    if(concept.getName() == conceptName) {

                        ViewManager vm = ApplicationManager.instance().getViewManager();
                        UmlProfileConceptHandler d = new UmlProfileConceptHandler(concept);
                        vm.showDialog(d);

                        concept.setName(d.getConcept().getName());
                        concept.setDescription(d.getConcept().getDescription());
                        concept.setType(d.getConcept().getType());
                        concept.setDesignConcerns(d.getConcept().getDesignConcerns());

                        DefaultTableModel model = (DefaultTableModel) UmlProfileConceptTable.getModel();
                        model.setValueAt(d.getConcept().getName(), selectedRow, 1);
                        break;
                    }
                }
            }   
        });

        // Add a Uml Profile Constraint
        UmlProfileConstraintAddButton.addActionListener((ActionListener) new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                ViewManager vm = ApplicationManager.instance().getViewManager();
                UmlProfileConstraintHandler d = new UmlProfileConstraintHandler(ConstraintUUID,Concepts);
                vm.showDialog(d);

                if(d.getUmlProfileConstraint().getId() != null){
                    Constraints.add(d.getUmlProfileConstraint());

                    DefaultTableModel model = (DefaultTableModel) UmlProfileConstraintTable.getModel();
                    model.addRow(new Object[]{d.getUmlProfileConstraint().getId(), d.getUmlProfileConstraint().getName()});
                    ConstraintUUID = UUID.randomUUID();
                }
            }
        });

        //Edit a Uml Profile Constraint
        UmlProfileConstraintEditButton.addActionListener((ActionListener) new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                int column = 1;
                int selectedRow = UmlProfileConstraintTable.getSelectedRow();

                String constraintName = (String) UmlProfileConstraintTable.getModel().getValueAt(selectedRow, column);
                for(Constraint constraint : Constraints) {
                    if(constraint.getName() == constraintName) {

                        ViewManager vm = ApplicationManager.instance().getViewManager();
                        UmlProfileConstraintHandler d = new UmlProfileConstraintHandler(constraint, Concepts);
                        vm.showDialog(d);

                        constraint.setName(d.getUmlProfileConstraint().getName());
                        constraint.setDescription(d.getUmlProfileConstraint().getDescription());
                        constraint.setType(d.getUmlProfileConstraint().getType());
                        constraint.setConceptA(d.getUmlProfileConstraint().getConceptA());
                        constraint.setConceptB(d.getUmlProfileConstraint().getConceptB());

                        DefaultTableModel model = (DefaultTableModel) UmlProfileConstraintTable.getModel();
                        model.setValueAt(d.getUmlProfileConstraint().getName(), selectedRow, 1);
                        break;
                    }
                }
            }   
        });

        //Remove a Uml Profile Constraint
        UmlProfileConstraintRemoveButton.addActionListener((ActionListener) new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int column = 1;
                int selectedRow = UmlProfileConstraintTable.getSelectedRow();

                String constraintName = (String) UmlProfileConstraintTable.getModel().getValueAt(selectedRow, column);
                for(Constraint constraint : Constraints) {
                    if(constraint.getName() == constraintName) {
                        Constraints.remove(constraint);
                        break;
                    }
                }

                ((DefaultTableModel)UmlProfileConstraintTable.getModel()).removeRow(selectedRow);
            }
        });

        // Add a Transformtion Template
        TransTemplateAddButton.addActionListener((ActionListener) new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ViewManager vm = ApplicationManager.instance().getViewManager();
                TransformationTemplateHandler d = new TransformationTemplateHandler(TTUUID, Concepts);
                vm.showDialog(d);

                if(d.getTransformationTemplate().getId() != null){
                    TransTemplates.add(d.getTransformationTemplate());

                    DefaultTableModel model = (DefaultTableModel) TransTemplateTable.getModel();
                    model.addRow(new Object[]{d.getTransformationTemplate().getId(), d.getTransformationTemplate().getName()});
                    TTUUID = UUID.randomUUID();
                }
            }
        });

        //Remove a Transformation Temaplate
        TransTemplateRemoveButton.addActionListener((ActionListener) new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int column = 1;
                int selectedRow = TransTemplateTable.getSelectedRow();

                String ttName = (String) TransTemplateTable.getModel().getValueAt(selectedRow, column);
                for(TransformationTemplate tt : TransTemplates) {
                    if(tt.getName() == ttName) {
                        TransTemplates.remove(tt);
                        break;
                    }
                }

                ((DefaultTableModel)TransTemplateTable.getModel()).removeRow(selectedRow);
            }
        });

        //Edit a Transformation Temaplate
        TransTemplateEditButton.addActionListener((ActionListener) new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                int column = 1;
                int selectedRow = TransTemplateTable.getSelectedRow();

                String ttName = (String) TransTemplateTable.getModel().getValueAt(selectedRow, column);
                for(TransformationTemplate tt : TransTemplates) {
                    if(tt.getName() == ttName) {

                        ViewManager vm = ApplicationManager.instance().getViewManager();
                        TransformationTemplateHandler d = new TransformationTemplateHandler(tt, Concepts);
                        vm.showDialog(d);

                        tt.setName(d.getTransformationTemplate().getName());
                        tt.setDescription(d.getTransformationTemplate().getDescription());
                        tt.setPrimaryImplementedConcept(d.getTransformationTemplate().getPrimaryImplementedConcept());
                        tt.setImplmementedConceptsForVariation(d.getTransformationTemplate().getImplmementedConceptsForVariation());
                        tt.setType(d.getTransformationTemplate().getType());
                        tt.setFileUri(d.getTransformationTemplate().getFileUri());

                        DefaultTableModel model = (DefaultTableModel) TransTemplateTable.getModel();
                        model.setValueAt(d.getTransformationTemplate().getName(), selectedRow, 1);
                        break;
                    }
                }
            }   
        });

        // Main Save Button
        MainSaveButton.addActionListener((ActionListener) new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(PDMName.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null, "Please, Complete all the fields !!");
                }
                else{
                    Pdm.setId(UUID.fromString(PDMId.getText()));
                    Pdm.setName(PDMName.getText());
                    Pdm.setDescription(PDMDescription.getText());
                    Pdm.setCreatingDate(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));

                    UmlProfile.setConcepts(Concepts);
                    UmlProfile.setConstraints(Constraints);
                    Pdm.setPdmUmlProfile(UmlProfile);

                    Pdm.setPdmTransformationTemplate(TransTemplates);
                    JOptionPane.showMessageDialog(null, "Saved");
                }
            }
        });

        //Main Cancel Button
        MainCancelButton.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(null, "Are sure you want to cancel?","Cancel", JOptionPane.YES_NO_OPTION);

            if (result == JOptionPane.YES_OPTION) {
                UserInterfaceUtil.CloseDialog(e);
            }
        });

        //Main Close Button
        MainCloseButton.addActionListener(e -> {
            UserInterfaceUtil.CloseDialog(e);
        });
        
    }

    
    @Override
    public boolean canClosed() {
        return true;
    }


    public PDM getPdm(){
        return Pdm;
    }
}