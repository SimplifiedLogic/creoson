/*
 * MIT LICENSE
 * Copyright 2000-2019 Simplified Logic, Inc
 * Permission is hereby granted, free of charge, to any person obtaining a copy 
 * of this software and associated documentation files (the "Software"), to deal 
 * in the Software without restriction, including without limitation the rights 
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell 
 * copies of the Software, and to permit persons to whom the Software is 
 * furnished to do so, subject to the following conditions: The above copyright 
 * notice and this permission notice shall be included in all copies or 
 * substantial portions of the Software. THE SOFTWARE IS PROVIDED "AS IS", 
 * WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED 
 * TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND 
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE 
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF 
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE 
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.simplifiedlogic.nitro.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.util.List;

import com.simplifiedlogic.nitro.jlink.DataUtils;

/**
 * Utility methods for managing external processes
 * @author Adam Andrews
 *
 */
public class ProcessUtils {
    
	public static final boolean debug = true;
	
    /**
     * @return The Process ID for the current process
     */
    public static int getProcessId() {
		String procname = ManagementFactory.getRuntimeMXBean().getName();
		if (procname!=null) {
			int atpos = procname.indexOf('@');
			if (atpos<0) atpos=procname.length();
			try {
				return Integer.parseInt(procname.substring(0, atpos));
			}
			catch (Exception e) {
				// ignore
			}
		}
		return 0;
    }

	/**
	 * Kill a process for a given image (executable) name.  
	 * Only supports Windows and Linux processes. 
	 * @param imageName The image name (for example: notepad.exe)
	 */
	public static void killProcess(String imageName) {
		killProcess(imageName, true);
	}

	/**
	 * Kill a process for a given process ID.  
	 * Only supports Windows and Linux processes. 
	 * @param procid The ID for the process (PID) on the operating system
	 */
	public static void killProcess(int procid) {
		killProcess(String.valueOf(procid), false);
	}
	
	/**
	 * Kill a process for a given image (executable) name or process ID.  
	 * Only supports Windows and Linux processes. 
	 * @param proc The image name or process ID
	 * @param isname True if proc is a name, false if it is a process ID
	 */
	private static void killProcess(String proc, boolean isname) {
		if (debug) System.out.println("Killing process " + proc);
		try {
			String os = System.getProperty("os.name", "").toLowerCase();
			
			if (os.indexOf("windows")>=0) {
				// windows version
				String cmd1 = null;
				String cmd2 = null;
				if (isname) {
					cmd1 = "tasklist /fi \"imagename eq " + proc + "\" /fo csv /nh";
					cmd2 = "taskkill /F /T /im " + proc;
				}
				else {
					cmd1 = "tasklist /fi \"pid eq " + proc + "\" /fo csv /nh";
					cmd2 = "taskkill /F /T /pid " + proc;
				}

				Process pid = Runtime.getRuntime().exec(cmd1);
	            BufferedReader in = new BufferedReader(new InputStreamReader(pid.getInputStream()));
	            while (true) {
	                String line = in.readLine();
	                if (line == null)
	                    break;
	                //Util.logDebug(line);
	                if (line.length()==0)
	                    continue;
	                if (line.startsWith("INFO:"))
	                	continue;

	                if (debug) System.out.println("Killing " + proc + " processes");
        			Process pr = Runtime.getRuntime().exec(cmd2);
        			try {
        				pr.waitFor();
        			}
        			catch (InterruptedException e) {
        				// ignore
        			}
		            break;
	            }
//                String line = in.readLine();
//                // if any lines are found, then kill
//                if (line!=null && line.length()>0) {
//        			if (debug) System.out.println("Killing Pro/E processes");
//		            Runtime.getRuntime().exec(cmd2); 
//                }
                in.close();
			}
			else {
				// assume some flavor of *nix
				String cmd1 = null;
				if (isname) {
					cmd1 = "ps -ef | grep " + proc;
				}
				else {
					cmd1 = "ps --no-headers -p " + proc;
				}
				
	            Process pid = Runtime.getRuntime().exec(cmd1);
	            BufferedReader in = new BufferedReader(new InputStreamReader(pid.getInputStream()));
	            while (true) {
	                String line = in.readLine();
	                if (line == null)
	                    break;
	                //Util.logDebug(line);
	                if (line.length()==0)
	                    continue;
	                
	                //parse line for second field
	                int start = line.indexOf(' ');
	                int limit = line.length();
	                for (++start; line.charAt(start)==' ' && start<limit; start++);
	                if (start==limit)
	                	continue;
	                int end = line.indexOf(' ', start);
	                if (end==-1) end=line.length();
	                int id = Integer.parseInt(line.substring(start, end));
	
	                // kill it
        			if (debug) System.out.println("Killing " + proc + " processes");
//	                Process pr = Runtime.getRuntime().exec("kill " + id);
//	                Process pr = Runtime.getRuntime().exec("kill -13 " + id); // stronger kill
        			Process pr = Runtime.getRuntime().exec("kill -9 " + id); // force kill
        			try {
        				pr.waitFor();
        			}
        			catch (InterruptedException e) {
        				// ignore
        			}
	            }
	            in.close();
			}
		}
        catch (IOException e) {
        	System.err.println("Error running system task: " + e.getMessage());
        }
	}

	/**
	 * Checks whether a process is running.
	 * Only supports Windows and Linux processes. 
	 * @param procid The process ID 
	 * @return Whether the process is running
	 */
	public static String isProcessRunning(int procid) {
		if (procid==0)
			return null;
		try {
			String os = System.getProperty("os.name", "").toLowerCase();
			String name = null;
			
			if (os.indexOf("windows")>=0) {
				// windows version
				String cmd1 = "tasklist /fi \"pid eq " + procid + "\" /fo csv /nh";

				Process listproc = Runtime.getRuntime().exec(cmd1);
	            BufferedReader in = new BufferedReader(new InputStreamReader(listproc.getInputStream()));
	            while (true) {
	                String line = in.readLine();
	                if (line == null)
	                    break;
	                if (line.length()==0)
	                    continue;
	                if (line.startsWith("INFO:"))
	                	continue;

	                //if (debug) System.out.println("Found process: " + line);
	                List<Object> linevals = DataUtils.getCsvList(line);
	                if (linevals!=null) {
	                	Object obj = linevals.get(0);
                		name = obj.toString();
	                }
		            break;
	            }
                in.close();
			}
			else {
				// assume some flavor of *nix
				String cmd1 = "ps --no-headers -o comm -p " + procid;
				
	            Process listproc = Runtime.getRuntime().exec(cmd1);
	            BufferedReader in = new BufferedReader(new InputStreamReader(listproc.getInputStream()));
	            while (true) {
	                String line = in.readLine();
	                if (line == null)
	                    break;
	                //Util.logDebug(line);
	                if (line.length()==0)
	                    continue;
	                
        			if (debug) System.out.println("Found process: " + line);
        			name=line;
		            break;
	            }
	            in.close();
			}
			return name;
		}
        catch (IOException e) {
        	System.err.println("Error running system task: " + e.getMessage());
        	return null;
        }
	}
	
	/**
	 * Check whether a process is running.  Alternatively check whether the 
	 * process is hosting a specific service.
	 * Only supports Windows and Linux processes. 
	 * @param processName The process name to look for
	 * @param serviceName If the process is a service, the service name to check for
	 * @return Whether the process is running and hosting the (optional) service
	 */
	public static int isProcessRunning(String processName, String serviceName) {
		if (processName==null)
			return -1;
		try {
			String os = System.getProperty("os.name", "").toLowerCase();
			int pid=-1;
			
			if (os.indexOf("windows")>=0) {
				// windows version
				String cmd1 = "tasklist /fi \"IMAGENAME eq " + processName + "\" /fo csv /nh";
				if (serviceName!=null)
					cmd1 += " /fi \"SERVICES eq " + serviceName + "*\" ";

				Process listproc = Runtime.getRuntime().exec(cmd1);
	            BufferedReader in = new BufferedReader(new InputStreamReader(listproc.getInputStream()));
	            while (true) {
	                String line = in.readLine();
	                if (line == null)
	                    break;
	                if (line.length()==0)
	                    continue;
	                if (line.startsWith("INFO:"))
	                	continue;

	                if (debug) System.out.println("Found process: " + line);
	                List<Object> linevals = DataUtils.getCsvList(line);
	                if (linevals!=null && linevals.size()>=2) {
	                	Object obj = linevals.get(1);
	                	if (obj instanceof Integer)
	                		pid = ((Integer)obj).intValue();
	                	else
	                		pid = Integer.parseInt(obj.toString());
	                }
		            break;
	            }
                in.close();
			}
			else {
				// assume some flavor of *nix
				String cmd1 = "ps --no-headers -C " + processName + " -o pid";
				if (serviceName!=null)
					cmd1 += ",command | grep \"" + serviceName + "\"";
				cmd1 += " | head -1";
				
	            Process listproc = Runtime.getRuntime().exec(cmd1);
	            BufferedReader in = new BufferedReader(new InputStreamReader(listproc.getInputStream()));
	            while (true) {
	                String line = in.readLine();
	                if (line == null)
	                    break;
	                //Util.logDebug(line);
	                if (line.length()==0)
	                    continue;
	                
        			if (debug) System.out.println("Found process: " + line);
        			int pos = line.indexOf(' ');
        			if (pos>0)
        				pid = Integer.parseInt(line.substring(0, pos));
        			else
        				pid = Integer.parseInt(line);
		            break;
	            }
	            in.close();
			}
			return pid;
		}
        catch (IOException e) {
        	System.err.println("Error running system task: " + e.getMessage());
        	return -1;
        }
	}
	
	/**
	 * Start a process.  Only supports Windows processes.
	 * @param command The command line to execute
	 * @param dirname The directory to execute in
	 */
	public static void startProcess(String command, String dirname) {
		// TODO: capture errors
		if (command==null || command.length()==0)
			return;
        if (command.indexOf(".bat")>=0 && !command.startsWith("cmd.exe"))
        	command = "cmd.exe /c " + command;
    	if (debug) System.out.println("Starting process: " + command);
        Process pr = null;

        try {
            Runtime rt = Runtime.getRuntime();
            if (dirname!=null && dirname.length()>0)
            	pr = rt.exec(command, null, new File(dirname));
            else
            	pr = rt.exec(command);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.err.println("Unable to start application: " + e.getMessage());
        }
	}

//	public static void stopProcess(String command, String dirname) {
//		if (command==null || command.length()==0)
//			return;
//        if (command.indexOf(".bat")>=0 && !command.startsWith("cmd.exe"))
//        	command = "cmd.exe /c " + command;
//    	if (debug) System.out.println("Stopping process: " + command);
//        Process pr = null;
//
//        try {
//            Runtime rt = Runtime.getRuntime();
//            if (dirname!=null && dirname.length()>0)
//            	pr = rt.exec(command, null, new File(dirname));
//            else
//            	pr = rt.exec(command);
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//            System.err.println("Unable to stop application: " + e.getMessage());
//        }
//	}
	
}
