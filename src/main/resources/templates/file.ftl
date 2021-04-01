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
    </script>
  </head>
  <body role="document">

    <nav class="navbar navbar-expand-lg navbar-light bg-light">
      <div class="container">
        <a class="navbar-brand" href="/">${locale("app.name")}</a>
        <div class="collapse navbar-collapse">
          <ul class="navbar-nav mr-auto">
            <li class="nav-item"><a class="nav-link" href="/documents">${locale("app.menu.documents")}</a></li>
          </ul>
        </div><!--/.nav-collapse -->
      </div>
    </nav>

    <div class="container">
     <div class="card">
      <div class="card-body">
        <div class="card-header bg-light">${locale("app.header.files.list")}
            <#assign path = "">
            <#list header?split("/") as x>
                /
                <#assign path = "${path}/${x}">
                <#if path?length < root?length>
                    ${x}
                <#else>
                    <a class="label label-default" href="/files${path}">${x}</a>
                </#if>
            </#list>

        </div>
        <div class="card-text">
          <#if (files?size > 0) >
            <div class="container-fluid scroll">
            
             <div class="row">

              <#list files as file>

                <div class="col-sm-4">  
                    <div class="card">
                     <div class="card-body">
                      <div class="card-title">
                        <div class="small">
                            <#if (file.key?length > 35)>
                              <span data-toggle="tooltip" title="${file.key}">
                                <a href="/files/${file.header}">${file.key?substring(0,35)}...</a>
                              </span>
                            <#else>
                              <a href="/files/${file.header}">${file.key}</a> 
                            </#if>

                            <span onclick="javascript:removeEntry('/files/${file.header}');" style="cursor:pointer">
                                &nbsp;&nbsp;<span class="fa fa-trash" aria-hidden="true" />
                            </span>
                        </div>
                      </div>
                      <div class="card-text" id="body_${file?index}">
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

                </div>

              </#list>

             </div>

             <br/>

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
                    <span class="fa fa-edit" aria-hidden="true" />
                </a>
             <#else>
                <a href="/documents/add?header=${header}&tags=filesystem&redirect=/files/${header}">
                    <span class="fa fa-file" aria-hidden="true" />
                </a>
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

  </body>
</html>
