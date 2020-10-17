package com.bridgelabs.fileoperations;

import static org.junit.Assert.*;

import java.io.*;
import java.nio.file.*;
import java.util.stream.*;

import org.junit.Test;

public class FileOperationsTest {
	private static String HOME = System.getProperty("user.home");
	private static String PLAY_PATH = "SampleTest";

	@Test
	public void givenPathWhenCheckedThenConfirm() throws IOException {

		// checks if file exits
		Path homePath = Paths.get(HOME);
		assertTrue(Files.exists(homePath));

		// deletes file
		Path playPath = Paths.get(HOME + "/" + PLAY_PATH);
		if (Files.exists(playPath)) {
			FileOperations.deleteFiles(playPath.toFile());
		}
		assertTrue(Files.notExists(playPath));

		// creates directory
		Files.createDirectory(playPath);
		assertTrue(Files.exists(playPath));

		// creates file
		IntStream.range(1, 10).forEach(i -> {
			Path tempFile = Paths.get(playPath + "/temp" + i);
			assertTrue(Files.notExists(tempFile));
			try {
				Files.createFile(tempFile);
			} catch (IOException e) {
			}
			assertTrue(Files.exists(tempFile));
		});

		// lists files
		Files.list(playPath).filter(Files::isRegularFile).forEach(System.out::println);
		Files.newDirectoryStream(playPath).forEach(System.out::println);
		Files.newDirectoryStream(playPath, path -> path.toFile().isFile() && path.toString().contains("temp"))
				.forEach(System.out::println);
	}

}
