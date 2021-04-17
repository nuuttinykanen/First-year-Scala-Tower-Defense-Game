import o1.grid._

import collection.mutable.Buffer
import scala.math.abs
class LevelMap(size: Int) extends Grid[MapSquare](size, size) {

  /** SETUP */

  def initialElements = for (y <- 0 until this.height; x <- 0 until this.width) yield new MapSquare(x, y)

  def initializeEnemyPath(vector: Vector[GridPos]) = {
     for(each <- vector) {
       this.update(each, new EnemyPathSquare(each.x, each.y))
       enemyTravelPath += this.elementAt(new GridPos(each.x, each.y))
     }
  }

  /** ENEMIES */

  var enemyTravelPath = Buffer[GridPos]()

  def getEnemySquares = {
    var locationList = Buffer[GridPos]()
    var filtered = this.allElements.filter(_.isInstanceOf[EnemySquare])
    filtered.map(_.asInstanceOf[EnemySquare])
  }

  def getEnemiesOnPath = {
    val elements = this.enemyTravelPath.map(n => this.elementAt(n))
    elements.filter(_.isInstanceOf[EnemySquare]).map(_.asInstanceOf[EnemySquare]).reverse
  }

  def moveEnemy(enemySquare: EnemySquare)() = {
    val currentIndex = enemyTravelPath.indexOf(enemySquare)
    val currentPos = enemyTravelPath(currentIndex)
    val newCurrentSquare = new EnemyPathSquare(currentPos.x, currentPos.y)

    this.update(currentPos, newCurrentSquare)

    val nextIndex = currentIndex + 1
    val nextPos = enemyTravelPath(nextIndex)
    val newSquare = new EnemySquare(nextPos.x, nextPos.y, enemySquare.getEnemy)

    this.update(nextPos, newSquare)
  }

  def moveEnemies() = {
    if(this.penaltyHealth.isDefined) this.penaltyHealth = None
    checkLastSquare()
    this.getEnemiesOnPath.foreach(moveEnemy(_)())
  }


  def getEnemySpawn = {
    if(enemyTravelPath.nonEmpty) enemyTravelPath.head
    else GridPos(0, 0)
  }

  def getEnemyLocation(target: Enemy): Option[EnemySquare] = {
    if(this.getEnemySquares.nonEmpty) this.getEnemySquares.find(_.getEnemy == target)
    else None
  }

  def removeAllEnemies() = this.getEnemySquares.foreach(n => this.removeEnemy(GridPos(n.x, n.y)))

   def placeEnemy(enemy: Enemy, location: GridPos) = {
    if(this.elementAt(location).isInstanceOf[EnemyPathSquare] && !this.elementAt(location).isOccupied) {
       this.update(location, new EnemySquare(location.x, location.y, enemy))
    }
    else println("FAILED TO PLACE ENEMY")
  }

  def removeEnemy(location: GridPos) = {
     this.elementAt(location) match {
       case square: EnemySquare => {
          if(replaceWithInner(square.getEnemy)) {
            println("REPLACED CARRIAGE")
          }
          else {
            this.update(location, new EnemyPathSquare(location.x, location.y))
            println("REMOVED ENEMY")
          }
       }
       case _ =>
     }
  }

  private var penaltyHealth: Option[Int] = None
  def getPenaltyHealth = this.penaltyHealth


  /** PROJECTILES */

  private var projectiles = Buffer[Projectile]()
  def getProjectiles = projectiles
  def addProjectile(projectile: Projectile) = projectiles += projectile
  def removeProjectile(projectile: Projectile) = {
    if(this.projectiles.contains(projectile)) projectiles -= projectile
  }
  def scanProjectiles() = {
    val list = this.getProjectiles.filter(_.getTargetLocation.isEmpty)
    if(list.nonEmpty) list.foreach(this.removeProjectile(_))
  }



  def getLastSquare = {
    val list = this.getEnemySquares.filter(_ == this.enemyTravelPath.last)
    if(list.nonEmpty) list.headOption
    else None
  }

  def checkLastSquare() = {
    if(getLastSquare.isDefined) {
     this.penaltyHealth = Some(getLastSquare.get.getEnemy.getAttack)
     println(s"${getLastSquare.get.getEnemy.getName} reached the end!")
     this.removeEnemy(getLastSquare.get)
    }
  }

  def replaceWithInner(enemy: Enemy): Boolean = {
    if(enemy.getInnerEnemy.isDefined && this.getEnemyLocation(enemy).isDefined && this.getEnemyLocation(enemy).get != this.enemyTravelPath.last) {
       val currentLoc = this.getEnemyLocation(enemy)
       this.update(currentLoc.get, new EnemySquare(currentLoc.get.x, currentLoc.get.y, enemy.getInnerEnemy.get))
       true
    }
    else false
  }

  def healthCheckRemoval() = {
     val list = this.getEnemySquares.map(_.getEnemy).filter(_.getHealth < 1)
     if(list.nonEmpty) {
       list.foreach(n => if(this.getEnemySquares.exists(_.getEnemy == n)) this.removeEnemy(this.getEnemySquares.find(_.getEnemy == n).get))
       this.getProjectiles.foreach(n => if(n != null && list.contains(n.getTarget)) this.removeProjectile(n))
     }
  }

  def getRecruitSquares = this.allElements.filter(_.isInstanceOf[RecruitSquare]).map(_.asInstanceOf[RecruitSquare])

  def getAttackRecruits: Vector[AttackRecruit] = getRecruitSquares.map(_.getRecruit).filter(_.isInstanceOf[AttackRecruit]).map(_.asInstanceOf[AttackRecruit])

  def getSupportRecruits: Vector[SupportRecruit] = getRecruitSquares.map(_.getRecruit).filter(_.isInstanceOf[SupportRecruit]).map(_.asInstanceOf[SupportRecruit])

  def getRecruits = getRecruitSquares.map(_.getRecruit)

  def placeRecruit(recruit: Recruit, location: GridPos) = {
    if(this.elementAt(location).isFree && this.elementAt(location).isInstanceOf[MapSquare]) {
       this.update(location, new RecruitSquare(location.x, location.y, recruit))
       recruit.setLocation(this.elementAt(location).asInstanceOf[RecruitSquare])
    }
  }

  def removeRecruit(location: GridPos) = {
     if(this.elementAt(location).isInstanceOf[RecruitSquare]) {
        this.update(location, new MapSquare(location.x, location.y))
     }
  }

  def upgradeRecruit(recruit: Recruit) = {
     val thisSquare = this.getRecruitSquares.find(_.getRecruit == recruit)
     if(recruit.getUpgrade.isDefined && thisSquare.isDefined) {
        this.update(thisSquare.get, new RecruitSquare(thisSquare.get.x, thisSquare.get.y, recruit.getUpgrade.get))
     }
  }

  def enemiesInRange(recruit: Recruit): Vector[Enemy] = {
    var enemyList = Buffer[Enemy]()

    def scanRange(location: RecruitSquare) = {
      val list = this.getEnemySquares.filter(_.distance(location) <= recruit.getRange)
      list.foreach(enemyList += _.getEnemy)
    }

    scanRange(recruit.getLocation)
    enemyList.toVector
  }

  def attack(recruit: AttackRecruit) = {
     if(getTargetLocation(recruit).isDefined && recruit.canAttack) {
       val newProjectile = createProjectile(recruit)
       this.addProjectile(newProjectile)
       recruit.restartCooldown()
     }
     else if(getTargetLocation(recruit).isDefined) recruit.reload()
  }

  def getTargetLocation(recruit: AttackRecruit): Option[EnemySquare] = {
     if(enemiesInRange(recruit).nonEmpty) this.getEnemyLocation(enemiesInRange(recruit).head)
     else None
  }

  def createProjectile(recruit: AttackRecruit) = {
     val target = enemiesInRange(recruit).head
     val spawnLoc: MapSquare = {
        if(abs(recruit.getLocation.xDiff(getTargetLocation(recruit).get)) >= abs(recruit.getLocation.yDiff(getTargetLocation(recruit).get))) {
          recruit.getLocation.xDirectionOf(getTargetLocation(recruit).get) match {
           case Some(way) => this.squareNeighbor(recruit.getLocation, way)
           case None => recruit.getLocation
           }
        }
       else {
        recruit.getLocation.yDirectionOf(getTargetLocation(recruit).get) match {
          case Some(way) => this.squareNeighbor(recruit.getLocation, way)
          case None => recruit.getLocation
        }
       }
     }
   new Projectile(recruit.getStrength, target, spawnLoc, this)
 }

 def supportAura(recruit: SupportRecruit) = {
    var auraList = collection.mutable.Buffer[MapSquare]()
    def scanRange(location: RecruitSquare) = {
      val list = this.allElements.filter(_.distance(location) <= recruit.getRange)
      list.foreach(auraList += _)
    }
    scanRange(recruit.getLocation)
    auraList.foreach(n => recruit.supportEffect(n))
 }

 def removeTempModifiers() = {
   this.getAttackRecruits.foreach(_.removeModifiers())
 }

  def squareNeighbor(square: MapSquare, direction: CompassDir) = this.elementAt(square.neighbor(direction))

}

class MapSquare(x: Int, y: Int) extends GridPos(x, y) {

  def getX = x
  def getY = y
  def getGridPos = new GridPos(x, y)

  var enemyPath = false
  var free = true
  var occupied = false

  def isEnemyPath = enemyPath
  def isFree = free
  def isOccupied = occupied
}

class RecruitSquare(x: Int, y: Int, recruit: Recruit) extends MapSquare(x, y) {
  free = false
  occupied = true

  def getRecruit = recruit
}

class EnemyPathSquare(x: Int, y: Int) extends MapSquare(x, y) {
  free = false
  occupied = false

}

class EnemySquare(x: Int, y: Int, enemy: Enemy) extends EnemyPathSquare(x, y) {
  occupied = true
  def getEnemy = enemy
}