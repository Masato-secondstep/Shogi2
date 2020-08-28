package shogi.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ShogiProperty extends Properties{

	public LoadedData load(String fileName) {
		try {
			super.load(new FileInputStream(new File(
					getClass().getResource(fileName).getFile())));
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println(this.getProperty("field0"));
		return null;
	}
}
