import o1.grid._

import collection.mutable.Buffer
import scala.math.abs

abstract class Recruit(name: String, description: String, range: Int, cost: Int, map: LevelMap) {

  def getName = this.name

  def setLocation(square: RecruitSquare) = currentLocation = square

  private var currentLocation = new RecruitSquare(0, 0, this)
  private var sellPrice = (cost / 8) * 5

  def getSellPrice = sellPrice
  def getCost = cost

  def enemiesInRange: Vector[Enemy] = {
    var enemyList = Buffer[Enemy]()

    def scanRange(location: RecruitSquare) = {
      val list = this.map.getEnemySquares.filter(_.distance(location) <= this.range)
      list.foreach(enemyList += _.getEnemy)
    }

    scanRange(this.currentLocation)
    enemyList.toVector
  }

  def attack() = {
     if(this.enemiesInRange.nonEmpty) {
       val newProjectile = createProjectile
       this.map.addProjectile(newProjectile)
     }
  }

  def createProjectile = {
    val target = this.enemiesInRange.head
    val spawnLoc: MapSquare = {
        if(abs(this.currentLocation.xDiff(target.getLocation)) >= abs(this.currentLocation.yDiff(target.getLocation))) {
          this.currentLocation.xDirectionOf(target.getLocation) match {
           case Some(way) => this.map.squareNeighbor(this.currentLocation, way)
           case None => this.currentLocation
           }
        }
       else {
        this.currentLocation.yDirectionOf(target.getLocation) match {
          case Some(way) => this.map.squareNeighbor(this.currentLocation, way)
          case None => this.currentLocation
        }
       }
   }
   new Projectile(20, target, spawnLoc, this.map)
 }

}
