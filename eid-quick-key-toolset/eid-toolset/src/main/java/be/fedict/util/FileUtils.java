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
package be.fedict.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import be.fedict.eidtoolset.eidlibrary.MasterFile;

public class FileUtils {
	public static InputStream bytesToStream(byte[] ba) throws IOException {
		ByteArrayInputStream fis = new ByteArrayInputStream(ba);
		DataInputStream dis = new DataInputStream(fis);
		byte[] bytes = new byte[dis.available()];
		dis.readFully(bytes);
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		return bais;
	}
	public static InputStream fileToStream(String filename) throws IOException {
		FileInputStream fis = new FileInputStream(filename);
		DataInputStream dis = new DataInputStream(fis);
		byte[] bytes = new byte[dis.available()];
		dis.readFully(bytes);
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		return bais;
	}
	public static byte[] readFile(String filename) throws FileNotFoundException, IOException {
		FileInputStream file = new FileInputStream(filename);
		byte[] data = new byte[(int) file.available()];
		file.read(data);
		file.close();
		return data;
	}
	public static void writeFile(String filename, byte[] data) throws FileNotFoundException, IOException {
		FileOutputStream file = new FileOutputStream(filename);
		file.write(data);
		file.close();
	}
	public static void writeBytesToFile(String filename, byte[] data) throws FileNotFoundException, IOException {
		FileOutputStream file = new FileOutputStream(filename);
		file.write(data);
		file.close();
	}
	public static String[] readTextFile(String filename) throws IOException {
		List<String> wordList = new ArrayList<String>();
		BufferedReader reader = new BufferedReader(new FileReader(filename));
		String line;
		while ((line = reader.readLine()) != null) {
			wordList.add(line);
		}
		reader.close();
		String[] words = new String[wordList.size()];
		wordList.toArray(words);
		return words;
	}
	public static void writeTextFile(String[] text, String filename) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
		for (int i = 0; i < text.length; i++) {
			writer.write(text[i]);
			writer.newLine();
		}
		writer.close();
	}
	/**
	 * Write a Masterfile to an xml document
	 * 
	 * @param masterf
	 * @param pathname
	 * @throws JAXBException
	 * @throws IOException
	 */
	public static void writeDocument(MasterFile masterf, String pathname) throws JAXBException, IOException {
		JAXBContext context = JAXBContext.newInstance(masterf.getClass().getPackage().getName());
		Marshaller m = context.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		m.marshal(masterf, new FileOutputStream(pathname));
	}
	/**
	 * Load a Masterfile using a .xml pathname
	 * 
	 * @param pathname
	 * @return
	 * @throws JAXBException
	 * @throws IOException
	 */
	public static MasterFile readDocument(String pathname) throws JAXBException, IOException {
		JAXBContext context = JAXBContext.newInstance(MasterFile.class.getPackage().getName());
		Unmarshaller u = context.createUnmarshaller();
		return (MasterFile) u.unmarshal(new File(pathname));
	}
}
