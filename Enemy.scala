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



