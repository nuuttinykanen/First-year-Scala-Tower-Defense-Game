package towerDefense.gui
import o1.grid.GridPos
import towerDefense._

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

  player.hireRecruit(new Simon, new MapSquare(3, 17))

  val listener = new ActionListener() {
     def actionPerformed(e: java.awt.event.ActionEvent) = {

       gridMap.repaint()

       game.passTime()
       player.hireRecruit(new Simon, new MapSquare(18, 19))
     }
  }

  val timer = new javax.swing.Timer(500, listener)
  timer.start()

  val michaelSprite = ImageIO.read(new File("towerDefense/graphics/michaelmyers.png"))
  var enemySprites: scala.collection.mutable.Map[String, BufferedImage] = Map("MichaelMyers" -> michaelSprite)


  def top = new MainFrame {
    title = "Night Stand"
    contents = gridMap
    size = new Dimension(gameMap.width * 10, gameMap.width * 10)
    preferredSize = new Dimension(gameMap.width * 10, gameMap.width * 10)
    minimumSize = new Dimension(gameMap.width * 10, gameMap.width * 10)
    maximumSize = new Dimension(gameMap.width * 10, gameMap.width * 10)
}


 val gridMap = new Component {
      var color = Color.black
      override def paintComponent(g: Graphics2D) = {
        g.setColor(color)
        for(each <- gameMap.allElements.map(n => (n.x, n.y))) {
          gameMap.elementAt(GridPos(each._1, each._2)) match {
             case square: EnemySquare => {
               if(enemySprites.keys.exists(n => square.getEnemy.getName == n)) {
                 g.drawImage(enemySprites(square.getEnemy.getName), each._1, each._2, null)
               }
               g.setColor(Color.blue)
             }
             case square: EnemyPathSquare => {
               g.setColor(Color.red)
             }
             case square: RecruitSquare => {
               g.setColor(Color.magenta)
             }
             case square: FreeSquare => {
               g.setColor(Color.gray)
             }
             case _ => g.setColor(Color.ORANGE)
           }
           g.fillRect(each._1 * 20, each._2 * 20, 50, 50)
        }

        for(each <- gameMap.getProjectiles.map(_.getLocation)) {
           g.setColor(Color.BLACK)
           g.fillOval(each.x * 20, each.y * 20, 10, 10)
        }

        for(each <- gameMap.getDeathMarks) {
           g.setColor(Color.PINK)
           g.fillRect(each.x * 20, each.y * 20, 1, 1)
        }

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
}

