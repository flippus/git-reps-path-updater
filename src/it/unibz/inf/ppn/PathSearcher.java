package it.unibz.inf.ppn;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class PathSearcher {

	private String pathString;
	private List<CmdExecutorThread> threadList = new ArrayList<>();

	public PathSearcher(String s) {
		this.pathString = s;
		searchPath();
	}

	private void searchPath() {
		Path folder = Paths.get(pathString);
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(folder)) {
			for (Path path : stream) {
				if (Files.isDirectory(path)) {
					CmdExecutorThread t = new CmdExecutorThread(path.toString());
					t.start();
					threadList.add(t);
				}
			}
		} catch (IOException e) {
			// ignore
		}

		for (int i = 0; i < threadList.size(); i++) {
			try {
				threadList.get(i).join();
			} catch (InterruptedException e) {
				i--;
			}
		}

		System.out.println("finished");
	}
}
