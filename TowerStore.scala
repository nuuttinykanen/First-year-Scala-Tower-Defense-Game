import scala.collection.mutable.Buffer
class TowerStore {

  // Towers that are available to purchase during the game.
  private val towers = Buffer[Tower]()

  def getTowers: Vector[Tower] = towers.toVector

  def getCosts = getTowers.map(_.getCost)

  def addTower(tower: Tower) = towers += tower

  def removeTower(tower: Tower) = towers -= tower

}
