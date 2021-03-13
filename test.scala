object test extends App {

  val player = new Player("Nuutti", 200, 300)
  val game = new Game(player, Vector[Wave](new Wave(Vector(new Zombie))))

  def testLoadGame() = {
    game.readFile
  }

  testLoadGame()

  println(s"What I got from file: ${game.sourceFileText}")
  println(s"Here it is separated: ${game.sourceFileText.mkString}")
  println(s"Here it is separated: ${game.sourceFileText.mkString.split('#').mkString(", ")}")
  println("\nMor Ardain - Roaming the Wastes")

}
