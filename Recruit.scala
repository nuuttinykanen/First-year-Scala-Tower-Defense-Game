import o1.grid._
import collection.mutable.Buffer

abstract class Recruit(name: String, description: String, range: Int, cost: Int, map: LevelMap, location: GridPos) {

  def getName = this.name

  private var currentLocation = location
  private var sellPrice = (cost / 8) * 5

  def getSellPrice = sellPrice
  def getCost = cost

  def enemiesInRange: Vector[Enemy] = {
    var enemyList = Buffer[Enemy]()

    def getEnemy(location: GridPos): Unit = {
       if(this.map.enemyLocations.contains(location)) {
          this.map.enemyLocations.filter(_.getX == location.x).filter(_.getY == location.y).head
       }
    }

    def scanRange(range: Int, location: GridPos): Unit = {
      if(range == 1) this.map.neighbors(location, true).foreach(getEnemy(_))
      else {
         this.map.neighbors(location, true).foreach(scanRange(range - 1, _))
      }
    }
    enemyList.toVector
  }

  def attack() = ???
}

class WhipMan(map: LevelMap, location: GridPos, game: Game) extends Recruit("Belmont", "", 20, 200, map, location) {


}

