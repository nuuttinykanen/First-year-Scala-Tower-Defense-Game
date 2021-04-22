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
  def addStrengthModifier(amount: Int) = this.strengthModifiers += amount
  def addRangeModifier(amount: Int) = this.rangeModifiers += amount

  def removeModifiers() = {
    cooldownModifiers = 0
    strengthModifiers = 0
    rangeModifiers = 0
  }

  var strengthModifiers = 0

  var currentStrength = this.strength + this.strengthModifiers

}

class Suzy extends        AttackRecruit("Suzy Bannion",      "",  4, 2, 2, 200,                  None)
class Simon extends       AttackRecruit("Simon Belmont",     "",  5, 3, 2, 300,                  None)
class VanHelsing extends  AttackRecruit("Van Helsing",       "",  8, 5, 3, 600,                  None)
class Ash extends         AttackRecruit("Ash",               "", 12, 2, 3, 800, Some(new ChainsawAsh))
class ChainsawAsh extends AttackRecruit("Ash with Chainsaw", "", 20, 3, 3, 700,                  None)
class MacReady extends    AttackRecruit("R.J.",              "", 10, 3, 5, 450,                  None)
class Venkman extends     AttackRecruit("Dr. Venkman",       "", 12, 4, 6, 650,                  None)

