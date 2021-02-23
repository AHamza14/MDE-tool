package code.plugin.vp.Controls;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;

import java.awt.BorderLayout;
import java.awt.Desktop;

import com.vp.plugin.ApplicationManager;
import com.vp.plugin.ViewManager;
import com.vp.plugin.action.VPAction;
import com.vp.plugin.action.VPActionController;
import com.vp.plugin.model.IProject;

import code.plugin.vp.Handlers.PIMParameterizationHandlers.ChoosePDMHandler;
import code.plugin.vp.Structures.PDM;
import code.plugin.vp.Structures.TransformationTemplate;
import code.plugin.vp.Utilities.UserInterfaceUtil;
import code.plugin.vp.Utilities.XML;

public class CodeGenerationControl implements VPActionController {

    @Override
    public void performAction(VPAction arg0) {
        // get the pdm xml path
        String pdmXMlPath = UserInterfaceUtil.getFilePath("Extensible Markup Language", "XML",null, "Choose the platform descritpion models");

        if (pdmXMlPath != null) {
            String savingPath = createSavingFolder();
            
            exportPIMasXML();

            // Select Main PDM
            PDM selectedPdm = getMainPdm(XML.ImportPDMs(pdmXMlPath));

            // Create PDM transformations templates command
            String ttCommand = createTransformationCommand(selectedPdm, savingPath);

            // Run transformation template command line
            runTransformationCommand(ttCommand, savingPath);
		}
    }

    @Override
    public void update(VPAction arg0) {
       

    }

    private String createSavingFolder(){
        //Create the save Path 
        IProject project = ApplicationManager.instance().getProjectManager().getProject();

        String saveFolder = project.getProjectFile().getParentFile().getAbsolutePath().replace("\\", "\\\\");
        saveFolder = saveFolder + "\\\\MDEPlugin\\\\Code";
        File saveFolderFile = new File(saveFolder); 

        if (!saveFolderFile.exists()) {
            saveFolderFile.mkdir();
        }
        return saveFolder;
    }
    
    private PDM getMainPdm(List<PDM> pdms){
        ViewManager vm = ApplicationManager.instance().getViewManager();
        ChoosePDMHandler pdmHandler = new ChoosePDMHandler(pdms, "Select the main PDM", ListSelectionModel.SINGLE_SELECTION);
        vm.showDialog(pdmHandler);
        return pdmHandler.getPdm().iterator().next();
    }
    
    private String createTransformationCommand(PDM pdm, String path){
        //Or run the command when ever the files are
        //example: "C:\Program Files\Saxonica\SaxonHE9.9N\bin\Transform" -t C:\...\project.xml C:\...\FeuilleCoreEnum.xsl

        String command = "cd \""+path+"\" ";
        String SaxonicaTransformPath = "\""+UserInterfaceUtil.getFilePath("Executable", "exe","C:\\Program Files\\Saxonica", "Choose Saxonica file path")+"\"";

        for (TransformationTemplate tt : pdm.getPdmTransformationTemplate()) {
            File ttFile = new File(tt.getFileUri().replace("\\", "\\\\"));
            command += "&& "+SaxonicaTransformPath+" -t project.xml "+ttFile.getName()+" ";
        }
        return command;
    }

    private void exportPIMasXML(){
        // Export parameterized PIM (Diagram) as "project.xml" to ditectory "Code"
        //Not supported by open api
        //https://forums.visual-paradigm.com/t/how-to-export-one-or-more-diagrams-to-a-visual-paradigm-project-using-openapi/16407/7
    }

    private void runTransformationCommand(String command, String savingPath){
        final JDialog loading = new JDialog();
        JPanel p1 = new JPanel(new BorderLayout());
        p1.add(new JLabel("Generating code..."), BorderLayout.CENTER);
        loading.setUndecorated(true);
        loading.getContentPane().add(p1);
        loading.pack();
        loading.setLocationRelativeTo(p1);
        loading.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        loading.setModal(true);

        SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws InterruptedException{
                /** Execute some operation */
                try 
                {
                    ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", command);
                    builder.redirectErrorStream(true);
                    Process p = builder.start();
        
                    BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
                    String line;
                    while (true) {
                        line = r.readLine();
                        if (line == null) { break; }
                    }
                } catch (Exception e) {
                }
                return " "; 
            }
            
            
            @Override
            protected void done() {
                loading.dispose();
                //default title and icon
                Object[] options = {"OK","Open code"};
                int n = JOptionPane.showOptionDialog(null, 
                    "Generating the code is done!!",
                    "Complete",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);

                if(n == 1){
                    try {
                        Desktop.getDesktop().open(new File(savingPath));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                //JOptionPane.showMessageDialog(null, "Generating the code is done.");
            }
        };
        worker.execute();
        loading.setVisible(true);
        try {
            worker.get();
        } catch (Exception e1) {
            e1.printStackTrace();
        } 
    }
    
}