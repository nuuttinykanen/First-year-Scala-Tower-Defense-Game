import o1.grid.GridPos
import scala.math.abs
import scala.swing

abstract class AttackRecruit(name: String, description: String, range: Int, cost: Int, map: LevelMap, strength: Int, upgrade: Option[Recruit]) extends Recruit(name, description, range, cost, map, upgrade) {

  def getStrength = this.strength

  def attack() = {
     if(getTargetLocation.isDefined) {
       val newProjectile = createProjectile
       this.map.addProjectile(newProjectile)
     }
  }

  def getTargetLocation: Option[EnemySquare] = {
     if(enemiesInRange.nonEmpty) map.getEnemyLocation(this.enemiesInRange.head)
     else None
  }

  def createProjectile = {
     val target = this.enemiesInRange.head
     val spawnLoc: MapSquare = {
        if(abs(this.getLocation.xDiff(getTargetLocation.get)) >= abs(this.getLocation.yDiff(getTargetLocation.get))) {
          this.getLocation.xDirectionOf(getTargetLocation.get) match {
           case Some(way) => this.map.squareNeighbor(this.getLocation, way)
           case None => this.getLocation
           }
        }
       else {
        this.getLocation.yDirectionOf(getTargetLocation.get) match {
          case Some(way) => this.map.squareNeighbor(this.getLocation, way)
          case None => this.getLocation
        }
       }
     }
   new Projectile(this.getStrength, target, spawnLoc, this.map)
 }
}

class Simon(map: LevelMap) extends AttackRecruit("Simon Belmont", "", 10, 1, map, 5, None) {
}

