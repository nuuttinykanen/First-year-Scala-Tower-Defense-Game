import scala.math.abs

class Projectile(strength: Int, target: Enemy, square: MapSquare, map: LevelMap) {

  private var currentLocation = square
  def getLocation = currentLocation

  def getTargetLocation = map.getEnemySquares.find(_.getEnemy == target)

  def act() = {
    if(this.currentLocation == target.getLocation) {
      hit()
    }
    else move()
  }

  def hit() = {
     target.changeHealth(-1 * strength)()
     map.removeProjectile(this)
  }

  def move() = {
   if(this.currentLocation.distance(getTargetLocation.get) == 1) None
    else {
    if(abs(this.currentLocation.xDiff(getTargetLocation.get)) >= abs(this.currentLocation.yDiff(getTargetLocation.get))) {
       this.currentLocation.xDirectionOf(getTargetLocation.get) match {
         case Some(way) => this.currentLocation = this.currentLocation.levelNeighbor(way)
         case None =>
       }
    }
    else {
       this.currentLocation.yDirectionOf(getTargetLocation.get) match {
         case Some(way) => this.currentLocation = this.currentLocation.levelNeighbor(way)
         case None =>
       }
    }
  }
 }
}
