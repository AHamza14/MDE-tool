package code.plugin.vp.UserInterface;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.vp.plugin.ApplicationManager;
import com.vp.plugin.ViewManager;
import com.vp.plugin.view.*;

import code.plugin.vp.Structures.Concept;
import code.plugin.vp.Structures.DesignConcern;
import code.plugin.vp.Utilities.Enums;
import code.plugin.vp.Utilities.UserInterfaceUtil;

public class UmlProfileConceptDialog implements IDialogHandler {

    //Logic
    Concept UmlProfileConcept;

    //Component
    JTextField Id = new JTextField();
    JTextField Name = new JTextField();
    JComboBox<String> Type = new JComboBox<>();;
    JTextArea Description = new JTextArea(5, 20);
    JButton SaveButton = new JButton("Save");
    JButton CancelButton = new JButton("Cancel");
    JButton CloseButton = new JButton("Close");

    //Design Concern
    JTable  DesignConcernTable = new JTable(new DefaultTableModel(Enums.TableColumns, 0));;
    JButton DesignConcernAddButton = new JButton("Add");
    JButton DesignConcernEditButton = new JButton("Edit");
    JButton DesignConcernRemoveButton = new JButton("Remove");

    List<DesignConcern> DesignConcerns = new ArrayList<DesignConcern>();
    UUID DesignConcernUUID = UUID.randomUUID();

    public UmlProfileConceptDialog(UUID paraId) {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>(Enums.UmlProfileConceptTypes);
        Type.setModel(model);

        getComponent();

        Id.setText(String.valueOf(paraId));
        UmlProfileConcept = new Concept();
    }

    public UmlProfileConceptDialog(Concept concept) {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>(Enums.UmlProfileConceptTypes);
        Type.setModel(model);
        Type.setSelectedIndex(Arrays.asList(Enums.UmlProfileConceptTypes).indexOf(concept.getType()));

        Id.setText(String.valueOf(concept.getId()));
        Name.setText(concept.getName());
        Description.setText(concept.getDescription());

        DesignConcerns = concept.getDesignConcerns();
        DesignConcernTable.getColumnModel().getColumn(0).setMaxWidth(80);
        for(DesignConcern dc: DesignConcerns){
            DefaultTableModel tableModel = (DefaultTableModel) DesignConcernTable.getModel();
            tableModel.addRow(new Object[]{dc.getId(), dc.getName()});
        }

        getComponent();
        
        
        UmlProfileConcept = concept;
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

        //Description
        gbc = UserInterfaceUtil.setGridBagConstraints(gbc, 0, 3, 2, 10, 10, 0.25, 4);
        gbc.anchor = GridBagConstraints.NORTHWEST;
        mainPane.add(new JLabel("Description : "), gbc);

        gbc = UserInterfaceUtil.setGridBagConstraints(gbc, 4, 3, 2, 10, 30, 0.75, 4);
        mainPane.add(Description, gbc);

        /*-------------- Design Concerns Panel----------------------*/
        JPanel designConcernsPane = new JPanel();
        designConcernsPane.setLayout(new GridBagLayout());
        
        GridBagConstraints designConcernsgbc = new GridBagConstraints();
        designConcernsgbc.insets = new Insets(2, 2, 2, 2);

        //Design Concerns Panel : Table
        designConcernsgbc = UserInterfaceUtil.setGridBagConstraints(designConcernsgbc, 0, 4, 2, 10, 10, 0.0, 3);
        DesignConcernTable.getColumnModel().getColumn(0).setMaxWidth(80);
        JScrollPane DDtableScroll = new JScrollPane(DesignConcernTable);
        DDtableScroll.setPreferredSize(new Dimension(300, 100));
        designConcernsPane.add(DDtableScroll, designConcernsgbc);

        //Design Concerns Panel : Add Button
        designConcernsgbc = UserInterfaceUtil.setGridBagConstraints(designConcernsgbc, 0, 5, 2, 10, 10, 0.5, 1);
        designConcernsPane.add(DesignConcernAddButton, designConcernsgbc);

        //Design Concerns Panel: Remove Button
        designConcernsgbc = UserInterfaceUtil.setGridBagConstraints(designConcernsgbc, 1, 5, 2, 10, 10, 0.5, 1);
        designConcernsPane.add(DesignConcernRemoveButton, designConcernsgbc);

        //Design Concerns Panel: Edit Button
        designConcernsgbc = UserInterfaceUtil.setGridBagConstraints(designConcernsgbc, 2, 5, 2, 10, 10, 0.5, 1);
        designConcernsPane.add(DesignConcernEditButton, designConcernsgbc);

        designConcernsPane.setBorder(BorderFactory.createTitledBorder("Design concerns"));
        gbc = UserInterfaceUtil.setGridBagConstraints(gbc, 0, 4, 2, 10, 30, 0.25, 8);
        mainPane.add(designConcernsPane, gbc);
        /*---------------Design Concerns Panel End ----------------*/
        JPanel mainControls = new JPanel();
        mainControls.setLayout(new FlowLayout());

        SaveButton.setPreferredSize(new Dimension(125, 30));
        mainControls.add(SaveButton);

        CancelButton.setPreferredSize(new Dimension(125, 30));
        mainControls.add(CancelButton);

        CloseButton.setPreferredSize(new Dimension(125, 30));
        mainControls.add(CloseButton);

        gbc = UserInterfaceUtil.setGridBagConstraints(gbc, 0, 5, 2, 10, 0, 0.50, 8);
        mainPane.add(mainControls, gbc);
        
        return mainPane;
    }

    @Override
    public void prepare(IDialog dialog) {
        dialog.setModal(true);
        dialog.setTitle("MDE - UML Profile Concept");
        dialog.setResizable(false);
        dialog.pack();
    }

    @Override
    public void shown() {
        //Add a Design Concern
        DesignConcernAddButton.addActionListener((ActionListener) new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                if(!DesignConcerns.isEmpty()){
                    DesignConcernUUID = UUID.randomUUID();
                }

                ViewManager vm = ApplicationManager.instance().getViewManager();
                DesignConcernDialog d = new DesignConcernDialog(DesignConcernUUID);
                vm.showDialog(d);

                if(d.getDesignConcern().getId() != null){
                    DesignConcerns.add(d.getDesignConcern());

                    DefaultTableModel model = (DefaultTableModel) DesignConcernTable.getModel();
                    model.addRow(new Object[]{d.getDesignConcern().getId(), d.getDesignConcern().getName()});

                   // DesignConcernCounter++;
                }
            }
        });

        //Remove a Design Concern
        DesignConcernRemoveButton.addActionListener((ActionListener) new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int column = 1;
                int selectedRow = DesignConcernTable.getSelectedRow();

                String designConcernName = (String) DesignConcernTable.getModel().getValueAt(selectedRow, column);
                for(DesignConcern dd : DesignConcerns) {
                    if(dd.getName() == designConcernName) {
                        DesignConcerns.remove(dd);
                        break;
                    }
                }

                ((DefaultTableModel)DesignConcernTable.getModel()).removeRow(selectedRow);
            }
        });

        //Edit a Design Concern
        DesignConcernEditButton.addActionListener((ActionListener) new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int column = 1;
                int selectedRow = DesignConcernTable.getSelectedRow();

                String designConcernName = (String) DesignConcernTable.getModel().getValueAt(selectedRow, column);
                for(DesignConcern dd : DesignConcerns) {
                    if(dd.getName() == designConcernName) {

                        ViewManager vm = ApplicationManager.instance().getViewManager();
                        DesignConcernDialog d = new DesignConcernDialog(dd);
                        vm.showDialog(d);

                        dd.setName(d.getDesignConcern().getName());
                        dd.setDescription(d.getDesignConcern().getDescription());
                        dd.setType(d.getDesignConcern().getType());
                        dd.setUmlElements(d.getDesignConcern().getUmlElements());

                        DefaultTableModel model = (DefaultTableModel) DesignConcernTable.getModel();
                        model.setValueAt(d.getDesignConcern().getName(), selectedRow, 1);
                        break;
                    }
                }
            }   
        });
        
        SaveButton.addActionListener((ActionListener) new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(Name.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null, "Please, Complete all the fields !!");
                }
                else{
                    UmlProfileConcept.setId(UUID.fromString(Id.getText()));
                    UmlProfileConcept.setName(Name.getText());
                    UmlProfileConcept.setType(Type.getSelectedItem().toString());
                    UmlProfileConcept.setDescription(Description.getText());
                    UmlProfileConcept.setDesignConcerns(DesignConcerns);
                
                    JOptionPane.showMessageDialog(null, "Concept added");
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

    public Concept getConcept(){
        return UmlProfileConcept;
    }

}