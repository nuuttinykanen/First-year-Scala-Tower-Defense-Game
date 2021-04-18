package towerDefense

object test extends App {

  formGame.readFile

  println(s"What I got from file: ${formGame.sourceFileText}")

  println(s"Here it is as mkString: ${formGame.sourceFileText.mkString}")
  println(s"Here it is separated: ${formGame.sourceFileText.mkString.split('#').mkString(",")}")
  println(s"Here it is as a vector: ${formGame.sourceFileText.mkString.split('#').toVector}")

  val game = formGame.processData
  val player = game.getPlayer

  // PLACING RECRUITS
  game.getPlayer.hireRecruit(new Ash, new MapSquare(3, 15))
  game.getPlayer.hireRecruit(new FatherMerrin, new MapSquare(3, 16))
  game.getPlayer.hireRecruit(new Ash, new MapSquare(1, 14))

  println(game.getMap.getRecruits.map(_.getClass))


  println(s"WAVES: ${game.getWaveList.map(_.getEnemyList)}")

  val ash = game.getMap.getAttackRecruits.headOption
  var pauseTicker = 0
  var ticker = 0
  while (!game.isDone) {
    println(s"ENEMIES: ${game.getMap.getEnemiesOnPath.map(_.getEnemy.getName)}")
    println(s"Player MONEY: ${game.getPlayer.getMoney}")
    game.passTime()
    println(s"Ticker: ${ticker}\n")
    if (game.isPaused) pauseTicker += 1
    else ticker += 1

    if (pauseTicker > 1000) {
      game.getPlayer.upgradeRecruit(ash.get)()
      game.continueGame()
      pauseTicker = 0
    }
  }

  println(s"Enemy health list: ${game.getMap.getEnemySquares.map(_.getEnemy).map(_.getHealth)}")
}
