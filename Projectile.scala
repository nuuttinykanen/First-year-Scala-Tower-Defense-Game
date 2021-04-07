import scala.math.abs

class Projectile(strength: Int, target: Enemy, square: MapSquare, map: LevelMap) {

  private var currentLocation = square
  def getLocation = currentLocation

  def getTargetLocation = target.getLocation

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
   if(this.currentLocation.distance(getTargetLocation) == 1) None
    else {
    if(abs(this.currentLocation.xDiff(getTargetLocation)) >= abs(this.currentLocation.yDiff(getTargetLocation))) {
       this.currentLocation.xDirectionOf(getTargetLocation) match {
         case Some(way) => this.currentLocation = this.currentLocation.levelNeighbor(way)
         case None =>
       }
    }
    else {
       this.currentLocation.yDirectionOf(getTargetLocation) match {
         case Some(way) => this.currentLocation = this.currentLocation.levelNeighbor(way)
         case None =>
       }
    }
  }
 }
}
