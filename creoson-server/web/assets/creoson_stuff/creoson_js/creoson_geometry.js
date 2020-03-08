// Define the Module
var creo = creo || {};

creo = (function (pub) {

    pub.GeometryObj = function(propsObj) {
        // BASE OBJECT
        this.file = undefined; // string - File name
        this.surface_ids = undefined; // array:integer - List of surface IDs
        
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


    // Get the bounding box for a model
    pub.GeometryObj.prototype.bound_box = function () {

        console.log('got into : pub.GeometryObj.bound_box');

        let reqObj = {
            command : "geometry",
            function : "bound_box",
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


    // Get the list of edges for a model for given surfaces
    pub.GeometryObj.prototype.get_edges = function () {

        console.log('got into : pub.GeometryObj.get_edges');

        let reqObj = {
            command : "geometry",
            function : "get_edges",
            data : {}
        };

        // set the properties for the request
        if (this.file) reqObj.data.file = this.file;
        if (this.surface_ids) reqObj.data.surface_ids = this.surface_ids;


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


    // Get the list of surfaces for a model
    pub.GeometryObj.prototype.get_surfaces = function () {

        console.log('got into : pub.GeometryObj.get_surfaces');

        let reqObj = {
            command : "geometry",
            function : "get_surfaces",
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


    return pub;

}(creo || {}));
