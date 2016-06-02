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
    <link rel="stylesheet" href="/public/jstree/themes/default/style.min.css" />

    <script src="/public/jquery-1.11.2.min.js"></script>
    <script src="/public/bootstrap.min.js"></script>
    <script src="/public/knowledgebase.js"></script>
    <script src="/public/jstree/jstree.min.js"></script>

    <script type="text/javascript">
      jQuery( document ).ready(function() {
        jQuery('#file').jstree({
            'plugins' : ['themes','sort','types'],
            'core' : {
              'data' : {
                'url' : '/files/metadata',
                'dataType' : 'json'
              },
              'sort' : function (a, b) {
                return this.get_text(a) > this.get_text(b) ? 1 : -1;
              }
            }
         })
         .on('changed.jstree', function (e, data) {
            if(data && data.selected && data.selected.length) {
                jQuery.get('/files/metadata/' + data.selected.join(':'), function (d) {
                    var text = "<table class='table table-striped'>";
                    jQuery.each(d.metadata, function(idx, obj) {
                        if(obj.key) {
                            text += "<tr><td>";
                            text += obj.key;
                            text += "</td>";
                            text += "<td><a href='/files/"+obj.value+"/raw' target='blank'>download<a></td></tr>";
                        }
                    });
                    text += "</table>";
                    jQuery('#filedetails').html(text);
                });
            }
          });
        });
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
            <li><a href="/documents">${locale("app.menu.list")}</a></li>
          </ul>
        </div><!--/.nav-collapse -->
      </div>
    </nav>

    <div class="container">

      <div class="col-sm-4">
        <div class="panel panel-default overflow_auto">
          <div class="panel-body">
            <div id="file"></div>
          </div>
        </div>
      </div>

      <div class="col-sm-8">
        <div class="panel panel-default overflow_auto">
          <div class="panel-heading">
            Metadaten
          </div>
          <div class="panel-body">
            <div id="filedetails"></div>
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