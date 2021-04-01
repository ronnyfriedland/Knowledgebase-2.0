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
          </ul>
        </div><!--/.nav-collapse -->
      </div>
    </nav>

  <div class="container" role="main">
  
    <div class="card">
     <div class="card-body">
      <div class="card-header bg-light">${status}</div>
      <div class="card-text">

      ${error}

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