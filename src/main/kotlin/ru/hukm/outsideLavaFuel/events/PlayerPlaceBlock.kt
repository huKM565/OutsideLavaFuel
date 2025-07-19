package ru.hukm.outsideLavaFuel.events

import org.bukkit.block.Furnace
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent
import ru.hukm.outsideLavaFuel.FurnacesManager

class PlayerPlaceBlock: Listener {
    @EventHandler
    fun onPlayerPlaceBlock(event: BlockPlaceEvent) {
        val blockState = event.block.state
        if(blockState is Furnace) FurnacesManager.addFurnaceLocationToCache(blockState)
    }
}