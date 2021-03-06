class Enemy(health: Int, speed: Int, moneyDrop: Int, status: Status, immunitites: Vector[Status], enemiesInside: Vector[Enemy]) {

private var currentHealth = health

private var currentSpeed = speed

private var currentStatus = status

def move() = ???

def getHealth: Int = currentHealth

def changeSpeed(newSpeed: Int)() = currentSpeed = newSpeed

def changeHealth(amount: Int)() = currentHealth = currentHealth + amount

def changeStatus(status: Status)() = currentStatus = status

}

