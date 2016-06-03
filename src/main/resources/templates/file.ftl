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

    <script src="/public/jquery-1.11.2.min.js"></script>
    <script src="/public/bootstrap.min.js"></script>
    <script src="/public/knowledgebase.js"></script>

    <script type="text/javascript">
      jQuery( document ).ready(function() {
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
        <#if parent??>
          <a class="label label-default" href="/files/${parent.key}">${parent.key}</a>
        </#if>
    </div>
    
    <br/>

    <div class="container">

      <div class="panel panel-default">
        <div class="panel-heading">${locale("app.header.list")}</div>
        <div class="panel-body">
          <#if (files?size > 0) >
            <div class="container-fluid scroll">
            
             <div class="row">

              <#list files as file>

                <div class="col-sm-4">
                    <div class="panel panel-warning">
                      <div class="panel-heading">
                        <p><b><a href="/files/${file.header}">${file.key}</a></b></p>
                      </div>
                      <div class="panel-body">
                        <!--<p>${file.header}</p>-->
                        <p>
                            <a role="button" href="/files/${file.header}/raw" class="btn btn-default">download</a>
                        </p>
                      </div>
                    </div>

                </div>

              </#list>

             </div>

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
