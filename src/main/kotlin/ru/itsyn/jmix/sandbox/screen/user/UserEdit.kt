package ru.itsyn.jmix.sandbox.screen.user

import ru.itsyn.jmix.sandbox.entity.User
import io.jmix.core.EntityStates
import io.jmix.ui.Notifications
import io.jmix.ui.component.ComboBox
import io.jmix.ui.component.PasswordField
import io.jmix.ui.component.TextField
import io.jmix.ui.navigation.Route
import io.jmix.ui.screen.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.*

@UiController("sb_User.edit")
@UiDescriptor("user-edit.xml")
@EditedEntityContainer("userDc")
@Route(value = "users/edit", parentPrefix = "users")
open class UserEdit : StandardEditor<User>() {

    @Autowired
    private lateinit var entityStates: EntityStates

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    private lateinit var passwordField: PasswordField

    @Autowired
    private lateinit var usernameField: TextField<String>

    @Autowired
    private lateinit var confirmPasswordField: PasswordField

    @Autowired
    private lateinit var notifications: Notifications

    @Autowired
    private lateinit var messageBundle: MessageBundle

    @Autowired
    private lateinit var timeZoneField: ComboBox<String>

    @Subscribe
    fun onInitEntity(event: InitEntityEvent<User>?) {
        usernameField.isEditable = true
        passwordField.isVisible = true
        confirmPasswordField.isVisible = true
    }

    @Subscribe
    fun onAfterShow(event: AfterShowEvent?) {
        if (entityStates.isNew(editedEntity)) {
            usernameField.focus()
        }
    }

    @Subscribe
    protected fun onBeforeCommit(event: BeforeCommitChangesEvent) {
        if (entityStates.isNew(editedEntity)) {
            if (passwordField.value != confirmPasswordField.value) {
                notifications.create(Notifications.NotificationType.WARNING)
                    .withCaption(messageBundle.getMessage("passwordsDoNotMatch") ?: "")
                    .show()
                event.preventCommit()
            }
            editedEntity.password = passwordEncoder.encode(passwordField.value)
        }
    }

    @Subscribe
    open fun onInit(event: InitEvent?) {
        timeZoneField.setOptionsList(listOf(*TimeZone.getAvailableIDs()))
    }
}