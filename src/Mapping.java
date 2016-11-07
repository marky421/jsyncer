import java.io.File;

import org.json.JSONObject;

public class Mapping {
	
	private String  src  = null;
	private String  dest = null;
	
	public Mapping(JSONObject o) {
		JSONObject opt = o.getJSONObject(Config.CONFIG_MAPPINGS_OPTIONS);
		
		src  = o.getString(Config.CONFIG_MAPPINGS_SRC);
		dest = o.getString(Config.CONFIG_MAPPINGS_DEST);
		
		if (opt.getBoolean(Config.CONFIG_MAPPINGS_USE_DEFAULT_DEST_PATH_ROOT)) {
			dest = Config.getDefaultDestPathRoot() + File.separator + dest;
		}
		
		if (opt.getBoolean(Config.CONFIG_MAPPINGS_USE_SRC_FOLDER_NAME)) {
			dest += File.separator + src.substring(src.lastIndexOf(File.separator));
		}
		
		dest = dest.replace(File.separator + File.separator, File.separator);
	}
	
	public String getSrc()  { return this.src;  }
	public String getDest() { return this.dest; }
	
	public String toString() {
		return " src: " + getSrc() + System.lineSeparator() + "dest: " + getDest();
	}
	
}