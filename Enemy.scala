import o1.grid.CompassDir.{East, North, South, West}
import o1.grid.GridPos

abstract class Enemy(health: Int, attack: Int, speed: Int, moneyDrop: Int, status: Status, immunitites: Vector[Status], innerEnemy: Option[Enemy]) {

private var currentHealth = health
private var currentSpeed  = speed
private var currentStatus = status

def getHealth: Int    = currentHealth
def getSpeed: Int     = currentSpeed
def getStatus: Status = currentStatus
def getAttack: Int = this.attack

def changeSpeed(amount: Int)() = currentSpeed = currentSpeed + amount
def changeHealth(amount: Int)() = currentHealth += amount
def changeStatus(newStatus: Status)() = currentStatus = newStatus

def getInnerEnemy = this.innerEnemy

def getName = this.getClass.toString
}

class Zombie extends Enemy(5, 1, 10, 10, new Confused, Vector[Status](), None)

class ZombieCarriage extends Enemy(10, 3, 10, 10, new Confused, Vector[Status](), Some(new Zombie))

class MichaelMyers extends Enemy(20, 10, 10, 10, new Confused, Vector[Status](), None)


