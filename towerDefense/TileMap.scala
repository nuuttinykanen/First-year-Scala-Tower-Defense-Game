package towerDefense

import java.awt.Color
import java.io.File
import javax.imageio.ImageIO
import scala.swing._

object TileMap extends SimpleSwingApplication {

  val game = new Game(new Player(200, 200, new LevelMap(200)), Vector[Wave]())

  def top = new MainFrame {
   title = "Night of Horrors"
   contents = new BallPanel
   size = new Dimension(200, 100)
  }
}

class BallPanel extends Panel {

  val example = ImageIO.read(new File("game/example.pnge.png"))
  override def paintComponent(g : Graphics2D) = {
    g.setColor(Color.black)
    g.fillRect(20, 20, 300, 200)

    g.setColor(Color.gray)
    g.drawString("Night of Horrors", 115,250)
    g.drawImage(example, 300, 200, null)
  }
}