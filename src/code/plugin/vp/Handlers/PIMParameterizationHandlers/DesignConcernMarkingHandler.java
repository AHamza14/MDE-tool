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

    JTable DesignConcernsTable = new JTable(new DefaultTableModel(Constants.DesignConcernMarkingTableColumns, 0));
    PDM Pdm;
    JButton SaveButton = new JButton("Save");
    JButton CancelButton = new JButton("Cancel");
    JButton CloseButton = new JButton("Close");

    public DesignConcernMarkingHandler(PDM pdm, String umlElementId, String umlElementType,  List<DesignConcernMarking> alreadyMarkedDCs){

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
                        DefaultTableModel model = (DefaultTableModel) DesignConcernsTable.getModel();
                        
                        if(designConcern.getType().equals("Stereotype")){
                            TableColumn valueColumn = DesignConcernsTable.getColumnModel().getColumn(3);
                            JComboBox<String> comboBox = new JComboBox<>();
                            
                            //For tagged values make there default values: for example if tagged value is boolean it must be "true" or "false"
                            comboBox.addItem("Yes");
                            comboBox.addItem("No");
                            valueColumn.setCellEditor(new DefaultCellEditor(comboBox));
                        }
                        
                        model.addRow(new Object[]{designConcern.getId(), designConcern.getName(), designConcern.getType(), designConcernValue==null?"":designConcernValue});
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

        //Design Concern Table
        gbc = UserInterfaceUtil.setGridBagConstraints(gbc, 0, 1, 2, 10, 30, 0.25, 4);
        DesignConcernsTable.getColumnModel().getColumn(0).setWidth(80);
        DesignConcernsTable.getColumnModel().getColumn(1).setWidth(150);
        JScrollPane designConcernsScroll = new JScrollPane(DesignConcernsTable);
        designConcernsScroll.setPreferredSize(new Dimension(300, 150));
        mainPane.add(designConcernsScroll, gbc);

        //Save Button
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
        // gbc = UserInterfaceUtil.setGridBagConstraints(gbc, 0, 2, 2, 10, 30, 0.25, 4);
        // mainPane.add(SaveButton, gbc);

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

                if(DesignConcernsTable.getModel().getRowCount() > 0){
                    DefaultTableModel model = (DefaultTableModel) DesignConcernsTable.getModel();
                    if(DesignConcerns.isEmpty()){
                        for (int i=0; i < model.getRowCount(); i++) {
                            //if(model.getValueAt(i, 3).toString() != ""){
                                for (Concept concept : Pdm.getPdmUmlProfile().getConcepts()) {
                                    for (DesignConcern dd : concept.getDesignConcerns()) {
                                        if(model.getValueAt(i, 3).toString() != ""){
                                            if(dd.getId().equals(UUID.fromString(model.getValueAt(i, 0).toString()))){
                                            
                                                DesignConcerns.add(new DesignConcernMarking(dd, model.getValueAt(i, 3).toString(), Pdm.getName()));
                                            }
                                        }
                                    }
                                }
                            
                            //}
                        }
                    }
                    else{
                        for (DesignConcernMarking dd : DesignConcerns) {
                            for (int i=0; i < model.getRowCount(); i++) {
                                if(model.getValueAt(i, 0).toString().equals(dd.getDesignConcern().getId().toString())){
                                    dd.setValue(model.getValueAt(i, 3).toString());
                                    //break;
                                }
                            }   
                        }
                    }
                    JOptionPane.showMessageDialog(null, "Saved");
                    UserInterfaceUtil.CloseDialog(e);
                }
                else{
                    JOptionPane.showMessageDialog(null, "The Design Concerns Table is empty");
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

    public List<DesignConcernMarking> getDesignConcernsMarking(){
        return DesignConcerns;
    }
}