// Define the Module
var creo = creo || {};

creo = (function (pub) {

    pub.FamilytableObj = function(propsObj) {
        // BASE OBJECT
        this.colid = undefined; // string - Column ID
        this.cur_inst = undefined; // string - Instance name to replace
        this.cur_model = undefined; // string - Generic model containing the instances
        this.erase = undefined; // boolean - Erase model and non-displayed models afterwards
        this.file = undefined; // string - File name
        this.instance = undefined; // string - New instance name
        this.new_inst = undefined; // string - New instance name
        this.path = undefined; // array:integer - Path to component to replace
        this.value = undefined; // depends on data type - Cell value
        
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


    // Add a new instance to a family table; creates a family table if one does not exist
    pub.FamilytableObj.prototype.add_inst = function () {

        console.log('got into : pub.FamilytableObj.add_inst');

        let reqObj = {
            command : "familytable",
            function : "add_inst",
            data : {}
        };

        // set the properties for the request
        if (this.file) reqObj.data.file = this.file;
        if (this.instance) reqObj.data.instance = this.instance;


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


    // Create a new model from a family table row
    pub.FamilytableObj.prototype.create_inst = function () {

        console.log('got into : pub.FamilytableObj.create_inst');

        let reqObj = {
            command : "familytable",
            function : "create_inst",
            data : {}
        };

        // set the properties for the request
        if (this.file) reqObj.data.file = this.file;
        if (this.instance) reqObj.data.instance = this.instance;


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


    // Delete an entire family table
    pub.FamilytableObj.prototype.delete = function () {

        console.log('got into : pub.FamilytableObj.delete');

        let reqObj = {
            command : "familytable",
            function : "delete",
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


    // Delete an instance from a family table
    pub.FamilytableObj.prototype.delete_inst = function () {

        console.log('got into : pub.FamilytableObj.delete_inst');

        let reqObj = {
            command : "familytable",
            function : "delete_inst",
            data : {}
        };

        // set the properties for the request
        if (this.file) reqObj.data.file = this.file;
        if (this.instance) reqObj.data.instance = this.instance;


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


    // Check whether an instance exists in a family table
    pub.FamilytableObj.prototype.exists = function () {

        console.log('got into : pub.FamilytableObj.exists');

        let reqObj = {
            command : "familytable",
            function : "exists",
            data : {}
        };

        // set the properties for the request
        if (this.file) reqObj.data.file = this.file;
        if (this.instance) reqObj.data.instance = this.instance;


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


    // Get one cell of a family table
    pub.FamilytableObj.prototype.get_cell = function () {

        console.log('got into : pub.FamilytableObj.get_cell');

        let reqObj = {
            command : "familytable",
            function : "get_cell",
            data : {}
        };

        // set the properties for the request
        if (this.file) reqObj.data.file = this.file;
        if (this.instance) reqObj.data.instance = this.instance;
        if (this.colid) reqObj.data.colid = this.colid;


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


    // Get the header of a family table
    pub.FamilytableObj.prototype.get_header = function () {

        console.log('got into : pub.FamilytableObj.get_header');

        let reqObj = {
            command : "familytable",
            function : "get_header",
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


    // Get the parent instances of a model in a nested family table
    pub.FamilytableObj.prototype.get_parents = function () {

        console.log('got into : pub.FamilytableObj.get_parents');

        let reqObj = {
            command : "familytable",
            function : "get_parents",
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


    // Get one row of a family table
    pub.FamilytableObj.prototype.get_row = function () {

        console.log('got into : pub.FamilytableObj.get_row');

        let reqObj = {
            command : "familytable",
            function : "get_row",
            data : {}
        };

        // set the properties for the request
        if (this.file) reqObj.data.file = this.file;
        if (this.instance) reqObj.data.instance = this.instance;


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


    // List the instance names in a family table
    pub.FamilytableObj.prototype.list = function () {

        console.log('got into : pub.FamilytableObj.list');

        let reqObj = {
            command : "familytable",
            function : "list",
            data : {}
        };

        // set the properties for the request
        if (this.file) reqObj.data.file = this.file;
        if (this.instance) reqObj.data.instance = this.instance;


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


    // Get a hierarchical structure of a nested family table
    pub.FamilytableObj.prototype.list_tree = function () {

        console.log('got into : pub.FamilytableObj.list_tree');

        let reqObj = {
            command : "familytable",
            function : "list_tree",
            data : {}
        };

        // set the properties for the request
        if (this.file) reqObj.data.file = this.file;
        if (this.erase) reqObj.data.erase = this.erase;


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


    // Replace a model in an assembly with another instance in the same family table
    pub.FamilytableObj.prototype.replace = function () {

        console.log('got into : pub.FamilytableObj.replace');

        let reqObj = {
            command : "familytable",
            function : "replace",
            data : {}
        };

        // set the properties for the request
        if (this.file) reqObj.data.file = this.file;
        if (this.cur_model) reqObj.data.cur_model = this.cur_model;
        if (this.cur_inst) reqObj.data.cur_inst = this.cur_inst;
        if (this.path) reqObj.data.path = this.path;
        if (this.new_inst) reqObj.data.new_inst = this.new_inst;


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


    // Set the value of one cell of a family table
    pub.FamilytableObj.prototype.set_cell = function () {

        console.log('got into : pub.FamilytableObj.set_cell');

        let reqObj = {
            command : "familytable",
            function : "set_cell",
            data : {}
        };

        // set the properties for the request
        if (this.file) reqObj.data.file = this.file;
        if (this.instance) reqObj.data.instance = this.instance;
        if (this.colid) reqObj.data.colid = this.colid;
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


    return pub;

}(creo || {}));
