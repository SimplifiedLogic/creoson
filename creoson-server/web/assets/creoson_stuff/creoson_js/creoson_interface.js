// Define the Module
var creo = creo || {};

creo = (function (pub) {

    pub.InterfaceObj = function(propsObj) {
        // BASE OBJECT
        this.advanced = undefined; // boolean - Whether to use the newer Creo 4 file export function.
        this.delay = undefined; // integer - Amount of time to wait after starting the mapkey, in milliseconds.
        this.depth = undefined; // integer - Image depth
        this.dirname = undefined; // string - Destination directory
        this.dpi = undefined; // integer - PDF Image DPI
        this.driver = undefined; // string - 
        this.file = undefined; // string - Model name
        this.filename = undefined; // string - Destination file name.  May also contain a path to the file.
        this.geom_flags = undefined; // string - Geometry type for the export.
        this.height = undefined; // double - PDF Image height
        this.new_model_type = undefined; // string - New model type
        this.new_name = undefined; // string - New model name.  Any extension will be stripped off and replaced with one based on new_model_type.
        this.script = undefined; // string - The mapkey script to run
        this.sheet_range = undefined; // string - Range of drawing sheets to export
        this.type = undefined; // string - File type
        this.use_drawing_settings = undefined; // boolean - Whether to use special settings for exporting drawings
        this.width = undefined; // double - PDF Image width
        
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


    // Export a model to a 3D PDF file
    pub.InterfaceObj.prototype.export_3dpdf = function () {

        console.log('got into : pub.InterfaceObj.export_3dpdf');

        let reqObj = {
            command : "interface",
            function : "export_3dpdf",
            data : {}
        };

        // set the properties for the request
        if (this.file) reqObj.data.file = this.file;
        if (this.filename) reqObj.data.filename = this.filename;
        if (this.height) reqObj.data.height = this.height;
        if (this.width) reqObj.data.width = this.width;
        if (this.dpi) reqObj.data.dpi = this.dpi;
        if (this.dirname) reqObj.data.dirname = this.dirname;
        if (this.use_drawing_settings) reqObj.data.use_drawing_settings = this.use_drawing_settings;
        if (this.sheet_range) reqObj.data.sheet_range = this.sheet_range;


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


    // Export a model to a file
    pub.InterfaceObj.prototype.export_file = function () {

        console.log('got into : pub.InterfaceObj.export_file');

        let reqObj = {
            command : "interface",
            function : "export_file",
            data : {}
        };

        // set the properties for the request
        if (this.type) reqObj.data.type = this.type;
        if (this.file) reqObj.data.file = this.file;
        if (this.filename) reqObj.data.filename = this.filename;
        if (this.dirname) reqObj.data.dirname = this.dirname;
        if (this.geom_flags) reqObj.data.geom_flags = this.geom_flags;
        if (this.advanced) reqObj.data.advanced = this.advanced;


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


    // Export a model to an image file
    pub.InterfaceObj.prototype.export_image = function () {

        console.log('got into : pub.InterfaceObj.export_image');

        let reqObj = {
            command : "interface",
            function : "export_image",
            data : {}
        };

        // set the properties for the request
        if (this.type) reqObj.data.type = this.type;
        if (this.file) reqObj.data.file = this.file;
        if (this.filename) reqObj.data.filename = this.filename;
        if (this.height) reqObj.data.height = this.height;
        if (this.width) reqObj.data.width = this.width;
        if (this.dpi) reqObj.data.dpi = this.dpi;
        if (this.depth) reqObj.data.depth = this.depth;


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


    // Export a model to a PDF file
    pub.InterfaceObj.prototype.export_pdf = function () {

        console.log('got into : pub.InterfaceObj.export_pdf');

        let reqObj = {
            command : "interface",
            function : "export_pdf",
            data : {}
        };

        // set the properties for the request
        if (this.file) reqObj.data.file = this.file;
        if (this.filename) reqObj.data.filename = this.filename;
        if (this.height) reqObj.data.height = this.height;
        if (this.width) reqObj.data.width = this.width;
        if (this.dpi) reqObj.data.dpi = this.dpi;
        if (this.dirname) reqObj.data.dirname = this.dirname;
        if (this.use_drawing_settings) reqObj.data.use_drawing_settings = this.use_drawing_settings;
        if (this.sheet_range) reqObj.data.sheet_range = this.sheet_range;


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


    // Export a model's program to a file
    pub.InterfaceObj.prototype.export_program = function () {

        console.log('got into : pub.InterfaceObj.export_program');

        let reqObj = {
            command : "interface",
            function : "export_program",
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


    // Import a program file for a model
    pub.InterfaceObj.prototype.import_program = function () {

        console.log('got into : pub.InterfaceObj.import_program');

        let reqObj = {
            command : "interface",
            function : "import_program",
            data : {}
        };

        // set the properties for the request
        if (this.dirname) reqObj.data.dirname = this.dirname;
        if (this.filename) reqObj.data.filename = this.filename;
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


    // Import a file as a model
    pub.InterfaceObj.prototype.import_file = function () {

        console.log('got into : pub.InterfaceObj.import_file');

        let reqObj = {
            command : "interface",
            function : "import_file",
            data : {}
        };

        // set the properties for the request
        if (this.type) reqObj.data.type = this.type;
        if (this.dirname) reqObj.data.dirname = this.dirname;
        if (this.filename) reqObj.data.filename = this.filename;
        if (this.new_name) reqObj.data.new_name = this.new_name;
        if (this.new_model_type) reqObj.data.new_model_type = this.new_model_type;


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


    // Run a Mapkey script in Creo
    pub.InterfaceObj.prototype.mapkey = function () {

        console.log('got into : pub.InterfaceObj.mapkey');

        let reqObj = {
            command : "interface",
            function : "mapkey",
            data : {}
        };

        // set the properties for the request
        if (this.script) reqObj.data.script = this.script;
        if (this.delay) reqObj.data.delay = this.delay;


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


    // Export a model plot
    pub.InterfaceObj.prototype.plot = function () {

        console.log('got into : pub.InterfaceObj.plot');

        let reqObj = {
            command : "interface",
            function : "plot",
            data : {}
        };

        // set the properties for the request
        if (this.file) reqObj.data.file = this.file;
        if (this.dirname) reqObj.data.dirname = this.dirname;
        if (this.driver) reqObj.data.driver = this.driver;


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
