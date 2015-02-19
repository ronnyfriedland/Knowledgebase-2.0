<html>
    <head>
        <link rel="stylesheet" type="text/css" href="/style.css"/>
    </head>
    <body>
        <h1>Knowledgebase 2.0</h1>
        
        <div class="container button_big">
            <a href="/">zur Startseite</a>
        </div>
        <div class="container button_big">
            <a href="/data/add">Eintrag hinzuf&uuml;gen</a>
        </div>

        <br/><br/>

        <div class="container">
            <#list messages as message>
            
            <div class="container">
                <a href="/data/${message.key}">${message.header}</a>
            </div>
            <br/>
        
            </#list>
    
        </div>
        
    </body>
</html>
