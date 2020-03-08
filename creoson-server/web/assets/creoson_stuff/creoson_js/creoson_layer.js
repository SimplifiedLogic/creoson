// Define the Module
var creo = creo || {};

creo = (function (pub) {

    pub.LayerObj = function(propsObj) {
        // BASE OBJECT
        this.file = undefined; // string - File name
        this.name = undefined; // string - Layer name
        this.show = undefined; // boolean - Whether to show or hide the layers
        
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


    // Delete one or more layers
    pub.LayerObj.prototype.delete = function () {

        console.log('got into : pub.LayerObj.delete');

        let reqObj = {
            command : "layer",
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


    // Check whether layer(s) exists on a model
    pub.LayerObj.prototype.exists = function () {

        console.log('got into : pub.LayerObj.exists');

        let reqObj = {
            command : "layer",
            function : "exists",
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


    // List layers that match criteria
    pub.LayerObj.prototype.list = function () {

        console.log('got into : pub.LayerObj.list');

        let reqObj = {
            command : "layer",
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


    // Show/Hide one or more layers
    pub.LayerObj.prototype.show = function () {

        console.log('got into : pub.LayerObj.show');

        let reqObj = {
            command : "layer",
            function : "show",
            data : {}
        };

        // set the properties for the request
        if (this.file) reqObj.data.file = this.file;
        if (this.name) reqObj.data.name = this.name;
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


    return pub;

}(creo || {}));
