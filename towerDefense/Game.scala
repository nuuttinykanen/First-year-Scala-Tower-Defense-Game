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

 def getWavesLeft = {
   if(this.getWaveList.length - 1 < 0) 0
   else this.getWaveList.length - 1
 }

 def getPlayer = player

 def endWave() = {
   if(gameMap.getPenaltyHealth.isDefined) {
      player.changeHealth(-1 * gameMap.getPenaltyHealth.get)()
      gameMap.emptyPenaltyHealth()
   }
   if(gameMap.getBounty.isDefined) {
      player.changeMoney(gameMap.getBounty.get)()
      gameMap.emptyBounty()
   }
   pauseGame()
   waveList = waveList.drop(1)
   if(waveList.nonEmpty) {
      player.changeMoney(waveNumber * 35)()
      waveNumber += 1
      wave = this.waveList.head
   }
   else endGame()
 }

 def endGame() = done = true

 private var enemyMoveCounter = 0
 private var spawnCounter = 0
 private var pause = true
 private var done = false

 def continueGame() = pause = false
 private def pauseGame() = pause = true

 def isPaused: Boolean = pause
 def isDone: Boolean = this.done

def passTime() = {
  if(this.getPlayer.isDead) endGame()
  if(isPaused) {
    gameMap.getSupportRecruits.foreach(n => gameMap.supportAura(n))
  }
  else {
   // MOVE PROJECTILES
   if(gameMap.getProjectiles.nonEmpty) gameMap.getProjectiles.foreach(n => if(n != null) n.move(2))
   // REMOVE DEAD ENEMIES AND PROJECTILES WITHOUT A TARGET
   gameMap.healthCheckRemoval()
   gameMap.scanProjectiles()
   // RECRUIT FUNCTIONS
   gameMap.getSupportRecruits.foreach(n => gameMap.supportAura(n))
   gameMap.getAttackRecruits.foreach(n => gameMap.attack(n))

   // ENEMY MOVEMENT EVERY SECOND TIME PASS
   if(enemyMoveCounter > 2) {
      this.gameMap.moveEnemies()
      enemyMoveCounter = 0
   }

   enemyMoveCounter += 1
   // IF AN ENEMY HAS REACHED THE END, REMOVE HEALTH FROM PLAYER
   if(this.getMap.getPenaltyHealth.isDefined) {
     this.getPlayer.changeHealth(-1 * this.getMap.getPenaltyHealth.get)()
     this.getMap.emptyPenaltyHealth()
   }
   // IF ENEMIES HAVE DIED, ADD BOUNTY TO PLAYER MONEY
   if(this.getMap.getBounty.isDefined) {
     this.getPlayer.changeMoney(this.getMap.getBounty.get)()
     this.getMap.emptyBounty()
   }

   // SPAWN EVERY THIRD TIME PASS
   if(!wave.enemyListEmpty) {
       if(spawnCounter > 4) {
         spawnEnemy()
         spawnCounter = 0
       }
     spawnCounter += 1
   }
   // END WAVE IF NO ENEMIES PRESENT AND CURRENT WAVE EMPTY
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
