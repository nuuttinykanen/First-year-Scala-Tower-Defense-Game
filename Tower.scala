import o1.grid.GridPos

abstract class Tower(name: String, description: String, range: Int, cost: Int, map: Map, location: GridPos) {

  def getCost = cost
}
