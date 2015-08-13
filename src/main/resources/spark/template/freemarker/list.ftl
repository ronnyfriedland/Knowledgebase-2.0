<html>
  <head>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <meta name="description" content="Knowledgebase 2.0"/>
    <meta name="author" content="Ronny Friedland"/>

    <link rel="stylesheet" href="/bootstrap.min.css" />
    <link rel="stylesheet" href="/bootstrap-theme.min.css"/>
    <link rel="stylesheet" href="/knowledgebase.css"/>

    <script src="/jquery-1.11.2.min.js"></script>
    <script src="/jquery.highlight.min.js"></script>
    <script src="/bootstrap.min.js"></script>
    <script src="/knowledgebase.js"></script>

    <script type="text/javascript">
      var removeEntry = function(url){
        jQuery.ajax({
          url: url,
          type: 'DELETE',
          async:false
        });
        window.location.reload();
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
    </script>
  </head>
  <body role="document">

    <nav class="navbar navbar-default">
      <div class="container">
        <div class="navbar-header">
          <a class="navbar-brand" href="/">Knowledgebase 2.0</a>
        </div>
        <div class="navbar-collapse">
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
                      <div class="panel panel-success">
                        <div class="panel-heading">
                          <div class="panel-title">
                            <#if (message.header?length > 50)>
                              <a data-toggle="collapse" href="#${message.key}">${message.header?substring(0,50)}...</a>
                            <#else>
                              <a data-toggle="collapse" href="#${message.key}">${message.header}</a>
                            </#if>
                          </div>
                          <div>
                            <#if (message.tags?size > 0) >
                              <#list message.tags as tag>
                                <a onClick="javascript:filter('${tag}');"><span class="label label-default">${tag}</span></a>
                              </#list>
                            </#if>
                          </div>
                        </div>
                        <div id="${message.key}" class="panel-collapse collapse in">
                          <div class="panel-body">
                            <p class="list-group-item-text collapse in">
                              <div class="message" id="${message.key}">${message.message}</div>
                            </p>
                            <form action="/data/${message.key}">
                                <input class="btn btn-default" type="submit" value="Bearbeiten" />
                                <input class="btn btn-default" type="button" value="L&ouml;schen" onclick="javascript:removeEntry('/data/${message.key}');" />
                            </form>
                          </div>
                        </div>
                      </div>
                      <#if (message_index > 0) >
                        <script type="text/javascript">
                          jQuery(function () { jQuery('#${message.key}').collapse('hide')});
                        </script>  
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
