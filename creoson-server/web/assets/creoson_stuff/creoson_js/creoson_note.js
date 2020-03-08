// Define the Module
var creo = creo || {};

creo = (function (pub) {

    pub.NoteObj = function(propsObj) {
        // BASE OBJECT
        this.encoded = undefined; // boolean - Value is Base64-encoded
        this.file = undefined; // string - Source model
        this.get_expanded = undefined; // boolean - Whether to return text with parameter values replaced
        this.location = undefined; // object:JLPoint - Coordinates for the note placement in Drawing Units
        this.name = undefined; // string - Note name to copy
        this.names = undefined; // array:string - List of note names
        this.select = undefined; // boolean - If true, the notes that are found will be selected in Creo
        this.to_file = undefined; // string - Destination model
        this.to_name = undefined; // string - Destination note
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


    // Copy note to another in the same model or another model
    pub.NoteObj.prototype.copy = function () {

        console.log('got into : pub.NoteObj.copy');

        let reqObj = {
            command : "note",
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


    // Delete a model or drawing note
    pub.NoteObj.prototype.delete = function () {

        console.log('got into : pub.NoteObj.delete');

        let reqObj = {
            command : "note",
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


    // Check whether note(s) exists on a model
    pub.NoteObj.prototype.exists = function () {

        console.log('got into : pub.NoteObj.exists');

        let reqObj = {
            command : "note",
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


    // Get the text of a model or drawing note
    pub.NoteObj.prototype.get = function () {

        console.log('got into : pub.NoteObj.get');

        let reqObj = {
            command : "note",
            function : "get",
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


    // Get a list of notes from one or more models
    pub.NoteObj.prototype.list = function () {

        console.log('got into : pub.NoteObj.list');

        let reqObj = {
            command : "note",
            function : "list",
            data : {}
        };

        // set the properties for the request
        if (this.file) reqObj.data.file = this.file;
        if (this.name) reqObj.data.name = this.name;
        if (this.names) reqObj.data.names = this.names;
        if (this.value) reqObj.data.value = this.value;
        if (this.get_expanded) reqObj.data.get_expanded = this.get_expanded;
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


    // Set the text of a model or drawing note
    pub.NoteObj.prototype.set = function () {

        console.log('got into : pub.NoteObj.set');

        let reqObj = {
            command : "note",
            function : "set",
            data : {}
        };

        // set the properties for the request
        if (this.file) reqObj.data.file = this.file;
        if (this.name) reqObj.data.name = this.name;
        if (this.value) reqObj.data.value = this.value;
        if (this.encoded) reqObj.data.encoded = this.encoded;
        if (this.location) reqObj.data.location = this.location;


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
