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
  val player = game.getPlayer
  val map = game.getMap
  formGame.formMap(map)

  println(s"${player.affordableRecruits}")
  player.hireRecruit(new Simon(map))
  println(s"${player.affordableRecruits}")

  var simon = new Simon(map)
  map.placeRecruit(simon, new GridPos(0, 20))
  map.placeRecruit(new Simon(map), new GridPos(6, 40))
  println(s"${map.getRecruitSquares}")
  println(s"${map.getRecruits}")

  map.placeEnemy(new Zombie, new GridPos(5, 21))
  map.placeEnemy(new MichaelMyers, new GridPos(5, 40))

  println("Enemies in range:")
  println(s"${simon.enemiesInRange}")
  println(s"${map.getRecruitSquares.map(_.getRecruit).map(_.enemiesInRange)}")
  println(s"${map.enemyTravelPath}")

  println("\nPlaying with waves:")
  println(s"All waves: ${game.getWaveList}")
  println(s"Here is the wave: ${game.getWave}")
  println(s"Wave enemy list: ${game.getWave.enemyList}")
  println(s"Wave enemy on top: ${game.getWave.enemyOnTop}")

  map.removeAllEnemies()
  println("Let's pass the time! \n Here is the situation now:")
  println(s"Enemies present: ${map.getEnemySquares.map(_.getEnemy)}")

  map.placeRecruit(new Simon(map), new GridPos(4, 1))
  game.passTime()
  println(s"Enemies present: ${map.getEnemySquares.map(_.getEnemy)}")
  println(s"Their locations: ${map.getEnemySquares.map(_.x).zip(map.getEnemySquares.map(_.y))}")
  game.passTime()
  println("After two:")
  println(s"Enemies present: ${map.getEnemySquares.map(_.getEnemy)}")
  println(s"Their locations: ${map.getEnemySquares.map(_.x).zip(map.getEnemySquares.map(_.y))}")
  println(s"Projectiles: ${map.getProjectiles}")
  game.passTime()
  println(s"Enemies present: ${map.getEnemySquares.map(_.getEnemy)}")
  println(s"Their locations: ${map.getEnemySquares.map(_.x).zip(map.getEnemySquares.map(_.y))}")
  game.passTime()
  println(s"Projectiles: ${map.getProjectiles}")
  println(s"Enemies present: ${map.getEnemySquares.map(_.getEnemy)}")
  println(s"Their locations: ${map.getEnemySquares.map(_.x).zip(map.getEnemySquares.map(_.y))}")
  println(s"Wave list: ${game.getWave.enemyList.mkString}")
  println(s"${map.getEnemySquares.map(_.getEnemy).map(_.getHealth)}")
}