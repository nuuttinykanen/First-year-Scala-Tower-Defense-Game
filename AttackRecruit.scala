import o1.grid.GridPos
import scala.swing
abstract class AttackRecruit(name: String, description: String, range: Int, cost: Int, map: LevelMap, location: GridPos) extends Recruit(name, description, range, cost, map, location) {

}

class Simon(map: LevelMap, location: GridPos) extends AttackRecruit("Simon Belmont", "", 10, 100, map, location) {
}

