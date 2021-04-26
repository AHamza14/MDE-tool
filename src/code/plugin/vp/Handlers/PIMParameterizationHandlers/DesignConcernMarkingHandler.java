package code.plugin.vp.Handlers.PIMParameterizationHandlers;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import com.vp.plugin.view.*;

import code.plugin.vp.Structures.*;
import code.plugin.vp.Structures.PIMParameterization.DesignConcernMarking;
import code.plugin.vp.Utilities.Constants;
import code.plugin.vp.Utilities.UserInterfaceUtil;
import code.plugin.vp.Utilities.XML;

public class DesignConcernMarkingHandler implements IDialogHandler {
    
    private List<DesignConcernMarking> DesignConcerns;

    JTable StereotypesTable = new JTable(new DefaultTableModel(Constants.DesignConcernMarkingTableColumns, 0));

    JTable TaggedValuesTable = new JTable(new DefaultTableModel(Constants.DesignConcernMarkingTableColumns, 0));

    PDM Pdm;

    JButton SaveButton = new JButton("Save");
    JButton CancelButton = new JButton("Cancel");
    JButton CloseButton = new JButton("Close");

    public DesignConcernMarkingHandler(PDM pdm, String umlElementId, String umlElementType,  List<DesignConcernMarking> alreadyMarkedDCs){

        //Create column with predefined values "yes" or "no" for steretypes.
        TableColumn valueColumn = StereotypesTable.getColumnModel().getColumn(2);
        JComboBox<String> comboBox = new JComboBox<>();
        
        comboBox.addItem("Yes");
        comboBox.addItem("No");
        valueColumn.setCellEditor(new DefaultCellEditor(comboBox));
        //end

        if(alreadyMarkedDCs == null){
            DesignConcerns = new ArrayList<DesignConcernMarking>();
        }
        else{
            DesignConcerns = alreadyMarkedDCs;
        }
        
        this.Pdm = pdm;
        
        String designConcernValue = "";
        Map<String, String> markedDesignConcerns = XML.getUmlElementDesignConcern(umlElementId);
        
        for (Concept concept : pdm.getPdmUmlProfile().getConcepts()) {
            for (DesignConcern designConcern : concept.getDesignConcerns()) {
                    if(designConcern.getUmlElements().contains(umlElementType)){
                        if(markedDesignConcerns != null){
                            designConcernValue = markedDesignConcerns.get(designConcern.getId().toString());
                        }
                        if(!DesignConcerns.isEmpty()){
                            DesignConcernMarking alreadyDesignConcern = DesignConcerns.stream().filter(ue -> designConcern.getId().equals(ue.getDesignConcern().getId())).findFirst().orElse(null);
                            designConcernValue = alreadyDesignConcern == null? null: alreadyDesignConcern.getValue();
                        }

                        if(designConcern.getType().equals("Stereotype")){
                            DefaultTableModel model = (DefaultTableModel) StereotypesTable.getModel();
                            model.addRow(new Object[]{designConcern.getId(), designConcern.getName(), designConcernValue==null?"":designConcernValue});
                        }

                        else if(designConcern.getType().equals("Tagged Value")){
                            DefaultTableModel model = (DefaultTableModel) TaggedValuesTable.getModel();
                            model.addRow(new Object[]{designConcern.getId(), designConcern.getName(), designConcernValue==null?"":designConcernValue});
                        }
                        
                    }
            }
        }
    }

    @Override
    public Component getComponent() {
        JPanel mainPane = new JPanel();

        mainPane.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 2, 2, 2);

        //Title
        gbc = UserInterfaceUtil.setGridBagConstraints(gbc, 0, 0, 2, 10, 30, 0.25, 4);
        mainPane.add(new JLabel("Marking Design Concerns", SwingConstants.CENTER), gbc);

        //Design Concern (Stereotypes) Table
        gbc = UserInterfaceUtil.setGridBagConstraints(gbc, 0, 1, 2, 10, 30, 0.25, 4);
        StereotypesTable.getColumnModel().getColumn(0).setWidth(50);
        StereotypesTable.getColumnModel().getColumn(1).setWidth(200);
        JScrollPane stereotypesScroll = new JScrollPane(StereotypesTable);
        stereotypesScroll.setPreferredSize(new Dimension(300, 150));
        mainPane.add(stereotypesScroll, gbc);

        //Design Concern (Tagged values) Table
        gbc = UserInterfaceUtil.setGridBagConstraints(gbc, 0, 2, 2, 10, 30, 0.25, 4);
        TaggedValuesTable.getColumnModel().getColumn(0).setWidth(50);
        TaggedValuesTable.getColumnModel().getColumn(1).setWidth(200);
        JScrollPane taggedValuesScroll = new JScrollPane(TaggedValuesTable);
        taggedValuesScroll.setPreferredSize(new Dimension(300, 150));
        mainPane.add(taggedValuesScroll, gbc);

        //Save Button
        JPanel mainControls = new JPanel();
        mainControls.setLayout(new FlowLayout());

        SaveButton.setPreferredSize(new Dimension(125, 30));
        mainControls.add(SaveButton);

        CancelButton.setPreferredSize(new Dimension(125, 30));
        mainControls.add(CancelButton);

        CloseButton.setPreferredSize(new Dimension(125, 30));
        mainControls.add(CloseButton);

        gbc = UserInterfaceUtil.setGridBagConstraints(gbc, 0, 3, 2, 10, 0, 0.50, 8);
        mainPane.add(mainControls, gbc);

        return mainPane;
    }

    @Override
    public void prepare(IDialog dialog) {
        dialog.setModal(true);
        dialog.setTitle("MDE - Select Design Concerns");
        dialog.setResizable(true);
        dialog.pack();
    }

    @Override
    public void shown() {
        // Save Button
        SaveButton.addActionListener((ActionListener) new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DefaultTableModel stereotypesModel = (DefaultTableModel) StereotypesTable.getModel();
                DefaultTableModel taggedValuesModel = (DefaultTableModel) TaggedValuesTable.getModel();

                if(stereotypesModel.getRowCount() > 0 || taggedValuesModel.getRowCount() > 0){
                    saveDesignConcernTable(stereotypesModel);
                    saveDesignConcernTable(taggedValuesModel);

                    JOptionPane.showMessageDialog(null, "Saved");
                    UserInterfaceUtil.CloseDialog(e);
                }
                else{
                    JOptionPane.showMessageDialog(null, "The Design Concerns Tables are empty");
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
        
    }

    @Override
    public boolean canClosed() {
        return true;
    }

    private void saveDesignConcernTable(DefaultTableModel designConcernModel){

        for (int i=0; i < designConcernModel.getRowCount(); i++) {

            String ddId = designConcernModel.getValueAt(i, 0).toString();
            DesignConcernMarking existedDesicnConcernMarking = DesignConcerns.stream().filter(dd -> UUID.fromString(ddId).equals(dd.getDesignConcern().getId())).findFirst().orElse(null);

            if(existedDesicnConcernMarking == null){
                for (Concept concept : Pdm.getPdmUmlProfile().getConcepts()) {
                    for (DesignConcern dd : concept.getDesignConcerns()) {
                        if(designConcernModel.getValueAt(i, 2).toString() != "" 
                        && dd.getId().equals(UUID.fromString(ddId))){
                            
                            DesignConcerns.add(new DesignConcernMarking(dd, designConcernModel.getValueAt(i, 2).toString(), Pdm.getName()));
                        }
                    }
                }
            }
            else{
                existedDesicnConcernMarking.setValue(designConcernModel.getValueAt(i, 2).toString());
            }
        } 
    }

    public List<DesignConcernMarking> getDesignConcernsMarking(){
        return DesignConcerns;
    }
}