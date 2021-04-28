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

class Zombie extends Enemy(4, 1, 19, None)

class ZombieHorde extends Enemy(8, 2, 40, Some(new Zombie))

class MichaelMyers extends Enemy(80, 5, 100, None)

class Dracula extends Enemy(150, 8, 400, None)

class Bat extends Enemy(4, 10, 1, Some(new Dracula))


