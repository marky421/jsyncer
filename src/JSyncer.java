import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.util.Comparator;

import org.apache.commons.codec.binary.Hex;

public class JSyncer {
		
	public static void main(String[] args) throws Exception {
		new JSyncer().go();
	}
	
	private void go() throws Exception {
		System.out.println("Starting JSyncer");
		
		Config.getMappings().forEach(m -> {
			try {
				sync(m);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		System.out.println("Stopping JSyncer");
	}
	
	private void sync(Mapping mapping) throws Exception {
		//Log log = new Log("C:\\Users\\mspain\\Desktop\\sync_log.txt", true);
		
		CopyOption[] options = {
				StandardCopyOption.REPLACE_EXISTING,
				StandardCopyOption.COPY_ATTRIBUTES,
				LinkOption.NOFOLLOW_LINKS
		};
		
		Path src  = Paths.get(mapping.getSrc());
		Path dest = Paths.get(mapping.getDest());
		
		// copy files
		Files.find(src, Integer.MAX_VALUE, (filePath, fileAttr) -> (fileAttr.isRegularFile() || fileAttr.isDirectory())).forEach(p -> {
			try {
				Path d = dest.resolve(src.relativize(p));
				if (Files.isDirectory(p, LinkOption.NOFOLLOW_LINKS)) {
					if (Files.notExists(d, LinkOption.NOFOLLOW_LINKS)) {
						Files.createDirectories(d);
					}
				} else {
					if (Files.notExists(d, LinkOption.NOFOLLOW_LINKS) || !getHash(p).equals(getHash(d))) {
						try { Files.copy(p, d, options); } catch (DirectoryNotEmptyException de) {}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		// cleanup
		Files.find(dest, Integer.MAX_VALUE, (filePath, fileAttr) -> (fileAttr.isRegularFile() || fileAttr.isDirectory())).sorted(Comparator.reverseOrder()).forEach(p -> {
			try {
				Path s = src.resolve(dest.relativize(p));
				if (Files.notExists(s, LinkOption.NOFOLLOW_LINKS)) {
					Files.deleteIfExists(p);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
	
	public String getHash(Path path) throws Exception {
		FileChannel channel = (FileChannel) Files.newByteChannel(path);
		MappedByteBuffer buffer = channel.map(MapMode.READ_ONLY, 0, channel.size());
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(buffer);
		return Hex.encodeHexString(md.digest());
	}
	
}