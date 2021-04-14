import scala.math.abs

class Projectile(strength: Int, target: Enemy, square: MapSquare, map: LevelMap) {

  def getTarget = this.target

  private var currentLocation = square
  def getLocation = currentLocation
  def getTargetLocation = this.map.getEnemyLocation(target)

  def move(count: Int): Unit = {
    if(this.getTargetLocation.isDefined) {
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
      if(abs(this.currentLocation.distance(getTargetLocation.get)) < 1) {
        target.changeHealth(-1 * strength)()
        println("Enemy took damage")
        println(this.map.getProjectiles.map(_.getLocation))
        this.map.removeProjectile(this)
      }
      else if(count > 0) move(count - 1)
   }
    else this.map.removeProjectile(this)
  }

}
