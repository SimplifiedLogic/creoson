// Define the Module
var creo = creo || {};

creo = (function (pub) {

    pub.DimensionObj = function(propsObj) {
        // BASE OBJECT
        this.assembly = undefined; // string - Assembly name; only used if path is given
        this.dim_type = undefined; // string - Dimension type filter
        this.encoded = undefined; // boolean - Whether to return the values Base64-encoded
        this.file = undefined; // string - Source model
        this.max = undefined; // integer - The maximum number of dimensions that the user can select
        this.name = undefined; // string - Dimension name to copy
        this.names = undefined; // array:string - List of dimension names
        this.path = undefined; // array:integer - Path to occurrence of the model within the assembly; the dimension will only be shown for that occurrence
        this.select = undefined; // boolean - If true, the dimensions that are found will be selected in Creo
        this.show = undefined; // boolean - Whether to show (or hide) the dimension
        this.text = undefined; // depends on data type - Dimension text
        this.to_file = undefined; // string - Destination model
        this.to_name = undefined; // string - Destination dimension; the dimension must already exist
        this.value = undefined; // depends on data type - Dimension value
        
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


    // Copy dimension to another in the same model or another model
    pub.DimensionObj.prototype.copy = function () {

        console.log('got into : pub.DimensionObj.copy');

        let reqObj = {
            command : "dimension",
            function : "copy",
            data : {}
        };

        // set the properties for the request
        if (this.file) reqObj.data.file = this.file;
        if (this.name) reqObj.data.name = this.name;
        if (this.to_name) reqObj.data.to_name = this.to_name;
        if (this.to_file) reqObj.data.to_file = this.to_file;


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


    // Get a list of dimensions from a model
    pub.DimensionObj.prototype.list = function () {

        console.log('got into : pub.DimensionObj.list');

        let reqObj = {
            command : "dimension",
            function : "list",
            data : {}
        };

        // set the properties for the request
        if (this.file) reqObj.data.file = this.file;
        if (this.name) reqObj.data.name = this.name;
        if (this.names) reqObj.data.names = this.names;
        if (this.dim_type) reqObj.data.dim_type = this.dim_type;
        if (this.encoded) reqObj.data.encoded = this.encoded;
        if (this.select) reqObj.data.select = this.select;


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


    // Get a list of dimension details from a model
    pub.DimensionObj.prototype.list_detail = function () {

        console.log('got into : pub.DimensionObj.list_detail');

        let reqObj = {
            command : "dimension",
            function : "list_detail",
            data : {}
        };

        // set the properties for the request
        if (this.file) reqObj.data.file = this.file;
        if (this.name) reqObj.data.name = this.name;
        if (this.names) reqObj.data.names = this.names;
        if (this.dim_type) reqObj.data.dim_type = this.dim_type;
        if (this.encoded) reqObj.data.encoded = this.encoded;
        if (this.select) reqObj.data.select = this.select;


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


    // Set a dimension value
    pub.DimensionObj.prototype.set = function () {

        console.log('got into : pub.DimensionObj.set');

        let reqObj = {
            command : "dimension",
            function : "set",
            data : {}
        };

        // set the properties for the request
        if (this.file) reqObj.data.file = this.file;
        if (this.name) reqObj.data.name = this.name;
        if (this.value) reqObj.data.value = this.value;
        if (this.encoded) reqObj.data.encoded = this.encoded;


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


    // Set dimension text
    pub.DimensionObj.prototype.set_text = function () {

        console.log('got into : pub.DimensionObj.set_text');

        let reqObj = {
            command : "dimension",
            function : "set_text",
            data : {}
        };

        // set the properties for the request
        if (this.file) reqObj.data.file = this.file;
        if (this.name) reqObj.data.name = this.name;
        if (this.text) reqObj.data.text = this.text;
        if (this.encoded) reqObj.data.encoded = this.encoded;


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


    // Display or hide a dimension in Creo
    pub.DimensionObj.prototype.show = function () {

        console.log('got into : pub.DimensionObj.show');

        let reqObj = {
            command : "dimension",
            function : "show",
            data : {}
        };

        // set the properties for the request
        if (this.file) reqObj.data.file = this.file;
        if (this.assembly) reqObj.data.assembly = this.assembly;
        if (this.name) reqObj.data.name = this.name;
        if (this.path) reqObj.data.path = this.path;
        if (this.show) reqObj.data.show = this.show;


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


    // Prompt the user to select one or more dimensions, and return their selections
    pub.DimensionObj.prototype.user_select = function () {

        console.log('got into : pub.DimensionObj.user_select');

        let reqObj = {
            command : "dimension",
            function : "user_select",
            data : {}
        };

        // set the properties for the request
        if (this.file) reqObj.data.file = this.file;
        if (this.max) reqObj.data.max = this.max;


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
