package code.plugin.vp.Utilities;

import java.awt.*;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.vp.plugin.ApplicationManager;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class UserInterfaceUtil {

    public static GridBagConstraints setGridBagConstraints(GridBagConstraints paraGbc, int gridx, int gridy, int fill, int ipady, int ipadx, double weightx, int gridwidth) {
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

    public static ArrayList<String> getFilePath(String fileDescription, String fileExtension, String defaultDirectory, String dialogTitle, boolean isSelectMultiple){
        final JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(fileDescription, fileExtension);
        if(defaultDirectory != null){
            chooser.setCurrentDirectory(new File(defaultDirectory)); 
        }
        chooser.setMultiSelectionEnabled(isSelectMultiple);
		chooser.setFileFilter(filter);
		chooser.setDialogTitle(dialogTitle);
        int returnVal = chooser.showOpenDialog(null);

        ArrayList<String> selectedFiles = new ArrayList<String>(); 
        if(returnVal == JFileChooser.APPROVE_OPTION) {
			if(isSelectMultiple){
                for (File afile : chooser.getSelectedFiles()) {
                    selectedFiles.add(afile.getAbsolutePath().replace("\\", "\\\\"));
                }
            }
            else{
                selectedFiles.add(chooser.getSelectedFile().getAbsolutePath().replace("\\", "\\\\"));
            }
		}
        return selectedFiles;
    }

    public static String getDirectoryPath(String defaultDirectory, String dialogTitle){
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setDialogTitle(dialogTitle);
        if(defaultDirectory != null){
            chooser.setCurrentDirectory(new File(defaultDirectory)); 
        }
        int returnVal = chooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile().getAbsolutePath();
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

    public static void logMessage(String msg) {
        ApplicationManager.instance().getViewManager().showMessage(msg);
    }
    
    public static void copyFolder(File source, File destination)
    {
        if (source.isDirectory())
        {
            if (!destination.exists())
            {
                destination.mkdirs();
            }

            String files[] = source.list();

            for (String file : files)
            {
                File srcFile = new File(source, file);
                File destFile = new File(destination, file);

                copyFolder(srcFile, destFile);
            }
        }
        else
        {
            InputStream in = null;
            OutputStream out = null;

            try
            {
                in = new FileInputStream(source);
                out = new FileOutputStream(destination);

                byte[] buffer = new byte[1024];

                int length;
                while ((length = in.read(buffer)) > 0)
                {
                    out.write(buffer, 0, length);
                }
            }
            catch (Exception e)
            {
                try
                {
                    in.close();
                }
                catch (IOException e1)
                {
                    ApplicationManager.instance().getViewManager().showMessage(e1.getMessage());
                }

                try
                {
                    out.close();
                }
                catch (IOException e1)
                {
                    ApplicationManager.instance().getViewManager().showMessage(e1.getMessage());
                }
            }
        }
    }

}