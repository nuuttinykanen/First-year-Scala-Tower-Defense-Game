import test.game
import java.util.Timer
import java.util.TimerTask
import java.io.{BufferedReader, FileNotFoundException, FileReader, IOException}
import scala.collection.mutable
import o1.View
class Game(player: Player, waves: Vector[Wave]) {

 private var round = 1
 private var wave: Wave = this.waves.head
 private var waveList = waves
 private var gameMap = player.getMap

 def getMap = gameMap
 def getWave = wave
 def getWaveList = waveList
 def getPlayer = player

 def startWave() = ???

 def endWave() = {
   waveList = waveList.drop(1)
   if(waveList.nonEmpty) wave = this.waveList.head
   else endGame()
 }

 def endGame() = ???

 private var enemyMoveCounter = 0

 def passTime() = {
   gameMap.scanProjectiles()
   if(gameMap.getProjectiles.nonEmpty && enemyMoveCounter > 10) gameMap.getProjectiles.foreach(_.act())
   gameMap.getRecruits.foreach(_.attack())

   if(enemyMoveCounter > 5) {
      this.gameMap.moveEnemies()
      enemyMoveCounter = 0
   }
   enemyMoveCounter += 1

   if(!wave.enemyListEmpty) spawnEnemy()
   else endWave()
 }

 def spawnEnemy() = {
   if(!this.wave.enemyListEmpty && this.gameMap.elementAt(gameMap.getEnemySpawn).isInstanceOf[EnemyPathSquare]) {
     this.gameMap.placeEnemy(this.wave.popNext.get, this.gameMap.getEnemySpawn)
   }
 }

 private var enemyList = collection.mutable.Buffer[Enemy]()
 def enemiesPresent = enemyList

 def placeTower(map: LevelMap, tower: Recruit, x: Double, y: Double) = {
 }

}
