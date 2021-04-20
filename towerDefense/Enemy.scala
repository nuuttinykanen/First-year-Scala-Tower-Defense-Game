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

class Zombie extends Enemy(100, 1, 5, None)

class ZombieCarriage extends Enemy(10, 3, 10, Some(new Zombie))

class MichaelMyers extends Enemy(1000, 10, 50, None)

class Dracula extends Enemy(25, 10, 100, None)

class Bat extends Enemy(4, 10, 2, Some(new Dracula))


