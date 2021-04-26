package towerDefense

abstract class Enemy(health: Int, attack: Int, bounty: Int, innerEnemy: Option[Enemy]) {


private var maxHealth = health
private var currentHealth = health
private var currentBounty = bounty

def getMaxHealth: Int = this.maxHealth
def getHealth: Int    = currentHealth
def getAttack: Int = this.attack
def getBounty: Int = currentBounty

def healthPercentage = this.currentHealth.toDouble / this.health.toDouble


def changeHealth(amount: Int)() = currentHealth += amount

def getInnerEnemy = this.innerEnemy

def getName = this.getClass.toString.drop(5).trim
}

class Zombie extends Enemy(4, 1, 10, None)

class ZombieHorde extends Enemy(12, 3, 30, Some(new Zombie))

class MichaelMyers extends Enemy(110, 10, 90, None)

class Dracula extends Enemy(200, 10, 400, None)

class Bat extends Enemy(4, 10, 2, Some(new Dracula))


