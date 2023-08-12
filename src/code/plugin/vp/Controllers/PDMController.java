package code.plugin.vp.Controllers;

import com.vp.plugin.ApplicationManager;
import com.vp.plugin.ViewManager;
import com.vp.plugin.action.VPAction;
import com.vp.plugin.action.VPActionController;

import code.plugin.vp.UserInterface.PDMsDialog;


public class PDMController implements VPActionController {

   @Override
   public void performAction(VPAction action) {
      ViewManager vm =  ApplicationManager.instance().getViewManager();
		PDMsDialog d = new PDMsDialog();
      vm.showDialog(d);
   }
   
   @Override
   public void update(VPAction action) {
   }
}