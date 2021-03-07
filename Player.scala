import scala.collection.mutable
import scala.collection.mutable.Buffer

class Player(name: String, health: Int, money: Int) {

 private var currentHealth = health
 private var currentMoney = money
 private var currentTowers = Buffer[Tower]()

 def getHealth: Int = currentHealth
 def getMoney: Int = currentMoney
 def getTowers: Buffer[Tower] = currentTowers


 def affordableTowers(pool: TowerStore): Vector[Tower] = {
    def compareCost(tower: Tower, list: Buffer[Tower]) = if(tower.getCost < this.getMoney) list += tower
    var affordable = Buffer[Tower]()
    pool.getTowers.foreach(compareCost(_, affordable))
    affordable.toVector
 }

 def buyTower(tower: Tower)() = ???
 def sellTower(tower: Tower)() = ???

 def upgradeTower(tower: Tower)() = ???
}
