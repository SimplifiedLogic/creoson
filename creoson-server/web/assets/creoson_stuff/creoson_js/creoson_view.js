// Define the Module
var creo = creo || {};

creo = (function (pub) {

    pub.ViewObj = function(propsObj) {
        // BASE OBJECT
        this.file = undefined; // string - File name
        this.name = undefined; // string - View name
        
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


    // Activate a model view
    pub.ViewObj.prototype.activate = function () {

        console.log('got into : pub.ViewObj.activate');

        let reqObj = {
            command : "view",
            function : "activate",
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


    // List views that match criteria
    pub.ViewObj.prototype.list = function () {

        console.log('got into : pub.ViewObj.list');

        let reqObj = {
            command : "view",
            function : "list",
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


    // List views that match criteria and are exploded
    pub.ViewObj.prototype.list_exploded = function () {

        console.log('got into : pub.ViewObj.list_exploded');

        let reqObj = {
            command : "view",
            function : "list_exploded",
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


    // Save a model's current orientation as a new view
    pub.ViewObj.prototype.save = function () {

        console.log('got into : pub.ViewObj.save');

        let reqObj = {
            command : "view",
            function : "save",
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


    return pub;

}(creo || {}));
