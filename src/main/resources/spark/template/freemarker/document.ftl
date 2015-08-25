<html>
  <head>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <meta name="description" content="Knowledgebase 2.0"/>
    <meta name="author" content="Ronny Friedland"/>
    <title>Knowledgebase 2.0</title>

    <link rel="stylesheet" href="/bootstrap.min.css"/>
    <link rel="stylesheet" href="/bootstrap-theme.min.css"/>
    <script src="/jquery-1.11.2.min.js"></script>
    <script src="/jquery.validate.min.js"></script>
    <script src="/bootstrap.min.js"></script>
    <script src="/ckeditor/ckeditor.js"></script>
    <script type="text/javascript">
        jQuery( document ).ready(function() {
            CKEDITOR.replace('message');

            header = jQuery("#header");
            if("" != header.val()) {
                header.attr('readonly', true);
            }

            // validate signup form on keyup and submit
            jQuery("#documentForm").validate({
                rules: {
                    header: {
                        required: true,
                        minlength: 2
                    }
                },
                messages: {
                    header: "&nbsp;<div class='alert alert-danger' role='alert'>Bitte eine g&uuml;ltige &Uuml;berschrift vergeben (mindestens 2 Zeichen)</div>",
                }
            });
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
          <li><a href="/data">Eintr&auml;ge auflisten</a></li>
          </ul>
        </div><!--/.nav-collapse -->
      </div>
    </nav>

    <div class="container">
      <div class="panel panel-default">
        <div class="panel-heading">Eintrag hinzuf&uuml;gen</div>
          <div class="panel-body">
            <form role="form" id="documentForm" action="/data" method="post">
              <div class="form-group"><label for="header">&Uuml;berschrift</label><br/><input class="form-control" type="text" name="header" id="header" value="${header}"/></div>
              <div class="form-group"><label for="message">Inhalt</label><br/><textarea id="message" name="message">${message}</textarea></div>
              <div class="form-group"><label for="tags">Schlagw&ouml;rter</label><br/><input class="form-control" type="text" name="tags" id="tags" value="${tags}"/></div>
              <input class="btn btn-default" type="submit" value="Speichern" />
            </form>
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


