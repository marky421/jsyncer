import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {
	
	private static final String DATE_FORMAT = "yyyy-MM-dd hh:mm:ss,SSS";
	private static final SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
	private BufferedWriter writer = null;
	
	private boolean doConsole = false;
	
	public Log(String path, boolean doConsole) {
		this.doConsole = doConsole;
		try {
			writer = new BufferedWriter(new FileWriter(new File(path)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void out(String s, boolean err) {
		try {
			writer.append(s);
			if (doConsole) { 
				if (err) System.err.println(s);
				else System.out.println(s);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void out(Object o) { out(o.toString()); }
	public void err(Object o) { err(o.toString()); }
	
	public void out(String s) { out(formatter.format(new Date()) + " OUT " + s, false); }
	public void err(String s) { out(formatter.format(new Date()) + " ERR " + s, true);  }
	
}