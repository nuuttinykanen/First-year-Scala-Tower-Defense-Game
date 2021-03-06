package towerDefense.gui

import o1.grid.GridPos
import towerDefense._
import scala.util.Random
import javax.imageio._
import java.awt.{Color, Graphics2D, Image}
import java.awt.event.ActionListener
import java.awt.geom.Rectangle2D
import java.awt.image.BufferedImage
import java.io.File
import javax.swing.UIManager
import scala.swing.{Dimension, MainFrame, Point, SimpleSwingApplication}
import scala.swing._
import scala.swing.event.{MouseMoved, MousePressed, MouseReleased}

object TowerDefenseGUI extends SimpleSwingApplication {
  UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName)
  formGame.readFile

  val game = formGame.processData
  def player = game.getPlayer
  def gameMap = game.getMap

  val recStore = new RecruitStore

  val listener = new ActionListener() {
     def actionPerformed(e: java.awt.event.ActionEvent) = {
       gameMap.removeTempModifiers()
       game.passTime()
       gridMap.repaint()
     }
  }

  // DEFAULT 60
  val timer = new javax.swing.Timer(60, listener)
  timer.start()

  // DEFAULT 30
  def scaleNum = 30

  def top = new MainFrame {
    title = "Night of Fright"
    contents = gridMap
    size = new Dimension(gameMap.width * scaleNum, gameMap.width * scaleNum)
    minimumSize = new Dimension(scaleNum * gameMap.width + scaleNum * 4 + scaleNum / 2, scaleNum * gameMap.width + scaleNum / 3)
  }


  def getSprite(spriteName: String): BufferedImage = ImageIO.read(new File("towerDefense/graphics/" + spriteName + ".png"))

  val enemySprites: scala.Vector[BufferedImage] = {
     var returnList = collection.mutable.Buffer[BufferedImage]()
     val enemyList = scala.Vector("zombie", "zombieHorde", "michaelMyers", "dracula", "bat")
     for(each <- enemyList) {
        returnList += getSprite(each)
     }
    returnList.toVector
  }

  def getEnemySprite(enemy: Enemy): Image = {
    val index = {
      enemy match {
      case some: Zombie => 0
      case some: ZombieHorde => 1
      case some: MichaelMyers => 2
      case some: Dracula => 3
      case some: Bat => 4
      case _ => 0
      }
    }
    enemySprites(index).getScaledInstance(scaleNum, scaleNum, Image.SCALE_DEFAULT)
  }

  val recruitSprites: scala.Vector[BufferedImage] = {
     var returnList = collection.mutable.Buffer[BufferedImage]()
     val recruitList = scala.Vector("suzy", "dancerSuzy", "simon", "vampKillerSimon", "vanHelsing", "slayerHelsing", "ash", "chainsawAsh", "macready", "flameRJ", "infernoRJ", "venkman", "captVenkman", "hunterVenkman", "fatherMerrin", "enlightenedMerrin", "lightkeeperMerrin", "drFrankenstein", "madDr", "insaneDr")
     for(each <- recruitList) {
        returnList += getSprite(each)
     }
    returnList.toVector
  }

  def getRecruitSprite(recruit: Recruit) = {
    val index = {
      recruit match {
        case some: Suzy => 0
        case some: DancerSuzy => 1
        case some: Simon => 2
        case some: VampKillerSimon => 3
        case some: VanHelsing => 4
        case some: SlayerHelsing => 5
        case some: Ash => 6
        case some: ChainsawAsh => 7
        case some: MacReady => 8
        case some: FlameRJ => 9
        case some: InfernoRJ => 10
        case some: Venkman => 11
        case some: CaptVenkman => 12
        case some: HunterVenkman => 13
        case some: FatherMerrin => 14
        case some: EnlightenedMerrin => 15
        case some: LightkeeperMerrin => 16
        case some: DrFrankenstein => 17
        case some: MadDrFrankenstein => 18
        case some: InsaneDrFrankenstein => 19
        case _ => 0
      }
    }
    recruitSprites(index).getScaledInstance(scaleNum, scaleNum, Image.SCALE_DEFAULT)
  }

  val gen = new Random()

  // The sprite for when an enemy dies.
  def deathSprite = {
    var file = "towerDefense/graphics/deathflame.png"
    if(gen.nextInt(10) < 3) file = "towerDefense/graphics/deathflame2.png"
    ImageIO.read(new File(file)).getScaledInstance(scaleNum, scaleNum, Image.SCALE_DEFAULT)
  }

  def getProjecSprite(spriteName: String): BufferedImage = ImageIO.read(new File("towerDefense/graphics/projectiles/" + spriteName + "projec" + ".png"))

  // Projectile sprites.
  var projecSprites = {
     var returnList = collection.mutable.Buffer[BufferedImage]()
     val recruitList = scala.Vector("simon", "vanHelsing", "ash", "chainsawAsh", "macready", "venkman", "suzy")
     for(each <- recruitList) {
        returnList += getProjecSprite(each)
     }
    returnList.toVector
  }

  // Gets the correct sprite for each projectile depending on who sent it.
  def getSpriteFromProjec(projectile: Projectile): Image = {
     val index = {
       projectile.getSender match {
        case some: Simon => 0
        case some: VampKillerSimon => 0
        case some: VanHelsing => 1
        case some: SlayerHelsing => 1
        case some: Ash => 2
        case some: ChainsawAsh => 3
        case some: MacReady => 4
        case some: FlameRJ => 4
        case some: InfernoRJ => 4
        case some: Venkman => 5
        case some: CaptVenkman => 5
        case some: HunterVenkman => 5
        case some: Suzy => 6
        case some: DancerSuzy => 6
        case _ => 0
       }
     }
     projecSprites(index).getScaledInstance(scaleNum, scaleNum, Image.SCALE_DEFAULT)
 }

 // The rectangles that form each recruit slot within the store.
 val recruitRectangles = {
    var recruitBuffer = scala.collection.mutable.Buffer[Recruit]()
    var rectBuffer = scala.collection.mutable.Buffer[Rectangle2D]()
    var row = 0
    for(each <- recStore.getRecruits) {
         recruitBuffer += each
         val f = new Rectangle(gameMap.width * scaleNum, scaleNum * (2 + row), scaleNum * 4, scaleNum)
         row += 1
         rectBuffer += f
    }
    recruitBuffer.zip(rectBuffer).toVector
 }

 // CHECKS IF MOUSE IS ON THE RECRUIT STORE
 def posOnStore(point: Point): Boolean = recruitRectangles.map(_._2).exists(n => n.contains(point.getX, point.getY))

 // CHECKS IF MOUSE IS ON THE 'START WAVE' BUTTON
 def posOnContinue(point: Point): Boolean = {
  val rectangle = new Rectangle(gameMap.width * scaleNum + (scaleNum * 2), scaleNum * 17, scaleNum * 2, (gameMap.width.toDouble * scaleNum * 0.1).toInt)
  rectangle.contains(point)
 }

 // CHECKS IF MOUSE IS ON THE UPGRADE BUTTON
 def posOnUpgrade(point: Point): Boolean = {
   val rectangle = new Rectangle(gameMap.width * scaleNum + 3, scaleNum * 17, scaleNum * 2, (gameMap.width.toDouble * scaleNum * 0.1).toInt)
   rectangle.contains(point)
 }

 // CHECKS IF MOUSE IS ON THE SELL BOX
 def posOnSell(point: Point): Boolean = {
   val rectangle = new Rectangle(gameMap.width * scaleNum, scaleNum * 16 + scaleNum / 2 - scaleNum / 4, scaleNum * 4, scaleNum / 2 + scaleNum / 4)
   rectangle.contains(point)
 }

 def getStoreRecruit(point: Point): Recruit = {
   val recruit = recruitRectangles.find(_._2.getBounds.contains(point.getX, point.getY))
   if(recruit.isDefined) {
      gameMap.matchRecruit(recruit.get._1)
   }
   else throw new IndexOutOfBoundsException
 }

 def mouseCoord(coord: Point): GridPos = GridPos(coord.getX.toInt / scaleNum, (coord.getY.toInt + scaleNum) / scaleNum)

 val gridMap: Component = new Component {

      // The optional recruit that the GUI displays more info on (stats, range)
      var analyzeRecruit: Option[Recruit] = None
      // The optional recruit which the player plans to purchase from the store. For visual recruit placement purposes.
      var purchaseCandidate: Option[Recruit] = None

      var mousePoint: Option[Point] = None
      var mouseJustPressed = false

      override def paintComponent(g: Graphics2D) = {
       listenTo(mouse.moves)
       listenTo(mouse.clicks)

       reactions += {
         case MousePressed(source, point, modifiers, clicks, triggersPopup) => {
           if(mouseJustPressed) {
           }
           else {
             // CLICKING THE 'NEXT WAVE' BUTTON WHEN GAME IS PAUSED CAUSES IT TO START THE NEXT WAVE.
             if(posOnContinue(point) && game.isPaused) game.continueGame()
             // CLICKING THE 'SELL' BUTTON CAUSES THE SELECTED RECRUIT TO BE SOLD.
             else if(posOnSell(point) && analyzeRecruit.nonEmpty && gameMap.getRecruits.contains(analyzeRecruit.get)) {
               player.sellRecruit(analyzeRecruit.get)()
               analyzeRecruit = None
             }
             // IF AN UPGRADE IS DEFINED IN THE SELECTED RECRUIT, CLICKING 'UPGRADE' CAUSES THE RECRUIT TO BE UPGRADED.
             else if(posOnUpgrade(point) && analyzeRecruit.isDefined && gameMap.getRecruits.contains(analyzeRecruit.get) && analyzeRecruit.get.getUpgrade.isDefined) {
               val loc = analyzeRecruit.get.getLocation
               player.upgradeRecruit(analyzeRecruit.get)()
               gameMap.elementAt(loc) match {
                 case square: RecruitSquare => analyzeRecruit = Some(square.getRecruit)
                 case _ =>
               }
             }
             // CLICKING RECRUITS WITHIN THE STORE AND SELECTING THEM TO BE PLACED ON THE MAP.
             else if(posOnStore(point) && getStoreRecruit(point).getCost <= player.getMoney) {
               purchaseCandidate = Some(getStoreRecruit(point))
               analyzeRecruit = purchaseCandidate
             }
             // CLICKING ON THE MAP WITH A SELECTED RECRUIT FROM THE STORE AND HIRING THEM, PLACING THEM ON THE MAP.
             else if(gameMap.contains(mouseCoord(mousePoint.get))) {
              if(purchaseCandidate.isDefined && mousePoint.isDefined && gameMap(mouseCoord(mousePoint.get)).isFree) {
               player.hireRecruit(purchaseCandidate.get, new MapSquare(mouseCoord(mousePoint.get).x, mouseCoord(mousePoint.get).y))
               purchaseCandidate = None
               analyzeRecruit = None
             }
             // CLICKING RECRUITS ON THE MAP TO SEE THEIR INFO DISPLAYED.
             else gameMap(mouseCoord(mousePoint.get)) match {
               case square: RecruitSquare => analyzeRecruit = Some(square.getRecruit)
               case _ => analyzeRecruit = None
             }
           }
           else {
             purchaseCandidate = None
           }
             mouseJustPressed = true
           }
        }
        case MouseReleased(c, point, mods, x, y) => mouseJustPressed = false
        case MouseMoved(c, point, mods) => {
           this.mousePoint = Some(point)
         }
      }

      drawMap(g)

      // DRAW PROJECTILES
      for(each <- gameMap.getProjectiles.map(_.getLocation)) {
          g.setColor(Color.BLACK)
          g.drawImage(getSpriteFromProjec(gameMap.getProjectiles.find(_.getLocation == each).get), each.x * scaleNum, each.y * scaleNum, null)
      }

      // DRAW ENEMY DEATH SPRITE
      for(each <- gameMap.getDeathMarks) g.drawImage(deathSprite, each.x * scaleNum, each.y * scaleNum - scaleNum, null)

      // DRAW SELECTED RECRUIT RANGE
      if(analyzeRecruit.isDefined && analyzeRecruit != purchaseCandidate && gameMap.getRecruits.contains(analyzeRecruit.get)) {
         for(each <- gameMap.squaresInRange(analyzeRecruit.get, analyzeRecruit.get.getLocation)) {
            g.setColor(Color.MAGENTA)
            g.drawRect(each.x * scaleNum, each.y * scaleNum - scaleNum, 30, 30)
        }
      }

      // DRAW PURCHASE CANDIDATE AND ITS RANGE
      if(mousePoint.isDefined && gameMap.contains(mouseCoord(mousePoint.get))) {
        if(purchaseCandidate.isDefined && gameMap(mouseCoord(mousePoint.get)).isFree) {
            for(each <- gameMap.squaresInRange(purchaseCandidate.get, gameMap(mouseCoord(mousePoint.get)))) {
                g.setColor(Color.MAGENTA)
                g.drawRect(each.x * scaleNum, each.y * scaleNum - scaleNum, 30, 30)
            }
            val gridPlace = gameMap(mouseCoord(mousePoint.get))
            g.drawImage(getRecruitSprite(purchaseCandidate.get), gridPlace.x * scaleNum, gridPlace.y * scaleNum - scaleNum, null)
        }
      }

      drawRecruitStore(g, analyzeRecruit)
      if(game.isPaused) drawPathText(g)()
      if(game.isDone) {
          if(player.isDead) gameOverMessage(g)()
          else winMessage(g)()
          timer.stop()
        }
    }
  }

  def drawMap(g: Graphics2D) = {
       g.setColor(Color.gray)
       g.fillRect(0, 0, gameMap.width * scaleNum, gameMap.width * scaleNum - scaleNum)
       for(each <- gameMap.allElements.map(n => (n.x, n.y))) {
          gameMap.elementAt(GridPos(each._1, each._2)) match {
             case square: EnemySquare => {
               g.setColor(Color.black)
                 g.fillRect(each._1 * scaleNum, each._2 * scaleNum - scaleNum, scaleNum, scaleNum)
                 g.drawImage(getEnemySprite(square.getEnemy), each._1 * scaleNum, each._2 * scaleNum - scaleNum, null)
               g.setColor(Color.red)
                 g.fillRect(each._1 * scaleNum, each._2 * scaleNum - scaleNum + (scaleNum - 4), (square.getEnemy.healthPercentage * scaleNum.toDouble).toInt, 3)
                 g.drawRect(each._1 * scaleNum, each._2 * scaleNum - scaleNum + (scaleNum - 4), scaleNum, 3)
             }
             case square: EnemyPathSquare => {
               g.setColor(Color.black)
                 g.fillRect(each._1 * scaleNum, each._2 * scaleNum - scaleNum, scaleNum, scaleNum)
             }
             case square: RecruitSquare => {
               g.setColor(Color.gray)
                 g.fillRect(each._1 * scaleNum, each._2 * scaleNum - scaleNum, scaleNum, scaleNum)
                 g.drawImage(getRecruitSprite(square.getRecruit), each._1 * scaleNum, each._2 * scaleNum - scaleNum, null)
               g.setColor(Color.WHITE)
               square.getRecruit match {
                 case some: AttackRecruit => {
                   g.fillRect(each._1 * scaleNum, each._2 * scaleNum - scaleNum + (scaleNum - 4), ((1 - some.cooldownPercentage) * scaleNum).toInt, 3)
                    g.setColor(Color.BLACK)
                   g.drawRect(each._1 * scaleNum, each._2 * scaleNum - scaleNum + (scaleNum - 4), scaleNum, 3)
                 }
                 case _ =>
               }
             }
             case _ =>
          }
          g.setColor(Color.black)
          g.drawRect(each._1 * scaleNum, each._2 * scaleNum - scaleNum, scaleNum, scaleNum)
        }
   }

  def drawRecruitStore(g: Graphics2D, analyzeRecruit: Option[Recruit]): Unit = {
        g.setColor(Color.red)
         g.drawRect(gameMap.width * scaleNum, 0, scaleNum * 4, gameMap.width * scaleNum - scaleNum)
         g.drawRect(gameMap.width * scaleNum, 0, scaleNum * 4, scaleNum * 5)
        def recStoreCoords(row: Int): GridPos = GridPos(gameMap.width * scaleNum, scaleNum * (2 + row))
        var row = 0
        g.setColor(Color.DARK_GRAY)
        g.fillRect(gameMap.width * scaleNum, 0, scaleNum * 4, scaleNum * gameMap.width - scaleNum)

        for(each <- recStore.getRecruits) {
            if(!player.affordableRecruits.contains(each.getName)) g.setColor(Color.GRAY)
            g.fillRect(gameMap.width * scaleNum, scaleNum * (2 + row), scaleNum * 4, scaleNum)

            g.setColor(Color.RED)
            g.drawRect(gameMap.width * scaleNum, scaleNum * (2 + row), scaleNum * 4, scaleNum)

            g.setColor(Color.WHITE)
            g.drawImage(getRecruitSprite(each), recStoreCoords(row).x, recStoreCoords(row).y, null)
            row += 1
            g.setFont(Font(Font.Serif, Font.Bold, (scaleNum / 3)))
            g.drawString(s"${each.getCost} G", recStoreCoords(row).x + 40, recStoreCoords(row).y - 18)
            g.setFont(Font(Font.Serif, Font.Bold, ((scaleNum * 4) / (scaleNum / 5 + each.getName.length / 2))))
            g.drawString(s"${each.getName}", recStoreCoords(row).x + 40, recStoreCoords(row).y - 5)
            g.setColor(Color.DARK_GRAY)
        }

       drawInfo(g)()
       if(analyzeRecruit.isDefined) {
         drawAnalyzeInfo(g, analyzeRecruit)()
         drawSellBox(g, analyzeRecruit)()
         drawUpgradeInfo(g, analyzeRecruit)()
       }

    drawStartButton(g)()
  }

 def drawInfo(g: Graphics2D)() = {
    g.setFont(Font(Font.Serif, Font.Bold, 10))
    g.setColor(Color.WHITE)
      g.drawString(s"WAVE: ${game.getWaveNumber}", gameMap.width * scaleNum, scaleNum / 2)
      g.drawString(s"WAVES LEFT: ${game.getWavesLeft}",   gameMap.width * scaleNum, scaleNum / 2 + scaleNum / 3)
      g.drawString(s"HEALTH: ${player.getHealth}",        gameMap.width * scaleNum, scaleNum + scaleNum / 4)
      g.drawString(s"MONEY: ${player.getMoney}",   gameMap.width * scaleNum, scaleNum + 2 * scaleNum / 3)

 }

 def drawAnalyzeInfo(g: Graphics2D, analyzeRec: Option[Recruit])() = {
      if(analyzeRec.isDefined) {
          g.setColor(Color.WHITE)
            g.setFont(Font(Font.Serif, Font.BoldItalic, 12))
          g.drawString(s"${analyzeRec.get.getName}", gameMap.width * scaleNum + 2, scaleNum * 11 - scaleNum / 2)
            g.setFont(Font(Font.Serif, Font.Plain, 11))
          g.drawString(s"Range: ${analyzeRec.get.getRange}", gameMap.width * scaleNum + 2, scaleNum * 11)
          g.drawString(s"Strength: ${analyzeRec.get.getStrength}", gameMap.width * scaleNum + 2, scaleNum * 11 + scaleNum / 2)
          analyzeRec.get match {
            case some: AttackRecruit => g.drawString(s"Cooldown: ${some.currentCooldown}", gameMap.width * scaleNum + 2, scaleNum * 12)
            case _ =>
          }
          var num = 0
          val desc = analyzeRec.get.getSeparatedDesc
          for(each <- desc) {
                g.setFont(Font(Font.Serif, Font.Plain, 9))
              g.drawString(s"${each}", gameMap.width * scaleNum + 2, scaleNum * 12 + (scaleNum / 2) * num)
              num += 1
          }
      }
 }

 def drawSellBox(g: Graphics2D, analyzeRec: Option[Recruit])() = {
       g.setColor(Color.YELLOW)
       g.fillRect(gameMap.width * scaleNum, scaleNum * 16 + scaleNum / 2 - scaleNum / 4, scaleNum * 4, scaleNum / 2 + scaleNum / 4)
       g.setColor(Color.BLACK)
       g.setFont(Font(Font.Serif, Font.Bold, 13))
       g.drawString(s"SELL FOR ${analyzeRec.get.getSellPrice} G", gameMap.width * scaleNum + scaleNum / 2, scaleNum * 17 - scaleNum / 4)
 }

 def drawUpgradeInfo(g: Graphics2D, analyzeRec: Option[Recruit])() = {
      if(analyzeRec.get.getUpgrade.isDefined) {
         val upgrade = analyzeRec.get.getUpgrade.get
             g.setFont(Font(Font.Serif, Font.Bold, 11))
          g.setColor(Color.WHITE)
          g.drawString(s"Upgrade to", gameMap.width * scaleNum + 2, scaleNum * 14)
             g.setFont(Font(Font.Serif, Font.Plain, 11))
          g.drawString(s"${upgrade.getName}", gameMap.width * scaleNum + 1, scaleNum * 14 + scaleNum / 2)
          g.drawString(s"Range: ${upgrade.getRange}", gameMap.width * scaleNum + 2, scaleNum * 15)
          g.drawString(s"Strength: ${upgrade.getStrength}", gameMap.width * scaleNum + 2, scaleNum * 15 + scaleNum / 2)

          upgrade match {
              case some: AttackRecruit => g.drawString(s"Cooldown: ${some.currentCooldown}", gameMap.width * scaleNum + 2, scaleNum * 16)
              case _ =>
            }

          if(upgrade.getCost > player.getMoney) g.setColor(Color.LIGHT_GRAY)
          else g.setColor(Color.GREEN)

           g.fillRect(gameMap.width * scaleNum, scaleNum * 17, scaleNum * 2, (gameMap.width.toDouble * scaleNum * 0.1).toInt)

           g.setColor(Color.WHITE)
           g.setFont(Font(Font.Serif, Font.Bold, scaleNum / 3))
             g.drawString("UPGRADE", gameMap.width * scaleNum + 5, scaleNum * 18)
           g.setFont(Font(Font.Serif, Font.Bold, scaleNum / 2))
             g.drawString(s"${upgrade.getCost} G", gameMap.width * scaleNum + scaleNum / 3, scaleNum * 18 + scaleNum / 2)
       }
 }

 def drawStartButton(g: Graphics2D)() = {
    if(game.isPaused) {
        g.setColor(Color.GREEN)
          g.fillRect(gameMap.width * scaleNum + (scaleNum * 2), scaleNum * 17, scaleNum * 2, (gameMap.width.toDouble * scaleNum * 0.1).toInt)
        g.setColor(Color.WHITE)
        g.setFont(Font(Font.Serif, Font.Bold, scaleNum / 2))
          g.drawString("START", gameMap.width * scaleNum + (scaleNum * 2) + 3, scaleNum * 18)
          g.drawString("WAVE", gameMap.width * scaleNum + (scaleNum * 2) + 3, scaleNum * 18 + scaleNum / 2)
    }
    else {
        g.setColor(Color.darkGray)
          g.fillRect(gameMap.width * scaleNum + (scaleNum * 2), scaleNum * 17, scaleNum * 2, (gameMap.width.toDouble * scaleNum * 0.1).toInt)
        g.setColor(Color.WHITE)
        g.setFont(Font(Font.Serif, Font.Bold, scaleNum / 3))
          g.drawString("SURVIVE", gameMap.width * scaleNum + (scaleNum * 2) + 3, scaleNum * 18)
    }
    g.setColor(Color.BLACK)
    g.drawRect(gameMap.width * scaleNum, scaleNum * 17, scaleNum * 2, (gameMap.width.toDouble * scaleNum * 0.1).toInt)

    g.drawRect(gameMap.width * scaleNum, scaleNum * 16 + scaleNum / 2 - scaleNum / 4, scaleNum * 4, scaleNum / 2 + scaleNum / 4)
    g.drawRect(gameMap.width * scaleNum + (scaleNum * 2), scaleNum * 17, scaleNum * 2, (gameMap.width.toDouble * scaleNum * 0.1).toInt)
 }

 def drawPathText(g: Graphics2D)() = {
    val start = gameMap.enemyTravelPath.head
    val end = gameMap.enemyTravelPath.last

    g.setColor(Color.WHITE)
    g.setFont(Font(Font.Serif, Font.Bold, 9))
      g.drawString("START", start.x * scaleNum, start.y * scaleNum + scaleNum / 2)
      g.drawString("END", end.x * scaleNum + 6, end.y * scaleNum - scaleNum / 3)
 }

 def gameOverMessage(g: Graphics2D)() = {
        g.setColor(Color.RED)
          g.fillRect((scaleNum * gameMap.width) / 2 - (scaleNum * 3), scaleNum * gameMap.width / 2 - (scaleNum * 3), scaleNum * 6, scaleNum * 6)
        g.setColor(Color.BLACK)
          g.drawRect((scaleNum * gameMap.width) / 2 - (scaleNum * 3), scaleNum * gameMap.width / 2 - (scaleNum * 3), scaleNum * 6, scaleNum * 6)
        g.setFont(Font(Font.Serif, Font.Bold, scaleNum / 2))
          g.drawString("YOU LOST.", (scaleNum * gameMap.width) / 2 - (scaleNum * 2), scaleNum * gameMap.width / 2 - scaleNum)
          g.drawString("THE MONSTERS", (scaleNum * gameMap.width) / 2 - (scaleNum * 2), scaleNum * gameMap.width / 2)
          g.drawString("PREVAIL.", (scaleNum * gameMap.width) / 2 - (scaleNum * 2), scaleNum * gameMap.width / 2 + scaleNum)
 }

 def winMessage(g: Graphics2D)() = {
        g.setColor(Color.GREEN)
          g.fillRect((scaleNum * gameMap.width) / 2 - (scaleNum * 3), scaleNum * gameMap.width / 2 - (scaleNum * 3), scaleNum * 6, scaleNum * 6)
        g.setColor(Color.BLACK)
          g.drawRect((scaleNum * gameMap.width) / 2 - (scaleNum * 3), scaleNum * gameMap.width / 2 - (scaleNum * 3), scaleNum * 6, scaleNum * 6)
        g.setFont(Font(Font.Serif, Font.Bold, scaleNum / 2))
          g.drawString("YOU WON.", (scaleNum * gameMap.width) / 2 + 2 - (scaleNum * 3), scaleNum * gameMap.width / 2 - scaleNum)
          g.drawString("NOW REST AND SEE", (scaleNum * gameMap.width) / 2 + 2 - (scaleNum * 3), scaleNum * gameMap.width / 2)
          g.drawString("A NEW DAY RISE.", (scaleNum * gameMap.width) / 2  + 2- (scaleNum * 3), scaleNum * gameMap.width / 2 + scaleNum)
 }
}

