/*
 * Quick-Key Toolset Project.
 * Copyright (C) 2010 FedICT.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License version
 * 3.0 as published by the Free Software Foundation.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, see 
 * http://www.gnu.org/licenses/.
 */
package be.fedict.eidtoolset.gui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import javax.swing.JOptionPane;
import be.fedict.util.FileUtils;

public class ExternalProcesses {
	public static void extractJarFiles() throws Exception {
		File lib = new File("lib" + File.separator);
		File[] jars = lib.listFiles();
		ProcessBuilder pb1 = new ProcessBuilder("jar", "xf", jars[0].getAbsolutePath());
		pb1.redirectErrorStream(true);
		Process p1 = pb1.start();
		OutputStream os1 = p1.getOutputStream();
		InputStream is1 = p1.getInputStream();
		// spawn two threads to handle I/O with child while we wait for it to
		// complete.
		new Thread(new Receiver(is1)).start();
		new Thread(new Sender(os1)).start();
		try {
			p1.waitFor();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		System.out.println("First jar file extraction done");
		// at this point the child is done. All of its output may or may not
		// have been processed however.
		// The Receiver thread will continue until it has finished processing
		// it.
		// You must close the streams, even if you never use them! In this case
		// the threads close is and os.
		p1.getErrorStream().close();
		ProcessBuilder pb2 = new ProcessBuilder("jar", "xf", jars[1].getAbsolutePath());
		pb2.redirectErrorStream(true);
		Process p2 = pb2.start();
		OutputStream os2 = p2.getOutputStream();
		InputStream is2 = p2.getInputStream();
		// spawn two threads to handle I/O with child while we wait for it to
		// complete.
		new Thread(new Receiver(is2)).start();
		new Thread(new Sender(os2)).start();
		try {
			p2.waitFor();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		} catch (Exception e) {
			throw new Exception();
		}
		System.out.println("Second jar file extraction done");
		p2.getErrorStream().close();
	}
	// makes java card produce .cap file in right directory
	// We assume both java card 2.2.1 and class files are in the resource
	// directory
	public static void runJavaCardConverter() throws Exception {
		String config = "-config";
		String optfile;
		String converter;
		// Change separator sign and converter path when not on windows
		if (System.getProperty("os.name").startsWith("Windows")) {
			converter = "java_card_kit-2_2_1" + File.separator + "bin" + File.separator + "converter.bat";
			optfile = "EmptyEidCardWindows.opt";
		} else {
			converter = "java_card_kit-2_2_1" + File.separator + "bin" + File.separator + "converter";
			optfile = "EmptyEidCardLinux.opt";
		}
		ProcessBuilder pb2 = new ProcessBuilder(converter, config, optfile);
		pb2.redirectErrorStream(true);
		Process p2 = pb2.start();
		OutputStream os2 = p2.getOutputStream();
		InputStream is2 = p2.getInputStream();
		// spawn two threads to handle I/O with child while we wait for it to
		// complete.
		new Thread(new Receiver(is2)).start();
		new Thread(new Sender(os2)).start();
		try {
			p2.waitFor();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		} catch (Exception e) {
			throw new Exception();
		}
		System.out.println(".cap file conversion done");
		
		// at this point the child is done. All of its output may or may not
		// have been processed however.
		// The Receiver thread will continue until it has finished processing
		// it.
		// You must close the streams even if you never use them! In this case
		// the threads close is and os.
		p2.getErrorStream().close();
	}
	// Load the .cap file on the java card and initialise it.
	public static void uploadCapFile(String cardType, String reader) throws IllegalArgumentException, Exception {
		String gpshell;
		
		
		
		if (System.getProperty("os.name").startsWith("Windows")) {
			gpshell = "GPShell-1.4.2" + File.separator + "GPShell.exe";
		} else if (System.getProperty("os.version").contains("fedora")){
			gpshell = File.separator + "usr" + File.separator + "bin" + File.separator + "gpshell";
		} else 
			gpshell = File.separator + "usr" + File.separator + "local" + File.separator + "bin" + File.separator + "gpshell";
		
		String type;
		if (cardType.equals("Gemalto GemXpresso")) {
			type = "installAppletXonTPC_IM.txt";
		} else if (cardType.equals("NXP SmartMX")) {
			type = "installAppletXonSmart_MX.txt";
		} else {
			JOptionPane.showMessageDialog(null, "Card not supported.", "Card not supported", JOptionPane.ERROR_MESSAGE);
			throw new Exception("Card not supported.");
		}
		// Change reader in .txt file so it corresponds to this reader
		String[] text = FileUtils.readTextFile(type);
		text[4] = "card_connect -reader \"" + reader + "\"";
		// Change separator sign when not on windows
		if (System.getProperty("os.name").startsWith("Windows")) {
			for (int i = 0; i < text.length; i++) {
				text[i].replace("/", File.separator);
			}
			FileUtils.writeTextFile(text, type);
		}
		ProcessBuilder pb = new ProcessBuilder(gpshell, type);
		pb.redirectErrorStream(true);
		Process p = pb.start();
		OutputStream os = p.getOutputStream();
		InputStream is = p.getInputStream();
		// spawn two threads to handle I/O with child while we wait for it to
		// complete.
		new Thread(new CapFileUploadReceiver(is)).start();
		new Thread(new Sender(os)).start();
		try {
			p.waitFor();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException(e.getMessage());
		} catch (Exception e) {
			throw new Exception();
		}
		System.out.println("uploading .cap file done.");
		// at this point the child is done. All of its output may or may not
		// have been processed however.
		// The Receiver thread will continue until it has finished processing
		// it.
		// You must close the streams even if you never use them! In this case
		// the threads close is and os.
		p.getErrorStream().close();
	}
}

/**
 * thread to send output to the child.
 */
final class Sender implements Runnable {
	// ------------------------------ CONSTANTS ------------------------------
	/**
	 * e.g. \n \r\n or \r, whatever system uses to separate lines in a text
	 * file. Only used inside multi-line fields. The file itself should use
	 * Windows format \r \n, though \n by itself will alsolineSeparator work.
	 */
	private static final String lineSeparator = System.getProperty("line.separator");
	// ------------------------------ FIELDS ------------------------------
	/**
	 * stream to send output to child on
	 */
	private final OutputStream os;
	// -------------------------- PUBLIC INSTANCE METHODS
	// --------------------------
	/**
	 * method invoked when Sender thread started. Feeds dummy data to child.
	 */
	public void run() throws IllegalArgumentException {
		try {
			final BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os), 50 /*
																						 * keep
																						 * small
																						 * for
																						 * tests
																						 */);
			for (int i = 99; i >= 0; i--) {
				bw.write("Dummy counter: " + i);
				bw.write(lineSeparator);
			}
			bw.close();
		} catch (IOException e) {
			throw new IllegalArgumentException("IOException sending data to child process.");
		}
	}
	// --------------------------- CONSTRUCTORS ---------------------------
	/**
	 * constructor
	 * 
	 * @param os
	 *            stream to use to send data to child.
	 */
	Sender(OutputStream os) {
		this.os = os;
	}
}

/**
 * thread to read output from child
 */
class Receiver implements Runnable {
	// ------------------------------ FIELDS ------------------------------
	/**
	 * stream to receive data from child
	 */
	private final InputStream is;
	// -------------------------- PUBLIC INSTANCE METHODS
	// --------------------------
	/**
	 * method invoked when Receiver thread started. Reads data from child and
	 * displays in on System.out.
	 */
	public void run() throws IllegalArgumentException {
		try {
			final BufferedReader br = new BufferedReader(new InputStreamReader(is), 50 /*
																						 * keep
																						 * small
																						 * for
																						 * testing
																						 */);
			String line;
			while ((line = br.readLine()) != null) {
				System.out.println(line);
			}
			br.close();
		} catch (IOException e) {
			throw new IllegalArgumentException("IOException receiving data from child process.");
		}
	}
	// --------------------------- CONSTRUCTORS ---------------------------
	/**
	 * contructor
	 * 
	 * @param is
	 *            stream to receive data from child
	 */
	Receiver(InputStream is) {
		this.is = is;
	}
}

/**
 * thread to read output from child
 */
class CapFileUploadReceiver implements Runnable {
	// ------------------------------ FIELDS ------------------------------
	/**
	 * stream to receive data from child
	 */
	private final InputStream is;
	// -------------------------- PUBLIC INSTANCE METHODS
	// --------------------------
	/**
	 * method invoked when Receiver thread started. Reads data from child and
	 * displays in on System.out.
	 */
	public void run() throws IllegalArgumentException {
		try {
			final BufferedReader br = new BufferedReader(new InputStreamReader(is), 50 /*
																						 * keep
																						 * small
																						 * for
																						 * testing
																						 */);
			String line;
			String line1 = null;
			String line2 = null;
			String line3 = null;
			while ((line = br.readLine()) != null) {
				line3 = line2;
				line2 = line1;
				line1 = line;
				System.out.println(line);
			}
			if (!line1.equals("release_context") || !line2.equals("card_disconnect") || !line3.equals("Response <-- 009000")) {
				JOptionPane.showMessageDialog(null, "Error loading applet. Check reader and smart card connection. Card could also not be supported.", "Applet loading failed", JOptionPane.ERROR_MESSAGE);
				throw new IllegalArgumentException();
			}
			br.close();
		} catch (IOException e) {
			throw new IllegalArgumentException("IOException receiving data from child process.");
		}
	}
	// --------------------------- CONSTRUCTORS ---------------------------
	/**
	 * contructor
	 * 
	 * @param is
	 *            stream to receive data from child
	 */
	CapFileUploadReceiver(InputStream is) throws IllegalArgumentException {
		this.is = is;
	}
}
