/**
 *
 * Copyright (C) 2014 Philipp Neugebauer
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 */
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
