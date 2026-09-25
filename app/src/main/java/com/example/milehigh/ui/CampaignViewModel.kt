package com.example.milehigh.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.milehigh.core.AlliancePowerManager
import com.example.milehigh.core.AllianceState
import com.example.milehigh.core.SentinelAuditReport
import com.example.milehigh.core.SentinelSecurity
import com.example.milehigh.data.CampaignDataLoader
import com.example.milehigh.data.CampaignMasterData
import com.example.milehigh.data.CharacterProfile
import com.example.milehigh.data.ObjectInteraction
import com.example.milehigh.data.SceneScenario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

data class CampaignUiState(
    val campaignData: CampaignMasterData? = null,
    val selectedScenarioIndex: Int = 0,
    val selectedCharacterId: String = "char_0",
    val auditReport: SentinelAuditReport? = null,
    val notificationMessage: String? = null,
    val isLoading: Boolean = false
)

class CampaignViewModel(application: Application) : AndroidViewModel(application) {

    private val allianceManager = AlliancePowerManager()
    val allianceState: StateFlow<AllianceState> = allianceManager.state

    private val _uiState = MutableStateFlow(CampaignUiState(isLoading = true))
    val uiState: StateFlow<CampaignUiState> = _uiState.asStateFlow()

    init {
        loadCampaign()
    }

    fun loadCampaign() {
        val data = CampaignDataLoader.loadCampaignData(getApplication())
        val audit = SentinelSecurity.runAudit(data)
        _uiState.update {
            it.copy(
                campaignData = data,
                auditReport = audit,
                isLoading = false,
                notificationMessage = "Campaign Master initialized. Parity 9 verified."
            )
        }
        allianceManager.setVoidSaturation(data.metadata.voidSaturationLevel)
    }

    fun selectScenario(index: Int) {
        val data = _uiState.value.campaignData ?: return
        if (index in data.scenarios.indices) {
            _uiState.update {
                it.copy(
                    selectedScenarioIndex = index,
                    notificationMessage = "Scenario shifted to: ${data.scenarios[index].title}"
                )
            }
        }
    }

    fun selectCharacter(id: String) {
        _uiState.update { it.copy(selectedCharacterId = id) }
    }

    fun updateInteractiveObject(scenarioId: String, objectId: String, newFloat: Float, newX: Float, newY: Float, newZ: Float) {
        val currentData = _uiState.value.campaignData ?: return
        val updatedScenarios = currentData.scenarios.map { scenario ->
            if (scenario.scenarioId == scenarioId) {
                val updatedObjects = scenario.interactiveObjects.map { obj ->
                    if (obj.objectId == objectId) {
                        obj.copy(floatValue = newFloat, x = newX, y = newY, z = newZ)
                    } else obj
                }
                scenario.copy(interactiveObjects = updatedObjects)
            } else scenario
        }

        val updatedData = currentData.copy(scenarios = updatedScenarios)
        val newAudit = SentinelSecurity.runAudit(updatedData)
        _uiState.update {
            it.copy(
                campaignData = updatedData,
                auditReport = newAudit,
                notificationMessage = "Vector state adjusted for object: $objectId"
            )
        }
    }

    // Alliance Actions
    fun adjustSync(delta: Int) = allianceManager.updateSync(delta)
    fun setVoidSaturation(level: Float) = allianceManager.setVoidSaturation(level)
    fun toggleSplitPeel() = allianceManager.executeSplitPeel()
    fun fireTsidkenuStrike(x: Float = 0f, y: Float = 1000f, z: Float = 0f) {
        allianceManager.executeTsidkenuStrike(x, y, z)
    }

    // Character Ability Simulators
    fun triggerMicahKineticRedirection() = allianceManager.triggerKineticRedirection(750f)
    fun triggerOmegaGeminiLogic() = allianceManager.triggerGeminiLogicReconciliation()
    fun triggerAeronAirborneStomp() = allianceManager.executeVoidLightningStomp()
    fun triggerAnastasiaDreamTrance() = allianceManager.toggleReverieDreamHarmony()
    fun triggerDelilahPurification() = allianceManager.attemptPurifyDelilah()

    fun runSentinelAudit() {
        val data = _uiState.value.campaignData ?: return
        val report = SentinelSecurity.runAudit(data)
        _uiState.update {
            it.copy(
                auditReport = report,
                notificationMessage = if (report.isAllPassed) "Sentinel check passed. All dimensional parameters safe." else "Warning: Sentinel detected anomalies."
            )
        }
    }

    fun dismissNotification() {
        _uiState.update { it.copy(notificationMessage = null) }
    }
}
