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
 * Constants defining the JSON request parameters for the drawing command
 * 
 * @author Adam Andrews
 */
public interface JLDrawingRequestParams {

	// command name
    public static final String COMMAND = "drawing";

    // function names
    public static final String FUNC_ADD_MODEL		= "add_model";
    public static final String FUNC_CREATE 			= "create";
    public static final String FUNC_CREATE_GEN_VIEW	= "create_gen_view";
    public static final String FUNC_CREATE_PROJ_VIEW	= "create_proj_view";
    public static final String FUNC_DELETE_MODELS	= "delete_models";
    public static final String FUNC_ADD_SHEET		= "add_sheet";
    public static final String FUNC_DELETE_SHEET	= "delete_sheet";
    public static final String FUNC_DELETE_VIEW		= "delete_view";
    public static final String FUNC_GET_CUR_MODEL	= "get_cur_model";
    public static final String FUNC_GET_CUR_SHEET	= "get_cur_sheet";
    public static final String FUNC_GET_NUM_SHEETS	= "get_num_sheets";
    public static final String FUNC_GET_SHEET_SCALE	= "get_sheet_scale";
    public static final String FUNC_GET_SHEET_SIZE	= "get_sheet_size";
    public static final String FUNC_GET_VIEW_LOC	= "get_view_loc";
    public static final String FUNC_GET_VIEW_SCALE	= "get_view_scale";
    public static final String FUNC_GET_VIEW_SHEET	= "get_view_sheet";
    public static final String FUNC_LIST_MODELS		= "list_models";
    public static final String FUNC_LIST_VIEWS		= "list_views";
    public static final String FUNC_LIST_VIEW_DETAILS	= "list_view_details";
    public static final String FUNC_REGENERATE		= "regenerate";
    public static final String FUNC_REGENERATE_SHEET	= "regenerate_sheet";
    public static final String FUNC_RENAME_VIEW		= "rename_view";
    public static final String FUNC_SCALE_SHEET		= "scale_sheet";
    public static final String FUNC_SELECT_SHEET	= "select_sheet";
    public static final String FUNC_SET_CUR_MODEL	= "set_cur_model";
    public static final String FUNC_SET_VIEW_LOC	= "set_view_loc";
    public static final String FUNC_SCALE_VIEW		= "scale_view";
    public static final String FUNC_VIEW_BOUND_BOX	= "view_bound_box";
    public static final String FUNC_LOAD_SYMBOL_DEF	= "load_symbol_def";
    public static final String FUNC_IS_SYMBOL_DEF_LOADED	= "is_symbol_def_loaded";
    public static final String FUNC_CREATE_SYMBOL	= "create_symbol";
    public static final String FUNC_LIST_SYMBOLS	= "list_symbols";
    public static final String FUNC_DELETE_SYMBOL_DEF	= "delete_symbol_def";
    public static final String FUNC_DELETE_SYMBOL_INST	= "delete_symbol_inst";
    public static final String FUNC_GET_SHEET_FORMAT	= "get_sheet_format";
    public static final String FUNC_SET_SHEET_FORMAT	= "set_sheet_format";
    public static final String FUNC_TABLE_LIST			= "table_list";
    public static final String FUNC_TABLE_GET_INFO		= "table_get_info";
    public static final String FUNC_TABLE_GET_CELL		= "table_get_cell";
    public static final String FUNC_TABLE_SET_CELL		= "table_set_cell";
    public static final String FUNC_TABLE_GET_RANGE		= "table_get_range";
    public static final String FUNC_TABLE_SET_RANGE		= "table_set_range";
    public static final String FUNC_TABLE_INSERT_ROWS	= "table_insert_rows";
    public static final String FUNC_TABLE_DELETE_ROWS	= "table_delete_rows";
    public static final String FUNC_TABLE_INSERT_COLUMNS	= "table_insert_columns";
    public static final String FUNC_TABLE_DELETE_COLUMNS	= "table_delete_columns";
    public static final String FUNC_TABLE_CLEAR_CELL		= "table_clear_cell";
    public static final String FUNC_TABLE_CLEAR_RANGE	= "table_clear_range";
    public static final String FUNC_TABLE_MOVE			= "table_move";
    public static final String FUNC_TABLE_SET_COLUMN_WIDTH	= "table_set_column_width";
    public static final String FUNC_TABLE_SET_ROW_HEIGHT	= "table_set_row_height";
    public static final String FUNC_TABLE_MERGE_CELLS	= "table_merge_cells";
    public static final String FUNC_TABLE_UNMERGE_CELLS	= "table_unmerge_cells";
    public static final String FUNC_TABLE_CREATE			= "table_create";
    public static final String FUNC_TABLE_DELETE			= "table_delete";
    public static final String FUNC_TABLE_REGENERATE	= "table_regenerate";

    // request fields
    public static final String PARAM_MODEL		= "model";
    public static final String PARAM_DIRNAME	= "dirname";
    public static final String PARAM_FILE		= "file";
//    public static final String PARAM_FORMAT_FILE	= "format_file";
    public static final String PARAM_DRAWING	= "drawing";
    public static final String PARAM_SHEET		= "sheet";
    public static final String PARAM_VIEW		= "view";
    public static final String PARAM_TEMPLATE	= "template";
    public static final String PARAM_SCALE		= "scale";
    public static final String PARAM_DISPLAY	= "display";
    public static final String PARAM_ACTIVATE	= "activate";
    public static final String PARAM_NEWWIN		= "new_window";
    public static final String PARAM_NEWVIEW	= "new_view";
    public static final String PARAM_DELETE_VIEWS	= "delete_views";
    public static final String PARAM_POINT		= "point";
    public static final String PARAM_RELATIVE	= "relative";
    public static final String PARAM_DEL_CHILDREN	= "del_children";
    public static final String PARAM_MODEL_VIEW	= "model_view";
    public static final String PARAM_PARENT_VIEW	= "parent_view";
    public static final String PARAM_EXPLODED	= "exploded";
    public static final String PARAM_POSITION	= "position";
    public static final String PARAM_SYMBOL_DIR	= "symbol_dir";
    public static final String PARAM_SYMBOL_FILE	= "symbol_file";
    public static final String PARAM_SYMBOL_ID	= "symbol_id";
    public static final String PARAM_REPLACE_VALUES	= "replace_values";
    public static final String PARAM_TABLE_INDEX	= "tableIndex";
    public static final String PARAM_AT_ROW		= "atRow";
    public static final String PARAM_AT_COLUMN	= "atColumn";
    public static final String PARAM_ROW		= "row";
    public static final String PARAM_COLUMN		= "column";
    public static final String PARAM_START_ROW	= "startRow";
    public static final String PARAM_START_COLUMN	= "startColumn";
    public static final String PARAM_END_ROW	= "endRow";
    public static final String PARAM_END_COLUMN	= "endColumn";
    public static final String PARAM_ROWS		= "rows";
    public static final String PARAM_COLUMNS	= "columns";
    public static final String PARAM_COUNT		= "count";
    public static final String PARAM_VALUE		= "value";
    public static final String PARAM_VALUES		= "values";
    public static final String PARAM_WIDTH		= "width";
    public static final String PARAM_HEIGHT		= "height";

    // view display data params
    public static final String PARAM_DISPLAY_DATA				= "display_data";
    public static final String PARAM_CABLE_STYLE				= "cable_style";
    public static final String PARAM_REMOVE_QUILT_HIDDEN_LINES	= "remove_quilt_hidden_lines";
    public static final String PARAM_SHOW_CONCEPT_MODEL			= "show_concept_model";
    public static final String PARAM_SHOW_WELD_XSECTION			= "show_weld_xsection";
    public static final String PARAM_STYLE						= "style";
    public static final String PARAM_TANGENT_STYLE				= "tangent_style";

}
