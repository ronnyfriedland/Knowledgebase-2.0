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
    <link rel="stylesheet" href="/public/knowledgebase.css"/>
    <link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap-glyphicons.css" />

    <script src="/public/jquery-1.11.2.min.js"></script>
    <script src="/public/jquery.confirm.min.js"></script>
    <script src="/public/bootstrap.min.js"></script>
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
    </script>
  </head>
  <body role="document">

    <nav class="navbar navbar-default">
      <div class="container">
        <div class="navbar-header">
          <div class="navbar-header">
            <a class="navbar-brand" href="/files">${locale("app.name")}</a>
          </div>
        </div>
        <div class="navbar-collapse">
          <ul class="nav navbar-nav">
            <li><a href="/documents">${locale("app.menu.documents")}</a></li>
          </ul>
        </div><!--/.nav-collapse -->
      </div>
    </nav>


    <div class="container">
        <#if parent??>
          <a class="label label-default" href="/files/${parent.key}">${parent.key}</a>
        </#if>
    </div>
    
    <br/>

    <div class="container">

      <div class="panel panel-default">
        <div class="panel-heading">${locale("app.header.files.list")} ${header}</div>
        <div class="panel-body">
          <#if (files?size > 0) >
            <div class="container-fluid scroll">
            
             <div class="row">

              <#list files as file>

                <div class="col-sm-4">  
                    <div class="panel panel-warning">
                      <div class="panel-heading">
                        <div class="small">
                            <#if (file.key?length > 35)>
                              <span data-toggle="tooltip" title="${file.key}">
                                <a href="/files/${file.header}">${file.key?substring(0,35)}...</a>
                              </span>
                            <#else>
                              <a href="/files/${file.header}">${file.key}</a> 
                            </#if>

                            <span onclick="javascript:removeEntry('/files/${file.header}');" style="cursor:pointer">
                                &nbsp;&nbsp;<span class="glyphicon glyphicon-trash" aria-hidden="true" />
                            </span>
                        </div>
                      </div>
                      <div class="panel-body" id="body_${file?index}">
                      </div>
                      <script type="text/javascript">
                        jQuery(function () { 
                            jQuery.get('/files/metadata/${file.header}', function (d) {
                                d.metadata.sort(sortByKeyAttribute);
                                var text = "<div class='table-responsive'><table class='table table-striped small'>";
                                jQuery.each(d.metadata, function(idx, obj) {
                                    if(obj.key) {
                                        text += "<tr><td>";
                                        text += obj.key;
                                        text += "</td>";
                                        text += "<td>";
                                        text += obj.value;
                                        text += "</td></tr>";
                                    }
                                });
                                text += "</table></div>";
                                jQuery("#body_${file?index}").html(text);
                            });
                        });
                        </script>
                    </div>

                </div>

              </#list>

             </div>

             <#if document??>
                <div class="alert alert-warning">
                  <div class="container">
                    <script type="text/javascript">
                            jQuery(function () { 
                                jQuery.get('/documents/${document.key}/raw', function (d) {
                                    jQuery('.alert').html(d);
                                });
                            });
                    </script>
                  </div>
                </div>
                <a href="/documents/${document.key}?redirect=/files/${header}">
                    <span class="glyphicon glyphicon-pencil" aria-hidden="true" />
                </a>
             <#else>
                <a href="/documents/add?header=${header}&tags=filesystem&redirect=/files/${header}">
                    <span class="glyphicon glyphicon-file" aria-hidden="true" />
                </a>
            </#if>

           </div>

          </#if>
        </div>

    </div>

    <div class="container">
      <footer>
        <p>Version: @project.version@</p>
      </footer>
    </div>

  </body>
</html>
