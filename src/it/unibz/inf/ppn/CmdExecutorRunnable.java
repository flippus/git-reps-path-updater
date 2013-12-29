package it.unibz.inf.ppn;

import java.io.File;
import java.io.IOException;

public class CmdExecutorRunnable implements Runnable {

	private String path;

	public CmdExecutorRunnable(String p) {
		path = p;
	}

	@Override
	public void run() {
		try {
			Process p = Runtime.getRuntime().exec("git pull", null,
					new File(path));
			while (true) {
				try {
					p.waitFor();
					System.out.println(path + " updated!");
					break;
				} catch (IllegalThreadStateException | InterruptedException e) {

				}
			}
			System.out.println(path + " updated!");

		} catch (IOException e) {

		}
	}
}
