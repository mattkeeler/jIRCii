package rero.client.functions;

import rero.client.Feature;
import sleep.bridges.BridgeUtilities;
import sleep.interfaces.Function;
import sleep.interfaces.Loadable;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptInstance;
import sleep.runtime.SleepUtils;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.util.Stack;

public class SoundOperators extends Feature implements Loadable {
	public void init() {
		getCapabilities().getScriptCore().addBridge(this);
	}

	public void scriptLoaded(ScriptInstance script) {
		script.getScriptEnvironment().getEnvironment().put("&loadSound", new loadSound());
		script.getScriptEnvironment().getEnvironment().put("&soundPlay", new soundPlay());
		script.getScriptEnvironment().getEnvironment().put("&soundLoop", new soundLoop());
		script.getScriptEnvironment().getEnvironment().put("&soundStop", new soundStop());
	}

	public void scriptUnloaded(ScriptInstance script) {
	}

	private static class loadSound implements Function {
		public Scalar evaluate(String name, ScriptInstance si, Stack locals) {
			String file = BridgeUtilities.getString(locals, null);

			if (file != null && (new File(file)).exists()) {
				try {
					AudioClip clip = Applet.newAudioClip((new File(file)).toURL());
					return SleepUtils.getScalar(clip);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			return SleepUtils.getEmptyScalar();
		}
	}

	private static class soundPlay implements Function {
		public Scalar evaluate(String name, ScriptInstance si, Stack locals) {
			AudioClip clip = (AudioClip) BridgeUtilities.getObject(locals);
			clip.play();

			return SleepUtils.getEmptyScalar();
		}
	}

	private static class soundLoop implements Function {
		public Scalar evaluate(String name, ScriptInstance si, Stack locals) {
			AudioClip clip = (AudioClip) BridgeUtilities.getObject(locals);
			clip.loop();

			return SleepUtils.getEmptyScalar();
		}
	}

	private static class soundStop implements Function {
		public Scalar evaluate(String name, ScriptInstance si, Stack locals) {
			AudioClip clip = (AudioClip) BridgeUtilities.getObject(locals);
			clip.stop();

			return SleepUtils.getEmptyScalar();
		}
	}

}
