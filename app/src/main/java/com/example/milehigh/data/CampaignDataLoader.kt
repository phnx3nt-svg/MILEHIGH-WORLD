package com.example.milehigh.data

import android.content.Context
import org.json.JSONObject
import java.io.InputStreamReader

object CampaignDataLoader {

    fun loadCampaignData(context: Context): CampaignMasterData {
        return try {
            val inputStream = context.assets.open("campaign_master.json")
            val jsonString = inputStream.bufferedReader().use { it.readText() }
            parseJson(jsonString)
        } catch (e: Exception) {
            e.printStackTrace()
            getFallbackCampaignData()
        }
    }

    fun parseJson(jsonString: String): CampaignMasterData {
        val root = JSONObject(jsonString)
        val sceneId = root.optString("sceneId", "MILEHIGH_INTO_THE_VOID_CAMPAIGN")

        val metaObj = root.optJSONObject("metadata") ?: JSONObject()
        val metadata = CampaignMetadata(
            lighting = metaObj.optString("lighting", "Dynamic"),
            environment = metaObj.optString("environment", "The Verse - Multiversal Nexus to The Void"),
            systemParity = metaObj.optInt("systemParity", 9),
            voidSaturationLevel = metaObj.optDouble("voidSaturationLevel", 0.0).toFloat()
        )

        val charactersList = mutableListOf<CharacterProfile>()
        val charactersArray = root.optJSONArray("characters")
        if (charactersArray != null) {
            for (i in 0 until charactersArray.length()) {
                val cObj = charactersArray.getJSONObject(i)
                val traitsArray = cObj.optJSONArray("traits")
                val traits = mutableListOf<String>()
                if (traitsArray != null) {
                    for (t in 0 until traitsArray.length()) {
                        traits.add(traitsArray.getString(t))
                    }
                }

                val name = cObj.optString("name", "Unknown Champion")
                val role = cObj.optString("role", "Combatant")
                val behavior = cObj.optString("behaviorScript", "// Autonomous Script")

                // Extract traits into stats
                var defense = 200
                var strength = 200
                var voidAffinity = 0.2f
                var quote = ""

                when {
                    name.contains("Micah", ignoreCase = true) -> {
                        defense = 500
                        strength = 320
                        voidAffinity = 0.05f
                        quote = "Stand behind me! The earth answers my call."
                    }
                    name.contains("Omega", ignoreCase = true) -> {
                        defense = 280
                        strength = 180
                        voidAffinity = 0.15f
                        quote = "Alliance synchronization at 100%. Initiating Split Peel..."
                    }
                    name.contains("Cyrus", ignoreCase = true) -> {
                        defense = 650
                        strength = 700
                        voidAffinity = 0.95f
                        quote = "You are a technological aberration, an error in my new order."
                    }
                    name.contains("Aeron", ignoreCase = true) -> {
                        defense = 240
                        strength = 450
                        voidAffinity = 0.30f
                        quote = "From the skies, we strike like lightning!"
                    }
                    name.contains("Anastasia", ignoreCase = true) -> {
                        defense = 180
                        strength = 300
                        voidAffinity = 0.45f
                        quote = "Dreams hold the fragments of reality."
                    }
                    name.contains("Delilah", ignoreCase = true) -> {
                        defense = 480
                        strength = 580
                        voidAffinity = 0.88f
                        quote = "The void devours all memories... unless you remind me."
                    }
                }

                charactersList.add(
                    CharacterProfile(
                        id = "char_$i",
                        name = name,
                        role = role,
                        traits = traits,
                        behaviorScript = behavior,
                        defense = defense,
                        strength = strength,
                        voidAffinity = voidAffinity,
                        quote = quote
                    )
                )
            }
        }

        val scenariosList = mutableListOf<SceneScenario>()
        val scenariosArray = root.optJSONArray("scenarios")
        if (scenariosArray != null) {
            for (s in 0 until scenariosArray.length()) {
                val sObj = scenariosArray.getJSONObject(s)
                val scenarioId = sObj.optString("scenarioId", "SCENARIO_$s")
                val description = sObj.optString("description", "")

                val (act, title) = parseScenarioId(scenarioId)

                val interactiveObjects = mutableListOf<ObjectInteraction>()
                val objectsArray = sObj.optJSONArray("interactiveObjects")
                if (objectsArray != null) {
                    for (o in 0 until objectsArray.length()) {
                        val oObj = objectsArray.getJSONObject(o)
                        interactiveObjects.add(
                            ObjectInteraction(
                                objectId = oObj.optString("objectId", "Object_$o"),
                                action = oObj.optString("action", "Trigger"),
                                isVector = oObj.optBoolean("isVector", false),
                                floatValue = oObj.optDouble("floatValue", 0.0).toFloat(),
                                x = oObj.optDouble("x", 0.0).toFloat(),
                                y = oObj.optDouble("y", 0.0).toFloat(),
                                z = oObj.optDouble("z", 0.0).toFloat()
                            )
                        )
                    }
                }

                val dialogueList = mutableListOf<DialogueEntry>()
                val dialogueArray = sObj.optJSONArray("dialogue")
                if (dialogueArray != null) {
                    for (d in 0 until dialogueArray.length()) {
                        val dObj = dialogueArray.getJSONObject(d)
                        dialogueList.add(
                            DialogueEntry(
                                speaker = dObj.optString("speaker", "Narrator"),
                                text = dObj.optString("text", ""),
                                trigger = dObj.optString("trigger", "")
                            )
                        )
                    }
                }

                scenariosList.add(
                    SceneScenario(
                        scenarioId = scenarioId,
                        title = title,
                        act = act,
                        description = description,
                        interactiveObjects = interactiveObjects,
                        dialogue = dialogueList
                    )
                )
            }
        }

        return CampaignMasterData(
            sceneId = sceneId,
            metadata = metadata,
            characters = if (charactersList.isEmpty()) getFallbackCharacters() else charactersList,
            scenarios = if (scenariosList.isEmpty()) getFallbackScenarios() else scenariosList
        )
    }

    private fun parseScenarioId(id: String): Pair<String, String> {
        return when (id) {
            "ACT_I_CH1_ONALYM_NEXUS" -> "Act I · Chapter 1" to "Onalym Nexus"
            "ACT_I_CH2_THE_CRASH_SITE" -> "Act I · Chapter 2" to "The Crash Site"
            "ACT_II_CH3_AETHYLGARD_FJORDS" -> "Act II · Chapter 3" to "Aethylgard Fjords"
            "ACT_III_CH5_CONCORD_PURIFICATION" -> "Act III · Chapter 5" to "Concord Purification"
            "ACT_IV_MMO_THE_GRAND_PURPOSE" -> "Act IV · Final Raid" to "The Grand Purpose"
            else -> "Act I" to id.replace("_", " ")
        }
    }

    fun getFallbackCampaignData(): CampaignMasterData {
        return CampaignMasterData(
            sceneId = "MILEHIGH_INTO_THE_VOID_CAMPAIGN",
            metadata = CampaignMetadata(
                lighting = "Dynamic",
                environment = "The Verse - Multiversal Nexus to The Void",
                systemParity = 9,
                voidSaturationLevel = 0.0f
            ),
            characters = getFallbackCharacters(),
            scenarios = getFallbackScenarios()
        )
    }

    private fun getFallbackCharacters(): List<CharacterProfile> {
        return listOf(
            CharacterProfile(
                id = "char_0",
                name = "Micah the Unbreakable",
                role = "Tank / Earthshaper",
                traits = listOf("Base_Defense_500", "Low_Void_Affinity", "Reactive_Hardening"),
                behaviorScript = "function update(incomingForce, attackType) {\n    if (attackType === 'rigid') {\n        redirectKineticForce(incomingForce);\n        FlowMeter.increment(15);\n    }\n    if (health.tookDamage) {\n        Shader.setFloat('_GauntletExtrusion', 2.5);\n    }\n}",
                defense = 500,
                strength = 320,
                voidAffinity = 0.05f,
                quote = "Stand behind me! The earth answers my call."
            ),
            CharacterProfile(
                id = "char_1",
                name = "Omega.one (The Architect)",
                role = "AI Companion / Astronaut Guide",
                traits = listOf("High_Reasoning_Calculations", "Gemini_Logic", "Data_Integrity_Scanner"),
                behaviorScript = "function checkStability(environment) {\n    if (player.StabilityIndex < 0.3) {\n        runGeminiLogic();\n        VoidPtr.reconcileCorruptedFiles(environment.nearestShard);\n    }\n}",
                defense = 280,
                strength = 180,
                voidAffinity = 0.15f,
                quote = "Alliance synchronization at 100%. Initiating Split Peel..."
            ),
            CharacterProfile(
                id = "char_2",
                name = "King Cyrus the Dragon King",
                role = "Antagonist / Void Anchor",
                traits = listOf("Brute_Force", "Void_Corrupted", "Shadow_Dominion_Ruler"),
                behaviorScript = "function executeInvasion() {\n    target = 'Onalym Nexus';\n    unleashVoidShockwave();\n    overwriteCoreLogic();\n}",
                defense = 650,
                strength = 700,
                voidAffinity = 0.95f,
                quote = "You are a technological aberration, an error in my new order."
            ),
            CharacterProfile(
                id = "char_3",
                name = "Aeron the Brave",
                role = "Winged Sentinel / Aerial Combatant",
                traits = listOf("Base_Strength_450", "Ginga_Stance_Airborne", "Kinetic_Peak_Native"),
                behaviorScript = "function aerialCombat() {\n    if (player.isFlappingArms || thrusters.active) {\n        maintainAltitude();\n        if (FlowMeter.isFull) {\n            executeVoidLightningStomp();\n        }\n    }\n}",
                defense = 240,
                strength = 450,
                voidAffinity = 0.30f,
                quote = "From the skies, we strike like lightning!"
            ),
            CharacterProfile(
                id = "char_4",
                name = "Anastasia the Dreamer",
                role = "Psychic Healer / CC",
                traits = listOf("Dream_Harmony", "Sanity_Parity_Check", "Psychic_Weaver"),
                behaviorScript = "function processDreamscape() {\n    if (geometry.state === 'brittle' || geometry.state === 'gray') {\n        snapMemoryFragments();\n    }\n    commandReverie('toggle_form');\n}",
                defense = 180,
                strength = 300,
                voidAffinity = 0.45f,
                quote = "Dreams hold the fragments of reality."
            ),
            CharacterProfile(
                id = "char_5",
                name = "Delilah the Desolate (Corrupted Ingris)",
                role = "Boss / Avatar of Decay",
                traits = listOf("Voidfire_Attacks", "Illusion_Controller", "Shadow_Clones"),
                behaviorScript = "function bossPhase() {\n    spawnShadowClones();\n    castSicklyGreenBlackVoidfire();\n    if (hasStatus('Blinding_Truth') && hasStatus('Dream_Harmony')) {\n        implodeVoid();\n        restoreIngris();\n    }\n}",
                defense = 480,
                strength = 580,
                voidAffinity = 0.88f,
                quote = "The void devours all memories... unless you remind me."
            )
        )
    }

    private fun getFallbackScenarios(): List<SceneScenario> {
        return listOf(
            SceneScenario(
                scenarioId = "ACT_I_CH1_ONALYM_NEXUS",
                title = "Onalym Nexus",
                act = "Act I · Chapter 1",
                description = "The Catalyst: King Cyrus invades the rain-slicked, neon heart of ŁĪƝĈ, destabilizing the Onalym Nexus and scattering the dimensions.",
                interactiveObjects = listOf(
                    ObjectInteraction("DurasteelFloor", "ShatterAndExtrude_EarthBarrier", true, 0f, 0f, 15.5f, 0f),
                    ObjectInteraction("VoidShockwave", "ExpandRadius_ScaleFactor", false, 500.0f, 0f, 0f, 0f),
                    ObjectInteraction("NexusPlatform_01", "DimensionalShift_XYZ", true, 0f, 100f, -50f, 200f)
                ),
                dialogue = listOf(
                    DialogueEntry("King Cyrus", "You are a technological aberration, an error in my new order.", "Encounter_Omega_One")
                )
            ),
            SceneScenario(
                scenarioId = "ACT_I_CH2_THE_CRASH_SITE",
                title = "The Crash Site",
                act = "Act I · Chapter 2",
                description = "The Void Beckons: Omega.one crash-lands in the 'Place Between Worlds'. Use holographic tools to locate Shard Memories and repair the Stability Index.",
                interactiveObjects = listOf(
                    ObjectInteraction("Kepler186f_Wreckage", "EmitSmoke_Scale", false, 3.5f, 0f, 0f, 0f),
                    ObjectInteraction("CorruptedData_TearsOfFire", "SpawnShadowCreature_XYZ", true, 0f, 12.5f, 0f, -8.2f),
                    ObjectInteraction("VoidDistort_Shader", "SetChromaticAberration_Intensity", false, 0.85f, 0f, 0f, 0f)
                ),
                dialogue = listOf(
                    DialogueEntry("Lucent the Lightweaver", "The Game has begun.", "Phoenix_Flyover")
                )
            ),
            SceneScenario(
                scenarioId = "ACT_II_CH3_AETHYLGARD_FJORDS",
                title = "Aethylgard Fjords",
                act = "Act II · Chapter 3",
                description = "Blood and Ash: Aerial VR combat against Kane, the Lava Demon. Players must utilize thermal updrafts and stay within Lyra's Solar Barrier.",
                interactiveObjects = listOf(
                    ObjectInteraction("Lyra_SolarBarrier", "SetAuraRadius", false, 25.0f, 0f, 0f, 0f),
                    ObjectInteraction("ThermalUpdraft_01", "ApplyVerticalVelocity", true, 0f, 0f, 45.0f, 0f)
                ),
                dialogue = listOf(
                    DialogueEntry("Lyra the Sun Stone", "Strength lies in the synergy of light, Aeron, not the rigidity of iron!", "Kane_LavaStrike_Parried")
                )
            ),
            SceneScenario(
                scenarioId = "ACT_III_CH5_CONCORD_PURIFICATION",
                title = "Concord Purification",
                act = "Act III · Chapter 5",
                description = "The Siege of the Soul: Dynamic Multi-Character control at Concord's spires. Zaia and Anastasia collaborate to purge the Void from Delilah the Desolate.",
                interactiveObjects = listOf(
                    ObjectInteraction("Delilah_ShadowClone", "SpawnAt_XYZ", true, 0f, -20.0f, 5.0f, 15.0f),
                    ObjectInteraction("Voidfire_Projectile", "Reflect_VelocityMultiplier", false, -1.5f, 0f, 0f, 0f)
                ),
                dialogue = listOf(
                    DialogueEntry("Zaia the Just", "Reveal the True Code!", "Cast_Unerring_Gaze")
                )
            ),
            SceneScenario(
                scenarioId = "ACT_IV_MMO_THE_GRAND_PURPOSE",
                title = "The Grand Purpose",
                act = "Act IV · Final Raid",
                description = "Confronting Era: 10-player cooperative raid at the Onalym Core to execute the TSIDKENU strike and synthesize GAMMA.PRIME.",
                interactiveObjects = listOf(
                    ObjectInteraction("Era_SwirlingVortex", "SetCorruptionMeter", false, 510.0f, 0f, 0f, 0f),
                    ObjectInteraction("Global_Lighting", "ShiftToRadiantMillenia_Intensity", false, 5.0f, 0f, 0f, 0f),
                    ObjectInteraction("TSIDKENU_LightningStrike", "StrikeCoordinate_XYZ", true, 0f, 0.0f, 1000.0f, 0.0f)
                ),
                dialogue = listOf(
                    DialogueEntry("Omega.one", "Alliance synchronization at 100%. Initiating Split Peel...", "Synchronized_Ginga_Success")
                )
            )
        )
    }
}
