// Define the Module
var creo = creo || {};

creo = (function (pub) {

    pub.FileObj = function(propsObj) {
        // BASE OBJECT
        this.activate = undefined; // boolean - Activate the model after displaying
        this.asm = undefined; // string - Assembly name
        this.assemble_to_root = undefined; // boolean - Whether to always assemble to the root assembly, or assemble to the subassembly containing the reference path/model
        this.constraints = undefined; // object_array:JLConstraint - Assembly constraints
        this.convert = undefined; // boolean - Whether to convert the model's length values to the new units (true) or leave them the same value (false)
        this.csys = undefined; // string - Coordinate system on the component to calculate the transform for
        this.dirname = undefined; // string - Directory name
        this.display = undefined; // boolean - Display the model after opening
        this.erase_children = undefined; // boolean - Erase children of the models too
        this.file = undefined; // string - File name of component
        this.files = undefined; // array:string - List of file names
        this.generic = undefined; // string - Generic model name (if file name represents an instance)
        this.include_non_matching_parts = undefined; // boolean - Whether to include parts that match the part name pattern but don't have a current material
        this.into_asm = undefined; // string - Target assembly
        this.material = undefined; // string - Material name
        this.new_name = undefined; // string - New file name
        this.new_window = undefined; // boolean - Open model in a new window
        this.onlysession = undefined; // boolean - Modify only in memory, not on disk
        this.package_assembly = undefined; // boolean - Whether to package the component to the assembly; only used if there are no constraints specified
        this.path = undefined; // array:integer - Path to a component that the new part will be constrained to
        this.ref_model = undefined; // string - Reference model that the new part will be constrained to; only used if path is not given.  If there are multiple of this model in the assembly, the component will be assembled multiple times, once to each occurrence
        this.regen_force = undefined; // boolean - Force regeneration after opening
        this.relations = undefined; // array:string - Relations text to import, one line per entry
        this.rep = undefined; // string - Simplified rep name pattern
        this.suppress = undefined; // boolean - Whether to suppress the components immediately after assembling them
        this.target_dir = undefined; // string - Target directory name
        this.transform = undefined; // object:JLTransform - Transform structure for the initial position and orientation of the new component; only used if there are no constraints, or for certain constraint types
        this.units = undefined; // string - New length units
        this.walk_children = undefined; // boolean - Whether to walk into subassemblies to find reference models to constrain to
        
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


    // Assemble a component into an assembly
    pub.FileObj.prototype.assemble = function () {

        console.log('got into : pub.FileObj.assemble');

        let reqObj = {
            command : "file",
            function : "assemble",
            data : {}
        };

        // set the properties for the request
        if (this.dirname) reqObj.data.dirname = this.dirname;
        if (this.file) reqObj.data.file = this.file;
        if (this.generic) reqObj.data.generic = this.generic;
        if (this.into_asm) reqObj.data.into_asm = this.into_asm;
        if (this.path) reqObj.data.path = this.path;
        if (this.ref_model) reqObj.data.ref_model = this.ref_model;
        if (this.transform) reqObj.data.transform = this.transform;
        if (this.constraints) reqObj.data.constraints = this.constraints;
        if (this.package_assembly) reqObj.data.package_assembly = this.package_assembly;
        if (this.walk_children) reqObj.data.walk_children = this.walk_children;
        if (this.assemble_to_root) reqObj.data.assemble_to_root = this.assemble_to_root;
        if (this.suppress) reqObj.data.suppress = this.suppress;


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


    // Back up a model
    pub.FileObj.prototype.backup = function () {

        console.log('got into : pub.FileObj.backup');

        let reqObj = {
            command : "file",
            function : "backup",
            data : {}
        };

        // set the properties for the request
        if (this.file) reqObj.data.file = this.file;
        if (this.target_dir) reqObj.data.target_dir = this.target_dir;


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


    // Close the window containing a model
    pub.FileObj.prototype.close_window = function () {

        console.log('got into : pub.FileObj.close_window');

        let reqObj = {
            command : "file",
            function : "close_window",
            data : {}
        };

        // set the properties for the request
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


    // Delete a material from a part or parts
    pub.FileObj.prototype.delete_material = function () {

        console.log('got into : pub.FileObj.delete_material');

        let reqObj = {
            command : "file",
            function : "delete_material",
            data : {}
        };

        // set the properties for the request
        if (this.file) reqObj.data.file = this.file;
        if (this.material) reqObj.data.material = this.material;


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


    // Display a model in a window
    pub.FileObj.prototype.display = function () {

        console.log('got into : pub.FileObj.display');

        let reqObj = {
            command : "file",
            function : "display",
            data : {}
        };

        // set the properties for the request
        if (this.file) reqObj.data.file = this.file;
        if (this.activate) reqObj.data.activate = this.activate;


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


    // Erase one or more models from memory
    pub.FileObj.prototype.erase = function () {

        console.log('got into : pub.FileObj.erase');

        let reqObj = {
            command : "file",
            function : "erase",
            data : {}
        };

        // set the properties for the request
        if (this.file) reqObj.data.file = this.file;
        if (this.files) reqObj.data.files = this.files;
        if (this.erase_children) reqObj.data.erase_children = this.erase_children;


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


    // Erase all non-displayed models from memory
    pub.FileObj.prototype.erase_not_displayed = function () {

        console.log('got into : pub.FileObj.erase_not_displayed');

        let reqObj = {
            command : "file",
            function : "erase_not_displayed",
            data : {}
        };

        // set the properties for the request


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


    // Check whether a model exists in memory
    pub.FileObj.prototype.exists = function () {

        console.log('got into : pub.FileObj.exists');

        let reqObj = {
            command : "file",
            function : "exists",
            data : {}
        };

        // set the properties for the request
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


    // Get the active model from Creo
    pub.FileObj.prototype.get_active = function () {

        console.log('got into : pub.FileObj.get_active');

        let reqObj = {
            command : "file",
            function : "get_active",
            data : {}
        };

        // set the properties for the request


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


    // Get the current material for a part
    pub.FileObj.prototype.get_cur_material = function () {

        console.log('got into : pub.FileObj.get_cur_material');

        let reqObj = {
            command : "file",
            function : "get_cur_material",
            data : {}
        };

        // set the properties for the request
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


    // Get the current material for a part or parts
    pub.FileObj.prototype.get_cur_material_wildcard = function () {

        console.log('got into : pub.FileObj.get_cur_material_wildcard');

        let reqObj = {
            command : "file",
            function : "get_cur_material_wildcard",
            data : {}
        };

        // set the properties for the request
        if (this.file) reqObj.data.file = this.file;
        if (this.include_non_matching_parts) reqObj.data.include_non_matching_parts = this.include_non_matching_parts;


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


    // Open one or more files in memory or from the drive
    pub.FileObj.prototype.get_fileinfo = function () {

        console.log('got into : pub.FileObj.get_fileinfo');

        let reqObj = {
            command : "file",
            function : "get_fileinfo",
            data : {}
        };

        // set the properties for the request
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


    // Get the current length units for a model
    pub.FileObj.prototype.get_length_units = function () {

        console.log('got into : pub.FileObj.get_length_units');

        let reqObj = {
            command : "file",
            function : "get_length_units",
            data : {}
        };

        // set the properties for the request
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


    // Get the current mass units for a model
    pub.FileObj.prototype.get_mass_units = function () {

        console.log('got into : pub.FileObj.get_mass_units');

        let reqObj = {
            command : "file",
            function : "get_mass_units",
            data : {}
        };

        // set the properties for the request
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


    // Get the 3D transform for a component in an assembly
    pub.FileObj.prototype.get_transform = function () {

        console.log('got into : pub.FileObj.get_transform');

        let reqObj = {
            command : "file",
            function : "get_transform",
            data : {}
        };

        // set the properties for the request
        if (this.asm) reqObj.data.asm = this.asm;
        if (this.path) reqObj.data.path = this.path;
        if (this.csys) reqObj.data.csys = this.csys;


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


    // Check whether a model has a family table
    pub.FileObj.prototype.has_instances = function () {

        console.log('got into : pub.FileObj.has_instances');

        let reqObj = {
            command : "file",
            function : "has_instances",
            data : {}
        };

        // set the properties for the request
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


    // Check whether a model is the active model
    pub.FileObj.prototype.is_active = function () {

        console.log('got into : pub.FileObj.is_active');

        let reqObj = {
            command : "file",
            function : "is_active",
            data : {}
        };

        // set the properties for the request
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


    // Get a list of files in the current Creo session that match patterns
    pub.FileObj.prototype.list = function () {

        console.log('got into : pub.FileObj.list');

        let reqObj = {
            command : "file",
            function : "list",
            data : {}
        };

        // set the properties for the request
        if (this.file) reqObj.data.file = this.file;
        if (this.files) reqObj.data.files = this.files;


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


    // List instances in a model's family table
    pub.FileObj.prototype.list_instances = function () {

        console.log('got into : pub.FileObj.list_instances');

        let reqObj = {
            command : "file",
            function : "list_instances",
            data : {}
        };

        // set the properties for the request
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


    // List materials on a part
    pub.FileObj.prototype.list_materials = function () {

        console.log('got into : pub.FileObj.list_materials');

        let reqObj = {
            command : "file",
            function : "list_materials",
            data : {}
        };

        // set the properties for the request
        if (this.file) reqObj.data.file = this.file;
        if (this.material) reqObj.data.material = this.material;


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


    // List materials on a part or parts
    pub.FileObj.prototype.list_materials_wildcard = function () {

        console.log('got into : pub.FileObj.list_materials_wildcard');

        let reqObj = {
            command : "file",
            function : "list_materials_wildcard",
            data : {}
        };

        // set the properties for the request
        if (this.file) reqObj.data.file = this.file;
        if (this.material) reqObj.data.material = this.material;
        if (this.include_non_matching_parts) reqObj.data.include_non_matching_parts = this.include_non_matching_parts;


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


    // List simplified reps in a model
    pub.FileObj.prototype.list_simp_reps = function () {

        console.log('got into : pub.FileObj.list_simp_reps');

        let reqObj = {
            command : "file",
            function : "list_simp_reps",
            data : {}
        };

        // set the properties for the request
        if (this.file) reqObj.data.file = this.file;
        if (this.rep) reqObj.data.rep = this.rep;


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


    // Load a new material file into a part or parts
    pub.FileObj.prototype.load_material_file = function () {

        console.log('got into : pub.FileObj.load_material_file');

        let reqObj = {
            command : "file",
            function : "load_material_file",
            data : {}
        };

        // set the properties for the request
        if (this.file) reqObj.data.file = this.file;
        if (this.dirname) reqObj.data.dirname = this.dirname;
        if (this.material) reqObj.data.material = this.material;


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


    // Get mass property information about a model
    pub.FileObj.prototype.massprops = function () {

        console.log('got into : pub.FileObj.massprops');

        let reqObj = {
            command : "file",
            function : "massprops",
            data : {}
        };

        // set the properties for the request
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


    // Open one or more files in memory or from the drive
    pub.FileObj.prototype.open = function () {

        console.log('got into : pub.FileObj.open');

        let reqObj = {
            command : "file",
            function : "open",
            data : {}
        };

        // set the properties for the request
        if (this.dirname) reqObj.data.dirname = this.dirname;
        if (this.file) reqObj.data.file = this.file;
        if (this.files) reqObj.data.files = this.files;
        if (this.generic) reqObj.data.generic = this.generic;
        if (this.display) reqObj.data.display = this.display;
        if (this.activate) reqObj.data.activate = this.activate;
        if (this.new_window) reqObj.data.new_window = this.new_window;
        if (this.regen_force) reqObj.data.regen_force = this.regen_force;


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


    // Check whether Creo errors have occurred opening a model
    pub.FileObj.prototype.open_errors = function () {

        console.log('got into : pub.FileObj.open_errors');

        let reqObj = {
            command : "file",
            function : "open_errors",
            data : {}
        };

        // set the properties for the request
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


    // Get post-regeneration relations for a model
    pub.FileObj.prototype.postregen_relations_get = function () {

        console.log('got into : pub.FileObj.postregen_relations_get');

        let reqObj = {
            command : "file",
            function : "postregen_relations_get",
            data : {}
        };

        // set the properties for the request
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


    // Set post-regeneration relations for a model
    pub.FileObj.prototype.postregen_relations_set = function () {

        console.log('got into : pub.FileObj.postregen_relations_set');

        let reqObj = {
            command : "file",
            function : "postregen_relations_set",
            data : {}
        };

        // set the properties for the request
        if (this.file) reqObj.data.file = this.file;
        if (this.relations) reqObj.data.relations = this.relations;


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


    // Refresh the window containing a model
    pub.FileObj.prototype.refresh = function () {

        console.log('got into : pub.FileObj.refresh');

        let reqObj = {
            command : "file",
            function : "refresh",
            data : {}
        };

        // set the properties for the request
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


    // Regenerate one or more models
    pub.FileObj.prototype.regenerate = function () {

        console.log('got into : pub.FileObj.regenerate');

        let reqObj = {
            command : "file",
            function : "regenerate",
            data : {}
        };

        // set the properties for the request
        if (this.file) reqObj.data.file = this.file;
        if (this.files) reqObj.data.files = this.files;
        if (this.display) reqObj.data.display = this.display;


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


    // Get relations for a model
    pub.FileObj.prototype.relations_get = function () {

        console.log('got into : pub.FileObj.relations_get');

        let reqObj = {
            command : "file",
            function : "relations_get",
            data : {}
        };

        // set the properties for the request
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


    // Set relations for a model
    pub.FileObj.prototype.relations_set = function () {

        console.log('got into : pub.FileObj.relations_set');

        let reqObj = {
            command : "file",
            function : "relations_set",
            data : {}
        };

        // set the properties for the request
        if (this.file) reqObj.data.file = this.file;
        if (this.relations) reqObj.data.relations = this.relations;


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


    // Rename a model
    pub.FileObj.prototype.rename = function () {

        console.log('got into : pub.FileObj.rename');

        let reqObj = {
            command : "file",
            function : "rename",
            data : {}
        };

        // set the properties for the request
        if (this.file) reqObj.data.file = this.file;
        if (this.new_name) reqObj.data.new_name = this.new_name;
        if (this.onlysession) reqObj.data.onlysession = this.onlysession;


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


    // Repaint the window containing a model
    pub.FileObj.prototype.repaint = function () {

        console.log('got into : pub.FileObj.repaint');

        let reqObj = {
            command : "file",
            function : "repaint",
            data : {}
        };

        // set the properties for the request
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


    // Save one or more models
    pub.FileObj.prototype.save = function () {

        console.log('got into : pub.FileObj.save');

        let reqObj = {
            command : "file",
            function : "save",
            data : {}
        };

        // set the properties for the request
        if (this.file) reqObj.data.file = this.file;
        if (this.files) reqObj.data.files = this.files;


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


    // Set the current material for a part or parts
    pub.FileObj.prototype.set_cur_material = function () {

        console.log('got into : pub.FileObj.set_cur_material');

        let reqObj = {
            command : "file",
            function : "set_cur_material",
            data : {}
        };

        // set the properties for the request
        if (this.file) reqObj.data.file = this.file;
        if (this.material) reqObj.data.material = this.material;


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


    // Set the current length units for a model
    pub.FileObj.prototype.set_length_units = function () {

        console.log('got into : pub.FileObj.set_length_units');

        let reqObj = {
            command : "file",
            function : "set_length_units",
            data : {}
        };

        // set the properties for the request
        if (this.file) reqObj.data.file = this.file;
        if (this.units) reqObj.data.units = this.units;
        if (this.convert) reqObj.data.convert = this.convert;


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


    // Set the current mass units for a model
    pub.FileObj.prototype.set_mass_units = function () {

        console.log('got into : pub.FileObj.set_mass_units');

        let reqObj = {
            command : "file",
            function : "set_mass_units",
            data : {}
        };

        // set the properties for the request
        if (this.file) reqObj.data.file = this.file;
        if (this.units) reqObj.data.units = this.units;
        if (this.convert) reqObj.data.convert = this.convert;


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
