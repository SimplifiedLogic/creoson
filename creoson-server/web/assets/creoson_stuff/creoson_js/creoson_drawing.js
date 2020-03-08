// Define the Module
var creo = creo || {};

creo = (function (pub) {

    pub.DrawingObj = function(propsObj) {
        // BASE OBJECT
        this.activate = undefined; // boolean - Activate the drawing window after opening
        this.del_children = undefined; // boolean - Whether to also delete any children of the view
        this.delete_views = undefined; // boolean - Whether to delete drawing views associated with the model
        this.dirname = undefined; // string - Directory name containing the format file
        this.display = undefined; // boolean - Display the drawing after opening
        this.display_data = undefined; // object:ViewDisplayData - Display parameters used to create the view
        this.drawing = undefined; // string - Drawing name
        this.exploded = undefined; // boolean - Whether to create the view as an exploded view
        this.file = undefined; // string - Format file name
        this.model = undefined; // string - Model name
        this.model_view = undefined; // string - Model view to use for the drawing view orientation
        this.new_view = undefined; // string - New view name
        this.new_window = undefined; // boolean - Open drawing in a new window
        this.parent_view = undefined; // string - Parent view for the projection view
        this.point = undefined; // object:JLPoint - Coordinates for the view in Drawing Units
        this.position = undefined; // integer - Position to add the sheet
        this.replace_values = undefined; // object - Object containing replacement values for any variable text in the symbol
        this.scale = undefined; // double - Drawing scale
        this.sheet = undefined; // integer - Sheet number
        this.symbol_dir = undefined; // string - Directory containing the symbol file; if relative, assumed to be relative to Creo's current working directory
        this.symbol_file = undefined; // string - Name of the symbol file
        this.symbol_id = undefined; // integer - ID of the symbol instance
        this.template = undefined; // string - Template
        this.view = undefined; // string - New view name
        
		// VALIDATE and SET ANY REQUESTED PROPERTIES
		if (typeof propsObj === "object") {
		    let propKeys = Object.keys(propsObj);
		    for (let p=0; p < propKeys.length; p++) {
		        var key = propKeys[p];
		        if (this.hasOwnProperty(key)) {
		            this[key] = propsObj[key];
		        } else {
		            throw("ERROR: '"+key+"' NOT A VALID OPTION!")
		        }
		    }
		}
    };


    // Add a model to a drawing
    pub.DrawingObj.prototype.add_model = function () {

        console.log('got into : pub.DrawingObj.add_model');

        let reqObj = {
            command : "drawing",
            function : "add_model",
            data : {}
        };

        // set the properties for the request
        if (this.drawing) reqObj.data.drawing = this.drawing;
        if (this.model) reqObj.data.model = this.model;


        return creo.ajax.request(reqObj)
            .then(function (respObj) {
                if (respObj.data) {
                    return Promise.resolve(respObj.data);
                } else {
                    return Promise.resolve(respObj);
                }
            })
            .catch(function (err) {
                console.log('Error : '+JSON.stringify(err));
                return Promise.reject(err);
            });

    };


    // Add a drawing sheet
    pub.DrawingObj.prototype.add_sheet = function () {

        console.log('got into : pub.DrawingObj.add_sheet');

        let reqObj = {
            command : "drawing",
            function : "add_sheet",
            data : {}
        };

        // set the properties for the request
        if (this.drawing) reqObj.data.drawing = this.drawing;
        if (this.position) reqObj.data.position = this.position;


        return creo.ajax.request(reqObj)
            .then(function (respObj) {
                if (respObj.data) {
                    return Promise.resolve(respObj.data);
                } else {
                    return Promise.resolve(respObj);
                }
            })
            .catch(function (err) {
                console.log('Error : '+JSON.stringify(err));
                return Promise.reject(err);
            });

    };


    // Create a new drawing from a template
    pub.DrawingObj.prototype.create = function () {

        console.log('got into : pub.DrawingObj.create');

        let reqObj = {
            command : "drawing",
            function : "create",
            data : {}
        };

        // set the properties for the request
        if (this.model) reqObj.data.model = this.model;
        if (this.drawing) reqObj.data.drawing = this.drawing;
        if (this.template) reqObj.data.template = this.template;
        if (this.scale) reqObj.data.scale = this.scale;
        if (this.display) reqObj.data.display = this.display;
        if (this.activate) reqObj.data.activate = this.activate;
        if (this.new_window) reqObj.data.new_window = this.new_window;


        return creo.ajax.request(reqObj)
            .then(function (respObj) {
                if (respObj.data) {
                    return Promise.resolve(respObj.data);
                } else {
                    return Promise.resolve(respObj);
                }
            })
            .catch(function (err) {
                console.log('Error : '+JSON.stringify(err));
                return Promise.reject(err);
            });

    };


    // Create general view on a drawing
    pub.DrawingObj.prototype.create_gen_view = function () {

        console.log('got into : pub.DrawingObj.create_gen_view');

        let reqObj = {
            command : "drawing",
            function : "create_gen_view",
            data : {}
        };

        // set the properties for the request
        if (this.drawing) reqObj.data.drawing = this.drawing;
        if (this.view) reqObj.data.view = this.view;
        if (this.sheet) reqObj.data.sheet = this.sheet;
        if (this.model) reqObj.data.model = this.model;
        if (this.model_view) reqObj.data.model_view = this.model_view;
        if (this.point) reqObj.data.point = this.point;
        if (this.scale) reqObj.data.scale = this.scale;
        if (this.display_data) reqObj.data.display_data = this.display_data;
        if (this.exploded) reqObj.data.exploded = this.exploded;


        return creo.ajax.request(reqObj)
            .then(function (respObj) {
                if (respObj.data) {
                    return Promise.resolve(respObj.data);
                } else {
                    return Promise.resolve(respObj);
                }
            })
            .catch(function (err) {
                console.log('Error : '+JSON.stringify(err));
                return Promise.reject(err);
            });

    };


    // Create projection view on a drawing
    pub.DrawingObj.prototype.create_proj_view = function () {

        console.log('got into : pub.DrawingObj.create_proj_view');

        let reqObj = {
            command : "drawing",
            function : "create_proj_view",
            data : {}
        };

        // set the properties for the request
        if (this.drawing) reqObj.data.drawing = this.drawing;
        if (this.view) reqObj.data.view = this.view;
        if (this.sheet) reqObj.data.sheet = this.sheet;
        if (this.parent_view) reqObj.data.parent_view = this.parent_view;
        if (this.point) reqObj.data.point = this.point;
        if (this.display_data) reqObj.data.display_data = this.display_data;
        if (this.exploded) reqObj.data.exploded = this.exploded;


        return creo.ajax.request(reqObj)
            .then(function (respObj) {
                if (respObj.data) {
                    return Promise.resolve(respObj.data);
                } else {
                    return Promise.resolve(respObj);
                }
            })
            .catch(function (err) {
                console.log('Error : '+JSON.stringify(err));
                return Promise.reject(err);
            });

    };


    // Add a symbol instance to a drawing
    pub.DrawingObj.prototype.create_symbol = function () {

        console.log('got into : pub.DrawingObj.create_symbol');

        let reqObj = {
            command : "drawing",
            function : "create_symbol",
            data : {}
        };

        // set the properties for the request
        if (this.drawing) reqObj.data.drawing = this.drawing;
        if (this.symbol_file) reqObj.data.symbol_file = this.symbol_file;
        if (this.point) reqObj.data.point = this.point;
        if (this.replace_values) reqObj.data.replace_values = this.replace_values;
        if (this.sheet) reqObj.data.sheet = this.sheet;


        return creo.ajax.request(reqObj)
            .then(function (respObj) {
                if (respObj.data) {
                    return Promise.resolve(respObj.data);
                } else {
                    return Promise.resolve(respObj);
                }
            })
            .catch(function (err) {
                console.log('Error : '+JSON.stringify(err));
                return Promise.reject(err);
            });

    };


    // Delete one or more models from a drawing
    pub.DrawingObj.prototype.delete_models = function () {

        console.log('got into : pub.DrawingObj.delete_models');

        let reqObj = {
            command : "drawing",
            function : "delete_models",
            data : {}
        };

        // set the properties for the request
        if (this.drawing) reqObj.data.drawing = this.drawing;
        if (this.model) reqObj.data.model = this.model;
        if (this.delete_views) reqObj.data.delete_views = this.delete_views;


        return creo.ajax.request(reqObj)
            .then(function (respObj) {
                if (respObj.data) {
                    return Promise.resolve(respObj.data);
                } else {
                    return Promise.resolve(respObj);
                }
            })
            .catch(function (err) {
                console.log('Error : '+JSON.stringify(err));
                return Promise.reject(err);
            });

    };


    // Delete a symbol definition and its instances from a drawing
    pub.DrawingObj.prototype.delete_symbol_def = function () {

        console.log('got into : pub.DrawingObj.delete_symbol_def');

        let reqObj = {
            command : "drawing",
            function : "delete_symbol_def",
            data : {}
        };

        // set the properties for the request
        if (this.drawing) reqObj.data.drawing = this.drawing;
        if (this.symbol_file) reqObj.data.symbol_file = this.symbol_file;


        return creo.ajax.request(reqObj)
            .then(function (respObj) {
                if (respObj.data) {
                    return Promise.resolve(respObj.data);
                } else {
                    return Promise.resolve(respObj);
                }
            })
            .catch(function (err) {
                console.log('Error : '+JSON.stringify(err));
                return Promise.reject(err);
            });

    };


    // Delete a specific symbol instance from a drawing
    pub.DrawingObj.prototype.delete_symbol_inst = function () {

        console.log('got into : pub.DrawingObj.delete_symbol_inst');

        let reqObj = {
            command : "drawing",
            function : "delete_symbol_inst",
            data : {}
        };

        // set the properties for the request
        if (this.drawing) reqObj.data.drawing = this.drawing;
        if (this.symbol_id) reqObj.data.symbol_id = this.symbol_id;


        return creo.ajax.request(reqObj)
            .then(function (respObj) {
                if (respObj.data) {
                    return Promise.resolve(respObj.data);
                } else {
                    return Promise.resolve(respObj);
                }
            })
            .catch(function (err) {
                console.log('Error : '+JSON.stringify(err));
                return Promise.reject(err);
            });

    };


    // Delete a drawing sheet
    pub.DrawingObj.prototype.delete_sheet = function () {

        console.log('got into : pub.DrawingObj.delete_sheet');

        let reqObj = {
            command : "drawing",
            function : "delete_sheet",
            data : {}
        };

        // set the properties for the request
        if (this.drawing) reqObj.data.drawing = this.drawing;
        if (this.sheet) reqObj.data.sheet = this.sheet;


        return creo.ajax.request(reqObj)
            .then(function (respObj) {
                if (respObj.data) {
                    return Promise.resolve(respObj.data);
                } else {
                    return Promise.resolve(respObj);
                }
            })
            .catch(function (err) {
                console.log('Error : '+JSON.stringify(err));
                return Promise.reject(err);
            });

    };


    // Delete a drawing view
    pub.DrawingObj.prototype.delete_view = function () {

        console.log('got into : pub.DrawingObj.delete_view');

        let reqObj = {
            command : "drawing",
            function : "delete_view",
            data : {}
        };

        // set the properties for the request
        if (this.drawing) reqObj.data.drawing = this.drawing;
        if (this.view) reqObj.data.view = this.view;
        if (this.sheet) reqObj.data.sheet = this.sheet;
        if (this.del_children) reqObj.data.del_children = this.del_children;


        return creo.ajax.request(reqObj)
            .then(function (respObj) {
                if (respObj.data) {
                    return Promise.resolve(respObj.data);
                } else {
                    return Promise.resolve(respObj);
                }
            })
            .catch(function (err) {
                console.log('Error : '+JSON.stringify(err));
                return Promise.reject(err);
            });

    };


    // Get the active model on a drawing
    pub.DrawingObj.prototype.get_cur_model = function () {

        console.log('got into : pub.DrawingObj.get_cur_model');

        let reqObj = {
            command : "drawing",
            function : "get_cur_model",
            data : {}
        };

        // set the properties for the request
        if (this.drawing) reqObj.data.drawing = this.drawing;


        return creo.ajax.request(reqObj)
            .then(function (respObj) {
                if (respObj.data) {
                    return Promise.resolve(respObj.data);
                } else {
                    return Promise.resolve(respObj);
                }
            })
            .catch(function (err) {
                console.log('Error : '+JSON.stringify(err));
                return Promise.reject(err);
            });

    };


    // Get the current drawing sheet
    pub.DrawingObj.prototype.get_cur_sheet = function () {

        console.log('got into : pub.DrawingObj.get_cur_sheet');

        let reqObj = {
            command : "drawing",
            function : "get_cur_sheet",
            data : {}
        };

        // set the properties for the request
        if (this.drawing) reqObj.data.drawing = this.drawing;


        return creo.ajax.request(reqObj)
            .then(function (respObj) {
                if (respObj.data) {
                    return Promise.resolve(respObj.data);
                } else {
                    return Promise.resolve(respObj);
                }
            })
            .catch(function (err) {
                console.log('Error : '+JSON.stringify(err));
                return Promise.reject(err);
            });

    };


    // Get the number of sheets on a drawing
    pub.DrawingObj.prototype.get_num_sheets = function () {

        console.log('got into : pub.DrawingObj.get_num_sheets');

        let reqObj = {
            command : "drawing",
            function : "get_num_sheets",
            data : {}
        };

        // set the properties for the request
        if (this.drawing) reqObj.data.drawing = this.drawing;


        return creo.ajax.request(reqObj)
            .then(function (respObj) {
                if (respObj.data) {
                    return Promise.resolve(respObj.data);
                } else {
                    return Promise.resolve(respObj);
                }
            })
            .catch(function (err) {
                console.log('Error : '+JSON.stringify(err));
                return Promise.reject(err);
            });

    };


    // Get the drawing format file of a drawing sheet
    pub.DrawingObj.prototype.get_sheet_format = function () {

        console.log('got into : pub.DrawingObj.get_sheet_format');

        let reqObj = {
            command : "drawing",
            function : "get_sheet_format",
            data : {}
        };

        // set the properties for the request
        if (this.drawing) reqObj.data.drawing = this.drawing;
        if (this.sheet) reqObj.data.sheet = this.sheet;


        return creo.ajax.request(reqObj)
            .then(function (respObj) {
                if (respObj.data) {
                    return Promise.resolve(respObj.data);
                } else {
                    return Promise.resolve(respObj);
                }
            })
            .catch(function (err) {
                console.log('Error : '+JSON.stringify(err));
                return Promise.reject(err);
            });

    };


    // Get the scale of a drawing sheet
    pub.DrawingObj.prototype.get_sheet_scale = function () {

        console.log('got into : pub.DrawingObj.get_sheet_scale');

        let reqObj = {
            command : "drawing",
            function : "get_sheet_scale",
            data : {}
        };

        // set the properties for the request
        if (this.drawing) reqObj.data.drawing = this.drawing;
        if (this.sheet) reqObj.data.sheet = this.sheet;
        if (this.model) reqObj.data.model = this.model;


        return creo.ajax.request(reqObj)
            .then(function (respObj) {
                if (respObj.data) {
                    return Promise.resolve(respObj.data);
                } else {
                    return Promise.resolve(respObj);
                }
            })
            .catch(function (err) {
                console.log('Error : '+JSON.stringify(err));
                return Promise.reject(err);
            });

    };


    // Get the size of a drawing sheet
    pub.DrawingObj.prototype.get_sheet_size = function () {

        console.log('got into : pub.DrawingObj.get_sheet_size');

        let reqObj = {
            command : "drawing",
            function : "get_sheet_size",
            data : {}
        };

        // set the properties for the request
        if (this.drawing) reqObj.data.drawing = this.drawing;
        if (this.sheet) reqObj.data.sheet = this.sheet;


        return creo.ajax.request(reqObj)
            .then(function (respObj) {
                if (respObj.data) {
                    return Promise.resolve(respObj.data);
                } else {
                    return Promise.resolve(respObj);
                }
            })
            .catch(function (err) {
                console.log('Error : '+JSON.stringify(err));
                return Promise.reject(err);
            });

    };


    // Get the location of a drawing view
    pub.DrawingObj.prototype.get_view_loc = function () {

        console.log('got into : pub.DrawingObj.get_view_loc');

        let reqObj = {
            command : "drawing",
            function : "get_view_loc",
            data : {}
        };

        // set the properties for the request
        if (this.drawing) reqObj.data.drawing = this.drawing;
        if (this.view) reqObj.data.view = this.view;


        return creo.ajax.request(reqObj)
            .then(function (respObj) {
                if (respObj.data) {
                    return Promise.resolve(respObj.data);
                } else {
                    return Promise.resolve(respObj);
                }
            })
            .catch(function (err) {
                console.log('Error : '+JSON.stringify(err));
                return Promise.reject(err);
            });

    };


    // Get the scale of a drawing view
    pub.DrawingObj.prototype.get_view_scale = function () {

        console.log('got into : pub.DrawingObj.get_view_scale');

        let reqObj = {
            command : "drawing",
            function : "get_view_scale",
            data : {}
        };

        // set the properties for the request
        if (this.drawing) reqObj.data.drawing = this.drawing;
        if (this.view) reqObj.data.view = this.view;


        return creo.ajax.request(reqObj)
            .then(function (respObj) {
                if (respObj.data) {
                    return Promise.resolve(respObj.data);
                } else {
                    return Promise.resolve(respObj);
                }
            })
            .catch(function (err) {
                console.log('Error : '+JSON.stringify(err));
                return Promise.reject(err);
            });

    };


    // Get the sheet number that contains a drawing view
    pub.DrawingObj.prototype.get_view_sheet = function () {

        console.log('got into : pub.DrawingObj.get_view_sheet');

        let reqObj = {
            command : "drawing",
            function : "get_view_sheet",
            data : {}
        };

        // set the properties for the request
        if (this.drawing) reqObj.data.drawing = this.drawing;
        if (this.view) reqObj.data.view = this.view;


        return creo.ajax.request(reqObj)
            .then(function (respObj) {
                if (respObj.data) {
                    return Promise.resolve(respObj.data);
                } else {
                    return Promise.resolve(respObj);
                }
            })
            .catch(function (err) {
                console.log('Error : '+JSON.stringify(err));
                return Promise.reject(err);
            });

    };


    // Check whether a symbol definition file is loaded into Creo
    pub.DrawingObj.prototype.is_symbol_def_loaded = function () {

        console.log('got into : pub.DrawingObj.is_symbol_def_loaded');

        let reqObj = {
            command : "drawing",
            function : "is_symbol_def_loaded",
            data : {}
        };

        // set the properties for the request
        if (this.drawing) reqObj.data.drawing = this.drawing;
        if (this.symbol_file) reqObj.data.symbol_file = this.symbol_file;


        return creo.ajax.request(reqObj)
            .then(function (respObj) {
                if (respObj.data) {
                    return Promise.resolve(respObj.data);
                } else {
                    return Promise.resolve(respObj);
                }
            })
            .catch(function (err) {
                console.log('Error : '+JSON.stringify(err));
                return Promise.reject(err);
            });

    };


    // List the models contained in a drawing
    pub.DrawingObj.prototype.list_models = function () {

        console.log('got into : pub.DrawingObj.list_models');

        let reqObj = {
            command : "drawing",
            function : "list_models",
            data : {}
        };

        // set the properties for the request
        if (this.drawing) reqObj.data.drawing = this.drawing;
        if (this.model) reqObj.data.model = this.model;


        return creo.ajax.request(reqObj)
            .then(function (respObj) {
                if (respObj.data) {
                    return Promise.resolve(respObj.data);
                } else {
                    return Promise.resolve(respObj);
                }
            })
            .catch(function (err) {
                console.log('Error : '+JSON.stringify(err));
                return Promise.reject(err);
            });

    };


    // List symbols contained on a drawing
    pub.DrawingObj.prototype.list_symbols = function () {

        console.log('got into : pub.DrawingObj.list_symbols');

        let reqObj = {
            command : "drawing",
            function : "list_symbols",
            data : {}
        };

        // set the properties for the request
        if (this.drawing) reqObj.data.drawing = this.drawing;
        if (this.symbol_file) reqObj.data.symbol_file = this.symbol_file;
        if (this.sheet) reqObj.data.sheet = this.sheet;


        return creo.ajax.request(reqObj)
            .then(function (respObj) {
                if (respObj.data) {
                    return Promise.resolve(respObj.data);
                } else {
                    return Promise.resolve(respObj);
                }
            })
            .catch(function (err) {
                console.log('Error : '+JSON.stringify(err));
                return Promise.reject(err);
            });

    };


    // List the views contained in a drawing, with more details
    pub.DrawingObj.prototype.list_view_details = function () {

        console.log('got into : pub.DrawingObj.list_view_details');

        let reqObj = {
            command : "drawing",
            function : "list_view_details",
            data : {}
        };

        // set the properties for the request
        if (this.drawing) reqObj.data.drawing = this.drawing;
        if (this.view) reqObj.data.view = this.view;


        return creo.ajax.request(reqObj)
            .then(function (respObj) {
                if (respObj.data) {
                    return Promise.resolve(respObj.data);
                } else {
                    return Promise.resolve(respObj);
                }
            })
            .catch(function (err) {
                console.log('Error : '+JSON.stringify(err));
                return Promise.reject(err);
            });

    };


    // List the views contained in a drawing
    pub.DrawingObj.prototype.list_views = function () {

        console.log('got into : pub.DrawingObj.list_views');

        let reqObj = {
            command : "drawing",
            function : "list_views",
            data : {}
        };

        // set the properties for the request
        if (this.drawing) reqObj.data.drawing = this.drawing;
        if (this.view) reqObj.data.view = this.view;


        return creo.ajax.request(reqObj)
            .then(function (respObj) {
                if (respObj.data) {
                    return Promise.resolve(respObj.data);
                } else {
                    return Promise.resolve(respObj);
                }
            })
            .catch(function (err) {
                console.log('Error : '+JSON.stringify(err));
                return Promise.reject(err);
            });

    };


    // Load a Creo symbol definition file into Creo from disk
    pub.DrawingObj.prototype.load_symbol_def = function () {

        console.log('got into : pub.DrawingObj.load_symbol_def');

        let reqObj = {
            command : "drawing",
            function : "load_symbol_def",
            data : {}
        };

        // set the properties for the request
        if (this.drawing) reqObj.data.drawing = this.drawing;
        if (this.symbol_dir) reqObj.data.symbol_dir = this.symbol_dir;
        if (this.symbol_file) reqObj.data.symbol_file = this.symbol_file;


        return creo.ajax.request(reqObj)
            .then(function (respObj) {
                if (respObj.data) {
                    return Promise.resolve(respObj.data);
                } else {
                    return Promise.resolve(respObj);
                }
            })
            .catch(function (err) {
                console.log('Error : '+JSON.stringify(err));
                return Promise.reject(err);
            });

    };


    // Regenerate a drawing
    pub.DrawingObj.prototype.regenerate = function () {

        console.log('got into : pub.DrawingObj.regenerate');

        let reqObj = {
            command : "drawing",
            function : "regenerate",
            data : {}
        };

        // set the properties for the request
        if (this.drawing) reqObj.data.drawing = this.drawing;


        return creo.ajax.request(reqObj)
            .then(function (respObj) {
                if (respObj.data) {
                    return Promise.resolve(respObj.data);
                } else {
                    return Promise.resolve(respObj);
                }
            })
            .catch(function (err) {
                console.log('Error : '+JSON.stringify(err));
                return Promise.reject(err);
            });

    };


    // Regenerate a sheet on a drawing
    pub.DrawingObj.prototype.regenerate_sheet = function () {

        console.log('got into : pub.DrawingObj.regenerate_sheet');

        let reqObj = {
            command : "drawing",
            function : "regenerate_sheet",
            data : {}
        };

        // set the properties for the request
        if (this.drawing) reqObj.data.drawing = this.drawing;
        if (this.sheet) reqObj.data.sheet = this.sheet;


        return creo.ajax.request(reqObj)
            .then(function (respObj) {
                if (respObj.data) {
                    return Promise.resolve(respObj.data);
                } else {
                    return Promise.resolve(respObj);
                }
            })
            .catch(function (err) {
                console.log('Error : '+JSON.stringify(err));
                return Promise.reject(err);
            });

    };


    // Rename a drawing view
    pub.DrawingObj.prototype.rename_view = function () {

        console.log('got into : pub.DrawingObj.rename_view');

        let reqObj = {
            command : "drawing",
            function : "rename_view",
            data : {}
        };

        // set the properties for the request
        if (this.drawing) reqObj.data.drawing = this.drawing;
        if (this.view) reqObj.data.view = this.view;
        if (this.new_view) reqObj.data.new_view = this.new_view;


        return creo.ajax.request(reqObj)
            .then(function (respObj) {
                if (respObj.data) {
                    return Promise.resolve(respObj.data);
                } else {
                    return Promise.resolve(respObj);
                }
            })
            .catch(function (err) {
                console.log('Error : '+JSON.stringify(err));
                return Promise.reject(err);
            });

    };


    // Set the scale of a drawing sheet
    pub.DrawingObj.prototype.scale_sheet = function () {

        console.log('got into : pub.DrawingObj.scale_sheet');

        let reqObj = {
            command : "drawing",
            function : "scale_sheet",
            data : {}
        };

        // set the properties for the request
        if (this.drawing) reqObj.data.drawing = this.drawing;
        if (this.sheet) reqObj.data.sheet = this.sheet;
        if (this.scale) reqObj.data.scale = this.scale;
        if (this.model) reqObj.data.model = this.model;


        return creo.ajax.request(reqObj)
            .then(function (respObj) {
                if (respObj.data) {
                    return Promise.resolve(respObj.data);
                } else {
                    return Promise.resolve(respObj);
                }
            })
            .catch(function (err) {
                console.log('Error : '+JSON.stringify(err));
                return Promise.reject(err);
            });

    };


    // Set the scale of one or more drawing views
    pub.DrawingObj.prototype.scale_view = function () {

        console.log('got into : pub.DrawingObj.scale_view');

        let reqObj = {
            command : "drawing",
            function : "scale_view",
            data : {}
        };

        // set the properties for the request
        if (this.drawing) reqObj.data.drawing = this.drawing;
        if (this.view) reqObj.data.view = this.view;
        if (this.scale) reqObj.data.scale = this.scale;


        return creo.ajax.request(reqObj)
            .then(function (respObj) {
                if (respObj.data) {
                    return Promise.resolve(respObj.data);
                } else {
                    return Promise.resolve(respObj);
                }
            })
            .catch(function (err) {
                console.log('Error : '+JSON.stringify(err));
                return Promise.reject(err);
            });

    };


    // Make a drawing sheet the current sheet
    pub.DrawingObj.prototype.select_sheet = function () {

        console.log('got into : pub.DrawingObj.select_sheet');

        let reqObj = {
            command : "drawing",
            function : "select_sheet",
            data : {}
        };

        // set the properties for the request
        if (this.drawing) reqObj.data.drawing = this.drawing;
        if (this.sheet) reqObj.data.sheet = this.sheet;


        return creo.ajax.request(reqObj)
            .then(function (respObj) {
                if (respObj.data) {
                    return Promise.resolve(respObj.data);
                } else {
                    return Promise.resolve(respObj);
                }
            })
            .catch(function (err) {
                console.log('Error : '+JSON.stringify(err));
                return Promise.reject(err);
            });

    };


    // Set the active model on a drawing
    pub.DrawingObj.prototype.set_cur_model = function () {

        console.log('got into : pub.DrawingObj.set_cur_model');

        let reqObj = {
            command : "drawing",
            function : "set_cur_model",
            data : {}
        };

        // set the properties for the request
        if (this.drawing) reqObj.data.drawing = this.drawing;
        if (this.model) reqObj.data.model = this.model;


        return creo.ajax.request(reqObj)
            .then(function (respObj) {
                if (respObj.data) {
                    return Promise.resolve(respObj.data);
                } else {
                    return Promise.resolve(respObj);
                }
            })
            .catch(function (err) {
                console.log('Error : '+JSON.stringify(err));
                return Promise.reject(err);
            });

    };


    // Set the drawing format file of a drawing sheet
    pub.DrawingObj.prototype.set_sheet_format = function () {

        console.log('got into : pub.DrawingObj.set_sheet_format');

        let reqObj = {
            command : "drawing",
            function : "set_sheet_format",
            data : {}
        };

        // set the properties for the request
        if (this.drawing) reqObj.data.drawing = this.drawing;
        if (this.sheet) reqObj.data.sheet = this.sheet;
        if (this.dirname) reqObj.data.dirname = this.dirname;
        if (this.file) reqObj.data.file = this.file;


        return creo.ajax.request(reqObj)
            .then(function (respObj) {
                if (respObj.data) {
                    return Promise.resolve(respObj.data);
                } else {
                    return Promise.resolve(respObj);
                }
            })
            .catch(function (err) {
                console.log('Error : '+JSON.stringify(err));
                return Promise.reject(err);
            });

    };


    // Set the location of a drawing view
    pub.DrawingObj.prototype.set_view_loc = function () {

        console.log('got into : pub.DrawingObj.set_view_loc');

        let reqObj = {
            command : "drawing",
            function : "set_view_loc",
            data : {}
        };

        // set the properties for the request
        if (this.drawing) reqObj.data.drawing = this.drawing;
        if (this.view) reqObj.data.view = this.view;
        if (this.point) reqObj.data.point = this.point;


        return creo.ajax.request(reqObj)
            .then(function (respObj) {
                if (respObj.data) {
                    return Promise.resolve(respObj.data);
                } else {
                    return Promise.resolve(respObj);
                }
            })
            .catch(function (err) {
                console.log('Error : '+JSON.stringify(err));
                return Promise.reject(err);
            });

    };


    // Get the 2D bounding box for a drawing view
    pub.DrawingObj.prototype.view_bound_box = function () {

        console.log('got into : pub.DrawingObj.view_bound_box');

        let reqObj = {
            command : "drawing",
            function : "view_bound_box",
            data : {}
        };

        // set the properties for the request
        if (this.drawing) reqObj.data.drawing = this.drawing;
        if (this.view) reqObj.data.view = this.view;


        return creo.ajax.request(reqObj)
            .then(function (respObj) {
                if (respObj.data) {
                    return Promise.resolve(respObj.data);
                } else {
                    return Promise.resolve(respObj);
                }
            })
            .catch(function (err) {
                console.log('Error : '+JSON.stringify(err));
                return Promise.reject(err);
            });

    };


    return pub;

}(creo || {}));
