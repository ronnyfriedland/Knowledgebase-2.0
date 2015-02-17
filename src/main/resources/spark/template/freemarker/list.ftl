<html>
    <head>
        <link rel="stylesheet" type="text/css" href="/style.css"/>
    </head>
    <body>
        <h1>Knowledgebase 2.0</h1>
        
        <#list messages as message>
        
        <a href="/data/${message.key}">${message.header}</a>
         <br/>
        
        </#list>
        
        <br/>
        <br/>
        <a href="/">zur Startseite</a>

    </body>
</html>
