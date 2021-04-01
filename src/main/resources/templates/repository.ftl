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
    <link rel="stylesheet" href="/public/META-INF/resources/webjars/jstree/3.3.8/dist/themes/default/style.min.css" />
    <link rel="stylesheet" href="/public/knowledgebase.css"/>

    <script src="/public/META-INF/resources/webjars/jquery/3.6.0/dist/jquery.min.js"></script>
    <script src="/public/META-INF/resources/webjars/bootstrap/4.6.0/dist/js/bootstrap.min.js"></script>
    <script src="/public/META-INF/resources/webjars/jstree/3.3.8/dist/jstree.min.js"></script>
    <script src="/public/knowledgebase.js"></script>

    <script type="text/javascript">
      jQuery( document ).ready(function() {
        jQuery('#metadata').jstree({
            'plugins' : ['themes','sort','types'],
            'core' : {
              'data' : {
                'url' : '/documents/repository/metadata',
                'dataType' : 'json'
              },
              'sort' : function (a, b) {
                return this.get_text(a) > this.get_text(b) ? 1 : -1;
              }
            }
         })
         .on('changed.jstree', function (e, data) {
            if(data && data.selected && data.selected.length) {
                jQuery.get('/documents/repository/metadata/' + data.selected.join(':'), function (d) {
                    d.metadata.sort(sortByKeyAttribute);
                    var text = "<table class='table table-striped'>";
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
                    text += "</table>";
                    jQuery('#metadatadetails').html(text);
                });
            }
          });
        });
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
            <div class="card-text">
                <div id="metadata"></div>
             </div>
         </div>
       </div>


      <div class="card">
        <div class="card-body">
            <div class="card-header bg-light">Metadaten</div>
            <div class="card-text">
                <div id="metadatadetails"></div>
             </div>
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
