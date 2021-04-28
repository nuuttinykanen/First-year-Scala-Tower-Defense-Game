package towerDefense

abstract class AttackRecruit(name: String, description: String, strength: Int, range: Int, cooldown: Int, cost: Int, upgrade: Option[Recruit]) extends Recruit(name, description, strength, range, cost, upgrade) {

  var cooldownModifiers = 0
  def currentCooldown = {
     if(this.cooldown + cooldownModifiers > 0) this.cooldown + cooldownModifiers
     else 0
  }

  def cooldownPercentage = {
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

class Suzy extends        AttackRecruit("Suzy Bannion",      "",  4, 2, 3, 250,                  Some(new DancerSuzy))
class DancerSuzy extends AttackRecruit("Suzy, Dancer of the Dark", "", 6, 3, 2, 450,             None)
class Simon extends       AttackRecruit("Simon Belmont",     "",  5, 3, 4, 300,                  Some(new VampKillerSimon))
class VampKillerSimon extends AttackRecruit("Vampire Killer Simon", "", 7, 3, 2, 540,                None)
class VanHelsing extends  AttackRecruit("Van Helsing",       "",  6, 5, 4, 650,                  Some(new SlayerHelsing))
class SlayerHelsing extends AttackRecruit("Vampire Slayer Helsing", "", 9, 6, 4, 900,           None)
class Ash extends         AttackRecruit("Ash",               "", 12, 2, 7, 850, Some(new ChainsawAsh))
class ChainsawAsh extends AttackRecruit("Ash with Chainsaw", "", 22, 2, 6, 1400,                 None)
class MacReady extends    AttackRecruit("R.J.",              "", 10, 3, 7, 450,     Some(new FlameRJ))
class FlameRJ extends     AttackRecruit("Flameburster R.J.", "", 13, 3, 6, 600,   Some(new InfernoRJ))
class InfernoRJ extends   AttackRecruit("Inferno R.J.",      "", 16, 4, 6, 650,                  None)
class Venkman extends     AttackRecruit("Dr. Venkman",       "", 12, 4, 10, 650,                  Some(new CaptVenkman))
class CaptVenkman extends AttackRecruit("Ghost Capturer Venkman", "", 14, 5, 9, 700,             Some(new HunterVenkman))
class HunterVenkman extends AttackRecruit("Ghost Hunter Venkman", "", 15, 5, 8, 750,              None)

