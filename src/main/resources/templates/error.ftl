<html>

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="Knowledgebase 2.0">
    <meta name="author" content="Ronny Friedland">
    <title>Knowledgebase 2.0</title>
    <link rel="icon" href="/public/images/icon.gif" type="image/gif" />

    <link rel="stylesheet" href="/public/bootstrap.min.css">
    <link rel="stylesheet" href="/public/bootstrap-theme.min.css">
    <script src="/public/jquery-1.11.2.min.js"></script>
    <script src="/public/bootstrap.min.js"></script>
</head>

<body role="document">
  <nav class="navbar navbar-default">
    <div class="container">
      <div class="navbar-header">
        <a class="navbar-brand" href="/">${locale("app.name")}</a>
      </div>
      <div class="navbar-collapse">
        <ul class="nav navbar-nav">
        </ul>
      </div><!--/.nav-collapse -->
    </div>
  </nav>

  <div class="container" role="main">
  
    <div class="panel panel-danger">
      <div class="panel-heading">${status}</div>
      <div class="panel-body">

      ${error}

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