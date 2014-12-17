function (doc, meta) {
    if(doc._class == "com.techlooper.entity.UserEntity" ) {    emit(doc.key, null);
    }
}
