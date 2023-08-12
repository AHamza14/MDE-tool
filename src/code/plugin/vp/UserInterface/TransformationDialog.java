package code.plugin.vp.UserInterface;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.awt.*;
import javax.swing.*;

import com.vp.plugin.view.*;

import code.plugin.vp.Structures.*;
import code.plugin.vp.Utilities.Enums;
import code.plugin.vp.Utilities.UserInterfaceUtil;

public class TransformationDialog implements IDialogHandler {

    //Logic
    Transformation TransTemplate;
    List<Concept> Concepts;

    //Components
    JTextField Id = new JTextField();
    JTextField Name = new JTextField();
    JTextArea Description = new JTextArea(5, 20);
    JList<String> ConceptsForVariationList = new JList<>(); 
    JComboBox<String> PrimaryImplementedConcept = new JComboBox<>();
    JComboBox<String> Type = new JComboBox<>();
    JButton ChooseFile = new JButton("...");
    JTextField File = new JTextField();
    JButton SaveButton = new JButton("Save");
    JButton CancelButton = new JButton("Cancel");
    JButton CloseButton = new JButton("Close");

    public TransformationDialog(UUID paraId, List<Concept> concepts) {
        Concepts = concepts;

        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>(Enums.TransformationTypes);
        Type.setModel(model);

        DefaultListModel<String> conceptsForVariationModel = new DefaultListModel<String>();
        List<String> StrConcepts = new ArrayList<String>();
        for(Concept concept: concepts){
            StrConcepts.add(concept.getName());
            conceptsForVariationModel.addElement(concept.getName());
        }
        ConceptsForVariationList.setModel(conceptsForVariationModel);

        DefaultComboBoxModel<String> primaryImplementedConceptModel = new DefaultComboBoxModel<String>(StrConcepts.toArray(new String[0]));
        PrimaryImplementedConcept.setModel(primaryImplementedConceptModel);



        getComponent();

        Id.setText(String.valueOf(paraId));
        TransTemplate = new Transformation();
    }

    public TransformationDialog(Transformation tt, List<Concept> concepts) {
        Concepts = concepts;

        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>(Enums.TransformationTypes);
        Type.setModel(model);
        Type.setSelectedIndex(Arrays.asList(Enums.TransformationTypes).indexOf(tt.getType()));

        Id.setText(String.valueOf(tt.getId()));
        Name.setText(tt.getName());
        Description.setText(tt.getDescription());
        File.setText(tt.getFileUri());

        DefaultListModel<String> conceptsForVariationModel = new DefaultListModel<String>();
        List<String> StrConcepts = new ArrayList<String>();
        for(Concept concept: concepts){
            StrConcepts.add(concept.getName());
            conceptsForVariationModel.addElement(concept.getName());
        }

        DefaultComboBoxModel<String> primaryImplementedConcept = new DefaultComboBoxModel<String>(StrConcepts.toArray(new String[0]));
        PrimaryImplementedConcept.setModel(primaryImplementedConcept);
        
        ConceptsForVariationList.setModel(conceptsForVariationModel);
        List<Integer> conceptsVariationIndexs = new ArrayList<Integer>();
        for(Concept concept: concepts){
            if(concept == tt.getPrimaryImplementedConcept()){
                PrimaryImplementedConcept.setSelectedIndex(Arrays.asList(StrConcepts.toArray(new String[0])).indexOf(concept.getName()));
            }

            if(tt.getImplmementedConceptsForVariation().contains(concept)){
                conceptsVariationIndexs.add(Arrays.asList(StrConcepts.toArray(new String[0])).indexOf(concept.getName()));
            }
        }

        ConceptsForVariationList.setSelectedIndices(conceptsVariationIndexs.stream().mapToInt(i -> i).toArray());

        

        

        getComponent();
        
        TransTemplate = tt;
    }

    @Override
    public Component getComponent() {
        JPanel mainPane = new JPanel();
        mainPane.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 2, 2, 2);

        //Id
        gbc = UserInterfaceUtil.setGridBagConstraints(gbc, 0, 0, 2, 10, 30, 0.25, 2);
        mainPane.add(new JLabel("Id : "), gbc);

        gbc = UserInterfaceUtil.setGridBagConstraints(gbc, 4, 0, 2, 10, 10, 0.75, 6);
        Id.setEditable(false);
        mainPane.add(Id, gbc);

        //Name 
        gbc = UserInterfaceUtil.setGridBagConstraints(gbc, 0, 1, 2, 10, 30, 0.25, 2);
        mainPane.add(new JLabel("Name : "), gbc);

        gbc = UserInterfaceUtil.setGridBagConstraints(gbc, 4, 1, 2, 10, 10, 0.75, 6);
        mainPane.add(Name, gbc);
        
        //Description
        gbc = UserInterfaceUtil.setGridBagConstraints(gbc, 0, 2, 2, 10, 10, 0.25, 2);
        gbc.anchor = GridBagConstraints.NORTHWEST;
        mainPane.add(new JLabel("Description : "), gbc);

        gbc = UserInterfaceUtil.setGridBagConstraints(gbc, 4, 2, 2, 10, 30, 0.75, 6);
        mainPane.add(Description, gbc);

        //primary Implemented Concept
        gbc = UserInterfaceUtil.setGridBagConstraints(gbc, 0, 3, 2, 10, 10, 0.25, 2);
        mainPane.add(new JLabel("Primary implemented concept:"), gbc);

        gbc = UserInterfaceUtil.setGridBagConstraints(gbc, 4, 3, 2, 10, 30, 0.75, 6);
        mainPane.add(PrimaryImplementedConcept, gbc);

        //Implemented Concepts for Variation
        gbc = UserInterfaceUtil.setGridBagConstraints(gbc, 0, 4, 2, 10, 10, 0.25, 2);
        mainPane.add(new JLabel("Implemented concepts for variation:"), gbc);

        gbc = UserInterfaceUtil.setGridBagConstraints(gbc, 4, 4, 2, 10, 30, 0.75, 6);
        ConceptsForVariationList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane tableScroll = new JScrollPane(ConceptsForVariationList);
        tableScroll.setPreferredSize(new Dimension(100, 100));
        mainPane.add(tableScroll, gbc);

        //Type
        gbc = UserInterfaceUtil.setGridBagConstraints(gbc, 0, 5, 2, 10, 10, 0.25, 2);
        mainPane.add(new JLabel("Type : "), gbc);

        gbc = UserInterfaceUtil.setGridBagConstraints(gbc, 4, 5, 2, 10, 30, 0.75, 6);
        mainPane.add(Type, gbc);

        //File
        gbc = UserInterfaceUtil.setGridBagConstraints(gbc, 0, 6, 2, 10, 10, 0.25, 2);
        mainPane.add(new JLabel("File URI : "), gbc);

        gbc = UserInterfaceUtil.setGridBagConstraints(gbc, 3, 6, 2, 10, 30, 0.25, 4);
        File.setEditable(false);
        mainPane.add(File, gbc);

        gbc = UserInterfaceUtil.setGridBagConstraints(gbc, 7, 6, 1, 10, 30, 0.25, 1);
        mainPane.add(ChooseFile, gbc);

        //Save
        //Save Button
        JPanel mainControls = new JPanel();
        mainControls.setLayout(new FlowLayout());

        SaveButton.setPreferredSize(new Dimension(125, 30));
        mainControls.add(SaveButton);

        CancelButton.setPreferredSize(new Dimension(125, 30));
        mainControls.add(CancelButton);

        CloseButton.setPreferredSize(new Dimension(125, 30));
        mainControls.add(CloseButton);

        gbc = UserInterfaceUtil.setGridBagConstraints(gbc, 0, 7, 2, 10, 0, 0.50, 8);
        mainPane.add(mainControls, gbc);

        return mainPane;
    }

    @Override
    public void prepare(IDialog dialog) {
        dialog.setModal(true);
        dialog.setTitle("MDE - Transformation Template");
        dialog.setResizable(false);
        dialog.pack();
    }

    @Override
    public void shown() {
        // Save Button
        SaveButton.addActionListener((ActionListener) new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(Name.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null, "Please, Complete all the fields !!");
                }
                else{
                    TransTemplate.setId(UUID.fromString(Id.getText()));
                    TransTemplate.setName(Name.getText());
                    TransTemplate.setType(Type.getSelectedItem().toString());
                    TransTemplate.setDescription(Description.getText());
                    TransTemplate.setFileUri(File.getText());
                    
                    List<Concept> ImplementedConceptsList = new ArrayList<Concept>();
                    for(Concept concept: Concepts){
                        if(concept.getName() == PrimaryImplementedConcept.getSelectedItem().toString()){
                            TransTemplate.setPrimaryImplementedConcept(concept);
                        }

                        if(ConceptsForVariationList.getSelectedValuesList().toString().contains(concept.getName())){
                            ImplementedConceptsList.add(concept);
                        }
                    }
                    TransTemplate.setImplmementedConceptsForVariation(ImplementedConceptsList);

                    JOptionPane.showMessageDialog(null, "Transformation template Added");
                    UserInterfaceUtil.CloseDialog(e);
                }
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
        

        // Choose File Button
        ChooseFile.addActionListener((ActionListener) new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> filePaths =  UserInterfaceUtil.getFilePath("XSL Format", "XSL", null,"Choose transformation template", false);
                File.setText(filePaths.get(0) == null?"":filePaths.get(0).replace("\\\\", "\\"));
            }
        });
    }

    @Override
    public boolean canClosed() {
        return true;
    }

    public Transformation getTransformationTemplate(){
        return TransTemplate;
    }

}