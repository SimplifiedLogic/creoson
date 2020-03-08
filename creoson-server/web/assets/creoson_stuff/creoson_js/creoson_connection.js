// Define the Module
var creo = creo || {};

creo = (function (pub) {

    pub.ConnectionObj = function(propsObj) {
        // BASE OBJECT
        this.retries = undefined; // integer - Number of retries to make when connecting
        this.start_command = undefined; // string - Name of the .bat file
        this.start_dir = undefined; // string - Directory containing the .bat file
        this.use_desktop = undefined; // boolean - Whether to use the desktop to start creo rather than the java runtime.  Should only be used if the runtime method doesn't work.
        
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


    // Connect to CREOSON
    pub.ConnectionObj.prototype.connect = function () {

        console.log('got into : pub.ConnectionObj.connect');

        let reqObj = {
            command : "connection",
            function : "connect",
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


    // Disconnect from CREOSON
    pub.ConnectionObj.prototype.disconnect = function () {

        console.log('got into : pub.ConnectionObj.disconnect');

        let reqObj = {
            command : "connection",
            function : "disconnect",
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


    // Check whether Creo is running.
    pub.ConnectionObj.prototype.is_creo_running = function () {

        console.log('got into : pub.ConnectionObj.is_creo_running');

        let reqObj = {
            command : "connection",
            function : "is_creo_running",
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


    // Kill primary Creo processes.
    pub.ConnectionObj.prototype.kill_creo = function () {

        console.log('got into : pub.ConnectionObj.kill_creo');

        let reqObj = {
            command : "connection",
            function : "kill_creo",
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


    // Execute an external .bat file to start Creo, then attempts to connect to Creo.
    pub.ConnectionObj.prototype.start_creo = function () {

        console.log('got into : pub.ConnectionObj.start_creo');

        let reqObj = {
            command : "connection",
            function : "start_creo",
            data : {}
        };

        // set the properties for the request
        if (this.start_dir) reqObj.data.start_dir = this.start_dir;
        if (this.start_command) reqObj.data.start_command = this.start_command;
        if (this.retries) reqObj.data.retries = this.retries;
        if (this.use_desktop) reqObj.data.use_desktop = this.use_desktop;


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


    // Disconnect current session from Creo and cause Creo to exit.
    pub.ConnectionObj.prototype.stop_creo = function () {

        console.log('got into : pub.ConnectionObj.stop_creo');

        let reqObj = {
            command : "connection",
            function : "stop_creo",
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


    return pub;

}(creo || {}));
