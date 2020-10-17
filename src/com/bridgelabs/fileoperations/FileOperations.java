package com.bridgelabs.fileoperations;

import java.io.File;

public class FileOperations {
	public static boolean deleteFiles(File filesTodelete) {
		File path[] = filesTodelete.listFiles();
		if (path != null) {
			for (File file : path) {
				deleteFiles(file);
			}
		}
		return filesTodelete.delete();
	}
}
