// Define the Module
var creo = creo || {};

creo = (function (pub) {

    pub.BomObj = function(propsObj) {
        // BASE OBJECT
        this.exclude_inactive = undefined; // boolean - Whether to exclude components which do not have an ACTIVE status
        this.file = undefined; // string - File name
        this.get_simpreps = undefined; // boolean - Whether to return the Simplified Rep data for each component
        this.get_transforms = undefined; // boolean - Whether to return the 3D transform matrix for each component
        this.paths = undefined; // boolean - Whether to return component paths for each component
        this.skeletons = undefined; // boolean - Whether to include skeleton components
        this.top_level = undefined; // boolean - Whether to return only the top-level components in the assembly
        
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


    // Get a hierarchy of components within an assembly
    pub.BomObj.prototype.get_paths = function () {

        console.log('got into : pub.BomObj.get_paths');

        let reqObj = {
            command : "bom",
            function : "get_paths",
            data : {}
        };

        // set the properties for the request
        if (this.file) reqObj.data.file = this.file;
        if (this.paths) reqObj.data.paths = this.paths;
        if (this.skeletons) reqObj.data.skeletons = this.skeletons;
        if (this.top_level) reqObj.data.top_level = this.top_level;
        if (this.get_transforms) reqObj.data.get_transforms = this.get_transforms;
        if (this.exclude_inactive) reqObj.data.exclude_inactive = this.exclude_inactive;
        if (this.get_simpreps) reqObj.data.get_simpreps = this.get_simpreps;


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
