package io.github.blugon0921.moredifficultwild

import io.github.blugon0921.moredifficultwild.wilddata.Temperature
import io.github.blugon0921.moredifficultwild.wilddata.Temperature.Companion.getConfigTemperature
import io.github.blugon0921.moredifficultwild.wilddata.Temperature.Companion.getTemperature
import io.github.blugon0921.moredifficultwild.wilddata.Temperature.Companion.getTemperaturePoint
import io.github.blugon0921.moredifficultwild.wilddata.Temperature.Companion.setConfigTemperature
import io.github.blugon0921.moredifficultwild.wilddata.Temperature.Companion.setTemperature
import io.github.blugon0921.moredifficultwild.wilddata.Temperature.Companion.setTemperaturePoint
import io.github.blugon0921.moredifficultwild.wilddata.Thirst
import io.github.blugon0921.moredifficultwild.wilddata.Thirst.Companion.getConfigThirst
import io.github.blugon0921.moredifficultwild.wilddata.Thirst.Companion.getThirst
import io.github.blugon0921.moredifficultwild.wilddata.Thirst.Companion.getThirstPoint
import io.github.blugon0921.moredifficultwild.wilddata.Thirst.Companion.setConfigThirst
import io.github.blugon0921.moredifficultwild.wilddata.Thirst.Companion.setThirst
import io.github.blugon0921.moredifficultwild.wilddata.Thirst.Companion.setThirstPoint
import net.kyori.adventure.text.Component
import net.md_5.bungee.api.ChatColor
import org.bukkit.Bukkit
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import kotlin.math.floor

class MoreDifficultWild : JavaPlugin(),Listener {

    //Companion Object (Static 변수 & 함수)
    companion object {
        //Static 변수
        val wild_data = File("plugins/MoreDifficultWild/playerData.yml")
        var config = YamlConfiguration.loadConfiguration(wild_data)


        //Static 함수
    }



    override fun onEnable() {
        logger.info("Plugin Enable")
        Bukkit.getPluginManager().registerEvents(this, this)
        Bukkit.getPluginManager().registerEvents(Thirst(), this)
        Bukkit.getPluginManager().registerEvents(Temperature(), this)


        if(!wild_data.exists()) {
            config.save(wild_data)
        }

        for(players in Bukkit.getOnlinePlayers()) {
            players.setTemperature(players.getConfigTemperature())
            players.setThirst(players.getConfigThirst())

            players.setThirstPoint(0)
            players.setTemperaturePoint(0)
        }


        Thirst().RepeatThirst()
        Temperature().RepeatTemperature()


        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, {
            for(players in Bukkit.getOnlinePlayers()) {
                //체온 & 갈증 표시
                players.sendActionBar(
                    Component.text(
                        "${ChatColor.RED}체온 : ${floor(players.getTemperature()*10)/10}" +
                                " ${players.getTemperaturePoint()}" +
                                "${ChatColor.WHITE} | " +
                                "${ChatColor.BLUE}갈증 : ${players.getThirst()}/100" +
                                " ${players.getThirstPoint()}"
                    )
                )
            }
        }, 0, 2)
    }

    override fun onDisable() {
        logger.info("Plugin Disable")


        for(players in Bukkit.getOnlinePlayers()) {
            players.setConfigTemperature(players.getTemperature())
            players.setConfigThirst(players.getThirst())
        }
    }


    @EventHandler
    fun join(event : PlayerJoinEvent) {
        val player = event.player

        player.setTemperature(player.getConfigTemperature())
        player.setThirst(player.getConfigThirst())
    }

    @EventHandler
    fun quit(event : PlayerQuitEvent) {
        val player = event.player

        player.setConfigTemperature(player.getTemperature())
        player.setConfigThirst(player.getThirst())
    }
}