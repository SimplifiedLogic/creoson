// http://json2table.com/#

var json_visible = !0, json_pnl_size = 0, table_visible = !0, tree_visible = !0, tree_pnl_size = 0, xxa_pnl_size = 0;
jQuery(function (a) {
    function e() {
        json_visible ? (a("#all_panels").split().position(a("#all_panels").width() - xxa_pnl_size - 2), table_visible && tree_visible ? a("#xxa").split().position(xxa_pnl_size - tree_pnl_size - 2) : !table_visible && tree_visible ? a("#xxa").split().position(0) : table_visible && !tree_visible ? a("#xxa").split().position(xxa_pnl_size) : table_visible || tree_visible || a("#all_panels").split().position(a("#all_panels").width())) : (a("#all_panels").split().position(0), table_visible && tree_visible ? a("#xxa").split().position(xxa_pnl_size -
            tree_pnl_size - 2) : !table_visible && tree_visible ? a("#xxa").split().position(0) : table_visible && !tree_visible && a("#xxa").split().position(a("#all_panels").width() - 2))
    }

    a("#load_json_btn").click(function () {
        processJson()
    });
    a("#aboutLnk").click(function () {
        a("#leaveMsg").val("");
        a("#msgConfirmation").hide();
        a("#msgForm").show();
        a("#aboutModal").modal("show")
    });
    a("#sendMsgBtn").click(function () {
        a("#msgForm").hide();
        a("#msgConfirmation").show();
        sendMsg()
    });
    a("#load_url_btn").click(function () {
        a("#inputURLModal").modal("show")
    });
    a("#exec_loadBtn").click(function () {
        loadfromURL(a("#urlInput").val())
    })
});
function sendMsg() {
    $.ajax({
        type: "GET",
        url: "http://json2table-env-ayji8pibkt.elasticbeanstalk.com/save_msg",
        data: {callback: "call", msg: $("#leaveMsg").val()},
        contentType: "application/json",
        dataType: "jsonp",
        success: function (a) {
        },
        error: function (a) {
        }
    })
}
var g;
function loadfromURL(a) {
    $("#json_vl").val("Loading...");
    $("#inner_tree").html("");
    $("#inner_tbl").html("");
    "http" != a.substr(0, 4) && (a = "http://" + a);
    $.ajax({
        type: "GET",
        url: "http://json2table-env-ayji8pibkt.elasticbeanstalk.com/getjson",
        data: {callback: "call", url: encodeURIComponent(a)},
        contentType: "application/json",
        dataType: "jsonp",
        success: function (a) {
            $("#json_vl").val(JSON.stringify(a, void 0, 2));
            processJson()
        },
        error: function (e) {
            $("#json_vl").val("");
            $("#error_msg").text("Not a valid JSON from " + a);
            $("#errorModal").modal("show");
            return {}
        }
    })
}
function call(a) {
    $("#json_vl").val(JSON.stringify(a, void 0, 2));
    processJson()
}
function processJson() {
    $("#inner_tbl").html(buildTable(getJsonVar()));
    //showTree()
}
function getJsonVar() {
    try {
        var a = $.parseJSON($("#json_vl").val());
        $("#json_vl").val(JSON.stringify(a, void 0, 2));
        return a
    } catch (e) {
        return $("#error_msg").text(e.message), $("#errorModal").modal("show"), {}
    }
}
function showTree() {
    var a = document.createElement("ol"), e = document.createElement("li"), d = "_" + Math.random().toString(36).substr(2, 9);
    e.innerHTML = "<label for='" + d + "' class='lbl_obj'>&nbsp;</label> <input type='checkbox' checked id='" + d + "' />";
    d = document.createElement("ol");
    e.appendChild(d);
    a.appendChild(e);
    buildTree(getJsonVar(), 0, d);
    $("#inner_tree").html(a)
}
function buildTable(a) {
    var e = document.createElement("table"), d, b;
    if (isArray(a))return buildArray(a);
    for (var c in a)"object" != typeof a[c] || isArray(a[c]) ? "object" == typeof a[c] && isArray(a[c]) ? (d = e.insertRow(-1), b = d.insertCell(-1), b.colSpan = 2, b.innerHTML = '<div class="td_head2">' + encodeText(c) + '</div><table style="width:100%">' + $(buildArray(a[c]), !1).html() + "</table>") : (d = e.insertRow(-1), b = d.insertCell(-1), b.innerHTML = "<div class='td_head'>" + encodeText(c) + "</div>", d = d.insertCell(-1), d.innerHTML = "<div class='td_row_even'>" +
        encodeText(a[c]) + "</div>") : (d = e.insertRow(-1), b = d.insertCell(-1), b.colSpan = 2, b.innerHTML = '<div class="td_head">' + encodeText(c) + '</div><table style="width:100%">' + $(buildTable(a[c]), !1).html() + "</table>");
    return e
}
function buildArray(a) {
    var e = document.createElement("table"), d, b, c = !1, p = !1, m = {}, h = -1, n = 0, l;
    l = "";
    if (0 == a.length)return "<div></div>";
    d = e.insertRow(-1);
    for (var f = 0; f < a.length; f++)if ("object" != typeof a[f] || isArray(a[f])) "object" == typeof a[f] && isArray(a[f]) ? (b = d.insertCell(h), b.colSpan = 2, b.innerHTML = '<div class="td_head"></div><table style="width:100%">' + $(buildArray(a[f]), !1).html() + "</table>", c = !0) : p || (h += 1, p = !0, b = d.insertCell(h), m.empty = h, b.innerHTML = "<div class='td_head'>&nbsp;</div>"); else for (var k in a[f])l =
        "-" + k, l in m || (c = !0, h += 1, b = d.insertCell(h), m[l] = h, b.innerHTML = "<div class='td_head'>" + encodeText(k) + "</div>");
    c || e.deleteRow(0);
    n = h + 1;
    for (f = 0; f < a.length; f++)if (d = e.insertRow(-1), td_class = isEven(f) ? "td_row_even" : "td_row_odd", "object" != typeof a[f] || isArray(a[f]))if ("object" == typeof a[f] && isArray(a[f]))for (h = m.empty, c = 0; c < n; c++)b = d.insertCell(c), b.className = td_class, l = c == h ? '<table style="width:100%">' + $(buildArray(a[f]), !1).html() + "</table>" : " ", b.innerHTML = "<div class='" + td_class + "'>" + encodeText(l) +
        "</div>"; else for (h = m.empty, c = 0; c < n; c++)b = d.insertCell(c), l = c == h ? a[f] : " ", b.className = td_class, b.innerHTML = "<div class='" + td_class + "'>" + encodeText(l) + "</div>"; else {
        for (c = 0; c < n; c++)b = d.insertCell(c), b.className = td_class, b.innerHTML = "<div class='" + td_class + "'>&nbsp;</div>";
        for (k in a[f])c = a[f], l = "-" + k, h = m[l], b = d.cells[h], b.className = td_class, "object" != typeof c[k] || isArray(c[k]) ? "object" == typeof c[k] && isArray(c[k]) ? b.innerHTML = '<table style="width:100%">' + $(buildArray(c[k]), !1).html() + "</table>" : b.innerHTML =
            "<div class='" + td_class + "'>" + encodeText(c[k]) + "</div>" : b.innerHTML = '<table style="width:100%">' + $(buildTable(c[k]), !1).html() + "</table>"
    }
    return e
}
function encodeText(a) {
    return $("<div />").text(a).html()
}
function isArray(a) {
    return "[object Array]" === Object.prototype.toString.call(a)
}
function isEven(a) {
    return 0 == a % 2
}
function buildTree(a, e, d) {
    e += 1;
    if ("undefined" === typeof a) log("undef!!", e); else for (var b in a)if ("object" == typeof a[b]) {
        var c = addTree(b, d, isArray(a[b]));
        buildTree(a[b], e, c)
    } else addLeaf(b, a, d)
}
function addTree(a, e, d) {
    var b = "lbl_obj";
    d && (b = "lbl_array");
    var c = "_" + Math.random().toString(36).substr(2, 9);
    d = document.createElement("li");
    d.innerHTML = "<label for='" + c + "' class='" + b + "'>" + encodeText(a) + "</label> <input type='checkbox' checked id='" + c + "' />";
    a = document.createElement("ol");
    d.appendChild(a);
    null != e && e.appendChild(d);
    return a
}
function addLeaf(a, e, d) {
    var b = "";
    isArray(e) || (b = a + ":");
    b += e[a];
    Math.random().toString(36).substr(2, 9);
    a = document.createElement("li");
    a.className = "file";
    a.innerHTML = "<a>" + encodeText(b) + "</a>";
    d.appendChild(a)
}
function log(a, e, d) {
    console.log(a)
};