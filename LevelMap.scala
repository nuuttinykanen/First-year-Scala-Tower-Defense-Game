import o1.grid._
import collection.mutable.Buffer
class LevelMap(x: Int, y: Int) extends Grid[MapSquare](x, y) {

  private val startPosition = (100, 60)
  private val endPosition = (0, 20)

  var enemyTravelPath = Buffer[GridPos]()

  private var projectiles = Buffer[Projectile]()

  def addProjectile(projectile: Projectile) = projectiles += projectile

  def removeProjectile(projectile: Projectile) = {
    if(this.projectiles.contains(projectile)) projectiles -= projectile
  }

  def getEnemySquares = {
    var locationList = Buffer[GridPos]()
    var filtered = this.allElements.filter(_.isInstanceOf[EnemySquare])
    filtered.map(_.asInstanceOf[EnemySquare])
  }

  def getEnemySpawn = enemyTravelPath.head

  def getRecruitSquares = this.allElements.filter(_.isInstanceOf[RecruitSquare]).map(_.asInstanceOf[RecruitSquare])

  def getRecruits = getRecruitSquares.map(_.getRecruit)

  def placeRecruit(recruit: Recruit, location: GridPos) = {
    if(this.elementAt(location).isFree && this.elementAt(location).isInstanceOf[MapSquare]) {
       this.update(location, new RecruitSquare(location.x, location.y, recruit))
       recruit.setLocation(this.elementAt(location).asInstanceOf[RecruitSquare])
    }
  }

  def removeRecruit(location: GridPos) = {
     if(this.elementAt(location).isInstanceOf[RecruitSquare]) {
        this.update(location, new MapSquare(location.x, location.y))
     }
  }

  def placeEnemy(enemy: Enemy, location: GridPos) = {
    if(this.elementAt(location).isInstanceOf[EnemyPathSquare] && !this.elementAt(location).isOccupied) {
       this.update(location, new EnemySquare(location.x, location.y, enemy))
    }
  }

  def removeEnemy(location: GridPos) = {
     if(this.elementAt(location).isInstanceOf[EnemyPathSquare]) {
        this.update(location, new MapSquare(location.x, location.y))
     }
  }

  def initializeEnemyPath(vector: Vector[GridPos]) = {
     for(each <- vector) {
       this.update(each, new EnemyPathSquare(each.x, each.y))
       enemyTravelPath += this.elementAt(new GridPos(each.x, each.y))
     }
  }

  def enemyPath: Vector[MapSquare] = ???

  def initialElements = for (y <- 0 until this.height; x <- 0 until this.width) yield new MapSquare(x, y)
}

class MapSquare(x: Int, y: Int) extends GridPos(x, y) {

  def getX = x
  def getY = y
  def getGridPos = new GridPos(x, y)

  var enemyPath = false
  var free = true
  var occupied = false

  def isEnemyPath = enemyPath
  def isFree = free
  def isOccupied = occupied


  def levelNeighbor(direction: CompassDir): MapSquare = {
      this.neighbor(direction) match {
        case square: MapSquare => square
        case _ => new MapSquare(0, 0)
      }
  }
}

class RecruitSquare(x: Int, y: Int, recruit: Recruit) extends MapSquare(x, y) {
  free = false
  occupied = true

  def getRecruit = recruit

}

class EnemyPathSquare(x: Int, y: Int) extends MapSquare(x, y) {
  free = false
  occupied = false

}

class EnemySquare(x: Int, y: Int, enemy: Enemy) extends EnemyPathSquare(x, y) {
  occupied = true
  def getEnemy = enemy
}