/*                                                                                 */
/*  Copyright 2009 Espen Fjellvær Olsen <espen@mrfjo.org>.  All rights reserved.  */
/*  See license distributed with this file and                                     */
/*  available online at http://http://www.gnu.org/licenses/gpl-3.0.txt             */
/*                                                                                 */
/*                                                                                 */
/*                                                                                 */

package main;
import java.io.*;
import java.net.*;

public class ModuleLoader extends ClassLoader {
	String path;

	public static Class load(String filename) {
		return (new ModuleLoader()).loadIt(new File(filename));
	}

	// Loads the file specified in the f file object.
	Class loadIt(File f) {
		System.out.println(f.getAbsolutePath());
		String filename = f.getPath();
		path = filename.substring(0, filename.lastIndexOf(f.separator) + 1);

		// Make sure the filename ends with .class
		if (!filename.endsWith(".class")) {
			filename += ".class";
		}
		try {
			// Read in the byte codes.
			InputStream is = new FileInputStream(filename);
			byte buf[] = new byte[is.available()];
			is.read(buf, 0, is.available());
			is.close();

			// Define and resolve the class.
			Class c = defineClass(null, buf, 0, buf.length);
			if (c != null) {
				super.resolveClass(c);
			}
			return c;
		} catch (Exception e) {
		}
		return null;
	}

	protected Class loadClass(String name, boolean resolve)
	throws ClassNotFoundException {
		System.out.println(name);
		path="";
		try {
			if(name.indexOf("modules.")<0)
			return findSystemClass(name);
		} catch (ClassNotFoundException e) {
		}

		// If the previous load failed, try again
		// after removing the package name, if any.
		
		if (name.indexOf(".") >= 0) {
			name = name.replace(".", "//");
		}
		
		
		return loadIt(new File(path + name));
	}

	// Returns a URL containing the location of the named resource.
	public URL getResource(String name) {
		try {
			File file = new File(path);
			String absPath = file.getAbsolutePath().replace(file.separatorChar,
			'/');

			// There is no need to insert a '/' between the absPath
			// and name because absPath already ends with a '/'.
			return new URL("file:///" + absPath + name);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}

	// Returns an input stream to the named resource.
	public InputStream getResourceAsStream(String name) {
		try {
			return new FileInputStream(path + name);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
}