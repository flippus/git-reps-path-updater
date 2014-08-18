package it.unibz.inf.ppn;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PathSearcher {

	private String pathString;

	public PathSearcher(String s) {
		this.pathString = s;
		searchPath();
	}

	private void searchPath() {

		Path folder = Paths.get(pathString);
		ExecutorService workerExecutor = Executors.newCachedThreadPool();
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(folder)) {
			for (Path path : stream) {
				if (Files.isDirectory(path) && isGitRepository(path)) {
					CmdExecutorRunnable t = new CmdExecutorRunnable(
							path.toString());
					workerExecutor.execute(t);
				}
			}
		} catch (IOException e) {
			System.err.println("IO Exception catched!");
		}

		workerExecutor.shutdown();

		while (true) {
			try {
				workerExecutor.awaitTermination(Long.MAX_VALUE,
						TimeUnit.NANOSECONDS);
				break;
			} catch (InterruptedException e) {

			}
		}

		System.out.println("finished");
	}

	private boolean isGitRepository(Path p) {
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(p)) {
			for (Path path : stream) {
				if (Files.isDirectory(path)) {
					if (path.toString().endsWith(".git")) {
						return true;
					}
				}
			}
		} catch (IOException e) {

		}
		return false;
	}
}
