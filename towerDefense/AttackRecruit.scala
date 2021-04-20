package towerDefense

abstract class AttackRecruit(name: String, description: String, range: Int, cooldown: Int, cost: Int, strength: Int, upgrade: Option[Recruit]) extends Recruit(name, description, range, cost, upgrade) {

  var cooldownModifiers = 0
  def currentCooldown = {
     if(this.cooldown + cooldownModifiers > 0) this.cooldown + cooldownModifiers
     else 0
  }
  def getCooldown = currentCooldown

  var attackCounter = 0

  def canAttack = attackCounter == 0
  def restartCooldown() = attackCounter = currentCooldown
  def reload() = {
    if(attackCounter > 0) attackCounter -= 1
  }

  def addCooldownModifier(amount: Int) = this.cooldownModifiers += amount
  def addStrengthModifier(amount: Int) = this.strengthModifiers += amount

  def removeModifiers() = {
    cooldownModifiers = 0
    strengthModifiers = 0
  }

  var strengthModifiers = 0

  var currentStrength = this.strength + this.strengthModifiers
  def getStrength = this.currentStrength

}

class Simon extends AttackRecruit("Simon Belmont", "", 5, 1, 2, 5, None)

class VanHelsing extends AttackRecruit("Van Helsing", "", 5, 2, 1, 5, None)

class Ash extends AttackRecruit("Ash", "", 5, 10, 4, 8, Some(new ChainsawAsh))

class ChainsawAsh extends AttackRecruit("Ash with Chainsaw", "", 5, 4, 1, 16, None)

class Suzy extends AttackRecruit("Suzy Bannion", "", 3, 2, 1, 16, None)

class MacReady extends AttackRecruit("R.J.", "", 5, 2, 1, 16, None)

class Venkman extends AttackRecruit("Dr.Venkman", "", 3, 2, 1, 16, None)

