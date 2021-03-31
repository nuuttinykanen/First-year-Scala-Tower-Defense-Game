import o1.grid._
class LevelMap(x: Int, y: Int, game: Game) extends Grid[LevelMapSquare](x, y) {

  private val startPosition = (100, 60)

  private val endPosition = (0, 20)

  def enemyLocations = game.enemiesPresent.map(_.getLocation)

  def initializeEnemyPath: Vector[LevelMapSquare] = ???

  def enemyPath: Vector[LevelMapSquare] = ???

  def initialElements = for (y <- 0 until this.height; x <- 0 until this.width) yield new LevelMapSquare(x, y)

}

class LevelMapSquare(x: Int, y: Int) extends GridPos(x, y) {

  var enemyPath = false
  var free = true
  var occupied = false

  def isEnemyPath = enemyPath
  def isFree = free
  def isOccupied = occupied

  def levelNeighbor(direction: CompassDir): LevelMapSquare = {
     this.neighbor(direction) match {
       case square: LevelMapSquare => square
       case _ => new LevelMapSquare(0, 0)
     }
  }
}