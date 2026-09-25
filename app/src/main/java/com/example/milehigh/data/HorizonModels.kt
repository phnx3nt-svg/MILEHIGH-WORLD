package com.example.milehigh.data

import org.json.JSONArray
import org.json.JSONObject

data class CampaignMetadata(
    val lighting: String,
    val environment: String,
    val systemParity: Int,
    val voidSaturationLevel: Float
) {
    fun isValid(): Boolean {
        return voidSaturationLevel in 0.0f..1.0f && systemParity == 9
    }
}

data class CharacterProfile(
    val id: String,
    val name: String,
    val role: String,
    val traits: List<String>,
    val behaviorScript: String,
    val defense: Int = 100,
    val strength: Int = 100,
    val voidAffinity: Float = 0.1f,
    val quote: String = ""
)

data class ObjectInteraction(
    val objectId: String,
    val action: String,
    val isVector: Boolean,
    var floatValue: Float = 0.0f,
    var x: Float = 0.0f,
    var y: Float = 0.0f,
    var z: Float = 0.0f
)

data class DialogueEntry(
    val speaker: String,
    val text: String,
    val trigger: String
)

data class SceneScenario(
    val scenarioId: String,
    val title: String,
    val act: String,
    val description: String,
    val interactiveObjects: List<ObjectInteraction>,
    val dialogue: List<DialogueEntry>
)

data class CampaignMasterData(
    val sceneId: String,
    val metadata: CampaignMetadata,
    val characters: List<CharacterProfile>,
    val scenarios: List<SceneScenario>
)
