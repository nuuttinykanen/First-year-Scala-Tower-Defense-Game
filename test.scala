import o1.grid._

object test extends App {

  def onTick() = {

  }

  formGame.readFile

  println(s"What I got from file: ${formGame.sourceFileText}")
  println(s"Here it is as mkString: ${formGame.sourceFileText.mkString}")
  println(s"Here it is separated: ${formGame.sourceFileText.mkString.split('#').mkString(", ")}")
  println(s"Here it is as a vector: ${formGame.sourceFileText.mkString.split('#').toVector}")

  val game = formGame.processData
  // PLACING RECRUITS
  game.getPlayer.hireRecruit(new Ash, new MapSquare(3, 15))
  println(s"RECRUITS: ${game.getMap.getRecruits}")
  // START GAME

  println(s"${game.getMap.enemyTravelPath}")
  println(s"TRAVEL PATH COUNT: ${game.getMap.enemyTravelPath.size}")

  println(s"Here is the coming wave: ${game.getWave.getEnemyList}")
  println(s"Size of aforementioned wave: ${game.getWave.waveSize}")

  println(s"Player stats: ${game.getPlayer.getHealth}, ${game.getPlayer.getMoney}")
  println(s"Map dimensions: ${game.getMap.width}, ${game.getMap.height}")

  println(s"Wave list: ${game.getWaveList}")

  val ash = game.getMap.getAttackRecruits.headOption
  var pauseTicker = 0
  var ticker = 0
  while(!game.isDone) {
    game.passTime()
    println(s"Ticker: ${ticker}\n")
    println(s"Enemy amount: ${game.getMap.getEnemySquares.size}")
    println(s"Enemy locations: ${game.getMap.getEnemySquares}")
    println(s"Recruits: ${game.getMap.getAttackRecruits}")
    println(s"\n\nProjectiles: ${game.getMap.getProjectiles}")
    if(game.isPaused) pauseTicker += 1
    else     ticker += 1
    if(pauseTicker > 1000) {
       game.getPlayer.upgradeRecruit(ash.get)()
       game.continueGame()
       pauseTicker = 0
    }
  }

  println(s"Enemy health list: ${game.getMap.getEnemySquares.map(_.getEnemy).map(_.getHealth)}")
}
