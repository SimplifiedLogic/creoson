//SET THE READY STATE
$(function() {
    console.log('successfully loaded csDoc.helpDocTables lib!');
});

// Define the Module
var csDoc = csDoc || {};

// define the sub-Module
csDoc.helpDocTables = csDoc.helpDocTables || {};

csDoc.helpDocTables = (function (pub) {

    pub.onlyUnique = function(value, index, self) {
        return self.indexOf(value) === index;
    };



    pub.FunctionHelpObjectOverview = function (dataObj) {

        this.data = dataObj;
        
        this.headersArr = null;

        this.table = $('<table>')
            .addClass('creosonHelpTable')
            .addClass('table')
            .addClass('table-sm')
            .addClass('table-bordered')
            .addClass('d-none')
            .addClass('d-md-table');

        this.genTableBody = function () {

            let tableBody = $('<tbody>');

            let rowsObjArr = [
                {
                    name: 'object_name',
                    title : "Description"
                },
                {
                    name: 'description',
                    title : "Command"
                }];

            for (let x=0; x < rowsObjArr.length ; x++) {
                let rowObj = rowsObjArr[x];

                let rowItem = $('<tr>');

                let rowTitle = $('<td>')
                    .text(rowObj.title)
                    .addClass('w-25')
                    .addClass('theme-bg-helptables-commands')
                    .addClass('text-right');

                let rowDetail = $('<td>')
                    .text(this.data[rowObj.name]);
                rowItem
                    .append(rowTitle)
                    .append(rowDetail);
                tableBody
                    .append(rowItem);
            }

            this.table.append(tableBody);

        };

        this.appendTable = function (target) {
            $(target).append(this.table);
        };

        this.genTableBody();

    };


    pub.FunctionHelpTableOverview = function (dataObj) {

        this.data = dataObj;

        this.table = $('<table>')
            .addClass('creosonHelpTable')
            .addClass('table')
            .addClass('table-sm')
            .addClass('table-bordered')
            .addClass('d-none')
            .addClass('d-md-table');

        this.genTableBody = function () {

            let tableBody = $('<tbody>');

            let rowsObjArr = [
                {
                    name: 'function_description',
                    title : "Description"
                },
                {
                    name: 'command',
                    title : "Command"
                },
                {
                    name: 'function',
                    title : "Function"
                }];

            for (let x=0; x < rowsObjArr.length ; x++) {
                let rowObj = rowsObjArr[x];

                let rowItem = $('<tr>');

                let rowTitle = $('<td>')
                    .text(rowObj.title)
                    .addClass('w-25')
                    .addClass('theme-bg-helptables-commands')
                    .addClass('text-right');

                let rowDetail = $('<td>')
                    .text(this.data[rowObj.name]);
                rowItem
                    .append(rowTitle)
                    .append(rowDetail);
                tableBody
                    .append(rowItem);
            }

            this.table.append(tableBody);

        };

        this.appendTable = function (target) {
            $(target).append(this.table);
        };

        this.genTableBody();

    };

    pub.FunctionHelpTableNotes = function (dataObj) {

        this.data = dataObj;

        this.table = $('<table>')
            .addClass('creosonHelpTable')
            .addClass('table')
            .addClass('table-sm')
            .addClass('table-bordered')
            .addClass('d-none')
            .addClass('d-md-table');

        let headerRow = $('<thead>');

        let titleRow = $('<tr>');
        let titleCell = $('<td>')
            .addClass('theme-bg-helptables-title')
            .text("Special Notes");
        
            headerRow.append(titleRow);
            titleRow.append(titleCell);

        this.table.append(headerRow);

        this.genTableBody = function () {

            let tableBody = $('<tbody>');

            for (let x=0; x < this.data.length ; x++) {
                let noteObj = this.data[x];

                let rowItem = $('<tr>');

                let noteDetail = $('<td>')
                    .text(noteObj)
                    .addClass('w-25')
                    .addClass('text-left');

                rowItem
                    .append(noteDetail);

                tableBody
                    .append(rowItem);
            }

            this.table.append(tableBody);

        };

        this.appendTable = function (target) {
            $(target).append(this.table);
        };

        this.genTableBody();

    };

    pub.FuntionHelpTableWide = function (sectionName, dataObjArr) {
        console.log('initializing : FuntionHelpTableWide');

        this.data = dataObjArr;

        this.headersArr = null;

        this.table = $('<table>')
            .addClass('creosonHelpTable')
            .addClass('table')
            .addClass('table-sm')
            .addClass('table-bordered')
            .addClass('d-none')
            .addClass('d-sm-table');

        this.getTableHeaders = function () {
            let headerArr = [];
            for (let i=0; i < dataObjArr.length; i++) {
                let keys = Object.keys(dataObjArr[i])
                console.log('found this : ' + keys);
                headerArr = headerArr.concat(keys);
            }
            this.headersArr = headerArr.filter(pub.onlyUnique);
        };

        this.genTableHeaders = function () {
            let headerRow = $('<thead>');

            let titleRow = $('<tr>');
            let titleCell = $('<td>')
                .attr('colspan',this.headersArr.length)
                .addClass('theme-bg-helptables-title')
                .text(sectionName);
            headerRow.append(titleRow);
            titleRow.append(titleCell);

            let headRow = $('<tr>');

            headerRow
                .append(headRow);

            for (let i=0; i < this.headersArr.length; i++) {
                let headerTitle = this.headersArr[i];
                let cell = $('<th>')
                    .attr('scope','col')
                    .addClass('theme-bg-helptables');
                cell.text(headerTitle);

                // make description column responsive
                if (headerTitle === 'description') {
                    cell.addClass('d-none d-md-table-cell')
                }

                headRow.append(cell);

            }
            this.table
                .append(headerRow);

        };

        this.genTableBody = function () {

            let tableBody = $('<tbody>');

            for (let i=0; i < this.data.length; i++) {

                let rowObj = this.data[i];

                let row = $('<tr>');

                // create row template
                for (let t=0; t < this.headersArr.length; t++) {
                    let headerTitle = this.headersArr[t];
                    let cellValue = $('<td>');
                    if (rowObj[headerTitle]) {
                        cellValue.text(rowObj[headerTitle]);
                    }

                    // make description column responsive
                    if (headerTitle === 'description') {
                        cellValue.addClass('d-none d-md-table-cell')
                    }
                    row.append(cellValue);
                }


                tableBody
                    .append(row);

            }

            this.table.append(tableBody);

        };

        this.appendTable = function (target) {
            $(target).append(this.table);
        };


        this.init = function () {
            this.getTableHeaders();
            this.genTableHeaders();
            this.genTableBody();
            console.log('computed headers : '+this.headersArr);
        };

        this.init();

    };




    pub.genHelpTables = function(data) {

        $('.creosonHelpTable').remove(); // clear any previously created tables from doc

        return new Promise(function (resolve, reject) {
            console.log('---- FOUND THIS ----');
            console.log(data);
            
            if (data.object_name) {

                let objectOverview = new csDoc.helpDocTables.FunctionHelpObjectOverview((data));
                objectOverview.appendTable('.content-inner');

                if (data.notes) {
                    let notesOverview = new csDoc.helpDocTables.FunctionHelpTableNotes(data.notes);
                    notesOverview.appendTable('.content-inner');
                }
                if (data.data) {
                    let helpRequests = new csDoc.helpDocTables.FuntionHelpTableWide('Request',data.data);
                    helpRequests.appendTable('.content-inner');
                }


            }

            if (data.spec) {
                let helpOverview = new csDoc.helpDocTables.FunctionHelpTableOverview(data.spec);
                helpOverview.appendTable('.content-inner');

                if (data.spec.notes) {
                    let notesOverview = new csDoc.helpDocTables.FunctionHelpTableNotes(data.spec.notes);
                    notesOverview.appendTable('.content-inner');
                }

                if (data.spec.request) {
                    let helpRequests = new csDoc.helpDocTables.FuntionHelpTableWide('Request',data.spec.request);
                    helpRequests.appendTable('.content-inner');
                }
                if (data.spec.response) {
                    let helpResponses = new csDoc.helpDocTables.FuntionHelpTableWide('Response', data.spec.response);
                    helpResponses.appendTable('.content-inner');
                }
            }
            resolve();
        })


    };


    return pub;

}(csDoc.helpDocTables || {}));


    