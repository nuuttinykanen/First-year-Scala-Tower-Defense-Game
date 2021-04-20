package towerDefense

import towerDefense.gui.TowerDefenseGUI.gameMap

class Game(player: Player, waves: Array[Wave]) {

 private var round = 1
 private var wave: Wave = this.waves.head
 private var waveList = waves
 private var waveNumber = 1
 private var gameMap = player.getMap

 def getMap = gameMap
 def getWave = wave
 def getWaveList = waveList
 def getWaveNumber = this.waveNumber
 def getWavesLeft = this.getWaveList.length - 1
 def getPlayer = player

 def endWave() = {
   pauseGame()
   waveList = waveList.drop(1)
   println("\n\nWAVE END\n\n")
   if(waveList.nonEmpty) {
      waveNumber += 1
      wave = this.waveList.head
   }
   else endGame()
 }

 def endGame() = done = true

 private var enemyMoveCounter = 0

 private var pause = false
 private var done = false

 def continueGame() = pause = false
 def pauseGame() = pause = true

 def isPaused: Boolean = pause
 def isDone: Boolean = this.done

def passTime() = {
  if(this.getPlayer.getHealth <= 0) endGame()
  if(isPaused) {
     println("isPaused")
  }
  else {
   if(gameMap.getProjectiles.nonEmpty) gameMap.getProjectiles.foreach(n => if(n != null) n.move(2))
   gameMap.healthCheckRemoval()
   gameMap.scanProjectiles()
   gameMap.getSupportRecruits.foreach(n => gameMap.supportAura(n))
   gameMap.getAttackRecruits.foreach(n => gameMap.attack(n))
   gameMap.removeTempModifiers()

   if(enemyMoveCounter > 3) {
      this.gameMap.moveEnemies()
      enemyMoveCounter = 0
   }

   enemyMoveCounter += 1
   if(this.getMap.getPenaltyHealth.isDefined) this.getPlayer.changeHealth(-1 * this.getMap.getPenaltyHealth.get)()
   if(this.getMap.getBounty.isDefined) this.getPlayer.changeMoney(this.getMap.getBounty.get)()

   if(!wave.enemyListEmpty) spawnEnemy()
   else if(this.getMap.getEnemySquares.isEmpty && this.getMap.getProjectiles.isEmpty) {
     gameMap.emptyDeathMarks()
     endWave()
   }
 }
}


 def spawnEnemy() = {
   if(this.gameMap.elementAt(gameMap.getEnemySpawn).isInstanceOf[EnemyPathSquare] && !this.gameMap.elementAt(gameMap.getEnemySpawn).isOccupied) {
     this.gameMap.placeEnemy(this.wave.popNext.get, this.gameMap.getEnemySpawn)
   }
 }

}
