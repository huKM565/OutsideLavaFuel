package ru.hukm.outsideLavaFuel

import org.bukkit.plugin.java.JavaPlugin
import ru.hukm.outsideLavaFuel.events.BlockDestroyEvents
import ru.hukm.outsideLavaFuel.events.ChunkLoadEvent
import ru.hukm.outsideLavaFuel.events.ChunkUnloadEvent
import ru.hukm.outsideLavaFuel.events.PlayerPlaceBlock
import ru.hukm.outsideLavaFuel.utils.Config

class OutsideLavaFuel : JavaPlugin() {

    companion object {
        lateinit var instance: OutsideLavaFuel
            private set
    }

    override fun onEnable() {
        instance = this

        Config.init()
        FurnacesManager.startTryBurnTimer()

        server.pluginManager.registerEvents(BlockDestroyEvents.BlockBreakListener(), instance)
        server.pluginManager.registerEvents(BlockDestroyEvents.BlockExplodeListener(), instance)
        server.pluginManager.registerEvents(ChunkLoadEvent(), instance)
        server.pluginManager.registerEvents(ChunkUnloadEvent(), instance)
        server.pluginManager.registerEvents(PlayerPlaceBlock(), instance)
    }
}
