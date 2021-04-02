import o1.grid.CompassDir.{East, North, South, West}
import o1.grid.GridPos

abstract class Enemy(health: Int, speed: Int, moneyDrop: Int, status: Status, immunitites: Vector[Status], enemiesInside: Vector[Enemy]) {

private var currentLocation: MapSquare = new MapSquare(0, 0)
private var pastLocation = new MapSquare(0, 0)

def getLocation = currentLocation
def changeLocation(newLoc: GridPos) = currentLocation
def getPastLocation = pastLocation

def move() = {
   val dirs = Vector(North, East, South, West)
   var current = this.currentLocation
   var next = {
     var list = collection.mutable.Buffer[MapSquare]()
     for(each <- dirs) {
       val neighbor = currentLocation.levelNeighbor(each)
       if(neighbor.isEnemyPath && neighbor != pastLocation) {
         list += neighbor
       }
     }
     list.head
   }
  this.changeLocation(next)
}

def reverseMove() = {
   val dirs = Vector(North, East, South, West)
   var current = this.currentLocation
   var next = {
     var list = collection.mutable.Buffer[MapSquare]()
     for(each <- dirs) {
       val neighbor = currentLocation.levelNeighbor(each)
       if(neighbor.isEnemyPath && neighbor == pastLocation) {
         list += neighbor
       }
     }
     list.head
   }
  this.changeLocation(next)
}

private var currentHealth = health
private var currentSpeed  = speed
private var currentStatus = status

def getHealth: Int    = currentHealth
def getSpeed: Int     = currentSpeed
def getStatus: Status = currentStatus

def changeSpeed(amount: Int)() = currentSpeed = currentSpeed + amount
def changeHealth(amount: Int)() = currentHealth = currentHealth + amount
def changeStatus(newStatus: Status)() = currentStatus = newStatus

}

class Zombie extends Enemy(20, 10, 10, new Confused, Vector[Status](), Vector[Enemy]())

class ZombieCarriage extends Enemy(20, 10, 10, new Confused, Vector[Status](), Vector[Enemy](new Zombie, new Zombie, new Zombie))

class MichaelMyers extends Enemy(20, 10, 10, new Confused, Vector[Status](), Vector[Enemy](new Zombie, new Zombie, new Zombie))


