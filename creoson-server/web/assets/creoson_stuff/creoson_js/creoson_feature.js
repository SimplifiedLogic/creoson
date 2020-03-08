// Define the Module
var creo = creo || {};

creo = (function (pub) {

    pub.FeatureObj = function(propsObj) {
        // BASE OBJECT
        this.clip = undefined; // boolean - Whether to clip-delete ANY features from this feature through the end of the structure
        this.designate = undefined; // boolean - Set parameter to be designated/not designated, blank=do not set
        this.encoded = undefined; // boolean - Whether to return the values Base64-encoded
        this.feat_id = undefined; // integer - Feature ID
        this.file = undefined; // string - File name
        this.group_name = undefined; // string - Group name
        this.inc_unnamed = undefined; // boolean - Whether to include unnamed features in the list
        this.max = undefined; // integer - The maximum number of dimensions that the user can select
        this.name = undefined; // string - Feature name; only used if names is not given
        this.names = undefined; // array:string - List of feature names
        this.new_name = undefined; // string - New name for the feature
        this.no_comp = undefined; // boolean - Whether to include component-type features in the list
        this.no_create = undefined; // boolean - If parameter does not already exist, do not create it
        this.no_datum = undefined; // boolean - Whether to exclude datum-type features from the list; these are COORD_SYS, CURVE, DATUM_AXIS, DATUM_PLANE, DATUM_POINT, DATUM_QUILT, and DATUM_SURFACE features.
        this.param = undefined; // string - Parameter name
        this.params = undefined; // array:string - List of parameter names
        this.paths = undefined; // boolean - Whether feature ID and feature number are returned with the data
        this.pattern_name = undefined; // string - Pattern name
        this.status = undefined; // string - Feature status pattern
        this.type = undefined; // string - Feature type pattern
        this.value = undefined; // string - Parameter value filter
        this.with_children = undefined; // boolean - Whether to resume any child features of the resumed feature
        
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


    // Delete one or more features that match criteria
    pub.FeatureObj.prototype.delete = function () {

        console.log('got into : pub.FeatureObj.delete');

        let reqObj = {
            command : "feature",
            function : "delete",
            data : {}
        };

        // set the properties for the request
        if (this.file) reqObj.data.file = this.file;
        if (this.name) reqObj.data.name = this.name;
        if (this.names) reqObj.data.names = this.names;
        if (this.status) reqObj.data.status = this.status;
        if (this.type) reqObj.data.type = this.type;
        if (this.clip) reqObj.data.clip = this.clip;


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


    // Delete a feature parameter
    pub.FeatureObj.prototype.delete_param = function () {

        console.log('got into : pub.FeatureObj.delete_param');

        let reqObj = {
            command : "feature",
            function : "delete_param",
            data : {}
        };

        // set the properties for the request
        if (this.file) reqObj.data.file = this.file;
        if (this.name) reqObj.data.name = this.name;
        if (this.param) reqObj.data.param = this.param;


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


    // List features that match criteria
    pub.FeatureObj.prototype.list = function () {

        console.log('got into : pub.FeatureObj.list');

        let reqObj = {
            command : "feature",
            function : "list",
            data : {}
        };

        // set the properties for the request
        if (this.file) reqObj.data.file = this.file;
        if (this.name) reqObj.data.name = this.name;
        if (this.status) reqObj.data.status = this.status;
        if (this.type) reqObj.data.type = this.type;
        if (this.paths) reqObj.data.paths = this.paths;
        if (this.no_datum) reqObj.data.no_datum = this.no_datum;
        if (this.inc_unnamed) reqObj.data.inc_unnamed = this.inc_unnamed;
        if (this.no_comp) reqObj.data.no_comp = this.no_comp;


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


    // List feature parameters that match criteria
    pub.FeatureObj.prototype.list_params = function () {

        console.log('got into : pub.FeatureObj.list_params');

        let reqObj = {
            command : "feature",
            function : "list_params",
            data : {}
        };

        // set the properties for the request
        if (this.file) reqObj.data.file = this.file;
        if (this.feat_id) reqObj.data.feat_id = this.feat_id;
        if (this.name) reqObj.data.name = this.name;
        if (this.type) reqObj.data.type = this.type;
        if (this.no_datum) reqObj.data.no_datum = this.no_datum;
        if (this.inc_unnamed) reqObj.data.inc_unnamed = this.inc_unnamed;
        if (this.no_comp) reqObj.data.no_comp = this.no_comp;
        if (this.param) reqObj.data.param = this.param;
        if (this.params) reqObj.data.params = this.params;
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


    // List features in a Creo Group
    pub.FeatureObj.prototype.list_group_features = function () {

        console.log('got into : pub.FeatureObj.list_group_features');

        let reqObj = {
            command : "feature",
            function : "list_group_features",
            data : {}
        };

        // set the properties for the request
        if (this.file) reqObj.data.file = this.file;
        if (this.group_name) reqObj.data.group_name = this.group_name;
        if (this.type) reqObj.data.type = this.type;


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


    // List features in a Creo Pattern
    pub.FeatureObj.prototype.list_pattern_features = function () {

        console.log('got into : pub.FeatureObj.list_pattern_features');

        let reqObj = {
            command : "feature",
            function : "list_pattern_features",
            data : {}
        };

        // set the properties for the request
        if (this.file) reqObj.data.file = this.file;
        if (this.pattern_name) reqObj.data.pattern_name = this.pattern_name;
        if (this.type) reqObj.data.type = this.type;


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


    // Check whether parameter(s) exists on a feature
    pub.FeatureObj.prototype.param_exists = function () {

        console.log('got into : pub.FeatureObj.param_exists');

        let reqObj = {
            command : "feature",
            function : "param_exists",
            data : {}
        };

        // set the properties for the request
        if (this.file) reqObj.data.file = this.file;
        if (this.name) reqObj.data.name = this.name;
        if (this.param) reqObj.data.param = this.param;
        if (this.params) reqObj.data.params = this.params;


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


    // Rename a feature
    pub.FeatureObj.prototype.rename = function () {

        console.log('got into : pub.FeatureObj.rename');

        let reqObj = {
            command : "feature",
            function : "rename",
            data : {}
        };

        // set the properties for the request
        if (this.file) reqObj.data.file = this.file;
        if (this.feat_id) reqObj.data.feat_id = this.feat_id;
        if (this.name) reqObj.data.name = this.name;
        if (this.new_name) reqObj.data.new_name = this.new_name;


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


    // Resume one or more features that match criteria
    pub.FeatureObj.prototype.resume = function () {

        console.log('got into : pub.FeatureObj.resume');

        let reqObj = {
            command : "feature",
            function : "resume",
            data : {}
        };

        // set the properties for the request
        if (this.file) reqObj.data.file = this.file;
        if (this.feat_id) reqObj.data.feat_id = this.feat_id;
        if (this.names) reqObj.data.names = this.names;
        if (this.name) reqObj.data.name = this.name;
        if (this.status) reqObj.data.status = this.status;
        if (this.type) reqObj.data.type = this.type;
        if (this.with_children) reqObj.data.with_children = this.with_children;


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


    // Set the value of a feature parameter
    pub.FeatureObj.prototype.set_param = function () {

        console.log('got into : pub.FeatureObj.set_param');

        let reqObj = {
            command : "feature",
            function : "set_param",
            data : {}
        };

        // set the properties for the request
        if (this.file) reqObj.data.file = this.file;
        if (this.name) reqObj.data.name = this.name;
        if (this.param) reqObj.data.param = this.param;
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


    // Suppress one or more features that match criteria
    pub.FeatureObj.prototype.suppress = function () {

        console.log('got into : pub.FeatureObj.suppress');

        let reqObj = {
            command : "feature",
            function : "suppress",
            data : {}
        };

        // set the properties for the request
        if (this.file) reqObj.data.file = this.file;
        if (this.feat_id) reqObj.data.feat_id = this.feat_id;
        if (this.names) reqObj.data.names = this.names;
        if (this.name) reqObj.data.name = this.name;
        if (this.status) reqObj.data.status = this.status;
        if (this.type) reqObj.data.type = this.type;
        if (this.clip) reqObj.data.clip = this.clip;
        if (this.with_children) reqObj.data.with_children = this.with_children;


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


    // Prompt the user to select one or more coordinate systems, and return their selections
    pub.FeatureObj.prototype.user_select_csys = function () {

        console.log('got into : pub.FeatureObj.user_select_csys');

        let reqObj = {
            command : "feature",
            function : "user_select_csys",
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
