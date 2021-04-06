<html>

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="Knowledgebase 2.0">
    <meta name="author" content="Ronny Friedland">
    <title>Knowledgebase 2.0</title>
    <link rel="icon" href="/public/images/icon.gif" type="image/gif" />

    <link rel="stylesheet" href="/public/META-INF/resources/webjars/bootstrap/4.6.0/dist/css/bootstrap.min.css" />
    <script src="/public/META-INF/resources/webjars/jquery/3.6.0/dist/jquery.min.js"></script>
    <script src="/public/META-INF/resources/webjars/bootstrap/4.6.0/dist/js/bootstrap.min.js"></script>
</head>

<body role="document">

    <nav class="navbar navbar-expand-lg navbar-light bg-light">
      <div class="container">
        <a class="navbar-brand" href="/">${locale("app.name")}</a>
        <div class="collapse navbar-collapse">
          <ul class="navbar-nav mr-auto">
            <li class="nav-item"><a class="nav-link" href="/documents">${locale("app.menu.documents")}</a></li>
            <li class="nav-item"><a class="nav-link" href="/documents/add">${locale("app.menu.documents.add")}</a></li>
            <li class="nav-item"><a class="nav-link" href="/documents/repository">${locale("app.menu.documents.repository")}</a></li>
          </ul>
        </div><!--/.nav-collapse -->
      </div>
  </nav>

  <div class="container" role="main">

    <div class="alert alert-warning">
      ${locale("app.text.import.hint")}
    </div>

    <div class="card">
      <div class="card-body">
        <div class="card-header bg-light">${locale("app.header.documents.import")}</div>
        <div class="card-footer">
          <form role="form" id="importForm" action="/documents/xml/import" method="post" enctype="multipart/form-data">
            <div class="form-group"><label for="importFile">${locale("app.label.importfile")}</label><br/><input class="form-control" type="file" name="importFile" id="importFile"/></div>
            <hr>
            <div class="form-group"><label for="importXml">${locale("app.label.importtext")}</label><br/><textarea class="form-control" name="importXml" id="importXml" rows="10"></textarea></div>
            <input class="btn btn-primary" type="submit" value="${locale("app.link.import")}" />
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