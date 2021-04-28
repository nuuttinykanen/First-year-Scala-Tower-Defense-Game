package towerDefense

import scala.math.abs

class Projectile(sender: AttackRecruit, strength: Int, target: Enemy, square: MapSquare, map: LevelMap) {

  def getSender = this.sender

  def getTarget = this.target

  private var currentLocation = square
  def getLocation = currentLocation
  def getTargetLocation = this.map.getEnemyLocation(target)

  def move(count: Int): Unit = {
    if(this.getTargetLocation.isDefined) {
      // IF THE DIFFERENCE IS GREATER ON THE X-AXIS, FIND THE DIRECTION IN WHICH TO APPROACH ON THE X-AXIS. ELSE SAME ON THE Y-AXIS
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
        this.map.removeProjectile(this)
      }
   }
    else this.map.removeProjectile(this)
  }

}
