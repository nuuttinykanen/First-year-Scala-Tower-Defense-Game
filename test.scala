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

  // PLACING RECRUITS
  game.getPlayer.hireRecruit(new Simon(game.getMap), game.getMap.elementAt(GridPos(3, 3)))
  game.getPlayer.hireRecruit(new Simon(game.getMap), game.getMap.elementAt(GridPos(4, 3)))
  game.getPlayer.hireRecruit(new Simon(game.getMap), game.getMap.elementAt(GridPos(10, 7)))
  game.getPlayer.hireRecruit(new Simon(game.getMap), game.getMap.elementAt(GridPos(50, 6)))

  // START GAME
  game.passTime()
  game.passTime()
  game.passTime()
  game.passTime()
  game.passTime()
  game.passTime()
  println(s"Projectile targets: ${game.getMap.getProjectiles.map(_.getTargetLocation)}")
  game.passTime()
  game.passTime()

  println(s"Enemies in range: ${game.getMap.getRecruitSquares.map(_.getRecruit).map(_.enemiesInRange)}")
  println(s"The enemy path: ${game.getMap.enemyTravelPath}")
  println(s"List of recruits and their positions: ${game.getMap.getRecruitSquares.map(_.getRecruit).zip(game.getMap.getRecruitSquares.map(_.getGridPos))}\n")
  println(s"List of enemies and their positions: ${game.getMap.getEnemySquares.map(_.getEnemy).zip(game.getMap.getEnemySquares.map(_.getGridPos))}\n")
  println(s"The current wave: ${game.getWave.getEnemyList}\n")

  println(s"Enemy health list: ${game.getMap.getEnemySquares.map(_.getEnemy).map(_.getHealth)}")
}