
// Define the Module
var creo = creo || {};

creo = (function (pub) {

    pub.CreosonUtils = function () {};


    pub.CreosonUtils.prototype.loopItems = function (instNamesArr, loopFunction) {

        return new Promise(function (resolve, reject) {

            if (typeof instNamesArr === typeof undefined) {
                reject("Nothing to process!");
            }

            let res = instNamesArr.reduce((accumPromise, nextID) => {
                return accumPromise.then(() => {
                    return loopFunction(nextID);
                });
            }, Promise.resolve());

            res
                .then(e => {
                    resolve();
                })
                .catch(e => {
                    reject(e)
                })

        });
    };



    pub.CreosonUtils.prototype.getFullInstNamesArr  = function (fileListInstDataObj) {

        return new Promise(function (resolve, reject) {


            if (!fileListInstDataObj.generic) {
                 reject('object missing generic property');
            }

            if (!fileListInstDataObj.files) {
                reject('object missing files property');
            }

            if (!Array.isArray(fileListInstDataObj.files)) {
                reject('getFullInstNamesArr must be an ARRAY!');
            }

            if (fileListInstDataObj.files.length === 0) {
                reject('no instances to process');
            }

            let finalArr = [];

            for (let i=0; i < fileListInstDataObj.files.length; i++) {
                    let instName = fileListInstDataObj.files[i];
                    let instRoot = instName.split('.')[0];
                    let instExt = instName.split('.')[1];
                    let finalName = instRoot+'<'+fileListInstDataObj.generic+'>.'+instExt;
                    finalArr.push(finalName);
            }

            resolve(finalArr);

        });

    };


    pub.CreosonUtils.prototype.getInstanceObjArr  = function (fileListInstDataObj) {

        return new Promise(function (resolve, reject) {


            if (!fileListInstDataObj.generic) {
                reject('object missing generic property');
            }

            if (!fileListInstDataObj.files) {
                reject('object missing files property');
            }

            if (!Array.isArray(fileListInstDataObj.files)) {
                reject('getFullInstNamesArr must be an ARRAY!');
            }

            if (fileListInstDataObj.files.length === 0) {
                reject('no instances to process');
            }

            let finalArr = [];

            for (let i=0; i < fileListInstDataObj.files.length; i++) {
                let instName = fileListInstDataObj.files[i];

                let instObj = {};
                    instObj.generic = fileListInstDataObj.generic;
                    instObj.instName = instName.split('.')[0];
                    instObj.ext = instName.split('.')[1];
                    instObj.file = instObj.instName+'<'+instObj.generic+'>.'+instObj.ext;
                finalArr.push(instObj);
            }

            resolve(finalArr);

        });

    };



    return pub;

}(creo || {}));

