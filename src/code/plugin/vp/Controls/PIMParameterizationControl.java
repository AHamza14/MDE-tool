package code.plugin.vp.Controls;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import com.vp.plugin.ApplicationManager;
import com.vp.plugin.ViewManager;
import com.vp.plugin.action.VPAction;
import com.vp.plugin.action.VPContext;
import com.vp.plugin.action.VPContextActionController;

import code.plugin.vp.Handlers.PIMParameterizationHandlers.PIMParameterizationHandler;
//import code.plugin.vp.Structures.PIMParameterization.MarkedUmlElement;
import code.plugin.vp.Utilities.UserInterfaceUtil;

public class PIMParameterizationControl implements VPContextActionController {

	private ArrayList<String> PdmXMlPath;
	
	@Override
	public void performAction(VPAction arg0, VPContext arg1, ActionEvent arg2) {

		PdmXMlPath = UserInterfaceUtil.getFilePath("Extensible Markup Language", "XML", null,"Choose the platform descritpion models", true);
		
		if(PdmXMlPath != null){
			ViewManager vm =  ApplicationManager.instance().getViewManager();
			PIMParameterizationHandler d = new PIMParameterizationHandler(PdmXMlPath);
			vm.showDialog(d);
		}
		  	    	    
	}

	@Override
	public void update(VPAction arg0, VPContext arg1) {
		
	}

}
