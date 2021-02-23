package code.plugin.vp.Controls;

import com.vp.plugin.ApplicationManager;
import com.vp.plugin.ViewManager;
import com.vp.plugin.action.VPAction;
import com.vp.plugin.action.VPActionController;

import code.plugin.vp.Handlers.PDMsHandler;


public class PDMControl implements VPActionController {

   @Override
   public void performAction(VPAction action) {
      ViewManager vm =  ApplicationManager.instance().getViewManager();
		PDMsHandler d = new PDMsHandler();
      vm.showDialog(d);
   }
   
   @Override
   public void update(VPAction action) {
   }
}