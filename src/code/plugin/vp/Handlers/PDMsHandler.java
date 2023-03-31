package code.plugin.vp.Handlers;

import java.util.ArrayList;
import java.util.List;
//import java.util.Locale;
import java.util.UUID;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.vp.plugin.*;
import com.vp.plugin.view.*;

import code.plugin.vp.Structures.*;
import code.plugin.vp.Utilities.TemplateGeneration;
import code.plugin.vp.Utilities.XML;

public class PDMsHandler extends JFrame implements IDialogHandler {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    // Logic
    List<PDM> Pdm = new ArrayList<PDM>();
    UUID PdmUUID = UUID.randomUUID();

    // Components
    JButton AddButton = new JButton("Add");
    JButton RemoveButton = new JButton("Remove");
    JButton EditButton = new JButton("Edit");
    JButton SaveButton = new JButton("Save");
    JButton CancelButton = new JButton("Cancel");
    JButton CloseButton = new JButton("Close");
    JTable PdmTable;

    @Override
    public Component getComponent() {

        JPanel pane = new JPanel();
        pane.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 2, 2, 2);

        // PDM title
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipady = 40; // make this component tall
        gbc.weightx = 0.0;
        gbc.gridwidth = 3;
        JLabel pdmLabel = new JLabel("Platform Description Model (PDM)", SwingConstants.CENTER);
        pane.add(pdmLabel, gbc);

        // PDM Table
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipady = 10; // make this component tall
        gbc.weightx = 0.0;
        gbc.gridwidth = 3;
        String column_names[] = { "Id", "Name", "Creating Date" };
        PdmTable = new JTable(new DefaultTableModel(column_names, 0));
        PdmTable.getColumnModel().getColumn(0).setMaxWidth(80);
        JScrollPane tableScroll = new JScrollPane(PdmTable);
        tableScroll.setPreferredSize(new Dimension(300, 300));
        pane.add(tableScroll, gbc);

        // PDM Add Button
        gbc.ipady = 20;
        gbc.ipadx = 20;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.5;
        gbc.gridx = 0;
        gbc.gridy = 2;
        pane.add(AddButton, gbc);

        // PDM Remove Button
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.5;
        gbc.gridx = 1;
        gbc.gridy = 2;
        pane.add(RemoveButton, gbc);

        // PDM Edit Button
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.5;
        gbc.gridx = 2;
        gbc.gridy = 2;
        pane.add(EditButton, gbc);

        // PDM Save Button
        gbc.ipady = 20;
        gbc.ipadx = 20;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.5;
        gbc.gridx = 0;
        gbc.gridy = 3;
        pane.add(SaveButton, gbc);

        // PDM Cancel Button
        gbc.ipady = 20;
        gbc.ipadx = 20;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.5;
        gbc.gridx = 1;
        gbc.gridy = 3;
        pane.add(CancelButton, gbc);

        // PDM Close Button
        gbc.ipady = 20;
        gbc.ipadx = 20;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.5;
        gbc.gridx = 2;
        gbc.gridy = 3;
        pane.add(CloseButton, gbc);

        pane.setPreferredSize(new Dimension(640, 480));
        pane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        this.add(pane);
        this.setVisible(true);
        this.pack();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        
        //Add a PDM
        AddButton.addActionListener(e -> {
            ViewManager vm = ApplicationManager.instance().getViewManager();
            PDMDetailsHandler d = new PDMDetailsHandler(PdmUUID);
            vm.showDialog(d);
    
            if (d.getPdm().getId() != null) {
                Pdm.add(d.getPdm());
    
                DefaultTableModel model = (DefaultTableModel) PdmTable.getModel();
                model.addRow(
                        new Object[] { d.getPdm().getId(), d.getPdm().getName(), d.getPdm().getCreatingDate() });
                PdmUUID = UUID.randomUUID();
            }
        });

        // Edit a PDM
        EditButton.addActionListener(e -> {
            // Fix this because i will remove the "Id" column
            int column = 1;
            int selectedRow = PdmTable.getSelectedRow();
            String pdmName = (String) PdmTable.getModel().getValueAt(selectedRow, column);

            for (PDM pdm : Pdm) {
                if (pdm.getName() == pdmName) {
                    ViewManager vm = ApplicationManager.instance().getViewManager();
                    PDMDetailsHandler d = new PDMDetailsHandler(pdm);
                    vm.showDialog(d);

                    pdm.setName(d.getPdm().getName());
                    pdm.setDescription(d.getPdm().getDescription());
                    pdm.setCreatingDate(d.getPdm().getCreatingDate());
                    pdm.setPdmTransformationTemplate(d.getPdm().getPdmTransformationTemplate());
                    pdm.setPdmUmlProfile(d.getPdm().getPdmUmlProfile());

                    DefaultTableModel model = (DefaultTableModel) PdmTable.getModel();
                    model.setValueAt(d.getPdm().getName(), selectedRow, 1);
                    model.setValueAt(d.getPdm().getCreatingDate(), selectedRow, 2);
                    break;
                }
            }
        });

        // Remove a PDM
        RemoveButton.addActionListener(e -> {
            int column = 1;
            int selectedRow = PdmTable.getSelectedRow();

            String pdmName = (String) PdmTable.getModel().getValueAt(selectedRow, column);
            for (PDM pdm : Pdm) {
                if (pdm.getName() == pdmName) {
                    Pdm.remove(pdm);
                    break;
                }
            }

            ((DefaultTableModel) PdmTable.getModel()).removeRow(selectedRow);
        });

        // Save the PDMs
        SaveButton.addActionListener(e -> {
            if (PdmTable.getModel().getRowCount() > 0) {
                for (PDM pdm : Pdm) {
                    XML.ExportPDM(pdm);
                }
                
                //TemplateGeneration.CreateTransformationTemplates(Pdm);
                JOptionPane.showMessageDialog(null, "XML File created");
            } else {
                JOptionPane.showMessageDialog(null, "There is no PDM in the Table");
            }
        });

        //Cancel the PDMs window
        CancelButton.addActionListener(e -> {
            // Locale us = Locale.forLanguageTag("en-US");
            // JOptionPane.setDefaultLocale(us);
            int result = JOptionPane.showConfirmDialog( this, "Are you sure you want to cancel?","Cancel", JOptionPane.YES_NO_OPTION);

            if (result == JOptionPane.YES_OPTION) {
                this.dispose();
            }
        });

        //Close the PDMs window
        CloseButton.addActionListener(e -> {
            this.dispose();
        });

        return this;
    }

    @Override
    public void prepare(IDialog dialog) {

        dialog.setModal(true);
        dialog.setTitle("MDE - PDMs");
        dialog.setResizable(true);
        dialog.pack();
    }

    @Override
    public void shown() {

    }


    @Override
    public boolean canClosed() {
        return true;
    }

}