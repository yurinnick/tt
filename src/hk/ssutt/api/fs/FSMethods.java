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
	private static File downloadFile(URL url, String path) throws IOException {
		File file = new File(path);

		ReadableByteChannel rbc = Channels.newChannel(url.openStream());
		FileOutputStream fos = new FileOutputStream(file);
		fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
		return file;
	}
}
