package ru.hukm.outsideLavaFuel.events

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.world.ChunkUnloadEvent
import ru.hukm.outsideLavaFuel.FurnacesManager

class ChunkUnloadEvent: Listener {
    @EventHandler
    fun onChunkUnloadEvent(event: ChunkUnloadEvent) {
        FurnacesManager.deleteFurnacesLocations(event.chunk)
    }
}