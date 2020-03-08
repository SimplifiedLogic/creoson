// Define the Module
var creo = creo || {};

creo = (function (pub) {

    pub.ParameterObj = function(propsObj) {
        // BASE OBJECT
        this.designate = undefined; // boolean - Set copied parameter to be designated/not designated, blank=do not set
        this.encoded = undefined; // boolean - Whether to return the values Base64-encoded
        this.file = undefined; // string - Source model
        this.name = undefined; // string - Parameter name to copy
        this.names = undefined; // array:string - List of parameter names
        this.no_create = undefined; // boolean - If parameter does not already exist, do not create it
        this.to_file = undefined; // string - Destination model
        this.to_name = undefined; // string - Destination parameter
        this.type = undefined; // string - Data type
        this.value = undefined; // string - Parameter value filter
        
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


    // Copy parameter to another in the same model or another model
    pub.ParameterObj.prototype.copy = function () {

        console.log('got into : pub.ParameterObj.copy');

        let reqObj = {
            command : "parameter",
            function : "copy",
            data : {}
        };

        // set the properties for the request
        if (this.file) reqObj.data.file = this.file;
        if (this.name) reqObj.data.name = this.name;
        if (this.to_name) reqObj.data.to_name = this.to_name;
        if (this.to_file) reqObj.data.to_file = this.to_file;
        if (this.designate) reqObj.data.designate = this.designate;


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


    // Delete a parameter
    pub.ParameterObj.prototype.delete = function () {

        console.log('got into : pub.ParameterObj.delete');

        let reqObj = {
            command : "parameter",
            function : "delete",
            data : {}
        };

        // set the properties for the request
        if (this.file) reqObj.data.file = this.file;
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


    // Check whether parameter(s) exists on a model
    pub.ParameterObj.prototype.exists = function () {

        console.log('got into : pub.ParameterObj.exists');

        let reqObj = {
            command : "parameter",
            function : "exists",
            data : {}
        };

        // set the properties for the request
        if (this.file) reqObj.data.file = this.file;
        if (this.name) reqObj.data.name = this.name;
        if (this.names) reqObj.data.names = this.names;


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


    // Get a list of parameters from one or more models
    pub.ParameterObj.prototype.list = function () {

        console.log('got into : pub.ParameterObj.list');

        let reqObj = {
            command : "parameter",
            function : "list",
            data : {}
        };

        // set the properties for the request
        if (this.file) reqObj.data.file = this.file;
        if (this.name) reqObj.data.name = this.name;
        if (this.names) reqObj.data.names = this.names;
        if (this.encoded) reqObj.data.encoded = this.encoded;
        if (this.value) reqObj.data.value = this.value;


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


    // Set the value of a parameter
    pub.ParameterObj.prototype.set = function () {

        console.log('got into : pub.ParameterObj.set');

        let reqObj = {
            command : "parameter",
            function : "set",
            data : {}
        };

        // set the properties for the request
        if (this.file) reqObj.data.file = this.file;
        if (this.name) reqObj.data.name = this.name;
        if (this.type) reqObj.data.type = this.type;
        if (this.value) reqObj.data.value = this.value;
        if (this.encoded) reqObj.data.encoded = this.encoded;
        if (this.designate) reqObj.data.designate = this.designate;
        if (this.no_create) reqObj.data.no_create = this.no_create;


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


    // Set the designated state of a parameter
    pub.ParameterObj.prototype.set_designated = function () {

        console.log('got into : pub.ParameterObj.set_designated');

        let reqObj = {
            command : "parameter",
            function : "set_designated",
            data : {}
        };

        // set the properties for the request
        if (this.file) reqObj.data.file = this.file;
        if (this.name) reqObj.data.name = this.name;
        if (this.designate) reqObj.data.designate = this.designate;


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
