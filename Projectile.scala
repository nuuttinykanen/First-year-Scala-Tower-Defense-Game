import scala.math.abs

class Projectile(strength: Int, target: Enemy, square: MapSquare, map: LevelMap) {

  def getTarget = this.target

  private var currentLocation = square
  def getLocation = currentLocation
  def getTargetLocation = this.map.getEnemyLocation(target)

  def move(): Unit = {
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
      if(abs(this.currentLocation.distance(getTargetLocation.get)) >= 0) {
        target.changeHealth(-1 * strength)()
        println("BOO")
        this.map.removeProjectile(this)
      }
   }
    else this.map.removeProjectile(this)
  }

}
