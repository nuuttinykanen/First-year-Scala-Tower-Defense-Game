class Game(player: Player, waves: Vector[Wave], map: Map, towers: TowerStore) {

 private var round = 1
 private var wave = this.waves.head
 private var waveList = waves

 def startWave() = ???

 def endWave() = {
   waveList = waveList.drop(1)
   if(waveList.isEmpty) endGame()
 }

 def endGame() = ???
 def passTime() = ???
 def spawnEnemy(enemy: Enemy) = ???
 def placeTower(map: Map, tower: Tower, x: Double, y: Double) = ???

}
