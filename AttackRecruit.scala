import o1.grid.GridPos
import scala.swing
abstract class AttackRecruit(name: String, description: String, range: Int, cost: Int, map: LevelMap) extends Recruit(name, description, range, cost, map) {

}

class Simon(map: LevelMap) extends AttackRecruit("Simon Belmont", "", 10, 1, map) {
}

