package io.github.blugon0921.moredifficultwild.wilddata

import io.github.blugon0921.moredifficultwild.MoreDifficultWild
import io.github.blugon0921.moredifficultwild.MoreDifficultWild.Companion.config
import io.github.blugon0921.moredifficultwild.MoreDifficultWild.Companion.wild_data
import io.github.blugon0921.moredifficultwild.wilddata.Temperature.Companion.addTemperaturePoint
import io.github.blugon0921.moredifficultwild.wilddata.Temperature.Companion.getTemperature
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.event.player.PlayerRespawnEvent
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class Thirst : Listener {


    //Companion Object (Static 변수 & 함수)
    companion object {
        //Static 변수
        val thirst = HashMap<Player, Int>()
        val thirst_point = HashMap<Player, Int>()


        //Static 함수
        fun Player.getThirst() : Int {
            if(thirst[this] != null) {
                return thirst[this]!!
            } else {
                thirst[this] = 100
                return thirst[this]!!
            }
        }

        fun Player.setThirst(value : Int) {
            thirst[this] = value
        }

        fun Player.addThirst(value : Int) {
            thirst[this] = thirst[this]!! + value
        }

        fun Player.subtractThirst(value : Int) {
            thirst[this] = thirst[this]!! - value
        }



        fun Player.getThirstPoint() : Int {
            if(thirst_point[this] != null) {
                return thirst_point[this]!!
            } else {
                thirst_point[this] = 0
                return thirst_point[this]!!
            }
        }

        fun Player.setThirstPoint(value : Int) {
            thirst_point[this] = value
        }

        fun Player.addThirstPoint(value : Int) {
            thirst_point[this] = thirst_point[this]!! + value
        }

        fun Player.subtractThirstPoint(value : Int) {
            thirst_point[this] = thirst_point[this]!! - value
        }

        //Config
        fun Player.getConfigThirst() : Int {
            if(config.contains("${this.name}.temperature")) {
                return config.getString("${this.name}.thirst")!!.toInt()
            } else {
                config.set("${this.name}.thirst", 100)
                return config.getString("${this.name}.thirst")!!.toInt()
            }
        }

        fun Player.setConfigThirst(value : Int) {
            config.set("${this.name}.thirst", value)
            config.save(wild_data)
        }
    }


    fun RepeatThirst() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(JavaPlugin.getPlugin(MoreDifficultWild::class.java), {
            for(players in Bukkit.getOnlinePlayers()) {

                //갈증 증가
                if(1000 < players.getThirstPoint()) {
                    if(90 <= players.getThirst()) players.subtractThirst(1)

                    players.addThirst(1)

                    players.setThirstPoint(0)

                //갈증 감소
                }  else if(players.getThirstPoint() < - 1000) {
                    if(players.location.block.type != Material.WATER) {
                        if (players.getThirst() <= 0) players.addThirst(1)

                        players.subtractThirst(1)

                        players.setThirstPoint(0)
                    }
                }




                //체온 38˚이상 (고열)
                if(38.0 <= players.getTemperature()) {
                    if(players.location.block.type != Material.WATER) {
                        players.subtractThirstPoint(7 + (players.getTemperature() / 10).toInt())
                    }

                    //체온 37˚이상 38˚미만 (열)
                } else if(players.getTemperature() in 37.0..37.9) {
                    if(players.location.block.type != Material.WATER) {
                        players.subtractThirstPoint(7)
                    }

                    //체온 36˚이상 37˚미만 (정상)
                } else if(players.getTemperature() in 36.0..36.9) {
                    if(players.location.block.type != Material.WATER) {
                        players.subtractThirstPoint(5)
                    }

                    //체온 35˚이상 36˚미만 (저체온)
                } else if(players.getTemperature() in 35.0..35.9) {
                    if(players.location.block.type != Material.WATER) {
                        players.subtractThirstPoint(5)
                    }

                    //체온 35˚미만 (초저체온)
                } else if(players.getTemperature() < 35.0) {
                    if(players.location.block.type != Material.WATER) {
                        players.subtractThirstPoint(5)
                    }
                }


//                30감소하면 체온이 올라가게
//                50감소하면 멀미가 걸리고
//                100감소하면 뒤지게하죠


                //갈증 30달았을때 (체온 증가)
                if(players.getThirst() <= 70) {
                    if(players.getTemperature() < 37.0) {
                        players.addTemperaturePoint(1)
                    }
                }
                //갈증 50달았을때 (멀미)
                if(players.getThirst() <= 50) {
                    players.addPotionEffect(PotionEffect(PotionEffectType.CONFUSION, 1000000, 2, false, false))
                }
                //갈증 100달았을때 (사망)
                if(players.getThirst() <= 0) {
                    players.damage(999999999999999.0)
                }

                //멀미 초기화
                if(50 < players.getThirst() && players.getTemperature() < 38.0) {
                    players.removePotionEffect(PotionEffectType.CONFUSION)
                }

                //물에 들어가있을때
                if(players.location.block.type == Material.WATER) {
                    if(players.getThirstPoint() < 0) players.setThirstPoint(0)

                    if(players.getThirst() < 90) {
                        if (100 <= players.getThirst()) players.subtractThirst(1)

                        players.addThirstPoint(100)
                    }
                }
            }
        }, 0, 2)
    }



    @EventHandler
    fun drinkWater(event : PlayerItemConsumeEvent) {
        val player = event.player
        val item = event.item

        if(item.type == Material.POTION) {
            if((item.itemMeta as PotionMeta).hasCustomEffects()) return

            var new_thirst = player.getThirst()+(Math.random()*20+10).toInt()
            if(100 < new_thirst) new_thirst = 100

            thirst[player] = new_thirst
        }
    }


    @EventHandler
    fun death(event : PlayerRespawnEvent) {
        val player = event.player

        player.setThirst(100)
        player.setThirstPoint(100)
    }
}