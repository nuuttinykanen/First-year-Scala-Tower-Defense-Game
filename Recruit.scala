import o1.grid._

import collection.mutable.Buffer
import scala.math.abs

abstract class Recruit(name: String, description: String, range: Int, cost: Int, map: LevelMap, upgrade: Option[Recruit]) {

  def getName = this.name

  def setLocation(square: RecruitSquare) = currentLocation = square

  private var currentLocation = new RecruitSquare(0, 0, this)
  def getLocation = currentLocation

  private var sellPrice = (cost / 8) * 5

  def getSellPrice = sellPrice
  def getCost = cost

  def getUpgrade = this.upgrade

  def enemiesInRange: Vector[Enemy] = {
    var enemyList = Buffer[Enemy]()

    def scanRange(location: RecruitSquare) = {
      val list = this.map.getEnemySquares.filter(_.distance(location) <= this.range)
      list.foreach(enemyList += _.getEnemy)
    }

    scanRange(this.currentLocation)
    enemyList.toVector
  }

}
