<html>
  <head>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <meta name="description" content="Knowledgebase 2.0"/>
    <meta name="author" content="Ronny Friedland"/>
    <title>Knowledgebase 2.0</title>
    <link rel="icon" href="/public/images/icon.gif" type="image/gif" />

    <link rel="stylesheet" href="/public/bootstrap.min.css" />
    <link rel="stylesheet" href="/public/bootstrap-theme.min.css"/>
    <link rel="stylesheet" href="/public/bootstrap-lightbox.min.css"/>
    <link rel="stylesheet" href="/public/knowledgebase.css"/>

    <script src="/public/jquery-1.11.2.min.js"></script>
    <script src="/public/jquery.highlight.min.js"></script>
    <script src="/public/jquery.confirm.min.js"></script>
    <script src="/public/bootstrap.min.js"></script>
    <script src="/public/bootstrap-lightbox.min.js"></script>
    <script src="/public/knowledgebase.js"></script>

    <script type="text/javascript">
      var removeEntry = function(url){
        jQuery.confirm({
          text: "${locale("app.label.delete.confirm.text")}",
          title: "${locale("app.label.delete.confirm.title")}",
          confirm: function(button) {
            jQuery.ajax({
              url: url,
              type: 'DELETE',
              async:false
            });
            window.location.reload();
          },
          confirmButton: "${locale("app.label.yes")}",
          cancelButton: "${locale("app.label.no")}",
          post: true,
          confirmButtonClass: "btn-danger",
          cancelButtonClass: "btn-default",
          dialogClass: "modal-dialog modal-lg"
        });
      };
      
      var setModalText = function(elem) {
        var header = jQuery('#header_' + elem).find('.header').html();
        var body = jQuery('#body_' + elem).find('.message').html();
        
        jQuery('#lightbox').find('.modal-title').html(header);
        jQuery('#lightbox').find('.modal-body').html(body);
      }

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
          <a class="navbar-brand" href="/">${locale("app.name")}</a>
        </div>
        <div class="navbar-collapse">
          <ul class="nav navbar-nav">
            <li><a href="/files">${locale("app.menu.files")}</a></li>
            <li><a href="#" onClick="javascript:exportxml();">${locale("app.menu.documents.export")}</a></li>
            <li><a href="/documents/import">${locale("app.menu.documents.import")}</a></li>
            <li><a href="/documents/add">${locale("app.menu.documents.add")}</a></li>
            <li><a href="/documents/management">${locale("app.menu.documents.management")}</a></li>
          </ul>
        </div><!--/.nav-collapse -->
      </div>
    </nav>

    <div class="container">
      <div class="form-group"><label for="search">${locale("app.label.search")}</label> <input type="text" id="search" />&nbsp;<input class="btn btn-default" type="button" value="${locale("app.link.search")}" onClick="javascript:search(jQuery('#search').val());"/></div>
      <div class="form-group"><label for="search">${locale("app.label.filter")}</label> <a href="#" onClick="javascript:refresh();" style="text-decoration:line-through;"><span id="filter" /></a></div>

      <div class="well">
        <#list tags as tag>
          <a class="tag" href="#" onClick="javascript:filter('${tag}');"><h3 class="label label-default">${tag}</h3></a>
        </#list>
      </div>

      <div class="panel panel-default">
        <div class="panel-heading">${locale("app.header.documents.list")}</div>
        <div class="panel-body">
          <#if (messages?size > 0) >
            <div class="container-fluid scroll">
            
              <#list messages as message>

                <div class="row">
                  <div class="col-md-10">
                    <div class="list-group">
                      <div class="panel panel-success">
                        <div id="header_${message.key}" class="panel-heading">
                          <div class="panel-title" >
                            <#if (message.header?length > 100)>
                              <a class="header" data-toggle="collapse" href="#body_${message.key}">${message.header?substring(0,50)}...</a>
                            <#else>
                              <a class="header" data-toggle="collapse" href="#body_${message.key}">${message.header}</a>
                            </#if>
                          </div>
                          <div>
                            <#if (message.tags?size > 0) >
                              <#list message.tags as tag>
                                <a class="tag" href="#" onClick="javascript:filter('${tag}');"><span class="label label-default">${tag}</span></a>&nbsp;
                              </#list>
                            </#if>
                          </div>
                        </div>
                        <div id="body_${message.key}" class="panel-collapse collapse in">
                          <div class="panel-body">
                            <p class="list-group-item-text collapse in">
                              <div class="message">${message.message}</div>
                            </p>
                            <form action="/documents/${message.key}">
                                <input class="btn btn-default" type="submit" value="${locale("app.link.edit")}" />
                                <input class="btn btn-default" type="button"  data-toggle="modal" data-target="#lightbox" onclick="javascript:setModalText('${message.key}')" value="${locale("app.link.fullscreen")}" />
                                <input class="btn btn-default confirm" type="button" value="${locale("app.link.delete")}" onclick="javascript:removeEntry('/documents/${message.key}');" />
                            </form>
                          </div>
                        </div>
                      </div>
                      <#if (message_index > 0) >
                        <script type="text/javascript">
                          jQuery(function () { jQuery('#body_${message.key}').collapse('hide')});
                        </script>  
                      </#if>
                    </div>
                  </div>
                </div>

              </#list>

              <#if (messages?size%10 == 0) >
                <a href="#" onClick="javascript:load();">${locale("app.link.more")}</a>
              </#if>
            </div>
          </#if>
        </div>
      </div>
    </div>

    <div class="container">
      <footer>
        <p>Version: @project.version@</p>
      </footer>
    </div>
    
    <div id="lightbox" class=" modal fade" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel" aria-hidden="true">
      <div class="modal-dialog modal-lg">
        <button type="button" class="close hidden" data-dismiss="modal" aria-hidden="true">×</button>
          <div class="modal-content">
            <div class="modal-header">
              <h4 class="modal-title">Modal Header</h4>
            </div>
            <div class="modal-body">
            </div>
            <div class="modal-footer">
              <button class="close" data-dismiss="modal" data-target="#lightbox">X</button>
            </div>
        </div>
      </div>
    </div>

  </body>
</html>
