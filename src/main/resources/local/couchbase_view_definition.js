// query user by email address view
function (doc, meta) {
    if (doc._class == "com.techlooper.entity.UserEntity") {
        emit(doc.emailAddress, null);
    }
}
