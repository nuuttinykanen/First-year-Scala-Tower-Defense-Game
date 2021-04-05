import o1.grid._

object test extends App {

  val map = new LevelMap(100, 100)
  val player = new Player(200, 100, map)
  val game = new Game(player, Vector[Wave](new Wave(Map(new Zombie -> 20))))

  def testLoadGame() = {
    formGame.readFile
  }

  testLoadGame()

  println(s"What I got from file: ${formGame.sourceFileText}")
  println(s"Here it is as mkString: ${formGame.sourceFileText.mkString}")
  println(s"Here it is separated: ${formGame.sourceFileText.mkString.split('#').mkString(", ")}")
  println(s"Here it is as a vector: ${formGame.sourceFileText.mkString.split('#').toVector}")

  println(s"${formGame.processData}")

  println(s"${player.affordableRecruits}")
  player.hireRecruit(new Simon(map))
  println(s"${player.affordableRecruits}")

  var simon = new Simon(map)
  map.placeRecruit(simon, new GridPos(0, 20))
  map.placeRecruit(new Simon(map), new GridPos(1, 14))
  println(s"${map.getRecruitSquares}")
  println(s"${map.getRecruits}")

  map.placeEnemy(new Zombie, new GridPos(0, 21))
  map.placeEnemy(new MichaelMyers, new GridPos(0, 40))

  println("Enemies in range:")
  println(s"${simon.enemiesInRange}")
  println("END")
}