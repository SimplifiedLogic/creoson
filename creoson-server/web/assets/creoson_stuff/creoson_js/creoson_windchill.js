// Define the Module
var creo = creo || {};

creo = (function (pub) {

    pub.WindchillObj = function(propsObj) {
        // BASE OBJECT
        this.context = undefined; // string - Windchill context
        this.filename = undefined; // string - File name
        this.filenames = undefined; // array:string - List of files to delete from the workspace
        this.password = undefined; // string - User's Windchill password
        this.server_url = undefined; // string - Server URL or Alias
        this.user = undefined; // string - User's Windchill login
        this.workspace = undefined; // string - Workspace name
        
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


    // Set user's Windchill login/password
    pub.WindchillObj.prototype.authorize = function () {

        console.log('got into : pub.WindchillObj.authorize');

        let reqObj = {
            command : "windchill",
            function : "authorize",
            data : {}
        };

        // set the properties for the request
        if (this.user) reqObj.data.user = this.user;
        if (this.password) reqObj.data.password = this.password;


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


    // Clear a workspace on the active server
    pub.WindchillObj.prototype.clear_workspace = function () {

        console.log('got into : pub.WindchillObj.clear_workspace');

        let reqObj = {
            command : "windchill",
            function : "clear_workspace",
            data : {}
        };

        // set the properties for the request
        if (this.workspace) reqObj.data.workspace = this.workspace;
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


    // Create a workspace on the active server
    pub.WindchillObj.prototype.create_workspace = function () {

        console.log('got into : pub.WindchillObj.create_workspace');

        let reqObj = {
            command : "windchill",
            function : "create_workspace",
            data : {}
        };

        // set the properties for the request
        if (this.workspace) reqObj.data.workspace = this.workspace;
        if (this.context) reqObj.data.context = this.context;


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


    // Delete a workspace on the active server
    pub.WindchillObj.prototype.delete_workspace = function () {

        console.log('got into : pub.WindchillObj.delete_workspace');

        let reqObj = {
            command : "windchill",
            function : "delete_workspace",
            data : {}
        };

        // set the properties for the request
        if (this.workspace) reqObj.data.workspace = this.workspace;


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


    // Get a list of workspaces the user can access on the active server
    pub.WindchillObj.prototype.list_workspaces = function () {

        console.log('got into : pub.WindchillObj.list_workspaces');

        let reqObj = {
            command : "windchill",
            function : "list_workspaces",
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


    // Check whether a server exists
    pub.WindchillObj.prototype.server_exists = function () {

        console.log('got into : pub.WindchillObj.server_exists');

        let reqObj = {
            command : "windchill",
            function : "server_exists",
            data : {}
        };

        // set the properties for the request
        if (this.server_url) reqObj.data.server_url = this.server_url;


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


    // Select a Windchill server
    pub.WindchillObj.prototype.set_server = function () {

        console.log('got into : pub.WindchillObj.set_server');

        let reqObj = {
            command : "windchill",
            function : "set_server",
            data : {}
        };

        // set the properties for the request
        if (this.server_url) reqObj.data.server_url = this.server_url;


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


    // Select a workspace on the active server
    pub.WindchillObj.prototype.set_workspace = function () {

        console.log('got into : pub.WindchillObj.set_workspace');

        let reqObj = {
            command : "windchill",
            function : "set_workspace",
            data : {}
        };

        // set the properties for the request
        if (this.workspace) reqObj.data.workspace = this.workspace;


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


    // Retrieve the name of the active workspace on the active server
    pub.WindchillObj.prototype.get_workspace = function () {

        console.log('got into : pub.WindchillObj.get_workspace');

        let reqObj = {
            command : "windchill",
            function : "get_workspace",
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


    // Check whether a workspace exists on the active server
    pub.WindchillObj.prototype.workspace_exists = function () {

        console.log('got into : pub.WindchillObj.workspace_exists');

        let reqObj = {
            command : "windchill",
            function : "workspace_exists",
            data : {}
        };

        // set the properties for the request
        if (this.workspace) reqObj.data.workspace = this.workspace;


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


    // Check whether a file is checked out in a workspace on the active server
    pub.WindchillObj.prototype.file_checked_out = function () {

        console.log('got into : pub.WindchillObj.file_checked_out');

        let reqObj = {
            command : "windchill",
            function : "file_checked_out",
            data : {}
        };

        // set the properties for the request
        if (this.workspace) reqObj.data.workspace = this.workspace;
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


    // Get a list of files in a workspace on the active server
    pub.WindchillObj.prototype.list_workspace_files = function () {

        console.log('got into : pub.WindchillObj.list_workspace_files');

        let reqObj = {
            command : "windchill",
            function : "list_workspace_files",
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


    return pub;

}(creo || {}));
