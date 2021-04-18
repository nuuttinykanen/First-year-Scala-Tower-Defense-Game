package towerDefense

abstract class Enemy(health: Int, attack: Int, bounty: Int, innerEnemy: Option[Enemy]) {


private var currentHealth = health
private var currentBounty = bounty

def getHealth: Int    = currentHealth
def getAttack: Int = this.attack
def getBounty: Int = currentBounty


def changeHealth(amount: Int)() = currentHealth += amount

def getInnerEnemy = this.innerEnemy

def getName = this.getClass.toString.drop(5).trim
}

class Zombie extends Enemy(5, 1, 1, None)

class ZombieCarriage extends Enemy(10, 3, 1, Some(new Zombie))

class MichaelMyers extends Enemy(1000, 10, 1, None)

class Dracula extends Enemy(25, 10, 1, Some(new Bat))

class Bat extends Enemy(4, 10, 1, None)


