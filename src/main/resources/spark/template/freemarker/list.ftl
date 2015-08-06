<html>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta name="description" content="Knowledgebase 2.0">
        <meta name="author" content="Ronny Friedland">

        <link rel="stylesheet" href="/bootstrap.min.css">
        <link rel="stylesheet" href="/bootstrap-theme.min.css">
        
        <style type="text/css">
            .highlight {
                background-color: yellow;
            }
            
            .message {
                height: 150px;
                min-height: 150px;
                overflow: auto;
                resize: vertical;
            }
        </style>
        
        <script src="/jquery-1.11.2.min.js"></script>
        <script src="/bootstrap.min.js"></script>
        <script src="/jquery.highlight.min.js"></script>
        <script type="text/javascript">
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

            var limit = new Number(getQueryVariable('limit', '10'));

            var load = function () {
                limit = limit + 10;
                window.location.href='/data?limit='+limit+'&offset=0';
            };
            var refresh = function () {
                window.location.href='/data?limit='+limit+'&offset=0';
            };
            var filter = function (value) {
                window.location.href='/data?limit='+limit+'&offset=0&tag='+value;
            };
            var search = function (value) {
                if(value != "") {
                    window.location.href='/data?limit='+limit+'&offset=0&search='+value;
                } else {
                    refresh();
                }
            };
            var exportxml = function () {
                window.location.href='/data/export' + window.location.search;
            };

            jQuery( document ).ready(function() {
                var tag = getQueryVariable('tag');
                if(tag != null) {
                    jQuery('#filter').text(tag);
                    jQuery('#message').highlight(tag);
                }
                var search = getQueryVariable('search');
                if(search != null) {
                    jQuery('#filter').text(search);
                    jQuery('.message').highlight(search);
                }
            });
        </script
    </head>
    <body role="document">

      <nav class="navbar navbar-default">
        <div class="container">
          <div class="navbar-header">
            <a class="navbar-brand" href="/">Knowledgebase 2.0</a>
          </div>
          <div class="navbar-collapse collapse">
            <ul class="nav navbar-nav">
            <li><a href="#" onClick="javascript:exportxml();">Eintr&auml;ge exportieren</a></li>
            <li><a href="/data/add">Eintrag hinzuf&uuml;gen</a></li>
            </ul>
          </div><!--/.nav-collapse -->
        </div>
      </nav>
      
        <div class="container">
            <div class="form-group"><label for="search">Suche</label> <input type="text" id="search" />&nbsp;<input class="btn btn-default" type="button" value="Suchen" onClick="javascript:search(jQuery('#search').val());"/></div>
            <div class="form-group"><label for="search">Filter</label> <a href="#" onClick="javascript:refresh();" style="text-decoration:line-through;"><span id="filter" /></a></div>
            
            <div class="panel panel-default">
                <div class="panel-heading">Liste der Eintr&auml;ge</div>
                <div class="panel-body">
                    <#if (messages?size > 0) >
                          <div class="container-fluid scroll">
                            <#list messages as message>
                            
                              <div class="row">
                                <div class="col-md-10">
                                  <div class="list-group">
                                    <a href="/data/${message.key}">
                                      <h4 class="list-group-item-heading">${message.header}</h4>
                                    </a>
                                    <p class="list-group-item-text">
                                        <div class="message" id="${message.key}">${message.message}</div>
                                    </p>
                                    <#if (message.tags?size > 0) >
                                        <#list message.tags as tag>
                                          <a onClick="javascript:filter('${tag}');"><span class="label label-success">${tag}</span></a>
                                          &nbsp;
                                        </#list>
                                    </#if>
                                  </div>
                                </div>
                              </div>
            
                            </#list>
            
                            <#if (messages?size%10 == 0) >
                                <a onClick="javascript:load();">mehr</a>
                            </#if>
                        </div>
                    </#if>
                </div>
            </div>
            
        </div>
        
        <div class="container">
            <footer>
                <p>Version: ${project.version}</p>
            </footer>
        </div>
    </body>
    
    
</html>
