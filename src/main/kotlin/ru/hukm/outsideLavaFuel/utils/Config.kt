package ru.hukm.outsideLavaFuel.utils

import org.bukkit.configuration.file.FileConfiguration
import ru.hukm.outsideLavaFuel.OutsideLavaFuel.Companion.instance
import ru.hukm.outsideLavaFuel.utils.dataclasses.RelativeBlockXYZ

class Config {
    companion object {
        lateinit var config: FileConfiguration

        fun init() {
            instance.saveDefaultConfig()
            config = instance.config
        }

        fun getIsCanRunBurn() = config.getBoolean("canRunBurn")
        fun getIsCanBurnSpeedUp() = config.getBoolean("canSpeedUpBurn")
        fun getAdditionalSpeedBurn() = config.getDouble("additionalSpeedBurn")
        fun maxLavaBlocksUsedTogether() = config.getInt("maxLavaBlocksUsedTogether")

        fun getAroundBlockPositions(): List<RelativeBlockXYZ> {
            val positions = arrayListOf<RelativeBlockXYZ>()

            if (!config.contains("aroundBlockPositions")) {
                // Default positions if not specified in config
                return arrayListOf(
                    RelativeBlockXYZ(1.0, 0.0, 0.0),
                    RelativeBlockXYZ(-1.0, 0.0, 0.0),
                    RelativeBlockXYZ(0.0, 1.0, 0.0),
                    RelativeBlockXYZ(0.0, -1.0, 0.0),
                    RelativeBlockXYZ(0.0, 0.0, 1.0),
                    RelativeBlockXYZ(0.0, 0.0, -1.0)
                )
            }

            val positionsList = config.getStringList("aroundBlockPositions")
            positionsList.forEach { posString ->
                try {
                    // Split the string by commas and parse each part as a Double
                    val parts = posString.split(",").map { it.trim().toDouble() }
                    if (parts.size == 3) {
                        positions.add(RelativeBlockXYZ(parts[0], parts[1], parts[2]))
                    }
                } catch (e: Exception) {
                    // Log error or handle invalid format
                    instance.logger.warning("Invalid position format: $posString")
                }
            }

            return positions
        }
    }
}
