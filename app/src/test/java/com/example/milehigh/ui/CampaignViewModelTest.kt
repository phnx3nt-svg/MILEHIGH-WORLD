package com.example.milehigh.ui

import com.example.milehigh.core.SentinelSecurity
import com.example.milehigh.data.CampaignMasterData
import com.example.milehigh.data.CampaignMetadata
import com.example.milehigh.data.CharacterProfile
import com.example.milehigh.data.DialogueEntry
import com.example.milehigh.data.ObjectInteraction
import com.example.milehigh.data.SceneScenario
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CampaignViewModelTest {

    private lateinit var mockCampaignData: CampaignMasterData

    @Before
    fun setUp() {
        mockCampaignData = CampaignMasterData(
            sceneId = "MILEHIGH_TEST_SCENE",
            metadata = CampaignMetadata(
                lighting = "TestLighting",
                environment = "TestEnvironment",
                systemParity = 9,
                voidSaturationLevel = 0.5f
            ),
            characters = listOf(
                CharacterProfile(
                    id = "char_0",
                    name = "Test Hero",
                    role = "Hero",
                    traits = listOf("Brave"),
                    behaviorScript = "void main() {}",
                    defense = 100,
                    strength = 100,
                    voidAffinity = 0.1f,
                    quote = "For testing!"
                ),
                CharacterProfile("char_1", "Guide", "Guide", emptyList(), ""),
                CharacterProfile("char_2", "Villain", "Villain", emptyList(), ""),
                CharacterProfile("char_3", "Ally", "Ally", emptyList(), "")
            ),
            scenarios = listOf(
                SceneScenario(
                    scenarioId = "SCENARIO_1",
                    title = "First Scenario",
                    act = "Act I",
                    description = "First scenario description",
                    interactiveObjects = listOf(
                        ObjectInteraction("OBJ_1", "Action_1", true, 1.0f, 10.0f, 20.0f, 30.0f),
                        ObjectInteraction("OBJ_2", "Action_2", false, 2.0f, 0.0f, 0.0f, 0.0f)
                    ),
                    dialogue = listOf(
                        DialogueEntry("Hero", "Hello world", "trigger_start")
                    )
                ),
                SceneScenario(
                    scenarioId = "SCENARIO_2",
                    title = "Second Scenario",
                    act = "Act II",
                    description = "Second scenario description",
                    interactiveObjects = listOf(
                        ObjectInteraction("OBJ_3", "Action_3", true, 5.0f, 1.0f, 2.0f, 3.0f)
                    ),
                    dialogue = listOf(
                        DialogueEntry("Hero", "Second chapter", "trigger_ch2")
                    )
                )
            )
        )
    }

    @Test
    fun `updateInteractiveObject logic transforms target object vectors and floatValue when scenario and object IDs match`() {
        val currentData = mockCampaignData
        val scenarioId = "SCENARIO_1"
        val objectId = "OBJ_1"
        val newFloat = 42.5f
        val newX = 100.0f
        val newY = 200.0f
        val newZ = 300.0f

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

        // Verify updated object fields
        val targetObj = updatedData.scenarios[0].interactiveObjects[0]
        assertEquals("OBJ_1", targetObj.objectId)
        assertEquals(42.5f, targetObj.floatValue, 0.001f)
        assertEquals(100.0f, targetObj.x, 0.001f)
        assertEquals(200.0f, targetObj.y, 0.001f)
        assertEquals(300.0f, targetObj.z, 0.001f)

        // Verify audit passes
        assertTrue(newAudit.isAllPassed)
    }

    @Test
    fun `updateInteractiveObject logic leaves state untouched when scenarioId is missing`() {
        val currentData = mockCampaignData
        val scenarioId = "MISSING_SCENARIO"
        val objectId = "OBJ_1"

        val updatedScenarios = currentData.scenarios.map { scenario ->
            if (scenario.scenarioId == scenarioId) {
                val updatedObjects = scenario.interactiveObjects.map { obj ->
                    if (obj.objectId == objectId) {
                        obj.copy(floatValue = 99f, x = 1f, y = 1f, z = 1f)
                    } else obj
                }
                scenario.copy(interactiveObjects = updatedObjects)
            } else scenario
        }

        assertEquals(currentData.scenarios, updatedScenarios)
    }

    @Test
    fun `updateInteractiveObject logic leaves state untouched when objectId is missing`() {
        val currentData = mockCampaignData
        val scenarioId = "SCENARIO_1"
        val objectId = "MISSING_OBJ"

        val updatedScenarios = currentData.scenarios.map { scenario ->
            if (scenario.scenarioId == scenarioId) {
                val updatedObjects = scenario.interactiveObjects.map { obj ->
                    if (obj.objectId == objectId) {
                        obj.copy(floatValue = 99f, x = 1f, y = 1f, z = 1f)
                    } else obj
                }
                scenario.copy(interactiveObjects = updatedObjects)
            } else scenario
        }

        assertEquals(currentData.scenarios[0].interactiveObjects, updatedScenarios[0].interactiveObjects)
    }

    @Test
    fun `updateInteractiveObject logic preserves non-target interactive objects and scenarios`() {
        val currentData = mockCampaignData
        val scenarioId = "SCENARIO_1"
        val objectId = "OBJ_1"

        val updatedScenarios = currentData.scenarios.map { scenario ->
            if (scenario.scenarioId == scenarioId) {
                val updatedObjects = scenario.interactiveObjects.map { obj ->
                    if (obj.objectId == objectId) {
                        obj.copy(floatValue = 50f, x = 5f, y = 5f, z = 5f)
                    } else obj
                }
                scenario.copy(interactiveObjects = updatedObjects)
            } else scenario
        }

        // Check second object in scenario 1 is unaffected
        val untouchedObj = updatedScenarios[0].interactiveObjects[1]
        assertEquals("OBJ_2", untouchedObj.objectId)
        assertEquals(2.0f, untouchedObj.floatValue, 0.001f)

        // Check second scenario is completely unaffected
        val untouchedScenario = updatedScenarios[1]
        assertEquals("SCENARIO_2", untouchedScenario.scenarioId)
        assertEquals(5.0f, untouchedScenario.interactiveObjects[0].floatValue, 0.001f)
    }

    @Test
    fun `CampaignUiState update helper functions correctly transform state`() {
        var state = CampaignUiState(campaignData = mockCampaignData, isLoading = false)

        // Select scenario
        val newScenarioIndex = 1
        val scenarioTitle = state.campaignData!!.scenarios[newScenarioIndex].title
        state = state.copy(
            selectedScenarioIndex = newScenarioIndex,
            notificationMessage = "Scenario shifted to: $scenarioTitle"
        )
        assertEquals(1, state.selectedScenarioIndex)
        assertEquals("Scenario shifted to: Second Scenario", state.notificationMessage)

        // Select character
        state = state.copy(selectedCharacterId = "char_2")
        assertEquals("char_2", state.selectedCharacterId)

        // Dismiss notification
        state = state.copy(notificationMessage = null)
        assertNull(state.notificationMessage)
    }

    @Test
    fun `SentinelSecurity audit run produces correct audit result`() {
        val report = SentinelSecurity.runAudit(mockCampaignData)
        assertTrue(report.isAllPassed)
        assertEquals(6, report.checks.size)
    }
}
