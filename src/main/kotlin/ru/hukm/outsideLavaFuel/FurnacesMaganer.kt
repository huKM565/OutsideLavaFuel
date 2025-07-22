package ru.hukm.outsideLavaFuel

import org.bukkit.Bukkit
import org.bukkit.Chunk
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.BlockState
import org.bukkit.block.Furnace
import org.bukkit.block.data.Levelled
import ru.hukm.outsideLavaFuel.OutsideLavaFuel.Companion.instance
import ru.hukm.outsideLavaFuel.utils.Config
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.abs
import kotlin.math.floor

class FurnacesManager {
    companion object {

        private class AdditionalTicks(var value: Double = 0.0)

        private val cachedFurnacesData = ConcurrentHashMap<Location, AdditionalTicks>()

        fun startTryBurnTimer() {
            Bukkit.getScheduler().runTaskTimer(instance, Runnable {
                tryBurn()
            }, 0, 1)
        }

        private fun tryBurn() {
            cachedFurnacesData.forEach { (location, additionalTicks) ->
                val furnaceState = location.block.state as? Furnace ?: run {
                    cachedFurnacesData.remove(location)
                    return@forEach
                }

                val acceptableCountLavaNear = minOf(
                    getCountNearSourceLavaBlocks(furnaceState),
                    Config.maxLavaBlocksUsedTogether()
                )

                if(Config.getIsCanRunBurn() && acceptableCountLavaNear != 0 && furnaceState.burnTime <= 20 && furnaceState.inventory.fuel == null) {
                    furnaceState.burnTime = 20
                }

                additionalTicks.value += acceptableCountLavaNear * Config.getAdditionalSpeedBurn()

                if(Config.getIsCanBurnSpeedUp() && furnaceState.burnTime != 0.toShort() && furnaceState.inventory.smelting != null) {
                    furnaceState.cookTime = (furnaceState.cookTime + additionalTicks.value.toInt()).toShort()
                    furnaceState.cookTime = abs(furnaceState.cookTime.toInt()).toShort()
                }

                additionalTicks.value -= floor(additionalTicks.value)

                furnaceState.update()
            }
        }

        private fun getCountNearSourceLavaBlocks(block: BlockState): Int {
            return Config.getAroundBlockPositions().count { relativePos ->
                val block = block.location.clone().add(relativePos.x, relativePos.y, relativePos.z).block
                block.type == Material.LAVA && (block.blockData as? Levelled)?.level == 0
            }
        }

        fun findAndAddFurnacesLocationsToCache(chunk: Chunk) {
            operationWithCachedFurnaceLocations(chunk) { furnaceState ->
                addFurnaceLocationToCache(furnaceState)
            }
        }

        fun deleteFurnacesLocations(chunk: Chunk) {
            operationWithCachedFurnaceLocations(chunk) { furnaceState ->
                deleteFurnaceLocationToCache(furnaceState)
            }
        }

        private fun operationWithCachedFurnaceLocations(chunk: Chunk, lambda: (Furnace) -> Unit) {
            val world = chunk.world
            for (x in 0..15) {
                for (y in world.minHeight until world.maxHeight) {
                    for (z in 0..15) {
                        val blockState = chunk.getBlock(x, y, z).state
                        if (blockState is Furnace) {
                            lambda(blockState)
                        }
                    }
                }
            }
        }

        fun addFurnaceLocationToCache(furnaceState: Furnace) {
            if(cachedFurnacesData.contains(furnaceState.location)) return
            cachedFurnacesData[furnaceState.location] = AdditionalTicks()
        }

        fun deleteFurnaceLocationToCache(furnaceState: Furnace) {
            cachedFurnacesData.remove(furnaceState.location)
        }
    }
}
