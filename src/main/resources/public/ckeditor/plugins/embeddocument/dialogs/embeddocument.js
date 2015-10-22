CKEDITOR.dialog.add('embeddocumentDialog', function(editor) {
    return {
        title : editor.lang.embeddocument.title,
        minWidth : 400,
        minHeight : 200,
        contents : [ {
            id : 'tab-basic',
            label : 'Basic Settings',
            elements : [ {
                type : 'text',
                id : 'link',
                label : editor.lang.embeddocument.label.link,
                validate : CKEDITOR.dialog.validate
                        .notEmpty(editor.lang.embeddocument.label.error)
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