package it.unibz.inf.ppn;

public class Main {

	public static void main(String[] args) {
		if (args.length == 1 && args != null && !args[0].isEmpty()) {
			new PathSearcher(args[0]);
		} else {
			System.err
					.println("Invalid parameter. Enter the folder of the repositories.");
			System.exit(1);
		}
	}
}
