package it.unibz.inf.ppn;

import java.io.File;
import java.io.IOException;

public class CmdExecutorThread extends Thread {

	private String path;

	public CmdExecutorThread(String p) {
		path = p;
	}

	@Override
	public void run() {
		try {
			ProcessBuilder pb = new ProcessBuilder("cmd.exe", " /c ", "start",
					"git", "pull", "master").redirectErrorStream(true);
			pb.directory(new File(path));
			Process p = pb.start();

			while (true) {
				try {
					p.exitValue();
					System.out.println(path + " updated!");
					break;
				} catch (IllegalThreadStateException e) {

				}
			}
			System.out.println(path + " updated!");

		} catch (IOException e) {

		}
	}
}
