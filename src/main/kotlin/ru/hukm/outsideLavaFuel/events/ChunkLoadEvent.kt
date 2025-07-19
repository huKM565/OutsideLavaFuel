package ru.hukm.outsideLavaFuel.events

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.world.ChunkLoadEvent
import ru.hukm.outsideLavaFuel.FurnacesManager

class ChunkLoadEvent: Listener {
    @EventHandler
    fun onChunkLoadedEvent(event: ChunkLoadEvent) {
        FurnacesManager.findAndAddFurnacesLocationsToCache(event.chunk)
    }
}