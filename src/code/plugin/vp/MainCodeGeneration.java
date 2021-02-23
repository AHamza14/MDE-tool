package code.plugin.vp;

import com.vp.plugin.*;

public class MainCodeGeneration implements VPPlugin {

	@Override
	public void loaded(VPPluginInfo vpi) {
		System.out.println("MDE Plugin loaded.");
	}
	
	@Override
	public void unloaded() {
	}
}