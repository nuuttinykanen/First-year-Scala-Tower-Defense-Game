import o1.grid.GridPos
import scala.math.abs
import scala.swing

abstract class AttackRecruit(name: String, description: String, range: Int, cost: Int, strength: Int, upgrade: Option[Recruit]) extends Recruit(name, description, range, cost, upgrade) {

  def getStrength = this.strength
}

class Simon extends AttackRecruit("Simon Belmont", "", 10, 1, 5, None)

class VanHelsing extends AttackRecruit("Van Helsing", "", 30, 1, 5, None)

class Ash extends AttackRecruit("Ash", "", 2, 1, 20, Some(new ChainsawAsh))

class ChainsawAsh extends AttackRecruit("Ash with Chainsaw", "", 3, 1, 30, None)


