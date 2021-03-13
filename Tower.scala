import lib.grid.GridPos

abstract class Tower(name: String, description: String, range: Int, cost: Int, map: Map, location: GridPos) {

  def sellPrice = ???

  def getCost = cost
}
