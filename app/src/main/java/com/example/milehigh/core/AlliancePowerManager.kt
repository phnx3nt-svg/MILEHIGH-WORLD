package com.example.milehigh.core

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.abs

data class AllianceState(
    val synchronization: Int = 85,          // 0 to 100%
    val voidSaturation: Float = 0.0f,       // 0.0 to 1.0
    val systemParity: Int = 9,              // 9-bit Parity / Conservation of Nine
    val isSplitPeelActive: Boolean = false,
    val isTsidkenuPrimed: Boolean = false,
    val lastActionReport: String = "Alliance idle. Standing by Onalym Core.",
    val activeStatuses: Set<String> = setOf("Reactive_Hardening"),
    val flowMeter: Int = 45,                // 0 to 100
    val gauntletExtrusion: Float = 1.0f,
    val stabilityIndex: Float = 0.88f,
    val ingrisRestored: Boolean = false,
    val totalStrikesExecuted: Int = 0
)

class AlliancePowerManager {

    private val _state = MutableStateFlow(AllianceState())
    val state: StateFlow<AllianceState> = _state.asStateFlow()

    fun updateSync(delta: Int) {
        _state.update { current ->
            val newSync = (current.synchronization + delta).coerceIn(0, 100)
            val primed = newSync >= 100
            current.copy(
                synchronization = newSync,
                isTsidkenuPrimed = primed,
                lastActionReport = if (primed) {
                    "Alliance synchronization reached 100%! TSIDKENU STRIKE READY!"
                } else {
                    "Alliance synchronization adjusted to $newSync%."
                }
            )
        }
    }

    fun setVoidSaturation(level: Float) {
        val clamped = level.coerceIn(0.0f, 1.0f)
        _state.update { current ->
            val warning = when {
                clamped >= 0.8f -> "CRITICAL: Void saturation high! Reality anchor degrading!"
                clamped >= 0.5f -> "WARNING: Void corruption expanding."
                else -> "Void saturation stable within safe tolerances."
            }
            current.copy(
                voidSaturation = clamped,
                lastActionReport = warning
            )
        }
    }

    fun triggerKineticRedirection(force: Float) {
        _state.update { current ->
            val newFlow = (current.flowMeter + 15).coerceAtMost(100)
            val newGauntlet = (current.gauntletExtrusion + 0.5f).coerceAtMost(3.5f)
            val statuses = current.activeStatuses + "Reactive_Hardening"
            current.copy(
                flowMeter = newFlow,
                gauntletExtrusion = newGauntlet,
                activeStatuses = statuses,
                lastActionReport = "Micah redirected kinetic force (${force}N). Gauntlet extrusion at ${String.format("%.1f", newGauntlet)}x. Flow +15."
            )
        }
    }

    fun triggerGeminiLogicReconciliation() {
        _state.update { current ->
            val newStability = (current.stabilityIndex + 0.2f).coerceAtMost(1.0f)
            val newSaturation = (current.voidSaturation - 0.15f).coerceAtLeast(0.0f)
            current.copy(
                stabilityIndex = newStability,
                voidSaturation = newSaturation,
                lastActionReport = "Omega.one executed Gemini Logic: Corrupted shard pointers reconciled. Stability Index +20%."
            )
        }
    }

    fun executeVoidLightningStomp() {
        _state.update { current ->
            val flow = 0
            val syncBoost = (current.synchronization + 10).coerceAtMost(100)
            current.copy(
                flowMeter = flow,
                synchronization = syncBoost,
                lastActionReport = "Aeron executed VOID LIGHTNING STOMP from Airborne Ginga stance! Shockwave cleared hostile nodes."
            )
        }
    }

    fun toggleReverieDreamHarmony() {
        _state.update { current ->
            val hasHarmony = current.activeStatuses.contains("Dream_Harmony")
            val nextStatuses = if (hasHarmony) {
                current.activeStatuses - "Dream_Harmony"
            } else {
                current.activeStatuses + "Dream_Harmony" + "Blinding_Truth"
            }
            val report = if (hasHarmony) {
                "Anastasia dismissed Reverie trance."
            } else {
                "Anastasia channeled Dream Harmony & Blinding Truth! Psychic parity woven across all allies."
            }
            current.copy(
                activeStatuses = nextStatuses,
                lastActionReport = report
            )
        }
    }

    fun attemptPurifyDelilah() {
        _state.update { current ->
            val hasTruth = current.activeStatuses.contains("Blinding_Truth")
            val hasHarmony = current.activeStatuses.contains("Dream_Harmony")

            if (hasTruth && hasHarmony) {
                current.copy(
                    ingrisRestored = true,
                    voidSaturation = (current.voidSaturation - 0.4f).coerceAtLeast(0.0f),
                    activeStatuses = current.activeStatuses - "Voidfire_Active",
                    lastActionReport = "PURIFICATION SUCCESS: Voidfire imploded! Delilah the Desolate was cleansed and restored as INGRIS THE UNTAINTED!"
                )
            } else {
                val newStatuses = current.activeStatuses + "Voidfire_Active"
                current.copy(
                    activeStatuses = newStatuses,
                    voidSaturation = (current.voidSaturation + 0.1f).coerceAtMost(1.0f),
                    lastActionReport = "Delilah cast sickly Voidfire! Need both 'Blinding_Truth' and 'Dream_Harmony' active to purify!"
                )
            }
        }
    }

    fun executeSplitPeel() {
        _state.update { current ->
            val active = !current.isSplitPeelActive
            val statuses = if (active) current.activeStatuses + "Split_Peel" else current.activeStatuses - "Split_Peel"
            current.copy(
                isSplitPeelActive = active,
                activeStatuses = statuses,
                lastActionReport = if (active) {
                    "Split Peel maneuver engaged! Defensive lines separated to flank Void Rift."
                } else {
                    "Split Peel maneuver disengaged. Vanguard regrouped."
                }
            )
        }
    }

    fun executeTsidkenuStrike(x: Float = 0.0f, y: Float = 1000.0f, z: Float = 0.0f): Boolean {
        var executed = false
        _state.update { current ->
            if (current.synchronization >= 100) {
                executed = true
                current.copy(
                    synchronization = 40,
                    isTsidkenuPrimed = false,
                    totalStrikesExecuted = current.totalStrikesExecuted + 1,
                    voidSaturation = 0.0f,
                    lastActionReport = "⚡ TSIDKENU STRIKE DISPATCHED at coordinates ($x, $y, $z)! Era's vortex shattered! GAMMA.PRIME synthesized!"
                )
            } else {
                current.copy(
                    lastActionReport = "Cannot execute TSIDKENU Strike: Alliance synchronization is ${current.synchronization}% (needs 100%)."
                )
            }
        }
        return executed
    }

    fun calculateConservationOfNine(input: Int): Int {
        if (input == 0) return 9
        val sum = abs(input)
        val remainder = sum % 9
        return if (remainder == 0) 9 else remainder
    }
}
