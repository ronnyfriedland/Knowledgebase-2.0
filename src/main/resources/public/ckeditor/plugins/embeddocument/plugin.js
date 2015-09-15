CKEDITOR.plugins.add( 'embeddocument', {
    icons: 'embeddocument',
    init: function( editor ) {
        editor.addCommand( 'embeddocument', new CKEDITOR.dialogCommand( 'embeddocumentDialog' ) );
        editor.ui.addButton( 'Embeddocument', {
            label: 'Dokument einbetten',
            command: 'embeddocument',
            toolbar: 'insert'
        });

        CKEDITOR.dialog.add( 'embeddocumentDialog', this.path + 'dialogs/embeddocument.js' );
    }
});