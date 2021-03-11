package code.plugin.vp.Utilities;

import java.awt.*;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.event.ActionEvent;
import java.io.File;

public class UserInterfaceUtil {

    public static GridBagConstraints setGridBagConstraints(GridBagConstraints paraGbc, int gridx, int gridy, int fill,
            int ipady, int ipadx, double weightx, int gridwidth) {
        paraGbc.ipady = ipady;
        paraGbc.ipadx = ipadx;
        paraGbc.gridwidth = gridwidth;
        paraGbc.fill = fill;
        paraGbc.weightx = weightx;
        paraGbc.gridx = gridx;
        paraGbc.gridy = gridy;
        return paraGbc;
    }

    public static void CloseDialog(ActionEvent e) {
        Component component = (Component) e.getSource();
        JDialog dialog = (JDialog) SwingUtilities.getRoot(component);
        dialog.dispose();
    }

    public static String getFilePath(String fileDescription, String fileExtension, String defaultDirectory, String dialogTitle){
        final JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(fileDescription, fileExtension);
        if(defaultDirectory != null){
            chooser.setCurrentDirectory(new File(defaultDirectory)); 
        }
		chooser.setFileFilter(filter);
		chooser.setDialogTitle(dialogTitle);
        int returnVal = chooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
			
            return chooser.getSelectedFile().getAbsolutePath().replace("\\", "\\\\");
		}
        return null;
    }

    public static void createFolder(String path){
        File projectFile = new File(path);         
        if (!projectFile.exists()) {
            projectFile.mkdir();
        }
    }

    public static void initializeComponents(){
        UIManager.put("OptionPane.yesButtonText", "Yes");
        UIManager.put("OptionPane.noButtonText", "No");
    }
}