package code.plugin.vp.Handlers.PIMParameterizationHandlers;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.awt.*;
import javax.swing.*;

import com.vp.plugin.view.*;
import code.plugin.vp.Structures.PDM;
import code.plugin.vp.Utilities.UserInterfaceUtil;

public class ChoosePDMHandler implements IDialogHandler {
    List<PDM> SelectedPdms;
    List<PDM> Pdms;
    JList<String> PdmsList = new JList<String>(); 
    JButton SaveButton = new JButton("Save");
    JButton CancelButton = new JButton("Cancel");
    JButton CloseButton = new JButton("Close");
    JLabel titleLabel;


    public ChoosePDMHandler(List<PDM> pdms, String title, int selectionMode){
        Pdms = pdms;
        SelectedPdms = new ArrayList<PDM>();

        titleLabel = new JLabel(title, SwingConstants.CENTER);
        PdmsList.setSelectionMode(selectionMode);
        DefaultListModel<String> pdmsModel = new DefaultListModel<String>();
        for(PDM pdm : Pdms){
            pdmsModel.addElement(pdm.getName());
        }
        PdmsList.setModel(pdmsModel);
    }
    
    @Override
    public Component getComponent() {
        JPanel mainPane = new JPanel();

        mainPane.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 2, 2, 2);

        //Title
        gbc = UserInterfaceUtil.setGridBagConstraints(gbc, 0, 0, 2, 10, 30, 0.25, 4);
        mainPane.add(titleLabel, gbc);

        //Design Concern Table
        gbc = UserInterfaceUtil.setGridBagConstraints(gbc, 0, 1, 2, 10, 30, 0.25, 4);
        JScrollPane tableScroll = new JScrollPane(PdmsList);
        tableScroll.setPreferredSize(new Dimension(100, 100));
        mainPane.add(tableScroll, gbc);

        //Save Button
        JPanel mainControls = new JPanel();
        mainControls.setLayout(new FlowLayout());

        SaveButton.setPreferredSize(new Dimension(90, 30));
        mainControls.add(SaveButton);

        CancelButton.setPreferredSize(new Dimension(90, 30));
        mainControls.add(CancelButton);

        CloseButton.setPreferredSize(new Dimension(90, 30));
        mainControls.add(CloseButton);

        gbc = UserInterfaceUtil.setGridBagConstraints(gbc, 0, 2, 2, 10, 0, 0.50, 8);
        mainPane.add(mainControls, gbc);

        return mainPane;
    }

    @Override
    public void prepare(IDialog dialog) {
        dialog.setModal(true);
        dialog.setTitle("MDE - Select PDM");
        dialog.setResizable(true);
        dialog.pack();
    }

    @Override
    public void shown() {
        //Save Button
        SaveButton.addActionListener(e -> {
            for (PDM pdm : Pdms) {
                if(PdmsList.getSelectedValuesList().contains(pdm.getName())){
                    SelectedPdms.add(pdm);
                }
            }
            UserInterfaceUtil.CloseDialog(e);
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

    public List<PDM> getPdm(){
        return SelectedPdms;
    }
}