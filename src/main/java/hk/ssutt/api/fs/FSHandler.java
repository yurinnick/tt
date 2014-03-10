package hk.ssutt.api.fs;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by fau on 18/02/14.
 */

public class FSHandler {
	private static FSHandler fsm;

	private static String ttDir = "timetables/";
	private static String ttDBName = "timetables.db";
	//for production
	//private static final String ttDir = "/var/timetables/";
	private static File dbFile;

	private FSHandler() {
	}

	public static FSHandler getInstance() {
		if (fsm == null) {
			fsm = new FSHandler();
		}

		return fsm;
	}

	public static void setTtDir(String ttDir) {
		FSHandler.ttDir = ttDir;
	}

	public static void setTtDBName(String ttDBName) {
		FSHandler.ttDBName = ttDBName;
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

    public String getTTDir(){
        return dbFile.getAbsolutePath().replace(ttDBName, "");
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

    public boolean notInExclusion(String s, Path exclFile) {
        try (InputStream in = Files.newInputStream(exclFile);
             BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {

            String line;
            while ((line = reader.readLine()) != null) {
                if (s.equals(line)) {
                    return false;
                }
            }
        } catch (IOException e) {
            System.out.printf("%s: %s%n", e.getClass().getName(), e.getMessage());
            System.exit(-3);
        }

        return true;

    }
}