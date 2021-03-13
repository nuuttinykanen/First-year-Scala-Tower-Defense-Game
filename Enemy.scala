import lib.grid.GridPos

abstract class Enemy(health: Int, speed: Int, moneyDrop: Int, status: Status, immunitites: Vector[Status], enemiesInside: Vector[Enemy]) {

private var currentLocation: GridPos = new GridPos(0, 0)
def getLocation = currentLocation
def changeLocation(newLoc: GridPos) = currentLocation = newLoc

private var currentHealth = health
private var currentSpeed  = speed
private var currentStatus = status

def getHealth: Int    = currentHealth
def getSpeed: Int     = currentSpeed
def getStatus: Status = currentStatus

def changeSpeed(amount: Int)() = currentSpeed = currentSpeed + amount
def changeHealth(amount: Int)() = currentHealth = currentHealth + amount
def changeStatus(newStatus: Status)() = currentStatus = newStatus

def move(): Unit
}

class Zombie extends Enemy(20, 10, 10, new Status, Vector[Status](), Vector[Enemy]()) {
 def move() = changeHealth(20)()
}

class ZombieCarriage extends Enemy(20, 10, 10, new Status, Vector[Status](), Vector[Enemy](new Zombie, new Zombie, new Zombie)) {
 def move() = changeHealth(30)()
}



