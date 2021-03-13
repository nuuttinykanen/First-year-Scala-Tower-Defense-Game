import lib.grid._
class Map(x: Int, y: Int) extends Grid(x, y) {

  def initializeEnemyPath: Vector[MapSquare] = ???

  def enemyPath: Vector[MapSquare] = ???


  override def initialElements: Seq[Nothing] = Seq[Nothing]()


}

class MapSquare(x: Int, y: Int) extends GridPos(x, y) {

  var isEnemyPath = false
  var isFree = true
  var isOccupied = false


}