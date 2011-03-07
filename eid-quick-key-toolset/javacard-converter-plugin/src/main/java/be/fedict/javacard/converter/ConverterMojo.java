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

package be.fedict.javacard.converter;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import com.sun.javacard.converter.Converter;

/**
 * Maven2 plugin to convert to CAP files.
 * 
 * 
 * @author Frank Cornelis
 * @goal convert
 */
public class ConverterMojo extends AbstractMojo {

	/**
	 * @parameter expression="${basedir}"
	 * @required
	 */
	private File basedir;

	/**
	 * @parameter
	 * @required
	 */
	private List<String> exports;

	/**
	 * @parameter expression="${project.build.outputDirectory}"
	 * @required
	 */
	private File outputDirectory;

	/**
	 * @parameter expression="${project.build.directory}"
	 * @required
	 */
	private File buildDirectory;

	/**
	 * @parameter
	 * @required
	 */
	private String packageName;

	/**
	 * @parameter
	 * @required
	 */
	private String packageAid;

	/**
	 * @parameter
	 * @required
	 */
	private String version;

	/**
	 * @parameter
	 * @required
	 */
	private String appletAid;

	/**
	 * @parameter
	 * @required
	 */
	private String appletClass;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		getLog().info("JavaCard Converter");
		getLog().info("basedir: " + this.basedir.getAbsolutePath());
		List<String> argsList = new LinkedList<String>();

		argsList.add("-out");
		argsList.add("CAP");

		argsList.add("-verbose");

		argsList.add("-applet");
		argsList.add(this.appletAid);
		argsList.add(this.appletClass);

		argsList.add("-classdir");
		argsList.add(this.outputDirectory.getAbsolutePath());

		argsList.add("-exportpath");
		String exportPath = "";
		for (String export : this.exports) {
			File exportFile = new File(this.basedir, export);
			exportPath += exportFile.getAbsolutePath() + ":";
		}
		getLog().info("export path: " + exportPath);
		argsList.add(exportPath);

		argsList.add("-d");
		argsList.add(this.buildDirectory.getAbsolutePath());

		argsList.add(this.packageName);
		argsList.add(this.packageAid);
		argsList.add(this.version);
		Converter.main(argsList.toArray(new String[argsList.size()]));
	}
}
