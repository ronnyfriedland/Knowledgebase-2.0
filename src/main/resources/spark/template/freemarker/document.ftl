<html>
    <head>
        <link rel="stylesheet" type="text/css" href="/style.css"/>
        <script src="/jquery-1.11.2.min.js"></script>
        <script src="/jquery.validate.js"></script>
        <script type="text/javascript" src="/openwysiwyg/scripts/wysiwyg.js"></script>
        <script type="text/javascript" src="/openwysiwyg/scripts/wysiwyg-settings.js"></script>
        <script type="text/javascript">
            jQuery( document ).ready(function() {
                WYSIWYG.attach('message');
    
                // validate signup form on keyup and submit
                jQuery("#documentForm").validate({
                    rules: {
                        header: {
                            required: true,
                            minlength: 2
                        }
                    },
                    messages: {
                        header: "Bitte einen Titel vergeben",
                    }
                });
            });
        </script>
    </head>

    <body>
        <h1>Knowledgebase 2.0</h1>
        <div>
    
            <form id="documentForm" action="/data" method="post">
                <label for="header">Titel</label><br/><input type="text" name="header" id="header" value="${header}"/><br/>
                <label for="message">Inhalt</label><br/><textarea id="message" name="message">${message}</textarea><br/>
                <label for="tags">Schlagw&ouml;rter</label><br/><input type="text" name="tags" id="tags" value="${tags}"/><br/>
                <input type="submit" value="Speichern" />
            </form>

            <a href="/data">zur&uumlck zur &Uuml;bersicht</a>
            
        </div>
    </body>
</html>


