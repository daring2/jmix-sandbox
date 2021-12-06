package ru.itsyn.jmix.sandbox.screen.user

import ru.itsyn.jmix.sandbox.entity.User
import io.jmix.ui.navigation.Route
import io.jmix.ui.screen.*

@UiController("sb_User.browse")
@UiDescriptor("user-browse.xml")
@LookupComponent("usersTable")
@Route("users")
open class UserBrowse : StandardLookup<User>()