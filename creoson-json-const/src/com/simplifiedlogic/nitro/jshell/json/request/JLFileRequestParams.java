/*
 * MIT LICENSE
 * Copyright 2000-2023 Simplified Logic, Inc
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
package com.simplifiedlogic.nitro.jshell.json.request;

/**
 * Constants defining the JSON request parameters for the file command
 * 
 * @author Adam Andrews
 */
public interface JLFileRequestParams {

	// command name
    public static final String COMMAND = "file";

    // function names
	public static final String FUNC_OPEN 			= "open";
	public static final String FUNC_OPEN_ERRORS 	= "open_errors";
	public static final String FUNC_RENAME 			= "rename";
	public static final String FUNC_SAVE 			= "save";
	public static final String FUNC_ERASE 			= "erase";
	public static final String FUNC_ERASE_NOT_DISP	= "erase_not_displayed";
	public static final String FUNC_BACKUP 			= "backup";
	public static final String FUNC_REGENERATE 		= "regenerate";
	public static final String FUNC_GET_ACTIVE 		= "get_active";
	public static final String FUNC_LIST	 		= "list";
	public static final String FUNC_EXISTS	 		= "exists";
	public static final String FUNC_GET_FILEINFO	= "get_fileinfo";
	public static final String FUNC_RELATIONS_GET	= "relations_get";
	public static final String FUNC_RELATIONS_SET	= "relations_set";
	public static final String FUNC_POST_REGEN_RELATIONS_GET	= "postregen_relations_get";
	public static final String FUNC_POST_REGEN_RELATIONS_SET	= "postregen_relations_set";
	public static final String FUNC_REFRESH 		= "refresh";
	public static final String FUNC_REPAINT 		= "repaint";
	public static final String FUNC_IS_ACTIVE 		= "is_active";
	public static final String FUNC_DISPLAY 		= "display";
	public static final String FUNC_CLOSE_WINDOW	= "close_window";
	public static final String FUNC_HAS_INSTANCES	= "has_instances";
	public static final String FUNC_LIST_INSTANCES	= "list_instances";
	public static final String FUNC_MASSPROPS 		= "massprops";
	public static final String FUNC_GET_LENGTH_UNITS	= "get_length_units";
	public static final String FUNC_GET_MASS_UNITS		= "get_mass_units";
	public static final String FUNC_SET_LENGTH_UNITS	= "set_length_units";
	public static final String FUNC_SET_MASS_UNITS		= "set_mass_units";
	public static final String FUNC_GET_UNIT_SYSTEM		= "get_unit_system";
	public static final String FUNC_SET_UNIT_SYSTEM		= "set_unit_system";
	public static final String FUNC_ASSEMBLE		= "assemble";
	public static final String FUNC_GET_TRANSFORM	= "get_transform";
	public static final String FUNC_LIST_SIMP_REPS	= "list_simp_reps";
	public static final String FUNC_GET_CUR_MATL			= "get_cur_material";
	public static final String FUNC_GET_CUR_MATL_WILDCARD	= "get_cur_material_wildcard";
	public static final String FUNC_SET_CUR_MATL			= "set_cur_material";
	public static final String FUNC_LIST_MATERIALS			= "list_materials";
	public static final String FUNC_LIST_MATERIALS_WILDCARD	= "list_materials_wildcard";
	public static final String FUNC_LOAD_MATL_FILE			= "load_material_file";
	public static final String FUNC_DELETE_MATERIAL			= "delete_material";
	public static final String FUNC_GET_ACCURACY			= "get_accuracy";
	public static final String FUNC_CREATE_UNIT_SYSTEM		= "create_unit_system";
	
	// request fields
    public static final String PARAM_MODEL       = "file";
    public static final String PARAM_MODELS      = "files";
    public static final String PARAM_GENERIC     = "generic";
    public static final String PARAM_NEWNAME     = "new_name";
    public static final String PARAM_DIRNAME     = "dirname";
    public static final String PARAM_TARGETDIR   = "target_dir";
    public static final String PARAM_REP         = "rep";
    public static final String PARAM_DISPLAY     = "display";
    public static final String PARAM_ACTIVATE    = "activate";
    public static final String PARAM_ONLYSESSION = "onlysession";
    public static final String PARAM_NEWWIN      = "new_window";
    public static final String PARAM_FORCE       = "regen_force";
    public static final String PARAM_RELATIONS   = "relations";
    public static final String PARAM_PATH        = "path";
    public static final String PARAM_INTOASM     = "into_asm";
    public static final String PARAM_TRANSFORM   = "transform";
    public static final String PARAM_CONSTRAINT  = "constraint";
    public static final String PARAM_CONSTRAINT1 = "constraint1";
    public static final String PARAM_CONSTRAINT2 = "constraint2";
    public static final String PARAM_CONSTRAINT3 = "constraint3";
    public static final String PARAM_CONSTRAINT4 = "constraint4";
    public static final String PARAM_CONSTRAINT5 = "constraint5";
    public static final String PARAM_CONSTRAINT6 = "constraint6";
    public static final String PARAM_CONSTRAINT7 = "constraint7";
    public static final String PARAM_CONSTRAINT8 = "constraint8";
    public static final String PARAM_CONSTRAINT9 = "constraint9";
    public static final String PARAM_CONSTRAINT10 = "constraint10";
    public static final String PARAM_CONSTRAINTS = "constraints";
    public static final String PARAM_ASM         = "asm";
    public static final String PARAM_CSYS        = "csys";
    public static final String PARAM_ERASE_CHILDREN	= "erase_children";
    public static final String PARAM_UNITS       = "units";
    public static final String PARAM_CONVERT     = "convert";
    public static final String PARAM_PACKAGE_ASSEMBLY	= "package_assembly";
    public static final String PARAM_REF_MODEL			= "ref_model";
    public static final String PARAM_WALK_CHILDREN		= "walk_children";
    public static final String PARAM_ASSEMBLE_TO_ROOT	= "assemble_to_root";
    public static final String PARAM_SUPPRESS	= "suppress";
    public static final String PARAM_MATERIAL	= "material";
    public static final String PARAM_INCLUDE_NON_MATCHING = "include_non_matching_parts";
	public static final String PARAM_UNIT_MASS_FORCE 	= "unit_mass";
	public static final String PARAM_UNIT_LENGTH 		= "unit_length";
	public static final String PARAM_UNIT_TIME 			= "unit_time";
	public static final String PARAM_UNIT_TEMP 			= "unit_temp";
	public static final String PARAM_NAME 		= "name";
	public static final String PARAM_MASS 		= "mass";

    // point/matrix/transform params
	public static final String PARAM_X			= "x";
	public static final String PARAM_Y			= "y";
	public static final String PARAM_Z			= "z";
	public static final String PARAM_XAXIS		= "x_axis";
	public static final String PARAM_YAXIS		= "y_axis";
	public static final String PARAM_ZAXIS		= "z_axis";
	public static final String PARAM_ORIGIN		= "origin";
	public static final String PARAM_X_ROT		= "x_rot";
	public static final String PARAM_Y_ROT		= "y_rot";
	public static final String PARAM_Z_ROT		= "z_rot";

	// constraint params
    public final static String PARAM_TYPE           = "type";
    public final static String PARAM_ASMREF         = "asmref";
    public final static String PARAM_COMPREF        = "compref";
    public final static String PARAM_ASMDATUM       = "asmdatum";
    public final static String PARAM_COMPDATUM      = "compdatum";
    public final static String PARAM_OFFSET         = "offset";
    
    public final static String DATUM_SIDE_RED		= "red";
    public final static String DATUM_SIDE_YELLOW	= "yellow";

}
