package jetbrains.buildServer.sesPlugin.email

import jetbrains.buildServer.sesPlugin.email.DisabledEMailStateProviderImpl.Companion.DISABLED_PROPERTY_KEY
import jetbrains.buildServer.sesPlugin.email.DisabledEMailStateProviderImpl.Companion.DISABLE_DESCRIPTION_PROPERTY_KEY
import jetbrains.buildServer.sesPlugin.util.check
import jetbrains.buildServer.sesPlugin.util.mock
import jetbrains.buildServer.sesPlugin.util.mocking
import jetbrains.buildServer.users.SUser
import org.assertj.core.api.BDDAssertions.then
import org.jmock.Expectations
import org.testng.annotations.Test

@Test
class DisabledEMailStateProviderImplTest {
    fun testIsDisabled() {
        mocking {
            val disabledUser = mock(SUser::class, "disabledUser")
            val enabledUser = mock(SUser::class, "enabledUser")

            check {
                one(disabledUser).getBooleanProperty(DISABLED_PROPERTY_KEY); will(Expectations.returnValue(true))
                one(enabledUser).getBooleanProperty(DISABLED_PROPERTY_KEY); will(Expectations.returnValue(false))
            }

            val prov = DisabledEMailStateProviderImpl()

            then(prov.isDisabled(disabledUser)).isTrue()
            then(prov.isDisabled(enabledUser)).isFalse()
        }
    }

    fun testDisableDescription() {
        mocking {
            val enabledUser = mock(SUser::class, "enabledUser")
            val disabledUser = mock(SUser::class, "disabledUser")
            val disabledUserWithDescr = mock(SUser::class, "disabledUser_withDescr")

            check {
                one(enabledUser).getPropertyValue(DISABLE_DESCRIPTION_PROPERTY_KEY); will(Expectations.returnValue(null))
                one(enabledUser).getBooleanProperty(DISABLED_PROPERTY_KEY); will(Expectations.returnValue(false))
                one(disabledUser).getPropertyValue(DISABLE_DESCRIPTION_PROPERTY_KEY); will(Expectations.returnValue(null))
                one(disabledUser).getBooleanProperty(DISABLED_PROPERTY_KEY); will(Expectations.returnValue(true))
                one(disabledUserWithDescr).getPropertyValue(DISABLE_DESCRIPTION_PROPERTY_KEY); will(Expectations.returnValue("The reason"))
            }

            val prov = DisabledEMailStateProviderImpl()

            then(prov.getEmailDisableDescription(enabledUser)).isEqualTo("enabled")
            then(prov.getEmailDisableDescription(disabledUser)).isEqualTo("disabled")
            then(prov.getEmailDisableDescription(disabledUserWithDescr)).isEqualTo("The reason")
        }
    }
}