package jetbrains.buildServer.sesPlugin.teamcity.ui.ajax

import jetbrains.buildServer.controllers.BasePropertiesBean
import jetbrains.buildServer.sesPlugin.data.AjaxRequestResult
import jetbrains.buildServer.sesPlugin.sqs.SQSConnectionChecker
import jetbrains.buildServer.sesPlugin.teamcity.SESIntegrationManager

class CheckAjaxRequest(private val sqsMessagesReceiver: SQSConnectionChecker,
                       private val sesIntegrationManager: SESIntegrationManager) : AjaxRequest {

    override val id: String = "check"

    override fun handle(data: BasePropertiesBean): AjaxRequestResult {
        val bean = sesIntegrationManager.createFrom(data.properties)
        // todo validate?

        return try {
            val checkConnectionResult = sqsMessagesReceiver.checkConnection(bean)

            AjaxRequestResult(checkConnectionResult.status, checkConnectionResult.description, checkConnectionResult.exception)
        } catch (e: Exception) {
            AjaxRequestResult(false, "Error occurred: ${e.message}", e)
        }

    }
}