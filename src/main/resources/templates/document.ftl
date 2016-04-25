<html>
  <head>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <meta name="description" content="Knowledgebase 2.0"/>
    <meta name="author" content="Ronny Friedland"/>
    <title>Knowledgebase 2.0</title>
    <link rel="icon" href="/public/images/icon.gif" type="image/gif" />

    <link rel="stylesheet" href="/public/bootstrap.min.css"/>
    <link rel="stylesheet" href="/public/bootstrap-theme.min.css"/>
    <script src="/public/jquery-1.11.2.min.js"></script>
    <script src="/public/jquery.validate.min.js"></script>
    <script src="/public/bootstrap.min.js"></script>
    <script src="/public/ckeditor/ckeditor.js"></script>
    <script type="text/javascript">
        jQuery( document ).ready(function() {
            CKEDITOR.replace( 'message', {
                language: '${locale("app.language")}',
            });

            CKEDITOR.on('dialogDefinition', function (ev) {
                var dialogName = ev.data.name;
                var dialogDefinition = ev.data.definition;
                var dialog = dialogDefinition.dialog;
                var editor = ev.editor;
            
                if (dialogName == 'image') {
                    dialogDefinition.onOk = function (e) {
                        var imageSrcUrl = e.sender.originalElement.$.src;
                        var width = e.sender.originalElement.$.width;
                        var height = e.sender.originalElement.$.height;
                        var imgHtml = CKEDITOR.dom.element.createFromHtml(imageSrcUrl);
                        editor.insertElement(imgHtml);
                    };
                }
            });

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
                    header: "&nbsp;<div class='alert alert-danger' role='alert'>${locale("app.error.header.missing")}</div>",
                }
            });
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
          <li><a href="/documents">${locale("app.menu.list")}</a></li>
          </ul>
        </div><!--/.nav-collapse -->
      </div>
    </nav>

    <div class="container">
      <div class="panel panel-default">
        <div class="panel-heading">${locale("app.header.add")}</div>
          <div class="panel-body">
            <form role="form" id="documentForm" action="/documents" method="post">
              <div class="form-group"><label for="header">${locale("app.label.header")}</label><br/><input class="form-control" type="text" name="header" id="header" value="${header}"/></div>
              <div class="form-group"><label for="message">${locale("app.label.content")}</label><br/><textarea id="message" name="message">${message}</textarea></div>
              <div class="form-group"><label for="tags">${locale("app.label.tags")}</label><br/><input class="form-control" type="text" name="tags" id="tags" value="${tags}"/></div>
              <div class="form-group"><label for="encrypted"><input type="checkbox" name="encrypted" id="encrypted" value="true" <#if (encrypted)>checked</#if> />&nbsp;${locale("app.label.encrypt")}</label></div>
              <input class="btn btn-default" type="submit" value="${locale("app.link.save")}" />
            </form>
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


