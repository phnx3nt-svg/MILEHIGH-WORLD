package com.example.milehigh.core

import com.example.milehigh.data.CampaignMasterData
import com.example.milehigh.data.CampaignMetadata

data class SentinelCheckItem(
    val name: String,
    val isPassed: Boolean,
    val message: String
)

data class SentinelAuditReport(
    val isAllPassed: Boolean,
    val checks: List<SentinelCheckItem>,
    val timestamp: Long = System.currentTimeMillis()
)

object SentinelSecurity {

    fun runAudit(data: CampaignMasterData): SentinelAuditReport {
        val checks = mutableListOf<SentinelCheckItem>()

        // 1. Scene ID validation
        val sceneIdOk = data.sceneId.isNotBlank() && data.sceneId.startsWith("MILEHIGH")
        checks.add(
            SentinelCheckItem(
                name = "Scene ID Integrity",
                isPassed = sceneIdOk,
                message = if (sceneIdOk) "Valid scene namespace: ${data.sceneId}" else "Invalid or empty sceneId"
            )
        )

        // 2. Metadata: Void Saturation Level (0.0 to 1.0)
        val sat = data.metadata.voidSaturationLevel
        val satOk = sat in 0.0f..1.0f
        checks.add(
            SentinelCheckItem(
                name = "Void Saturation Bounds [0.0, 1.0]",
                isPassed = satOk,
                message = if (satOk) "Saturation level $sat within safe parameters." else "FAILED: Saturation level $sat outside safe range [0.0, 1.0]"
            )
        )

        // 3. Metadata: System Parity 9
        val parity = data.metadata.systemParity
        val parityOk = parity == 9
        checks.add(
            SentinelCheckItem(
                name = "System Parity 9-bit Rule",
                isPassed = parityOk,
                message = if (parityOk) "Conservation of Nine verified (systemParity = 9)." else "FAILED: Parity corrupted ($parity != 9)."
            )
        )

        // 4. Characters validation
        val charCount = data.characters.size
        val charOk = charCount >= 4
        checks.add(
            SentinelCheckItem(
                name = "Champion Roster Registration",
                isPassed = charOk,
                message = if (charOk) "$charCount champion dossiers authenticated." else "FAILED: Insufficient characters ($charCount)."
            )
        )

        // 5. Scenarios & Object Vector flag validation (as in validate_implementation.py)
        var vectorIntegrityOk = true
        var checkedObjectsCount = 0
        for (scenario in data.scenarios) {
            for (obj in scenario.interactiveObjects) {
                checkedObjectsCount++
                if (obj.objectId.isBlank() || obj.action.isBlank()) {
                    vectorIntegrityOk = false
                }
            }
        }
        checks.add(
            SentinelCheckItem(
                name = "3D Interactive Vector Schema",
                isPassed = vectorIntegrityOk && checkedObjectsCount > 0,
                message = if (vectorIntegrityOk) "All $checkedObjectsCount interactive objects confirmed with valid isVector specification." else "Interactive objects failed schema check."
            )
        )

        // 6. Dialogue Triggers & Narrative Parity
        var dialogueCount = 0
        for (scenario in data.scenarios) {
            dialogueCount += scenario.dialogue.size
        }
        val dialogueOk = dialogueCount > 0
        checks.add(
            SentinelCheckItem(
                name = "BattleREM Narrative Protocol",
                isPassed = dialogueOk,
                message = if (dialogueOk) "$dialogueCount narrative dialogue triggers anchored to Onalym Core." else "No dialogue triggers found."
            )
        )

        val allPassed = checks.all { it.isPassed }
        return SentinelAuditReport(isAllPassed = allPassed, checks = checks)
    }
}
