CKEDITOR.plugins.add( 'embeddocument', {
    lang : ['en','de'],
    icons: 'embeddocument',
    init: function( editor ) {
        editor.addCommand( 'embeddocument', new CKEDITOR.dialogCommand( 'embeddocumentDialog' ) );
        editor.ui.addButton( 'Embeddocument', {
            label: editor.lang.embeddocument.title,
            command: 'embeddocument',
            toolbar: 'insert'
        });

        CKEDITOR.dialog.add( 'embeddocumentDialog', this.path + 'dialogs/embeddocument.js' );
    }
});
