package it.unibz.inf.ppn;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public class CmdExecutorThread extends Thread {

	private String path;

	public CmdExecutorThread(String p) {
		path = p;
	}

	@Override
	public void run() {
		OutputStream out = null;
		try {
			Runtime r = Runtime.getRuntime();
			Process p = r.exec("cmd.exe", null, new File(path));
			out = p.getOutputStream();

			out.write("git fetch && git rebase master".getBytes());
			out.flush();

			System.out.println(path + " updated!");

		} catch (IOException e) {

		} finally {
			try {
				out.close();
			} catch (IOException e) {

			}
		}

	}
}
