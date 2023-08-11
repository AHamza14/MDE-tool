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

import code.plugin.vp.Structures.DesignConcern;
import code.plugin.vp.Utilities.Enums;
import code.plugin.vp.Utilities.UserInterfaceUtil;

public class DesignConcernDialog implements IDialogHandler {
    
    //Logic
    DesignConcern DesignConcern;

    //Components
    JTextField Id = new JTextField();
    JTextField Name = new JTextField();
    JComboBox<String> Type = new JComboBox<String>();
    JCheckBox Package = new JCheckBox("Package");
    JCheckBox Classifier = new JCheckBox("Classifier");
    JCheckBox Attribute = new JCheckBox("Attribute");
    JCheckBox Operation = new JCheckBox("Operation");
    JCheckBox Parameter = new JCheckBox("Parameter");
    JCheckBox Generalization = new JCheckBox("Generalization");
    JCheckBox Association = new JCheckBox("Association");
    JCheckBox AssociationEnd = new JCheckBox("AssociationEnd");
    JCheckBox Dependency = new JCheckBox("Dependency");
    JCheckBox UmlElementsCheckBox[] = new JCheckBox[] {Package, Classifier, Attribute, Operation, Parameter, Generalization, Association, AssociationEnd, Dependency};
    JTextArea Description = new JTextArea(5, 20);
    JButton SaveButton = new JButton("Save");
    JButton CancelButton = new JButton("Cancel");
    JButton CloseButton = new JButton("Close");

    public DesignConcernDialog(UUID paraId) {
        DefaultComboBoxModel<String> modelType = new DefaultComboBoxModel<String>(Enums.DesignConcernTypes);
        Type.setModel(modelType);

        getComponent();

        Id.setText(String.valueOf(paraId));
        DesignConcern = new DesignConcern();
    }

    public DesignConcernDialog(DesignConcern dd) {
        DefaultComboBoxModel<String> modelType = new DefaultComboBoxModel<String>(Enums.DesignConcernTypes);
        Type.setModel(modelType);
        Type.setSelectedIndex(Arrays.asList(Enums.DesignConcernTypes).indexOf(dd.getType()));

        //Here when modifing existing Design concern you must check the the uml elements of it
        for (JCheckBox jCheckBox : UmlElementsCheckBox) {
            if(dd.getUmlElements().contains(jCheckBox.getText())){
                jCheckBox.setSelected(true);
            }
        }
        
        Id.setText(String.valueOf(dd.getId()));
        Name.setText(dd.getName());
        Description.setText(dd.getDescription());

        getComponent();
        
        DesignConcern = dd;
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
        
        //Type
        gbc = UserInterfaceUtil.setGridBagConstraints(gbc, 0, 2, 2, 10, 10, 0.25, 4);
        mainPane.add(new JLabel("Type : "), gbc);

        gbc = UserInterfaceUtil.setGridBagConstraints(gbc, 4, 2, 2, 10, 30, 0.75, 4);
        mainPane.add(Type, gbc);

        //UML Element
        gbc = UserInterfaceUtil.setGridBagConstraints(gbc, 0, 3, 2, 10, 10, 0.25, 4);
        mainPane.add(new JLabel("UML Elements : "), gbc);

        gbc = UserInterfaceUtil.setGridBagConstraints(gbc, 4, 3, 2, 10, 30, 0.75, 1);
        mainPane.add(Package, gbc);

        gbc = UserInterfaceUtil.setGridBagConstraints(gbc, 5, 3, 2, 10, 30, 0.75, 1);
        mainPane.add(Classifier, gbc);

        gbc = UserInterfaceUtil.setGridBagConstraints(gbc, 6, 3, 2, 10, 30, 0.75, 1);
        mainPane.add(Attribute, gbc);

        gbc = UserInterfaceUtil.setGridBagConstraints(gbc, 4, 4, 2, 10, 30, 0.75, 1);
        mainPane.add(Operation, gbc);

        gbc = UserInterfaceUtil.setGridBagConstraints(gbc, 5, 4, 2, 10, 30, 0.75, 1);
        mainPane.add(Parameter, gbc);

        gbc = UserInterfaceUtil.setGridBagConstraints(gbc, 6, 4, 2, 10, 30, 0.75, 1);
        mainPane.add(Generalization, gbc);

        gbc = UserInterfaceUtil.setGridBagConstraints(gbc, 4, 5, 2, 10, 30, 0.75, 1);
        mainPane.add(Association, gbc);

        gbc = UserInterfaceUtil.setGridBagConstraints(gbc, 5, 5, 2, 10, 30, 0.75, 1);
        mainPane.add(AssociationEnd, gbc);

        gbc = UserInterfaceUtil.setGridBagConstraints(gbc, 6, 5, 2, 10, 30, 0.75, 1);
        mainPane.add(Dependency, gbc);

        //Description
        gbc = UserInterfaceUtil.setGridBagConstraints(gbc, 0, 6, 2, 10, 10, 0.25, 4);
        gbc.anchor = GridBagConstraints.NORTHWEST;
        mainPane.add(new JLabel("Description : "), gbc);

        gbc = UserInterfaceUtil.setGridBagConstraints(gbc, 4, 6, 2, 10, 30, 0.75, 4);
        mainPane.add(Description, gbc);

        // //Save
        // gbc = UserInterfaceUtil.setGridBagConstraints(gbc, 0, 7, 2, 10, 30, 0.75, 8);
        // mainPane.add(SaveButton, gbc);

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
        dialog.setTitle("MDE - Design Concern");
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
                    DesignConcern.setId(UUID.fromString(Id.getText()));
                    DesignConcern.setName(Name.getText());
                    DesignConcern.setType(Type.getSelectedItem().toString());
                    
                    List<String> umlElements = new ArrayList<String>();
                    for (JCheckBox jCheckBox : UmlElementsCheckBox) {
                        if(jCheckBox.isSelected()){
                            umlElements.add(jCheckBox.getText());
                        }
                    }

                    DesignConcern.setUmlElements(umlElements);
                    DesignConcern.setDescription(Description.getText());
                    
                    JOptionPane.showMessageDialog(null, "Design concern added");
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

        //Main Close Button
        CloseButton.addActionListener(e -> {
            UserInterfaceUtil.CloseDialog(e);
        });
    }

    @Override
    public boolean canClosed() {
        return true;
    }


    public DesignConcern getDesignConcern(){
        return DesignConcern;
    }

}