package code.plugin.vp;

import com.vp.plugin.*;
import code.plugin.vp.Utilities.UserInterfaceUtil;

public class MainMDE implements VPPlugin {

	@Override
	public void loaded(VPPluginInfo vpi) {
		UserInterfaceUtil.initializeComponents();
		System.out.println("MDE Plugin loaded.");
	}
	
	@Override
	public void unloaded() {
	}
}