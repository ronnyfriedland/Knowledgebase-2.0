<html>
  <head>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <meta name="description" content="Knowledgebase 2.0"/>
    <meta name="author" content="Ronny Friedland"/>
    <title>Knowledgebase 2.0</title>
    <link rel="icon" href="/public/images/icon.gif" type="image/gif" />

    <link rel="stylesheet" href="/public/META-INF/resources/webjars/bootstrap/4.6.0/dist/css/bootstrap.min.css" />
    <link rel="stylesheet" href="/public/META-INF/resources/webjars/jquery-confirm/3.3.4/dist/jquery-confirm.min.css" />
    <link rel="stylesheet" href="/public/META-INF/resources/webjars/font-awesome/4.7.0/css/font-awesome.min.css" />
    <link rel="stylesheet" href="/public/knowledgebase.css"/>

    <script src="/public/META-INF/resources/webjars/jquery/3.6.0/dist/jquery.min.js"></script>
    <script src="/public/META-INF/resources/webjars/bootstrap/4.6.0/dist/js/bootstrap.min.js"></script>
    <script src="/public/META-INF/resources/webjars/jquery-confirm/3.3.4/dist/jquery-confirm.min.js"></script>
    <script src="/public/jquery.highlight.min.js"></script>
    <script src="/public/knowledgebase.js"></script>

    <script type="text/javascript">
      var removeEntry = function(url){
        jQuery.confirm({
          content: "${locale("app.label.delete.confirm.text")}",
          title: "${locale("app.label.delete.confirm.title")}",
          buttons: {
                confirm: {
                    text: "${locale("app.label.yes")}",
                    action: function () {
                        jQuery.ajax({
                          url: url,
                          type: 'DELETE',
                          async:false
                        });
                        window.location.reload();
                    }
                },
                cancel: {
                    text: "${locale("app.label.no")}"
                }
          }
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

    <nav class="navbar navbar-expand-lg navbar-light bg-light">
      <div class="container">
        <a class="navbar-brand" href="/">${locale("app.name")}</a>
        <div class="collapse navbar-collapse">
          <ul class="navbar-nav mr-auto">
            <li class="nav-item"><a class="nav-link" href="/files">${locale("app.menu.files")}</a></li>
            <li class="nav-item"><a class="nav-link" href="#" onClick="javascript:exportxml();">${locale("app.menu.documents.export")}</a></li>
            <li class="nav-item"><a class="nav-link" href="/documents/import">${locale("app.menu.documents.import")}</a></li>
            <li class="nav-item"><a class="nav-link" href="/documents/add">${locale("app.menu.documents.add")}</a></li>
            <li class="nav-item"><a class="nav-link" href="/documents/repository">${locale("app.menu.documents.repository")}</a></li>
          </ul>
        </div><!--/.nav-collapse -->
      </div>
    </nav>

    <div class="container">

      <div class="alert alert-primary">
        <div class="form-group"><label for="search">${locale("app.label.search")}</label> <input type="text" id="search" />&nbsp;<input class="btn btn-primary" type="button" value="${locale("app.link.search")}" onClick="javascript:search(jQuery('#search').val());"/></div>
        <div class="form-group"><label for="search">${locale("app.label.filter")}</label> <a href="#" onClick="javascript:refresh();" style="text-decoration:line-through;"><span id="filter" /></a></div>

        <div class="well">
          <#list tags as tag>
            <a class="tag" href="#" onClick="javascript:filter('${tag}');"><h3 class="badge badge-primary">${tag}</h3></a>
          </#list>
        </div>
      </div>

      <div class="card">
        <div class="card-body">
            <div class="card-title">${locale("app.header.documents.list")}</div>
            <div class="card-text">
              <#if (messages?size > 0) >
                <div class="container-fluid scroll">

                  <#list messages as message>

                    <div class="row">
                      <div class="col-md-10">
                        <div class="list-group">
                          <div class="card">
                            <div id="header_${message.key}" class="card-body">
                              <div class="card-header bg-light">
                                <#if (message.header?length > 100)>
                                  <span data-toggle="tooltip" title="${message.header}">
                                    <a class="header" data-toggle="collapse" href="#body_${message.key}">${message.header?substring(0,100)}...</a>
                                  </span>
                                <#else>
                                  <a class="header" data-toggle="collapse" href="#body_${message.key}">${message.header}</a>
                                </#if>
                                <#if (message.encrypted)>
                                  <span class="fa fa-lock" aria-hidden="true" />
                                </#if>
                              </div>
                              <div id="body_${message.key}" class="card-footer panel-collapse collapse" aria-expanded="false">
                                <p class="list-group-item-text collapse in">
                                  <div class="message">${message.message}</div>
                                </p>
                                <form action="/documents/${message.key}">
                                  <input class="btn btn-primary" type="submit" value="${locale("app.link.edit")}" />
                                  <input class="btn btn-primary" type="button"  data-toggle="modal" data-target="#lightbox" onclick="javascript:setModalText('${message.key}')" value="${locale("app.link.fullscreen")}" />
                                  <input class="btn btn-primary confirm" type="button" value="${locale("app.link.delete")}" onclick="javascript:removeEntry('/documents/${message.key}');" />
                                </form>
                              </div>
                              <div class="card-text">
                                <#if (message.tags?size > 0) >
                                  <#list message.tags as tag>
                                    <a class="badge badge-primary" href="#" onClick="javascript:filter('${tag}');"><span class="label label-default">${tag}</span></a>&nbsp;
                                  </#list>
                                </#if>
                              </div>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>

                    <br/>

                  </#list>

                  <#if (messages?size%10 == 0) >
                    <a href="#" onClick="javascript:load();">${locale("app.link.more")}</a>
                  </#if>
                </div>
              </#if>
            </div>
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
