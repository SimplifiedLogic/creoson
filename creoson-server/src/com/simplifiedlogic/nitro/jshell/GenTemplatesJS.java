/*
 * MIT LICENSE
 * Copyright 2000-2021 Simplified Logic, Inc
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.simplifiedlogic.nitro.jshell.json.help.JLJsonCommandHelp;
import com.simplifiedlogic.nitro.jshell.json.help.JLJsonHelpHandler;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionArgument;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionSpec;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionTemplate;

/**
 * Generate help-doc JSON files for all the functions supported.  Run this class from the command
 * line in the server installation directory, and it will update the existing JSON files
 * in web/jsonSpecs
 * 
 * @author Adam Andrews
 *
 */
public class GenTemplatesJS {

	public static final String CMD_VAR 			= "#cmd#";
	public static final String CMD_VAR_CAMEL 	= "#Cmd#";
	public static final String FUNC_VAR 		= "#func#";
	public static final String FUNC_DESC_VAR	= "#func_desc#";
	public static final String PROP_VAR 		= "#prop#";
	public static final String PROP_TYPE_VAR 	= "#prop_type#";
	public static final String PROP_DESC_VAR 	= "#prop_desc#";
	
	public static final String FUNCTION_BLOCK		= "#FUNCTION BLOCK#";
	public static final String END_FUNCTION_BLOCK	= "#END FUNCTION BLOCK#";

	public static void main(String[] args) {
		if (args.length<2) {
			System.err.println("Usage: GenTemplatesJS <js-template> <output dir>");
			return;
		}
		File f = new File(args[0]);
		if (!f.exists()) {
			System.err.println("js-template file not found: "+args[0]);
		}
		// read in template data
		List<String> jsfile = new ArrayList<String>();
		try {
			BufferedReader r = new BufferedReader(new FileReader(f));
			String line;
			while ((line=r.readLine())!=null) {
				jsfile.add(line);
			}
		}
		catch (Exception e) {
			System.err.println("Error reading js template "+f.getName()+": "+e.getMessage());
			return;
		}
		
		
		File dir = new File(args[1]);
		if (!dir.exists());
			dir.mkdirs();
		generateTemplates(jsfile, dir.getAbsolutePath());
	}

	/**
	 * Generate the JS files
	 * 
	 * @param dir The output directory
	 */
	private static void generateTemplates(List<String> jsfile, String dir) {
		JLJsonHelpHandler hdler = new JLJsonHelpHandler();

		// generate the function files
		List<JLJsonCommandHelp> helps= hdler.getHelps();
//		List<String> functions = new ArrayList<String>();
		if (helps!=null) {
			try {
				for (JLJsonCommandHelp help : helps) {
					String output = substitute(jsfile, help);

					String fname = "creoson_"+help.getCommand()+".js";
					FileWriter fw = new FileWriter(new File(dir, fname));
					System.out.println("Writing " + fname);
					fw.write(output);
					fw.close();
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}


/*
		// generate the shared object files
		List<FunctionObject> objects = hdler.getTemplateObjects();
		if (objects!=null) {
			try {
				for (FunctionObject obj : objects) {
					Map<String, Object> map = obj.getObject();
					String json = mapper.writeValueAsString(map);
					// prettify here
					String fname = "creoson_"+obj.getObjectName()+".js";
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
*/
		// write the index file which contains all the json file names
/*		try {
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
*/
	}

	private static String substitute(List<String> jsfile, JLJsonCommandHelp help) {
		StringBuffer buf = new StringBuffer();
		String outline;
		List<String> funcBlock = new ArrayList<String>();
		List<FunctionTemplate> funcs = help.getHelp();
		
		List<NameType> allargs = new ArrayList<NameType>();
		Map<String, NameType> allmap = new HashMap<String, NameType>();
		for (FunctionTemplate temp : funcs) {
			List<FunctionArgument> args = temp.getSpec().getRequest();
			if (args!=null) {
				for (FunctionArgument arg : args) {
					if (allmap.get(arg.getName())==null) {
						String propType = arg.getType();
						if (FunctionSpec.TYPE_OBJECT.equals(propType) || FunctionSpec.TYPE_ARRAY.equals(propType) || FunctionSpec.TYPE_OBJARRAY.equals(propType))
							if (arg.getArrayType()!=null)
								propType = propType + ":" + arg.getArrayType();
						allmap.put(arg.getName(), new NameType(arg.getName(), propType, arg.getDescription()));
					}
				}
			}
		}
		for (String name : allmap.keySet()) {
			allargs.add(allmap.get(name));
		}
		Collections.sort(allargs, new Comparator<NameType>() {
			@Override
			public int compare(NameType o1, NameType o2) {
				return o1.name.compareTo(o2.name);
			}
		});

		int len = jsfile.size();
		for (int i=0; i<len; i++) {
			String line = jsfile.get(i);
			if (line.contains(FUNCTION_BLOCK)) {
				for (i++; i<len; i++) {
					line = jsfile.get(i);
					if (line.contains(END_FUNCTION_BLOCK)) {
						i++;
						break;
					}
					funcBlock.add(line);
				}
				if (funcs==null)
					continue; // skip if no functions
				
				for (FunctionTemplate temp : funcs) {
					substitute(funcBlock, temp, buf);
				}
			}
			else {
				if (line.contains(PROP_VAR) || line.contains(PROP_TYPE_VAR) || line.contains(PROP_DESC_VAR)) {
					if (allargs==null)
						continue;
					for (NameType nt : allargs) {
						outline = replaceVars(line, help, nt);
						buf.append(outline);
						buf.append("\r\n");
					}
				}
				else {
					outline = replaceVars(line, help);
					buf.append(outline);
					buf.append("\r\n");
				}
			}
		}
		
		return buf.toString();
	}
	
	private static void substitute(List<String> jsfile, FunctionTemplate temp, StringBuffer buf) {
		String outline;
		
		FunctionSpec spec = temp.getSpec();
		List<FunctionArgument> args = spec.getRequest();
		for (String line : jsfile) {
			if (line.contains(PROP_VAR) || line.contains(PROP_TYPE_VAR)) {
				if (args==null)
					continue; // skip any line with props if there aren't any props in the spec
				for (FunctionArgument arg : args) {
					outline = replaceVars(line, temp, arg.getName(), arg.getType(), arg.getArrayType());
					buf.append(outline);
					buf.append("\r\n");
				}
			}
			else {
				outline = replaceVars(line, temp, null, null, null);
				buf.append(outline);
				buf.append("\r\n");
			}
		}
	}
	
	private static String replaceVars(String line, JLJsonCommandHelp help) {
		StringBuffer out = new StringBuffer(line);
		
		replaceVar(out, CMD_VAR, help.getCommand());
		if (line.contains(CMD_VAR_CAMEL)) {
			char uc = Character.toUpperCase(help.getCommand().charAt(0));
			String camel = String.valueOf(uc)+help.getCommand().substring(1);
			replaceVar(out, CMD_VAR_CAMEL, camel);
		}
		
		return out.toString();
	}

	private static String replaceVars(String line, JLJsonCommandHelp help, NameType nt) {
		StringBuffer out = new StringBuffer(line);
		
		replaceVar(out, CMD_VAR, help.getCommand());
		if (line.contains(CMD_VAR_CAMEL)) {
			char uc = Character.toUpperCase(help.getCommand().charAt(0));
			String camel = String.valueOf(uc)+help.getCommand().substring(1);
			replaceVar(out, CMD_VAR_CAMEL, camel);
		}
		if (nt!=null) {
			replaceVar(out, PROP_VAR, nt.name);
			replaceVar(out, PROP_TYPE_VAR, nt.type);
			replaceVar(out, PROP_DESC_VAR, nt.desc);
		}
		
		return out.toString();
	}

	private static String replaceVars(String line, FunctionTemplate temp, String prop, String propType, String propSubType) {
		StringBuffer out = new StringBuffer(line);
		
		replaceVar(out, CMD_VAR, temp.getCommand());
		if (line.contains(CMD_VAR_CAMEL)) {
			char uc = Character.toUpperCase(temp.getCommand().charAt(0));
			String camel = String.valueOf(uc)+temp.getCommand().substring(1);
			replaceVar(out, CMD_VAR_CAMEL, camel);
		}
		replaceVar(out, FUNC_VAR, temp.getFunction());
		replaceVar(out, FUNC_DESC_VAR, temp.getSpec().getFunctionDescription());
		if (prop!=null) {
			replaceVar(out, PROP_VAR, prop);
			if (line.contains(PROP_TYPE_VAR)) {
				if (FunctionSpec.TYPE_OBJECT.equals(propType) || FunctionSpec.TYPE_ARRAY.equals(propType) || FunctionSpec.TYPE_OBJARRAY.equals(propType))
					if (propSubType!=null)
						propType = propType + ":" + propSubType;
				replaceVar(out, PROP_TYPE_VAR, propType);
			}
		}
		
		return out.toString();
	}

	private static void replaceVar(StringBuffer line, String var, String value) {
		if (value==null)
			value="";
		int pos;
		while ((pos=line.indexOf(var))>=0)
			line.replace(pos, pos+var.length(), value);
	}

	private static class NameType {
		public String name;
		public String type;
		public String desc;
		public NameType(String name, String type, String desc) {
			this.name=name;
			this.type=type;
			this.desc=desc;
		}
	}
}
