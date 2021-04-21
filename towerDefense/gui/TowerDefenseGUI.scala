package towerDefense.gui
import o1.grid.GridPos
import towerDefense._

import scala.util.Random
import scala.collection.mutable.Map
import javax.imageio._
import java.awt.{Color, Graphics2D, Image}
import java.awt.event.ActionListener
import java.awt.geom.Rectangle2D
import java.awt.image.BufferedImage
import java.io.File
import java.util
import javax.swing.UIManager
import scala.swing.{Action, Dimension, GridBagPanel, Insets, Label, MainFrame, Menu, MenuBar, MenuItem, Point, SimpleSwingApplication, TextArea, TextField}
import scala.swing._
import java.util._
import scala.Predef.->
import scala.swing.event.{MouseClicked, MouseMoved, MouseReleased}

object TowerDefenseGUI extends SimpleSwingApplication {
  UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName)
  formGame.readFile

  val game = formGame.processData
  game.continueGame()
  def player = game.getPlayer
  def gameMap = game.getMap

  val gen = new Random()

  player.hireRecruit(new Simon, new MapSquare(3, 17))

  player.hireRecruit(new MacReady, new MapSquare(3, 18))
  player.hireRecruit(new Ash, new MapSquare(1, 14))
  player.hireRecruit(new Simon, new MapSquare(21, 29))
  player.hireRecruit(new Suzy, new MapSquare(5, 6))

  val listener = new ActionListener() {
     def actionPerformed(e: java.awt.event.ActionEvent) = {
       gridMap.repaint()
       game.passTime()
       if(game.isPaused) game.continueGame()
     }
  }

  val timer = new javax.swing.Timer(1000, listener)
  timer.start()

 // val michaelSprite = ImageIO.read(new File("towerDefense/graphics/michaelmyers.png"))
 //  var enemySprites: scala.collection.mutable.Map[String, BufferedImage] = Map("MichaelMyers" -> michaelSprite)

  def scaleNum = 30

  def top = new MainFrame {
    title = "Night Stand"
    contents = gridMap
    size = new Dimension(gameMap.width * scaleNum, gameMap.width * scaleNum)
    minimumSize = new Dimension(gameMap.width * 10, gameMap.width * 10)
}
  def getSprite(spriteName: String): BufferedImage = ImageIO.read(new File("towerDefense/graphics/" + spriteName + ".png"))

  val enemySprites: scala.Vector[BufferedImage] = {
     var returnList = collection.mutable.Buffer[BufferedImage]()
     val enemyList = scala.Vector("zombie", "zombieCarriage", "michaelMyers", "dracula", "bat")
     for(each <- enemyList) {
        returnList += getSprite(each)
     }
    returnList.toVector
  }

  def getEnemySprite(enemy: Enemy) = {
    val index = {
      enemy match {
      case some: Zombie => 0
      case some: ZombieCarriage => 1
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
     val recruitList = scala.Vector("simon", "vanHelsing", "ash", "chainsawAsh", "macready", "venkman", "suzy")
     for(each <- recruitList) {
        returnList += getSprite(each)
     }
    returnList.toVector
  }

  def getRecruitSprite(recruit: Recruit) = {
    val index = {
      recruit match {
      case some: Simon => 0
      case some: VanHelsing => 1
      case some: Ash => 2
      case some: ChainsawAsh => 3
      case some: MacReady => 4
      case some: Venkman => 5
      case some: Suzy => 6
      case _ => 0
      }
    }
    recruitSprites(index).getScaledInstance(scaleNum, scaleNum, Image.SCALE_DEFAULT)
  }

  def bloodSprite = {
    var file = "towerDefense/graphics/deathflame.png"
    if(gen.nextInt(10) < 3) file = "towerDefense/graphics/deathflame2.png"
    ImageIO.read(new File(file)).getScaledInstance(scaleNum, scaleNum, Image.SCALE_DEFAULT)
  }
  def crossSprite = {
    var file = "towerDefense/graphics/cross.png"
    if(gen.nextInt(10) < 6) file = "towerDefense/graphics/cross2.png"
    ImageIO.read(new File(file)).getScaledInstance(scaleNum, scaleNum, Image.SCALE_DEFAULT)
  }

  def getProjecSprite(spriteName: String): BufferedImage = {
     ImageIO.read(new File("towerDefense/graphics/projectiles/" + spriteName + "projec" + ".png"))
  }

  var projecSprites = {
     var returnList = collection.mutable.Buffer[BufferedImage]()
     val recruitList = scala.Vector("simon", "vanHelsing", "ash", "chainsawAsh", "macready", "venkman", "suzy")
     for(each <- recruitList) {
        returnList += getProjecSprite(each)
     }
    returnList.toVector
  }

  def getSpriteFromProjec(projectile: Projectile) = {
     val index = {
       projectile.getSender match {
        case some: Simon => 0
        case some: VanHelsing => 1
        case some: Ash => 2
        case some: ChainsawAsh => 3
        case some: MacReady => 4
        case some: Venkman => 5
        case some: Suzy => 6
        case _ => 0
       }
     }
     projecSprites(index).getScaledInstance(scaleNum, scaleNum, Image.SCALE_DEFAULT)
  }

 val recStore = new RecruitStore

 val recruitRectangles = {
    var recruitBuffer = scala.collection.mutable.Buffer[Recruit]()
    var rectBuffer = scala.collection.mutable.Buffer[Rectangle2D]()
    var row = 0
    for(each <- recStore.getRecruits) {
         recruitBuffer += each
         val f = new Rectangle(gameMap.width * scaleNum, scaleNum * (5 + row), scaleNum * 4, scaleNum)
         row += 1
         rectBuffer += f
    }
    recruitBuffer.zip(rectBuffer).toVector
 }

 def posOnStore(point: Point): Boolean = {
    recruitRectangles.map(_._2).exists(n => n.contains(point.getX, point.getY))
 }

 def getStoreRecruit(point: Point): Recruit = {
   val recruit = recruitRectangles.find(_._2.getBounds.contains(point.getX, point.getY))
   if(recruit.isDefined) {
       recruit.get._1 match {
         case some: Simon => new Simon
         case some: VanHelsing => new VanHelsing
         case some: Ash => new Ash
         case some: ChainsawAsh => new ChainsawAsh
         case some: MacReady => new MacReady
         case some: Venkman => new Venkman
         case some: Suzy => new Suzy
         case _ => new Simon
       }
   }
   else throw new IndexOutOfBoundsException
 }

 def mouseCoord(coord: Point): GridPos = GridPos(coord.getX.toInt / scaleNum, (coord.getY.toInt + scaleNum) / scaleNum)

 val gridMap = new Component {

      var color = Color.black
      var analyzeRecruit: Option[Recruit] = None
      var purchaseCandidate: Option[Recruit] = None
      var mousePoint: Option[Point] = None
      override def paintComponent(g: Graphics2D) = {

       listenTo(mouse.moves)
       listenTo(mouse.clicks)
       reactions += {
        case MouseClicked(source, point, modifiers, clicks, triggersPopup) => {
           if(posOnStore(point)) {
              purchaseCandidate = Some(getStoreRecruit(point))
           }
           else if(gameMap.contains(mouseCoord(mousePoint.get))) {
             if(purchaseCandidate.isDefined && mousePoint.isDefined && gameMap(mouseCoord(mousePoint.get)).isFree) {
              player.hireRecruit(purchaseCandidate.get, new MapSquare(mouseCoord(mousePoint.get).x, mouseCoord(mousePoint.get).y))
              purchaseCandidate = None
             }
             else gameMap(mouseCoord(mousePoint.get)) match {
               case square: RecruitSquare => analyzeRecruit = Some(square.getRecruit)
               case _ => analyzeRecruit = None
             }
           }
           else {
             purchaseCandidate = None
           }
          }
         case MouseMoved(c, point, mods) => {
           this.mousePoint = Some(point)
         }
          // the components must be redrawn to make the selection visible
      }
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
             }
             case _ =>
           }
          g.setColor(Color.black)
          g.drawRect(each._1 * scaleNum, each._2 * scaleNum - scaleNum, scaleNum, scaleNum)
          g.setColor(Color.green)
        }

        for(each <- gameMap.getProjectiles.map(_.getLocation)) {
           g.setColor(Color.BLACK)
           g.drawImage(getSpriteFromProjec(gameMap.getProjectiles.find(_.getLocation == each).get), each.x * scaleNum, each.y * scaleNum, null)
        }

        for(each <- gameMap.getDeathMarks) {
           g.drawImage(bloodSprite, each.x * scaleNum, each.y * scaleNum - scaleNum, null)
        }

        if(analyzeRecruit.isDefined) {
          for(each <- gameMap.squaresInRange(analyzeRecruit.get).filter(n => gameMap.contains(n))) {
             g.setColor(Color.MAGENTA)
             g.drawRect(each.x * scaleNum, each.y * scaleNum - scaleNum, 30, 30)
          }
        }

        if(mousePoint.isDefined && gameMap.contains(mouseCoord(mousePoint.get))) {
           if(purchaseCandidate.isDefined && gameMap(mouseCoord(mousePoint.get)).isFree) {
              val gridPlace = gameMap(mouseCoord(mousePoint.get))
              g.drawImage(getRecruitSprite(purchaseCandidate.get), gridPlace.x * scaleNum, gridPlace.y * scaleNum - scaleNum, null)
           }
        }

        // RECRUIT STORE BASE
        g.setColor(Color.darkGray)
        g.fillRect(gameMap.width * scaleNum, 0, scaleNum * 4, gameMap.width * scaleNum - scaleNum)
        g.setColor(Color.red)
        g.drawRect(gameMap.width * scaleNum, 0, scaleNum * 4, gameMap.width * scaleNum - scaleNum)
        g.drawRect(gameMap.width * scaleNum, 0, scaleNum * 4, scaleNum * 5)


        val recStore = new RecruitStore
        g.setColor(Color.white)
        g.drawString("- NIGHT STAND -", gameMap.width * scaleNum, 15)
        g.drawString(s"Player health: ${player.getHealth}", gameMap.width * scaleNum, scaleNum)
        g.drawString(s"Player money: ${player.getMoney}", gameMap.width * scaleNum, scaleNum * 2)
        g.drawString(s"Wave: ${game.getWaveNumber}", gameMap.width * scaleNum, scaleNum * 3)
        g.drawString(s"Waves left: ${game.getWavesLeft}", gameMap.width * scaleNum, scaleNum * 4)

        def scaledWidth = gameMap.width * scaleNum
        def recStoreCoords(row: Int): GridPos = GridPos(gameMap.width * scaleNum, scaleNum * (5 + row))

        var first = true
        var row = 0
        for(each <- recStore.getRecruits) {
            g.setColor(Color.RED)
            g.drawRect(gameMap.width * scaleNum, scaleNum * (5 + row), scaleNum * 4, scaleNum)

            g.setColor(Color.WHITE)
            g.drawImage(getRecruitSprite(each), recStoreCoords(row).x, recStoreCoords(row).y, null)
            row += 1
            g.setFont(Font(Font.Serif, Font.Bold, (scaleNum / 3)))
            g.drawString(s"${each.getCost} G", recStoreCoords(row).x + 40, recStoreCoords(row).y - 18)
            g.setFont(Font(Font.Serif, Font.Bold, (scaleNum * 3) / (scaleNum / 5 + each.getName.length / 2)))
            g.drawString(s"${each.getName}", recStoreCoords(row).x + 40, recStoreCoords(row).y - 5)
        }
    }
 }

 val recrStore = new BoxPanel(Orientation.Horizontal) {
   contents += gridMap
 }
}

