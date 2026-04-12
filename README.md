# Cursed Crown

## 1. Project Description
**Cursed Crown** is a top-down dungeon-crawler designed for entertainment purposes. Players traverse rooms filled with enemies to gain items, increase their strength, and eventually fight the boss. Every run is randomized via a unique seed system, though a save system allows players to quit and continue their progress at any time.

---

## 2. Key Features
* **Dynamic Combat:** Includes 8-directional movement and 4-directional attacks.
* **Advanced Mechanics:** Features a parry system and a dash mechanic for fluid room navigation.
* **Enemy Variety:**
    * **Melee:** Damages the player through physical collision.
    * **Ranged:** Throws projectiles at the player.
    * **Boss:** Fires projectiles and spawns melee/ranged enemies.
* **A* Pathfinding:** Integrated into specific projectiles to track the player and force defensive parrying.
* **Economy & Progression:** A shop exists in every run for buying items or upgrading and merging weapons using currency.

---

## 3. Technical Architecture
The game is built with an object-oriented structure:
* **Entity:** Abstract base class for coordinates and collision areas.
* **GameCharacter:** Abstract class managing health, movement, and drawing.
* **DynamicOverlay:** Manages the map grid, room transitions, and game state updates.

---

## 4. User Manual & Controls
### Controls
* **Movement:** 8-directional input.
* **Attack:** 4-directional input.
* **Interact (Shop):** Use the **'E'** button while standing on a yellow square (items) or blue square (upgrades/merging).
* **Pause:** Press **Escape** to open the pause menu.

### Menu Options
* **Continue:** Resumes the game from the saved state.
* **New Game:** Opens a menu to select difficulty, character class, and seed.
* **Options:** Displays controls and brightness settings.
* **Save & Quit:** Saves current progress and returns to the main menu.

---

## 5. Development Team
* **Artun Tuna Taç:** Room/map generation, rendering, and shop stations.
* **Mehmet Halim Uslu:** Character rendering, Player class, and Game Engine.
* **Doruk Candansayar:** Boss design, Enemy AI, weapon animations, and collision handling.
* **Ahmet Ege Çiğdem:** Items, inventory, sounds, and merge handling.
* **Kerem Uçar:** Navigation menus, Save system, Game Engine, and Main Method.

---

## 6. Course Information
* **Course:** CS102 - Spring 2025/2026
* **Instructor:** Uğur Güdükbay
* **Assistant:** Osama Awad
* **Section:** 3
* **Report Date:** 12 April 2026
