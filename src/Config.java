import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class Config {
	
	public static final String CONFIG_FILE_NAME                           = "JSyncerConfig.json";
	public static final String CONFIG_DEFAULT_DEST_PATH_ROOT              = "defaultDestPathRoot";
	public static final String CONFIG_MAPPINGS                            = "mappings";
	public static final String CONFIG_MAPPINGS_SRC                        = "src";
	public static final String CONFIG_MAPPINGS_DEST                       = "dest";
	public static final String CONFIG_MAPPINGS_OPTIONS                    = "options";
	public static final String CONFIG_MAPPINGS_USE_DEFAULT_DEST_PATH_ROOT = "useDefaultDestPathRoot";
	public static final String CONFIG_MAPPINGS_USE_SRC_FOLDER_NAME        = "useSrcFolderName";
	
	private static String defaultDestPathRoot = null;
	private static List<Mapping> mappings = null;
	
	static {
		try {
			Path path = Paths.get(Paths.get("").toAbsolutePath().toString() + File.separator + CONFIG_FILE_NAME);
			JSONObject config = new JSONObject(new String(Files.readAllBytes(path)));
			
			defaultDestPathRoot = config.getString(CONFIG_DEFAULT_DEST_PATH_ROOT);
			
			JSONArray mappingsJSON = config.getJSONArray(CONFIG_MAPPINGS);
			mappings = new ArrayList<Mapping>(mappingsJSON.length());
			mappingsJSON.forEach(o -> mappings.add(new Mapping((JSONObject) o)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private Config() {}
	
	public static String        getDefaultDestPathRoot() { return defaultDestPathRoot; }
	public static List<Mapping> getMappings()            { return mappings;            }
	
}