import test.game
import java.util.Timer
import java.util.TimerTask
import java.io.{BufferedReader, FileNotFoundException, FileReader, IOException}
import scala.collection.mutable
import o1.View
class Game(player: Player, waves: Vector[Wave]) {

 private var round = 1
 private var wave = if(waves.nonEmpty) this.waves.head
 private var waveList = waves
 private var gameMap = new LevelMap(200, 200)

 def getMap = gameMap

 def startWave() = ???

 def endWave() = {
   waveList = waveList.drop(1)
   if(waveList.isEmpty) endGame()
 }

 val t = new java.util.Timer()
 val task = new java.util.TimerTask {
   def run() = passTime()
 }
 t.schedule(task, 1000L, 1000L)
 task.cancel()

 def endGame() = ???
 def passTime() = ???
 def spawnEnemy(enemy: Enemy) = ???

 private var enemyList = collection.mutable.Buffer[Enemy]()
 def enemiesPresent = enemyList

 def placeTower(map: LevelMap, tower: Recruit, x: Double, y: Double) = {

 }

}
