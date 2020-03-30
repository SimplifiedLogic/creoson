
// Define the Module
var creo = creo || {};

creo.ajax = (function (pub) {

        if (typeof jQuery === "undefined") {
            alert('creo.ajax requires the jQuery Library to be loaded in your HTML file!');
            throw('creo.ajax requires the jQuery Library to be loaded in your HTML file!');
        }

        pub.sessionId = -1;
        pub.url  =  '/creoson';
        pub.port =  9050;
        pub.dataType =  "json";          
        pub.type =  'post';
        pub.traditional = true;

        pub.lastRequestObj = {};
        
        pub.request = function(dataObj) {
            console.log('got into : creo.ajax request');

            return new Promise(function (resolve, reject) {

                if (pub.sessionId !== -1) {
                    dataObj.sessionId = pub.sessionId;
                }

                // define the default request - inherit current pub vars
                let requestObjConst = {
                    url: pub.url,
                    port: pub.port,
                    dataType: pub.dataType,
                    type: pub.type,
                    async : false,
                    traditional: pub.traditional,
                    data: null
                };

                requestObjConst.data = JSON.stringify(dataObj); // set the transaction to the request object

                pub.lastRequestObj = requestObjConst;  // expose the request

                // log the request to the console
                console.log(JSON.stringify(requestObjConst, null, 2));
                $.ajax(
                    requestObjConst
                )
                    .done(function (data) {

                        if (data.status.error) {

                            console.log('---- ERROR -----');
                            console.log(JSON.stringify(data, null, 2));

                            if (data.status.hasOwnProperty('message')) {
                                reject(data.status.message);
                            } else {
                                reject('creoson Operation failed! - check console for details');
                            }


                        } else {

                            if (dataObj.command === 'connection' && dataObj.function === 'connect') {
                                console.log('automatically setting the sessionId : '+data.sessionId);
                                pub.sessionId = data.sessionId;
                            }

                            resolve(data);
                        }

                    })
                    .fail(function (e) {
                        // reject('creoson Operation failed! - check console for details');
                        reject(e);
                    });

            })
        };

    return pub;

}(creo.ajax || {}));
