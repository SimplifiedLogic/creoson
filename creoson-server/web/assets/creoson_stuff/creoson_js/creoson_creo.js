// Define the Module
var creo = creo || {};

creo = (function (pub) {

    pub.CreoObj = function(propsObj) {
        // BASE OBJECT
        this.blue = undefined; // integer - Blue value (0-255)
        this.color_type = undefined; // string - Color type
        this.dirname = undefined; // string - New directory name
        this.filename = undefined; // string - File name filter; only used if filenames is not given
        this.filenames = undefined; // array:string - List of file names
        this.green = undefined; // integer - Green value (0-255)
        this.ignore_errors = undefined; // boolean - Whether to ignore errors that might occur when setting the config option
        this.name = undefined; // string - Option name
        this.red = undefined; // integer - Red value (0-255)
        this.value = undefined; // string - New option value
        
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


    // Change Creo's working directory
    pub.CreoObj.prototype.cd = function () {

        console.log('got into : pub.CreoObj.cd');

        let reqObj = {
            command : "creo",
            function : "cd",
            data : {}
        };

        // set the properties for the request
        if (this.dirname) reqObj.data.dirname = this.dirname;


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


    // Delete files from a directory working directory
    pub.CreoObj.prototype.delete_files = function () {

        console.log('got into : pub.CreoObj.delete_files');

        let reqObj = {
            command : "creo",
            function : "delete_files",
            data : {}
        };

        // set the properties for the request
        if (this.dirname) reqObj.data.dirname = this.dirname;
        if (this.filename) reqObj.data.filename = this.filename;
        if (this.filenames) reqObj.data.filenames = this.filenames;


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


    // Get the value of a Creo config option
    pub.CreoObj.prototype.get_config = function () {

        console.log('got into : pub.CreoObj.get_config');

        let reqObj = {
            command : "creo",
            function : "get_config",
            data : {}
        };

        // set the properties for the request
        if (this.name) reqObj.data.name = this.name;


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


    // Get one of Creo's standard colors
    pub.CreoObj.prototype.get_std_color = function () {

        console.log('got into : pub.CreoObj.get_std_color');

        let reqObj = {
            command : "creo",
            function : "get_std_color",
            data : {}
        };

        // set the properties for the request
        if (this.color_type) reqObj.data.color_type = this.color_type;


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


    // List subdirectories of Creo's current working directory
    pub.CreoObj.prototype.list_dirs = function () {

        console.log('got into : pub.CreoObj.list_dirs');

        let reqObj = {
            command : "creo",
            function : "list_dirs",
            data : {}
        };

        // set the properties for the request
        if (this.dirname) reqObj.data.dirname = this.dirname;


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


    // List files in Creo's current working directory
    pub.CreoObj.prototype.list_files = function () {

        console.log('got into : pub.CreoObj.list_files');

        let reqObj = {
            command : "creo",
            function : "list_files",
            data : {}
        };

        // set the properties for the request
        if (this.filename) reqObj.data.filename = this.filename;


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


    // Create a new directory
    pub.CreoObj.prototype.mkdir = function () {

        console.log('got into : pub.CreoObj.mkdir');

        let reqObj = {
            command : "creo",
            function : "mkdir",
            data : {}
        };

        // set the properties for the request
        if (this.dirname) reqObj.data.dirname = this.dirname;


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


    // Return Creo's current working directory
    pub.CreoObj.prototype.pwd = function () {

        console.log('got into : pub.CreoObj.pwd');

        let reqObj = {
            command : "creo",
            function : "pwd",
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


    // Delete a new directory
    pub.CreoObj.prototype.rmdir = function () {

        console.log('got into : pub.CreoObj.rmdir');

        let reqObj = {
            command : "creo",
            function : "rmdir",
            data : {}
        };

        // set the properties for the request
        if (this.dirname) reqObj.data.dirname = this.dirname;


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


    // Set a Creo config option
    pub.CreoObj.prototype.set_config = function () {

        console.log('got into : pub.CreoObj.set_config');

        let reqObj = {
            command : "creo",
            function : "set_config",
            data : {}
        };

        // set the properties for the request
        if (this.name) reqObj.data.name = this.name;
        if (this.value) reqObj.data.value = this.value;
        if (this.ignore_errors) reqObj.data.ignore_errors = this.ignore_errors;


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


    // Set one of Creo's standard colors
    pub.CreoObj.prototype.set_std_color = function () {

        console.log('got into : pub.CreoObj.set_std_color');

        let reqObj = {
            command : "creo",
            function : "set_std_color",
            data : {}
        };

        // set the properties for the request
        if (this.color_type) reqObj.data.color_type = this.color_type;
        if (this.red) reqObj.data.red = this.red;
        if (this.green) reqObj.data.green = this.green;
        if (this.blue) reqObj.data.blue = this.blue;


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
