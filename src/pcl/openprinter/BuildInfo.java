package pcl.openprinter;

/**
 * This file is automatically updated by Jenkins as part of the CI build script
 * in Ant. Don't put any pre-set values here.
 * 
 * @author AfterLifeLochie, stolen from LanteaCraft, another fine PC-Logix Minecraft mod.
 */
public class BuildInfo {
	public static final String modName = "LanteaCraft";
	public static final String modID = "LanteaCraft";

	/**
	 * Enable or disable the SoundSystem debugging; often this is useful for
	 * recording all the operations on the SoundDevice, meaning that clients
	 * aren't slammed with logging if they don't need to be.
	 */
	public static final boolean SS_DEBUGGING = false && isDevelopmentEnvironment();

	/**
	 * Enable or disable network traffic dumping mode.
	 */
	public static final boolean NET_DEBUGGING = false && isDevelopmentEnvironment();
	
	/**
	 * Enable or disable asset and configuration access dumping.
	 */
	public static final boolean ASSET_DEBUGGING = false && isDevelopmentEnvironment();

	public static int getBuildNumber() {
		if (buildNumber.equals("@" + "BUILD" + "@"))
			return 0;
		return Integer.parseInt(buildNumber);
	}

	public static boolean isDevelopmentEnvironment() {
		return getBuildNumber() == 0;
	}
}
