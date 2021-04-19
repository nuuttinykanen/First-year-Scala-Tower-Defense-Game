package towerDefense.gui
import o1.grid.GridPos
import towerDefense._
import scala.util.Random
import scala.collection.mutable.Map
import javax.imageio._
import java.awt.Color
import java.awt.Graphics2D
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

  player.hireRecruit(new Simon, new MapSquare(18, 19))
  player.hireRecruit(new Simon, new MapSquare(21, 29))

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

  def top = new MainFrame {
    title = "Night Stand"
    contents = gridMap
    size = new Dimension(gameMap.width * 15, gameMap.width * 15)
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
    enemySprites(index)
  }


  val recruitSprites = ImageIO.read(new File("towerDefense/graphics/simon.png"))
  def bloodSprite = {
    var file = "towerDefense/graphics/deathflame.png"
    if(gen.nextInt(10) < 3) file = "towerDefense/graphics/deathflame2.png"
    ImageIO.read(new File(file))
  }
  def crossSprite = {
    var file = "towerDefense/graphics/cross.png"
    if(gen.nextInt(10) < 6) file = "towerDefense/graphics/cross2.png"
    ImageIO.read(new File(file))
  }

 val gridMap = new Component {
      var color = Color.black
      override def paintComponent(g: Graphics2D) = {
        for(each <- gameMap.allElements.map(n => (n.x, n.y))) {
          gameMap.elementAt(GridPos(each._1, each._2)) match {
             case square: EnemySquare => {
               g.setColor(Color.red)
               g.fillRect(each._1 * 30, each._2 * 30, 80, 80)
               g.drawImage(getEnemySprite(square.getEnemy), each._1 * 20, each._2 * 20, null)
             }
             case square: EnemyPathSquare => {
               g.setColor(Color.red)
               g.fillRect(each._1 * 30, each._2 * 30, 80, 80)
             }
             case square: RecruitSquare => {
               g.setColor(Color.gray)
               g.fillRect(each._1 * 30, each._2 * 30, 80, 80)
               g.drawImage(recruitSprites, each._1 * 20, each._2 * 20, null)
             }
             case square: FreeSquare => {
               g.setColor(Color.gray)
               g.fillRect(each._1 * 30, each._2 * 30, 80, 80)
             }
             case _ => {
                g.setColor(Color.ORANGE)
                g.fillRect(each._1 * 30, each._2 * 30, 80, 80)
             }
           }
          g.setColor(Color.black)
          g.drawRect(each._1 * 30, each._2 * 30, 80, 80)
        }

        for(each <- gameMap.getProjectiles.map(_.getLocation)) {
           g.setColor(Color.BLACK)
           g.drawImage(crossSprite, each.x * 30, each.y * 30, null)
        }

        for(each <- gameMap.getDeathMarks) {
           g.drawImage(bloodSprite, each.x * 30, each.y * 30, null)
           g.drawImage(crossSprite, each.x * 30, each.y * 30, null)
        }

       // RECRUIT STORE
   //    g.setColor(Color.BLACK)
   //    g.fillRect(gameMap.width * 21, 0, 200, gameMap.width * 21)


     //  g.fillRect(0, gameMap.width * 21, gameMap.width * 21 + 200, 100)


   //    g.setColor(Color.WHITE)
     //  g.fillRect(gameMap.width * 21, 0, 10, gameMap.width * 21 + 10)
     //  g.fillRect(0, gameMap.width * 21, gameMap.width * 21, 10)

       listenTo(mouse.moves)
       reactions += {
        case MouseMoved(c, point, mods) => {
          g.setColor(Color.GREEN)
          g.fillRect(point.x, point.y, 50, 50)
            // the components must be redrawn to make the selection visible
          repaint()
        }
      }
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

