<html>
    <head>
        <link rel="stylesheet" type="text/css" href="/style.css"/>
        
        <script src="/jquery-1.11.2.min.js"></script>
        <script type="text/javascript">
            function getQueryVariable(variable, def) {
                var query = window.location.search.substring(1);
                var vars = query.split('&');
                for (var i = 0; i < vars.length; i++) {
                    var pair = vars[i].split('=');
                    if (decodeURIComponent(pair[0]) == variable) {
                        return decodeURIComponent(pair[1]);
                    }
                }
                return def;
            }

            var limit = new Number(getQueryVariable('limit', '10'));

            var load = function () {
                limit = limit + 10;
                window.location.href='/data?limit='+limit+'&offset=0';
            };
        </script
    </head>
    <body>
        <h1>Knowledgebase 2.0</h1>
        
        <a href="/"><div class="container button_big">zur Startseite</div></a>
        <a href="/data/add"><div class="container button_big">Eintrag hinzuf&uuml;gen</div></a>

        <br/>
        <p>Vorhandene Eintr&auml;ge</p>
        <br/>

        <#if (messages?size > 0) >
            <div class="container scroll">
                <#list messages as message>
                <a href="/data/${message.key}"><div class="button">${message.header}</div></a>
                <br/>
            
                </#list>

                <a onClick="javascript:load();">mehr</a>
            </div>
        </#if>

    </body>
</html>
