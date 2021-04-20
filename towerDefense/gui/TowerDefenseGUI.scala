package towerDefense.gui
import o1.grid.GridPos
import towerDefense._

import scala.util.Random
import scala.collection.mutable.Map
import javax.imageio._
import java.awt.{Color, Graphics2D, Image}
import java.awt.event.ActionListener
import java.awt.image.BufferedImage
import java.io.File
import java.util
import javax.swing.UIManager
import scala.swing.{Action, Dimension, GridBagPanel, Insets, Label, MainFrame, Menu, MenuBar, MenuItem, Point, SimpleSwingApplication, TextArea, TextField}
import scala.swing._
import java.util._
import scala.Predef.->
import scala.swing.event.MouseMoved

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

       game.passTime()

       gridMap.repaint()

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

 val gridMap = new Component {

      var color = Color.black
      var rangePrint: Option[Recruit] = None
      override def paintComponent(g: Graphics2D) = {

       listenTo(mouse.moves)
       reactions += {
        case MouseMoved(c, point, mods) => {
           gameMap.elementAt(GridPos(point.getX.toInt / scaleNum, (point.getY.toInt + scaleNum) / scaleNum)) match {
             case square: RecruitSquare => rangePrint = Some(square.getRecruit)
             case _ => {
               rangePrint = None
             }
           }
            // the components must be redrawn to make the selection visible
          repaint()
        }
      }

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
             case square: FreeSquare => {
               g.setColor(Color.gray)
               g.fillRect(each._1 * scaleNum, each._2 * scaleNum - scaleNum, scaleNum, scaleNum)
             }
             case _ => {
                g.setColor(Color.ORANGE)
                g.fillRect(each._1 * scaleNum, each._2 * scaleNum - scaleNum, scaleNum, scaleNum)
             }
           }
          g.setColor(Color.black)
          g.drawRect(each._1 * scaleNum, each._2 * scaleNum - scaleNum, scaleNum, scaleNum)
          g.setColor(Color.green)
        }

        for(each <- gameMap.getProjectiles.map(_.getLocation)) {
           g.setColor(Color.BLACK)
           val rotation = gen.nextInt(359)
           g.drawImage(getSpriteFromProjec(gameMap.getProjectiles.find(_.getLocation == each).get), each.x * scaleNum, each.y * scaleNum, null)
        }

        for(each <- gameMap.getDeathMarks) {
           g.drawImage(bloodSprite, each.x * scaleNum, each.y * scaleNum - scaleNum, null)
        }

        if(rangePrint.isDefined) {
          for(each <- gameMap.squaresInRange(rangePrint.get).filter(n => gameMap.contains(n))) {
             g.setColor(Color.MAGENTA)
             g.drawRect(each.x * scaleNum, each.y * scaleNum - scaleNum, 30, 30)
          }
        }

        g.setColor(Color.BLACK)
        g.drawString(s"Player health: ${player.getHealth}", 10, gameMap.width * scaleNum + 2)
        g.drawString(s"Player money: ${player.getMoney}", 10, gameMap.width * scaleNum + 12)
        g.drawString(s"Wave: ${game.getWaveNumber}", 10, gameMap.width * scaleNum + 22)
        g.drawString(s"Waves left: ${game.getWavesLeft}", 10, gameMap.width * scaleNum + 32)

       // RECRUIT STORE
   //    g.setColor(Color.BLACK)
   //    g.fillRect(gameMap.width * 21, 0, 200, gameMap.width * 21)


     //  g.fillRect(0, gameMap.width * 21, gameMap.width * 21 + 200, 100)


   //    g.setColor(Color.WHITE)
     //  g.fillRect(gameMap.width * 21, 0, 10, gameMap.width * 21 + 10)
     //  g.fillRect(0, gameMap.width * 21, gameMap.width * 21, 10)
 }

  val zombie = ImageIO.read(new File("towerDefense/graphics/zombie.png"))
  val floor = ImageIO.read(new File("towerDefense/graphics/castlefloor.png"))

  class GUIMapSquare(x: Int, y: Int) extends Panel {
    override def paintComponent(g: Graphics2D) = {
      g.setColor(Color.gray)
      g.drawRect(x, y, 1, 1)
    }
  }
 }

 val recruitStore = new Component {
    override def paintComponent(g: Graphics2D): Unit = {
       g.setColor(Color.RED)
       g.fillRect(gameMap.width * 10, 0, 100, gameMap.width * 10)
    }
 }
}

