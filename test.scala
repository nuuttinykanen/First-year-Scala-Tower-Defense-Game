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
  formGame.formMap(game.getMap)

  game.getPlayer.changeMoney(9000)()
  // PLACING RECRUITS
  game.getPlayer.hireRecruit(new Simon(game.getMap), game.getMap.elementAt(GridPos(30, 6)))
  game.getPlayer.hireRecruit(new Simon(game.getMap), game.getMap.elementAt(GridPos(4, 2)))
  game.getPlayer.hireRecruit(new Simon(game.getMap), game.getMap.elementAt(GridPos(7, 10)))

  // START GAME
  game.passTime()
  game.passTime()
  game.passTime()
  println(s"${game.getMap.getEnemySquares.map(_.getEnemy).map(_.getHealth)}")
  game.passTime()

  game.passTime()
  game.passTime()
  println(game.getMap.getProjectiles)

  println(s"Projectiles: ${game.getMap.getProjectiles}")
  println(s"Projectile positions: ${game.getMap.getProjectiles.map(_.getLocation)}")
  game.passTime()
  game.passTime()

  game.passTime()
  game.passTime()

  game.passTime()
  game.passTime()

  game.passTime()
  game.passTime()
  game.passTime()
  game.passTime()
  game.passTime()
  game.passTime()

  game.passTime()
  game.passTime()

  game.passTime()
  game.passTime()
  game.passTime()
  game.passTime()
  game.passTime()
  game.passTime()

  game.passTime()
  game.passTime()

  game.passTime()
  game.passTime()
  game.passTime()
  game.passTime()
  game.passTime()
  game.passTime()

  game.passTime()
  game.passTime()

  game.passTime()
  game.passTime()
  game.passTime()
  game.passTime()
  game.passTime()
  game.passTime()

  game.passTime()
  game.passTime()

  game.passTime()
  game.passTime()
  game.passTime()
  game.passTime()
  game.passTime()
  game.passTime()

  println(s"Projectile positions: ${game.getMap.getProjectiles.map(_.getLocation)}")
  println(s"Enemies in range: ${game.getMap.getRecruitSquares.map(_.getRecruit).map(_.enemiesInRange)}")
  println(s"The enemy path: ${game.getMap.enemyTravelPath}")
  println(s"List of recruits and their positions: ${game.getMap.getRecruitSquares.map(_.getRecruit).zip(game.getMap.getRecruitSquares.map(_.getGridPos))}\n")
  println(s"List of enemies and their positions: ${game.getMap.getEnemySquares.map(_.getEnemy).zip(game.getMap.getEnemySquares.map(_.getGridPos))}\n")
  println(s"The current wave: ${game.getWave.getEnemyList}\n")
  println(s"Enemy squares: ${game.getMap.getEnemySquares}")

  println(s"Recruit locations by themself: ${game.getMap.getRecruits.map(_.getLocation)}")

  println(s"Enemy health list: ${game.getMap.getEnemySquares.map(_.getEnemy).map(_.getHealth)}")

  println(s"Kill count: ${game.getMap.getKillCount}")
}