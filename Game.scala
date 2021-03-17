import test.game

import java.io.{BufferedReader, FileNotFoundException, FileReader, IOException}
import scala.collection.mutable
class Game(player: Player, waves: Vector[Wave]) {

 private var round = 1
 private var wave = if(waves.nonEmpty) this.waves.head
 private var waveList = waves


 def startWave() = ???

 def endWave() = {
   waveList = waveList.drop(1)
   if(waveList.isEmpty) endGame()
 }

 def endGame() = ???
 def passTime() = ???
 def spawnEnemy(enemy: Enemy) = ???
 def placeTower(map: LevelMap, tower: Tower, x: Double, y: Double) = ???

}
