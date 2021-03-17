import lib.grid._
class LevelMap(x: Int, y: Int) extends Grid(x, y) {

  def initializeEnemyPath: Vector[LevelMapSquare] = ???

  def enemyPath: Vector[LevelMapSquare] = ???


  override def initialElements: Seq[Nothing] = Seq[Nothing]()


}

class LevelMapSquare(x: Int, y: Int) extends GridPos(x, y) {

  var isEnemyPath = false
  var isFree = true
  var isOccupied = false


}