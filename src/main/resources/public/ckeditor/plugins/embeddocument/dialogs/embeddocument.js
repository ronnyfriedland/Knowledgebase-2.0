CKEDITOR.dialog.add('embeddocumentDialog', function(editor) {
    return {
        title : 'Dokument einbetten',
        minWidth : 400,
        minHeight : 200,
        contents : [ {
            id : 'tab-basic',
            label : 'Basic Settings',
            elements : [ {
                type : 'text',
                id : 'link',
                label : 'Dokumentenlink',
                validate : CKEDITOR.dialog.validate
                        .notEmpty("Dokumentenlink darf nicht leer sein.")
            } ]
        } ],
        onOk : function() {
            var link = this.getValueOf('tab-basic', 'link');

            var response = '';
            jQuery.ajax({
                type : "GET",
                url : link + "/raw",
                async : false,
                success : function(text) {
                    response = text;
                }
            });

            editor.insertHtml(response);
        }
    };
});