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
                    break;
                } catch (IllegalThreadStateException | InterruptedException e) {

                }
            }
            System.out.println(path + " updated!");

        } catch (IOException e) {

        }
    }
}
