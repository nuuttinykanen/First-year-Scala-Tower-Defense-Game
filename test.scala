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

  // START GAME

  game.getMap.placeRecruit(new Simon(game.getMap), new GridPos(2, 40))
  game.getMap.placeRecruit(new Simon(game.getMap), new GridPos(10, 55))
  game.getMap.placeRecruit(new Simon(game.getMap), new GridPos(53, 53))
  game.getMap.placeRecruit(new Simon(game.getMap), new GridPos(60, 55))
  game.getMap.placeRecruit(new Simon(game.getMap), new GridPos(70, 62))
  game.getMap.placeRecruit(new Simon(game.getMap), new GridPos(2, 41))
  game.getMap.placeRecruit(new Simon(game.getMap), new GridPos(2, 42))
  game.getMap.placeRecruit(new Simon(game.getMap), new GridPos(2, 44))
  game.getMap.placeRecruit(new Simon(game.getMap), new GridPos(2, 43))
  game.getMap.placeRecruit(new Simon(game.getMap), new GridPos(2, 45))

  println(s"Here is the coming wave: ${game.getWave.getEnemyList}")
  println(s"Size of aforementioned wave: ${game.getWave.waveSize}")

  println(s"Player stats: ${game.getPlayer.getHealth}, ${game.getPlayer.getMoney}")
  println(s"Map dimensions: ${game.getMap.width}, ${game.getMap.height}")
  var pauseTicker = 0
  while(game.isDone) {
    game.passTime()
    println(game.getPlayer.getHealth)
    println(s"Enemy amount: ${game.getMap.getEnemySquares.size}")
    if(game.isPaused) pauseTicker += 1
    if(pauseTicker > 1000) {
       game.continueGame()
       pauseTicker = 0
    }
  }

  println(s"Enemy health list: ${game.getMap.getEnemySquares.map(_.getEnemy).map(_.getHealth)}")
}
