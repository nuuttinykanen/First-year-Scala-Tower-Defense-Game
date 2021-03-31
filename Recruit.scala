import o1.grid._
import collection.mutable.Buffer

abstract class Recruit(name: String, description: String, range: Int, cost: Int, map: LevelMap, location: GridPos, game: Game) {

  def getName = this.name

  private var currentLocation = location
  private var sellPrice = (cost / 8) * 5

  def getSellPrice = sellPrice
  def getCost = cost

  def recursiveSquareScan(range: Int) = {
    if(range == 1) this.map.neighbors(this.currentLocation, true)

  }

  def enemiesInRange: Vector[Enemy] = {

     def getEnemy(location: LevelMapSquare, list: Buffer[Enemy]): Vector[Enemy] = {
       var newList = list
       this.game.enemiesPresent.find(_.getLocation == location) match {
         case Some(e) => newList += e
         case None =>
       }
       newList.toVector
     }

     var enemyList = collection.mutable.Buffer[Enemy]()
     def scanRange(range: Int) = {
      if(range == 1) this.map.neighbors(this.currentLocation, true).foreach(getEnemy(_, enemyList))
      else {
       this.map.neighbors(this.currentLocation, true).foreach(scanRange(range -1))
      }
     }
    enemyList.toVector
  }

  def attack() = ???
}

class WhipMan(map: LevelMap, location: GridPos) extends Recruit("Belmont", "", 20, 200, map, location) {


}

