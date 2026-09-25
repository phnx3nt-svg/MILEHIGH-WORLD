# Milehigh World: Into the Void

**Platform:** Android (Kotlin, Jetpack Compose, Material 3)  
**Architecture:** MVVM with StateFlow, Coroutines, and Sentinel Parity Engine  
**Aesthetic:** Neo-Arcane Fractured Realism  

## Overview
An interactive Android tactical campaign director and companion app for **Milehigh World: Into the Void**. Ported from the original Unity/C# Infiniteration Engine architecture, preserving the full multiversal campaign, autonomous character behavior scripts, Alliance Power synchronization protocols, and Sentinel parity validation rules.

## Core Features & Ported Systems

### 1. Multiversal Campaign Director (`CampaignScreen`)
- **Act & Chapter Timeline**: Complete narrative progression across:
  - *Act I · Chapter 1*: Onalym Nexus (King Cyrus invasion, durasteel extrusion, dimensional shifting)
  - *Act I · Chapter 2*: The Crash Site (Kepler-186f wreckage, stability index repair)
  - *Act II · Chapter 3*: Aethylgard Fjords (Aerial combat against Kane, Lyra's solar barrier)
  - *Act III · Chapter 5*: Concord Purification (Purging Delilah's voidfire)
  - *Act IV · Final Raid*: The Grand Purpose (10-player Onalym Core raid, TSIDKENU strike)
- **Interactive 3D Objects & Vectors**: Real-time adjustment of scalar values (Aura radius, void shockwave scale) and 3D coordinate vectors `(X, Y, Z)` with live Sentinel bounds verification.
- **BattleREM Narrative Dialogue**: Speaker badges, authentic quotes, and trigger dispatching.

### 2. Champion Archive & Ability Simulator (`ChampionsScreen`)
- **Champion Dossiers**: Micah the Unbreakable, Omega.one (The Architect), King Cyrus the Dragon King, Aeron the Brave, Anastasia the Dreamer, and Delilah the Desolate / Ingris.
- **Active Tactical Ability Chamber**:
  - *Micah*: Kinetic force redirection (+15 Flow Meter, dynamic Gauntlet Extrusion up to 3.5x).
  - *Omega.one*: Gemini Logic reconciliation (shard repair, stability index restoration).
  - *Aeron*: Airborne Ginga Stance & Void Lightning Stomp.
  - *Anastasia*: Dreamscape Reverie trance (weaves Dream Harmony & Blinding Truth).
  - *Delilah / Ingris*: Authentic boss phase logic requiring Dream Harmony + Blinding Truth to implode voidfire and restore Ingris the Untainted!
  - *King Cyrus*: Void shockwave distortion.
- **Autonomous Behavior Script Inspector**: Expandable viewer for raw character scripts.

### 3. Onalym Core Alliance Command (`AllianceScreen`)
- **Alliance Synchronization Gauge**: Radial meter tracking 0% to 100% raid synchronization.
- **Tsidkenu Ultimate Lightning Strike**: High-impact tactical strike at coordinates `(0, 1000, 0)` available upon reaching 100% synchronization.
- **Split Peel Maneuver**: Engages and disengages tactical vanguard flanking protocols.
- **Void Saturation Level**: Dynamic slider and warning system regulating void saturation bounds `[0.0, 1.0]`.
- **Infiniteration Engine Conservation of Nine**: Interactive Vortex Mathematics calculator computing digital root mod 9 for any input frequency or seed.

### 4. Sentinel Protocol & Parity Gates (`SentinelScreen`)
- Replicates the Sentinel validation rules from `HorizonGameData.cs` and `validate_implementation.py`:
  - Scene ID validation
  - Void Saturation safety limits `[0.0, 1.0]`
  - 9-bit Parity (`systemParity == 9`)
  - 3D Interactive Vector schema (`isVector` flag integrity)
  - BattleREM narrative trigger anchors

## Build & Tech Stack
- **Target SDK**: Android 36 (minSdk 26)
- **Kotlin**: 2.2.10
- **UI**: Jetpack Compose with Material 3 Dark Theme
- **Lifecycle & ViewModel**: AndroidX Lifecycle Runtime & Compose integration
