object test extends App {

  val player = new Player(200, 300)
  val game = new Game(player, Vector[Wave](new Wave(Vector(new Zombie))))

  def testLoadGame() = {
    formGame.readFile
  }

  testLoadGame()

  println(s"What I got from file: ${formGame.sourceFileText}")
  println(s"Here it is as mkString: ${formGame.sourceFileText.mkString}")
  println(s"Here it is separated: ${formGame.sourceFileText.mkString.split('#').mkString(", ")}")
  println(s"Here it is as a vector: ${formGame.sourceFileText.mkString.split('#').toVector}")

  formGame.processData
  println(s"${formGame.waves}")
}