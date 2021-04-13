import scala.math.abs

class Projectile(strength: Int, target: Enemy, square: MapSquare, map: LevelMap) {

  private var currentLocation = square
  def getLocation = currentLocation

  def getTargetLocation = map.getEnemySquares.find(_.getEnemy == target)

  def move() = {
    if(abs(this.currentLocation.xDiff(getTargetLocation.get)) >= abs(this.currentLocation.yDiff(getTargetLocation.get))) {
       this.currentLocation.xDirectionOf(getTargetLocation.get) match {
         case Some(way) => this.currentLocation = this.map.squareNeighbor(this.currentLocation, way)
         case None =>
       }
    }
    else {
       this.currentLocation.yDirectionOf(getTargetLocation.get) match {
         case Some(way) => this.currentLocation = this.map.squareNeighbor(this.currentLocation, way)
         case None =>
       }
    }
    if(this.currentLocation.distance(this.target.getLocation) < 2) {
       target.changeHealth(strength)()
       this.map.removeProjectile(this)
    }
  }

}
