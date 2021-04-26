package towerDefense

abstract class AttackRecruit(name: String, description: String, strength: Int, range: Int, cooldown: Int, cost: Int, upgrade: Option[Recruit]) extends Recruit(name, description, strength, range, cost, upgrade) {

  var cooldownModifiers = 0
  def currentCooldown = {
     if(this.cooldown + cooldownModifiers > 0) this.cooldown + cooldownModifiers
     else 0
  }
  def getCooldown = currentCooldown
  def cooldownPerc = {
      if(currentCooldown > 0) attackCounter.toDouble / currentCooldown.toDouble
      else 0
  }
  var attackCounter = 0

  def canAttack = attackCounter == 0
  def restartCooldown() = attackCounter = currentCooldown
  def reload() = {
    if(attackCounter > 0) attackCounter -= 1
  }

  var rangeModifiers = 0
  private def currentRange = this.range + rangeModifiers
  override def getRange = this.currentRange

  def addCooldownModifier(amount: Int) = this.cooldownModifiers += amount
  def addRangeModifier(amount: Int) = this.rangeModifiers += amount

  def removeModifiers() = {
    cooldownModifiers = 0
    rangeModifiers = 0
  }

}

class Suzy extends        AttackRecruit("Suzy Bannion",      "",  4, 2, 3, 200,                  Some(new DancerSuzy))
class DancerSuzy extends AttackRecruit("Suzy, Dancer of the Dark", "", 6, 3, 2, 450,             None)
class Simon extends       AttackRecruit("Simon Belmont",     "",  5, 3, 3, 300,                  Some(new VampKillerSimon))
class VampKillerSimon extends AttackRecruit("Vampire Killer Simon", "", 7, 3, 2, 500,                None)
class VanHelsing extends  AttackRecruit("Van Helsing",       "",  8, 5, 5, 600,                  Some(new SlayerHelsing))
class SlayerHelsing extends AttackRecruit("Vampire Slayer Helsing", "", 10, 7, 4, 900,           None)
class Ash extends         AttackRecruit("Ash",               "", 12, 2, 5, 800, Some(new ChainsawAsh))
class ChainsawAsh extends AttackRecruit("Ash with Chainsaw", "", 22, 3, 5, 1400,                 None)
class MacReady extends    AttackRecruit("R.J.",              "", 10, 3, 6, 450,     Some(new FlameRJ))
class FlameRJ extends     AttackRecruit("Flameburster R.J.", "", 13, 3, 5, 400,   Some(new InfernoRJ))
class InfernoRJ extends   AttackRecruit("Inferno R.J.",      "", 16, 4, 5, 550,                  None)
class Venkman extends     AttackRecruit("Dr. Venkman",       "", 12, 4, 7, 650,                  Some(new CaptVenkman))
class CaptVenkman extends AttackRecruit("Ghost Capturer Venkman", "", 14, 5, 6, 700,             Some(new HunterVenkman))
class HunterVenkman extends AttackRecruit("Ghost Hunter Venkman", "", 15, 5, 5, 750,              None)

