package io.github.blugon0921.moredifficultwild.wilddata

import io.github.blugon0921.moredifficultwild.MoreDifficultWild
import io.github.blugon0921.moredifficultwild.MoreDifficultWild.Companion.config
import io.github.blugon0921.moredifficultwild.MoreDifficultWild.Companion.wild_data
import io.github.blugon0921.moredifficultwild.utils.TemperatureVal.Companion.cold_biom
import io.github.blugon0921.moredifficultwild.utils.TemperatureVal.Companion.dry_biom
import io.github.blugon0921.moredifficultwild.utils.TemperatureVal.Companion.extremecold_biom
import io.github.blugon0921.moredifficultwild.utils.TemperatureVal.Companion.temperate_biom
import io.github.blugon0921.moredifficultwild.utils.TemperatureVal.Companion.temperatureUpBlocks
import io.github.blugon0921.moredifficultwild.utils.TemperatureVal.Companion.tropical_biom
import io.github.blugon0921.moredifficultwild.wilddata.Temperature.Companion.getTemperature
import io.github.blugon0921.moredifficultwild.wilddata.Thirst.Companion.addThirstPoint
import io.github.blugon0921.moredifficultwild.wilddata.Thirst.Companion.getThirst
import io.github.blugon0921.moredifficultwild.wilddata.Thirst.Companion.getThirstPoint
import io.github.blugon0921.moredifficultwild.wilddata.Thirst.Companion.setThirstPoint
import io.github.blugon0921.moredifficultwild.wilddata.Thirst.Companion.subtractThirst
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.block.data.type.Candle
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerRespawnEvent
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import kotlin.math.floor

class Temperature : Listener {

    //Companion Object (Static 변수 & 함수)
    companion object {
        //Static 변수
        val temperature = HashMap<Player, Float>()
        val temperature_point = HashMap<Player, Int>()

        //Static 함수
        fun Player.getTemperature() : Float {
            if(temperature[this] != null) {
                return temperature[this]!!
            } else {
                temperature[this] = 36.5f
                return temperature[this]!!
            }
        }

        fun Player.setTemperature(value : Float) {
            temperature[this] = value
        }

        fun Player.addTemperature(value : Float) {
            temperature[this] = temperature[this]!! + value
        }

        fun Player.subtractTemperature(value : Float) {
            temperature[this] = temperature[this]!! - value
        }


        fun Player.getTemperaturePoint() : Int {
            if(temperature_point[this] != null) {
                return temperature_point[this]!!
            } else {
                temperature_point[this] = 0
                return temperature_point[this]!!
            }
        }

        fun Player.setTemperaturePoint(value : Int) {
            temperature_point[this] = value
        }

        fun Player.addTemperaturePoint(value : Int) {
            temperature_point[this] = temperature_point[this]!! + value
        }

        fun Player.subtractTemperaturePoint(value : Int) {
            temperature_point[this] = temperature_point[this]!! - value
        }



        //Config
        fun Player.getConfigTemperature() : Float {
            if(config.contains("${this.name}.temperature")) {
                return config.getString("${this.name}.temperature")!!.toFloat()
            } else {
                config.set("${this.name}temperature.", 36.5f)
                return config.getString("${this.name}.temperature")!!.toFloat()
            }
        }

        fun Player.setConfigTemperature(value : Float) {
            config.set("${this.name}.temperature", floor(value*10)/10)
            config.save(wild_data)
        }
    }



    fun RepeatTemperature() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(JavaPlugin.getPlugin(MoreDifficultWild::class.java), {
            for(players in Bukkit.getOnlinePlayers()) {

                if(50 < players.getTemperaturePoint()) {
                    players.addTemperature(0.1f)

                    players.setTemperaturePoint(0)
                } else if(players.getTemperaturePoint() < -50) {
                    players.subtractTemperature(0.1f)

                    players.setTemperaturePoint(0)
                }



                var isFrozen = true

                val biom = players.location.block.biome

                if(extremecold_biom.contains(biom) || cold_biom.contains(biom)) {
                    if(players.inventory.itemInMainHand.type == Material.TORCH || players.inventory.itemInOffHand.type == Material.TORCH) {
                        isFrozen = false
                        if(players.getTemperature() < 36.5) {
                            players.addTemperaturePoint(1)
                        } else if(36.5 < players.getTemperature()) {
                            players.subtractTemperaturePoint(1)
                        }
                    }

                    val location = players.location
                    location.x = location.x-2
                    location.z = location.z-2

                    for(y in 0..4) {
                        for(z in 0..4) {
                            for(x in 0..4) {
                                val block = location.clone().add(x.toDouble(), y.toDouble(), z.toDouble()).block

                                if(temperatureUpBlocks.contains(block.type)) {
                                    isFrozen = false
                                    if(players.getTemperature() < 36.5) {
                                        players.addTemperaturePoint(1)
                                    } else if(36.5 < players.getTemperature()) {
                                        players.subtractTemperaturePoint(1)
                                    }
                                }
                            }
                        }
                    }
                }




                //건조기후
                if(dry_biom.contains(biom)) {
                    players.addTemperaturePoint(3)

                    //열대기후
                } else if(tropical_biom.contains(biom)) {
                    players.addTemperaturePoint(2)

                    //온대기후
                } else if(temperate_biom.contains(biom)) {
                    if(players.location.block.type != Material.WATER) {
                        if(players.getTemperature() < 36.5) {
                            players.addTemperaturePoint(1)
                        } else if(36.5 < players.getTemperature()) {
                            players.subtractTemperaturePoint(1)
                        }
                    }

                    //냉대기후
                } else if(cold_biom.contains(biom)) {
                    if(isFrozen) {
                        players.subtractTemperaturePoint(2)
                    }

                    //한대기후
                } else if(extremecold_biom.contains(biom)) {
                    if(isFrozen) {
                        players.subtractTemperaturePoint(3)
                    }
                }


                //체온 39˚이상 (사망)
                if(39.0 < players.getTemperature()) {
                    players.damage(999999999999999.0)

                    //체온 38˚이상 (멀미)
                } else if(players.getTemperature() in 38.0..38.9) {
                    players.addPotionEffect(PotionEffect(PotionEffectType.CONFUSION, 1000000, 2, false, false))

                    //체온 37˚이상 38˚미만 (밑에확인)
                } else if(players.getTemperature() in 37.0..37.9) {
                    players.world

                    //체온 36˚이상 37˚미만 (아무일도 없음)
                } else if(players.getTemperature() in 36.0..36.9) {
                    players.world

                    //체온 35˚이상 36˚미만 (살짝 떰)
                } else if(players.getTemperature() in 35.0..35.9) {
                    if(isFrozen) {
                        players.freezeTicks = 70
                    }

                    //체온 34˚이상 35˚미만 (많이 떰)
                } else if(players.getTemperature() < 34.9) {
                    if(isFrozen) {
                        players.freezeTicks = 139
                    }

                    //체온 34˚미만 (사망)
                } else if(players.getTemperature() < 34.0) {
                    players.damage(999999999999999.0)
                }


                //물에 들어가있을때
                if(players.location.block.type == Material.WATER) {
                    if(36.0 < players.getTemperature()) {
                        players.subtractTemperaturePoint(5)
                    }
                }

                //멀미 초기화
                if(50 < players.getThirst() && players.getTemperature() < 38.0) {
                    players.removePotionEffect(PotionEffectType.CONFUSION)
                }
            }
        }, 0, 2)
    }


    @EventHandler
    fun death(event : PlayerRespawnEvent) {
        val player = event.player

        player.setTemperature(36.5f)
        player.setTemperaturePoint(0)
    }


    //체온 37˚이상 (받는 데미지 30%추가)
    @EventHandler
    fun damage(event : EntityDamageEvent) {
        if(event.entity is Player) {
            val player = event.entity as Player
            val damage = event.damage

            if(player.getTemperature() in 37.0..37.9) {
                val lastDamage = damage+(damage*0.3)

                event.damage = lastDamage
            }
        }
    }
}