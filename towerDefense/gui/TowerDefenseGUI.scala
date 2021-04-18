package towerDefense.gui
import o1.grid.GridPos
import towerDefense._

import javax.imageio._
import java.awt.Color
import java.awt.Graphics2D
import java.awt.event.ActionListener
import java.io.File
import javax.swing.UIManager
import scala.swing.{Action, Dimension, GridBagPanel, Insets, Label, MainFrame, Menu, MenuBar, MenuItem, Point, SimpleSwingApplication, TextArea, TextField}
import scala.swing._
import java.util._
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
       println(s"${gameMap.getEnemySquares}")
     }
  }

  val timer = new javax.swing.Timer(5000, listener)
  timer.start()

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
             case square: EnemyPathSquare => {
               g.setColor(Color.red)
             }
             case square: EnemySquare => {
               g.setColor(Color.blue)
             }
             case square: RecruitSquare => {
               g.setColor(Color.magenta)
             }
             case square: FreeSquare => {
               g.setColor(Color.white)
             }
             case _ => g.setColor(Color.ORANGE)
           }
           g.fillRect(each._1 * 20, each._2 * 20, 50, 50)
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

