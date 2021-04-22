package code.plugin.vp.Handlers;

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
import code.plugin.vp.Utilities.Constants;
import code.plugin.vp.Utilities.UserInterfaceUtil;

public class UmlProfileConstraintHandler implements IDialogHandler {
    
    //Logic 
    Constraint UmlProfileConstraint;
    List<Concept> Concepts;

    //Components
    JTextField Id = new JTextField();
    JTextField Name = new JTextField();
    JComboBox<String> AConcept = new JComboBox<>();
    JComboBox<String> BConcept = new JComboBox<>();
    JComboBox<String> Type = new JComboBox<>();
    JTextArea Description = new JTextArea(5, 20);
    JButton SaveButton = new JButton("Save");
    JButton CancelButton = new JButton("Cancel");
    JButton CloseButton = new JButton("Close");

    public UmlProfileConstraintHandler(UUID paraId, List<Concept> concepts) {
        Concepts = concepts;
        DefaultComboBoxModel<String> typeModel = new DefaultComboBoxModel<String>(Constants.ConstraintTypes);
        Type.setModel(typeModel);
        
        List<String> StrConcepts = new ArrayList<String>();
        for(Concept concept: concepts){
            StrConcepts.add(concept.getName());
        }

        DefaultComboBoxModel<String> conceptAModel = new DefaultComboBoxModel<String>(StrConcepts.toArray(new String[0]));
        AConcept.setModel(conceptAModel);

        DefaultComboBoxModel<String> conceptBModel = new DefaultComboBoxModel<String>(StrConcepts.toArray(new String[0]));
        BConcept.setModel(conceptBModel);

        getComponent();

        Id.setText(String.valueOf(paraId));
        UmlProfileConstraint = new Constraint();
    }

    public UmlProfileConstraintHandler(Constraint constraint, List<Concept> concepts) {
        Concepts = concepts;

        DefaultComboBoxModel<String> typeModel = new DefaultComboBoxModel<String>(Constants.ConstraintTypes);
        Type.setModel(typeModel);
        Type.setSelectedIndex(Arrays.asList(Constants.ConstraintTypes).indexOf(constraint.getType()));

        List<String> StrConcepts = new ArrayList<String>();
        for(Concept concept: concepts){
            StrConcepts.add(concept.getName());
        }

        DefaultComboBoxModel<String> conceptAModel = new DefaultComboBoxModel<String>(StrConcepts.toArray(new String[0]));
        AConcept.setModel(conceptAModel);
        AConcept.setSelectedIndex(Arrays.asList(StrConcepts.toArray(new String[0])).indexOf(constraint.getConceptA().getName()));

        DefaultComboBoxModel<String> conceptBModel = new DefaultComboBoxModel<String>(StrConcepts.toArray(new String[0]));
        BConcept.setModel(conceptBModel);
        BConcept.setSelectedIndex(Arrays.asList(StrConcepts.toArray(new String[0])).indexOf(constraint.getConceptB().getName()));

        Id.setText(String.valueOf(constraint.getId()));
        Name.setText(constraint.getName());
        Description.setText(constraint.getDescription());

        getComponent();
        
        UmlProfileConstraint = constraint;
    }

    @Override
    public Component getComponent() {
        JPanel mainPane = new JPanel();

        mainPane.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 2, 2, 2);

        //Id
        gbc = UserInterfaceUtil.setGridBagConstraints(gbc, 0, 0, 2, 10, 30, 0.25, 4);
        mainPane.add(new JLabel("Id : "), gbc);

        gbc = UserInterfaceUtil.setGridBagConstraints(gbc, 4, 0, 2, 10, 10, 0.75, 4);
        Id.setEditable(false);
        mainPane.add(Id, gbc);

        //Name 
        gbc = UserInterfaceUtil.setGridBagConstraints(gbc, 0, 1, 2, 10, 30, 0.25, 4);
        mainPane.add(new JLabel("Name : "), gbc);

        gbc = UserInterfaceUtil.setGridBagConstraints(gbc, 4, 1, 2, 10, 10, 0.75, 4);
        mainPane.add(Name, gbc);

        //Concept A 
        gbc = UserInterfaceUtil.setGridBagConstraints(gbc, 0, 2, 2, 10, 30, 0.25, 4);
        mainPane.add(new JLabel("Concept A : "), gbc);
 
        gbc = UserInterfaceUtil.setGridBagConstraints(gbc, 4, 2, 2, 10, 10, 0.75, 4);
        mainPane.add(AConcept, gbc);

        //Concept B
        gbc = UserInterfaceUtil.setGridBagConstraints(gbc, 0, 3, 2, 10, 30, 0.25, 4);
        mainPane.add(new JLabel("Concept B : "), gbc);

        gbc = UserInterfaceUtil.setGridBagConstraints(gbc, 4, 3, 2, 10, 10, 0.75, 4);
        mainPane.add(BConcept, gbc);
        
        //Type
        gbc = UserInterfaceUtil.setGridBagConstraints(gbc, 0, 4, 2, 10, 10, 0.25, 4);
        mainPane.add(new JLabel("Type : "), gbc);

        gbc = UserInterfaceUtil.setGridBagConstraints(gbc, 4, 4, 2, 10, 30, 0.75, 4);
        mainPane.add(Type, gbc);

        //Description
        gbc = UserInterfaceUtil.setGridBagConstraints(gbc, 0, 5, 2, 10, 10, 0.25, 4);
        gbc.anchor = GridBagConstraints.NORTHWEST;
        mainPane.add(new JLabel("Description : "), gbc);

        gbc = UserInterfaceUtil.setGridBagConstraints(gbc, 4, 5, 2, 10, 30, 0.75, 4);
        mainPane.add(Description, gbc);

        //Save Button
        JPanel mainControls = new JPanel();
        mainControls.setLayout(new FlowLayout());

        SaveButton.setPreferredSize(new Dimension(125, 30));
        mainControls.add(SaveButton);

        CancelButton.setPreferredSize(new Dimension(125, 30));
        mainControls.add(CancelButton);

        CloseButton.setPreferredSize(new Dimension(125, 30));
        mainControls.add(CloseButton);

        gbc = UserInterfaceUtil.setGridBagConstraints(gbc, 0, 6, 2, 10, 0, 0.50, 8);
        mainPane.add(mainControls, gbc);
        
        return mainPane;
    }

    @Override
    public void prepare(IDialog dialog) {
        dialog.setModal(true);
        dialog.setTitle("MDE - UML Profile Constraint");
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
                    UmlProfileConstraint.setId(UUID.fromString(Id.getText()));
                    UmlProfileConstraint.setName(Name.getText());
                    UmlProfileConstraint.setType(Type.getSelectedItem().toString());
                    UmlProfileConstraint.setDescription(Description.getText());

                    for(Concept concept: Concepts){
                        if(concept.getName() == AConcept.getSelectedItem().toString()){
                            UmlProfileConstraint.setConceptA(concept);
                        }
                        if(concept.getName() == BConcept.getSelectedItem().toString()){
                            UmlProfileConstraint.setConceptB(concept);
                        }
                    }

                    JOptionPane.showMessageDialog(null, "Constraint added");
                    UserInterfaceUtil.CloseDialog(e);
                }
            }
        });

        //Cancel Button
        CancelButton.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(null, "Are sure you want to cancel?","Cancel", JOptionPane.YES_NO_OPTION);

            if (result == JOptionPane.YES_OPTION) {
                UserInterfaceUtil.CloseDialog(e);
            }
        });

        //Close Button
        CloseButton.addActionListener(e -> {
            UserInterfaceUtil.CloseDialog(e);
        });
    }

    @Override
    public boolean canClosed() {
        return true;
    }


    public Constraint getUmlProfileConstraint(){
        return UmlProfileConstraint;
    }

}