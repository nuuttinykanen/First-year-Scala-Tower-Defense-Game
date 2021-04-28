package towerDefense

import o1.grid._

import java.io.IOException
import scala.collection.mutable.Buffer
import scala.math.abs
class LevelMap(size: Int) extends Grid[MapSquare](size, size) {

  /** SETUP */

  def initialElements: IndexedSeq[FreeSquare] = for (y <- 0 until this.height; x <- 0 until this.width) yield new FreeSquare(x, y)

  def initializeEnemyPath(vector: Vector[GridPos]) = {
     for(each <- vector) {
       this.update(each, new EnemyPathSquare(each.x, each.y))
       enemyTravelPath += this.elementAt(new GridPos(each.x, each.y))
     }
  }

  /** ENEMIES */

  var enemyTravelPath = Buffer[GridPos]()

  def getEnemySquares = {
    var filtered = this.allElements.filter(_.isInstanceOf[EnemySquare])
    filtered.map(_.asInstanceOf[EnemySquare])
  }

  def getEnemiesOnPath = {
    val elements = this.enemyTravelPath.map(n => this.elementAt(n))
    elements.filter(_.isInstanceOf[EnemySquare]).map(_.asInstanceOf[EnemySquare]).reverse.toVector
  }

  def moveEnemy(square: EnemySquare)() = {
    val currentIndex = enemyTravelPath.indexOf(square)
    val currentPos = enemyTravelPath(currentIndex)
    val newCurrentSquare = new EnemyPathSquare(currentPos.x, currentPos.y)

    this.update(currentPos, newCurrentSquare)

    val nextPos = enemyTravelPath(currentIndex + 1)
    val newSquare = new EnemySquare(nextPos.x, nextPos.y, square.getEnemy)

    this.update(nextPos, newSquare)
  }

  def moveEnemies() = {
    checkLastSquare()
    this.getEnemiesOnPath.foreach(moveEnemy(_)())
  }

  def getEnemySpawn: GridPos = {
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
  }

  def removeEnemy(location: GridPos) = {
     this.elementAt(location) match {
       case square: EnemySquare => {
          if(replaceWithInner(square.getEnemy) && location != this.enemyTravelPath.last) {
          }
          else {
            this.update(location, new EnemyPathSquare(location.x, location.y))
          }
       }
       case _ =>
     }
  }

 def getLastSquare = {
    val list = this.getEnemySquares.filter(_ == this.enemyTravelPath.last)
    if(list.nonEmpty) list.headOption
    else None
 }

 def checkLastSquare() = {
    if(getLastSquare.isDefined) {
     this.penaltyHealth = Some(getLastSquare.get.getEnemy.getAttack)
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
     if(this.bounty.isDefined) emptyBounty()
     emptyDeathMarks()
     val list = this.getEnemySquares.map(_.getEnemy).filter(_.getHealth < 1)
     if(list.nonEmpty) {
       list.foreach(n => if(this.getEnemySquares.exists(_.getEnemy == n)){
         this.deathMarks += this.getEnemySquares.find(_.getEnemy == n).get
         this.removeEnemy(this.getEnemySquares.find(_.getEnemy == n).get)
         if(this.bounty.isDefined) {
            bounty = Some(bounty.get + n.getBounty)
         }
         else bounty = Some(n.getBounty)
       }
       )
       this.getProjectiles.foreach(n => if(n != null && list.contains(n.getTarget)) this.removeProjectile(n))
     }
  }


  // HERE IS THE DAMAGE THE PLAYER TAKES WHEN AN ENEMY REACHES THE END, UPDATED FOR EACH PASS OF TIME.
  private var penaltyHealth: Option[Int] = None
  def getPenaltyHealth = this.penaltyHealth
  def emptyPenaltyHealth() = penaltyHealth = None

  // HERE IS ALL THE BOUNTY GATHERED TO BE GIVEN TO THE PLAYER IN A SINGLE PASS OF TIME.
  private var bounty: Option[Int] = None
  def getBounty = this.bounty
  def emptyBounty() = bounty = None


  // DEATH MARKS MARK WHERE AN ENEMY DIED FOR THE GUI TO DRAW A DEATH SPRITE
  private var deathMarks = collection.mutable.Buffer[MapSquare]()
  def getDeathMarks = this.deathMarks.toVector
  def emptyDeathMarks() = deathMarks = deathMarks.empty

 /** PROJECTILES */

  private var projectiles = Buffer[Projectile]()
  def getProjectiles = projectiles
  def addProjectile(projectile: Projectile) = projectiles += projectile
  def removeProjectile(projectile: Projectile) = {
    if(this.projectiles.contains(projectile)) projectiles -= projectile
  }

  // REMOVE ALL PROJECTILES WITHOUT TARGETS
  def scanProjectiles() = {
    val list = this.getProjectiles.filter(_.getTargetLocation.isEmpty)
    if(list.nonEmpty) list.foreach(this.removeProjectile(_))
  }

  /** RECRUITS */

  def getRecruitSquares: Vector[RecruitSquare] = this.allElements.filter(_.isInstanceOf[RecruitSquare]).map(_.asInstanceOf[RecruitSquare])

  def getAttackRecruits: Vector[AttackRecruit] = getRecruitSquares.map(_.getRecruit).filter(_.isInstanceOf[AttackRecruit]).map(_.asInstanceOf[AttackRecruit])

  def getSupportRecruits: Vector[SupportRecruit] = getRecruitSquares.map(_.getRecruit).filter(_.isInstanceOf[SupportRecruit]).map(_.asInstanceOf[SupportRecruit])

  def getRecruits: Vector[Recruit] = getRecruitSquares.map(_.getRecruit)

  def placeRecruit(recruit: Recruit, location: GridPos) = {
    if(this.contains(location) && this.elementAt(location).isInstanceOf[FreeSquare]) {
       this.update(location, new RecruitSquare(location.x, location.y, recruit))
       recruit.setLocation(this.elementAt(location).asInstanceOf[RecruitSquare])
    }
  }

  def removeRecruit(location: GridPos) = {
     if(this.elementAt(location).isInstanceOf[RecruitSquare]) {
        this.update(location, new FreeSquare(location.x, location.y))
     }
  }

  def matchRecruit(recruit: Recruit) = {
    recruit match {
      case some: Simon => new Simon
      case some: VampKillerSimon =>      new VampKillerSimon
      case some: VanHelsing =>           new VanHelsing
      case some: SlayerHelsing =>        new SlayerHelsing
      case some: Ash =>                  new Ash
      case some: ChainsawAsh =>          new ChainsawAsh
      case some: MacReady =>             new MacReady
      case some: FlameRJ =>              new FlameRJ
      case some: InfernoRJ =>            new InfernoRJ
      case some: CaptVenkman =>          new CaptVenkman
      case some: HunterVenkman =>        new HunterVenkman
      case some: Venkman =>              new Venkman
      case some: Suzy =>                 new Suzy
      case some: DancerSuzy =>           new DancerSuzy
      case some: FatherMerrin =>         new FatherMerrin
      case some: EnlightenedMerrin =>    new EnlightenedMerrin
      case some: LightkeeperMerrin =>    new LightkeeperMerrin
      case some: DrFrankenstein =>       new DrFrankenstein
      case some: MadDrFrankenstein =>    new MadDrFrankenstein
      case some: InsaneDrFrankenstein => new InsaneDrFrankenstein
      case _ => throw new IOException("Failed to find upgrade for recruit!")
      }
  }

  def upgradeRecruit(recruit: Recruit): Option[RecruitSquare] = {
     val thisSquare = this.getRecruitSquares.find(_.getRecruit == recruit)
     if(recruit.getUpgrade.isDefined && thisSquare.isDefined) {
        val newRecruit = matchRecruit(recruit.getUpgrade.get)
        this.update(thisSquare.get, new RecruitSquare(thisSquare.get.x, thisSquare.get.y, newRecruit))
        newRecruit.setLocation(thisSquare.get)
     }
    thisSquare
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


  def squaresInRange(recruit: Recruit, square: MapSquare): Vector[MapSquare] = {
    var squareList = Buffer[MapSquare]()

    def scanRange(location: MapSquare) = {
      val list = this.allElements.filter(_.distance(location) <= recruit.getRange)
      list.foreach(squareList += _)
    }

    scanRange(square)
    squareList.toVector
  }

  def attack(recruit: AttackRecruit) = {
     if(getTargetLocation(recruit).isDefined && recruit.canAttack) {
       val newProjectile = createProjectile(recruit)
       this.addProjectile(newProjectile)
       recruit.restartCooldown()
     }
     else if(getTargetLocation(recruit).isDefined) recruit.reload()
  }

  def getFarthestEnemy(list: Vector[Enemy]) = list.maxBy(n => enemyTravelPath.indexOf(getEnemyLocation(n).get))

  def getTargetLocation(recruit: AttackRecruit): Option[EnemySquare] = {
     if(enemiesInRange(recruit).nonEmpty) this.getEnemyLocation(this.getFarthestEnemy(enemiesInRange(recruit)))
     else None
  }

  def createProjectile(recruit: AttackRecruit) = {
     val target = enemiesInRange(recruit).head
     val spawnLoc: MapSquare = {
        // IF THE DIFFERENCE IS GREATER ON THE X-AXIS COMPARED TO THE TARGET ENEMY, FIND THE DIRECTION IN WHICH TO SPAWN ON THE X-AXIS. ELSE SAME ON THE Y-AXIS
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
   new Projectile(recruit, recruit.getStrength, target, spawnLoc, this)
 }

 // SUPPORT EFFECT FOR ALL SQUARES IN A SUPPORTRECRUIT'S RANGE
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

  var enemyPath = false
  var free = true
  var occupied = false

  def isEnemyPath = enemyPath
  def isFree = free
  def isOccupied = occupied
}

class FreeSquare(x: Int, y: Int) extends MapSquare(x, y) {
  free = true
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