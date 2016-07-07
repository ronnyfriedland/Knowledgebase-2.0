/**
 * Retrieves the value of the given parameter.
 * @param variable the name of the parameter
 * @param def the default value
 * @returns the value of the request parameter
 */
function getQueryVariable(variable, def) {
    var query = window.location.search.substring(1);
    var vars = query.split('&');
    for (var i = 0; i < vars.length; i++) {
        var pair = vars[i].split('=');
        if (decodeURIComponent(pair[0]) == variable) {
            return decodeURIComponent(pair[1]);
        }
    }
    return def;
}
/** The result limit */
var limit = new Number(getQueryVariable('limit', '10'));

/** Load page with current result limit */
var load = function () {
    limit = limit + 10;
    window.location.href='/documents?limit='+limit+'&offset=0';
};
/** Reload page with previous result limit */
var refresh = function () {
    window.location.href='/documents?limit='+limit+'&offset=0';
};
/** Filter the result by given tag */
var filter = function (value) {
    window.location.href='/documents?limit='+limit+'&offset=0&tag='+value;
};
/** Filter the result by given search string */
var search = function (value) {
    if(value != "") {
        window.location.href='/documents?limit='+limit+'&offset=0&search='+value;
    } else {
        refresh();
    }
};
/** Export current result list as xml */
var exportxml = function () {
    window.location.href='/documents/xml/export' + window.location.search;
};

/** Sort the given complex values by attribute 'key' */
var sortByKeyAttribute = function(a, b) {
    var aName = a.key.toLowerCase();
    var bName = b.key.toLowerCase(); 
    return ((aName < bName) ? -1 : ((aName > bName) ? 1 : 0));
}

/** Enable tooltips */
jQuery(document).ready(function(){
	jQuery('[data-toggle="tooltip"]').tooltip();
});