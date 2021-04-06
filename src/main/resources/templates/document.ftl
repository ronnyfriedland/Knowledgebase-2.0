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

    <script src="/public/META-INF/resources/webjars/ckeditor4/4.16.0/ckeditor.js"></script>
    <script src="/public/META-INF/resources/webjars/jquery/3.6.0/dist/jquery.min.js"></script>
    <script src="/public/META-INF/resources/webjars/jquery-validation/1.19.3/dist/jquery.validate.min.js"></script>
    <script src="/public/META-INF/resources/webjars/bootstrap/4.6.0/dist/js/bootstrap.min.js"></script>
    <script src="/public/knowledgebase.js"></script>

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
                        minlength: 2,
                        maxlength: 1000
                    }
                },
                messages: {
                    header: "&nbsp;<div class='alert alert-danger' role='alert'>${locale("app.error.header.missing")}</div>",
                }
            });
            
            // forward redirect to save resource
            action = jQuery("#documentForm").attr('action');
            redirect = getQueryVariable("redirect", "");
            console.log(action);
            console.log(redirect);
            if("" != header.val()) {
                jQuery("#documentForm").attr('action', action + "?redirect=" + redirect);
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
          <li class="nav-item"><a class="nav-link" href="/documents">${locale("app.menu.documents")}</a></li>
          </ul>
        </div><!--/.nav-collapse -->
      </div>
    </nav>

    <div class="container">
      <div class="card">
        <div class="card-body">
          <div class="card-header bg-light">${locale("app.header.documents.add")}</div>
            <div class="card-footer">
              <form role="form" id="documentForm" action="/documents" method="post">
                <div class="form-group"><label for="header">${locale("app.label.header")}</label><br/><input class="form-control" type="text" name="header" id="header" value="${header}"/></div>
                <div class="form-group"><label for="message">${locale("app.label.content")}</label><br/><textarea id="message" name="message">${message}</textarea></div>
                <div class="form-group"><label for="tags">${locale("app.label.tags")}</label><br/><input class="form-control" type="text" name="tags" id="tags" value="${tags}"/></div>
                <div class="form-group"><label for="encrypted"><input type="checkbox" name="encrypted" id="encrypted" value="true" <#if (encrypted)>checked</#if> />&nbsp;${locale("app.label.encrypt")}</label></div>
                <input class="btn btn-primary" type="submit" value="${locale("app.link.save")}" />
              </form>
            </div>
          </div>
        </div>
      </div>
    <div class="container">
      <footer class="pt-4">
        <p>Version: @project.version@</p>
      </footer>
    </div>
  </body>
</html>


