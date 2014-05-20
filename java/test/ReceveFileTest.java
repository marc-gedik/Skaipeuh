package test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import skaipeuh.ReceveFile;

public class ReceveFileTest {

	@Test
	public void fileRename() {
		String fileName = "test_fic";
		String fileNameExpected = "test_fic(0)";
		File f = new File("test_fic");
		try {
			f.createNewFile();
			assertTrue(ReceveFile.renameExist(fileName)
					.equals(fileNameExpected));
			f.delete();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
