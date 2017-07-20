/*
 * MIT LICENSE
 * Copyright 2000-2017 Simplified Logic, Inc
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
package com.simplifiedlogic.nitro.jshell;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.simplifiedlogic.nitro.jshell.funcs.ServerJsonHelp;
import com.simplifiedlogic.nitro.jshell.json.help.JLJsonHelpHandler;
import com.simplifiedlogic.nitro.jshell.json.help.OrderedMap;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionObject;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionTemplate;

/**
 * Generate help-doc JSON files for all the functions supported.  Run this class from the command
 * line in the server installation directory, and it will update the existing JSON files
 * in web/jsonSpecs
 * 
 * @author Adam Andrews
 *
 */
public class GenTemplates {

	/**
	 * Object for converting java objects to JSON strings
	 */
	private static ObjectMapper mapper = new ObjectMapper();
	
	public static void main(String[] args) {
		if (args.length==0) {
			System.err.println("Usage: GenTemplates <output dir>");
			return;
		}
		File f = new File(args[0]);
		if (!f.exists());
			f.mkdirs();
		generateTemplates(f.getAbsolutePath());
	}

	/**
	 * Generate the JSON files
	 * 
	 * @param dir The output directory
	 */
	private static void generateTemplates(String dir) {
		JLJsonHelpHandler hdler = new JLJsonHelpHandler();
		ServerJsonHelp svr_hdler = new ServerJsonHelp();
		mapper.enable(SerializationFeature.INDENT_OUTPUT);

		// generate the function files
		List<FunctionTemplate> templates = hdler.getTemplates();
		List<String> functions = new ArrayList<String>();
		if (templates!=null) {
			List<FunctionTemplate> temp2 = svr_hdler.getHelp();
			if (temp2!=null && temp2.size()>0)
				templates.addAll(temp2);
			
			try {
				for (FunctionTemplate temp : templates) {
					Map<String, Object> map = temp.getObject();
					String json = mapper.writeValueAsString(map);
					json = cleanHTML(json);
					// prettify here?

					String fname = temp.getCommand()+"-"+temp.getFunction()+".json";
					functions.add(fname);
					FileWriter fw = new FileWriter(new File(dir, fname));
					System.out.println("Writing " + fname);
					fw.write(json);
					fw.close();
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		// generate the shared object files
		List<FunctionObject> objects = hdler.getTemplateObjects();
		if (objects!=null) {
			List<FunctionObject> temp2 = svr_hdler.getHelpObjects();
			if (temp2!=null && temp2.size()>0)
				objects.addAll(temp2);
			
			try {
				for (FunctionObject obj : objects) {
					Map<String, Object> map = obj.getObject();
					String json = mapper.writeValueAsString(map);
					// prettify here
					String fname = "object"+"-"+obj.getObjectName()+".json";
					functions.add(fname);
					FileWriter fw = new FileWriter(new File(dir, fname));
					System.out.println("Writing " + fname);
					fw.write(json);
					fw.close();
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		// write the index file which contains all the json file names
		try {
			Collections.sort(functions);
			Map<String, Object> fmap = new OrderedMap<String, Object>();
			fmap.put("creosonFunctions", functions);
			String json = mapper.writeValueAsString(fmap);
			// prettify here
			String fname = "creosonFunctions.json";
			FileWriter fw = new FileWriter(new File(dir, fname));
			System.out.println("Writing " + fname);
			fw.write(json);
			fw.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Clean up special HTML tags from the JSON input, replace them as necessary
	 * @param input The JSON string to be cleaned
	 * @return The cleaned JSON string
	 */
	public static String cleanHTML(String input) {
		String out = input.replaceAll("<", "&lt;");
		out = out.replaceAll(">", "&gt;");
		return out;
	}
}
