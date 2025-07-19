package ru.hukm.outsideLavaFuel.events

import org.bukkit.block.Furnace
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockExplodeEvent
import ru.hukm.outsideLavaFuel.FurnacesManager

class BlockDestroyEvents {
    class BlockBreakListener : Listener {
        @EventHandler
        fun onBlockBreak(event: BlockBreakEvent) {
            val blockState = event.block.state
            if (blockState is Furnace) {
                FurnacesManager.deleteFurnaceLocationToCache(blockState)
            }
        }
    }

    class BlockExplodeListener : Listener {
        @EventHandler
        fun onBlockExplode(event: BlockExplodeEvent) {
            event.blockList().forEach { block ->
                val blockState = block.state
                if (blockState is Furnace) {
                    FurnacesManager.deleteFurnaceLocationToCache(blockState)
                }
            }
        }
    }
}