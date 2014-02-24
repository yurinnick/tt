package hk.ssutt.api.fs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

/**
 * Created by fau on 18/02/14.
 */

public class FSMethods {
	private static FSMethods fsm;

	private static String ttDir = "timetables/";
	private static String ttDBName = "timetables.db";
	//for production
	//private static final String ttDir = "/var/timetables/";
	private static File dbFile;

	private FSMethods() {
	}

	public static FSMethods getInstance() {
		if (fsm == null) {
			fsm = new FSMethods();
		}

		return fsm;
	}

	public static void setTtDir(String ttDir) {
		FSMethods.ttDir = ttDir;
	}

	public static void setTtDBName(String ttDBName) {
		FSMethods.ttDBName = ttDBName;
	}

	private static File downloadFile(URL url, String path) throws IOException {
		File file = new File(path);

		ReadableByteChannel rbc = Channels.newChannel(url.openStream());
		FileOutputStream fos = new FileOutputStream(file);
		fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

		return file;
	}

	public boolean hasTTInstance() {
		dbFile = new File(ttDir + ttDBName);
		if (dbFile.isFile()) {
			String[] names = new File(ttDir).list();

			//not a really good directory check
			//at least something
			for (String s : names) {
				if (new File(ttDir + s).isDirectory()) {
					return true;
				}
			}

		}

		return false;
	}

	public String getTTDirPath() {
		return dbFile.getAbsolutePath();
	}

	public void touch(File file) {
		try {
			if (!file.exists()) {
				new FileOutputStream(file).close();
			}

			file.setLastModified(System.currentTimeMillis());
		} catch (IOException e) {
			System.out.println(e.getClass().getName() + ": " + e.getMessage());
		}
	}
}
