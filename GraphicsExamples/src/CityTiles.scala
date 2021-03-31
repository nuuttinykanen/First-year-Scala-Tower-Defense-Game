import scala.swing._
import java.awt.{ Point, Rectangle }
import scala.io.Source
import javax.imageio.ImageIO
import java.io.File
import java.awt.image.BufferedImage
import scala.swing.event.MouseMoved

/**
 *  This example covers the following topics
 *
 *  + Reading an image from a file
 *  + Saving and image into a file
 *  + Drawing on the screen
 *  + Drawing into picture
 *  + Reading a csv file
 *  + Drawing a city map from sprites
 *  + Listening to mouse events
 *  + Selecting map rectangles with a mouse
 *
 *  Execute the program and try moving the cursor on the map
 *
 *  Map sprites : Kenney, http://opengameart.com
 *  csv file modified from opengameart-project xml files
 *
 */

object CityTiles extends SimpleSwingApplication {

  // Class for a single sprite, which includes image, scales, id and possible additional dasta (string type flags)
  case class Sprite(id: String, image: BufferedImage, width: Int, height: Int, data: Set[String])

  def getSprites(dataFile: String, imageFile: String) = {
    // Read the sprite id and locations in the image
    val lines = Source.fromFile(dataFile).getLines()

    // Read image file
    val spriteSheet = ImageIO.read(new File(imageFile))

    // Traverse sprites and disconnect them from the big image
    // Return the result as a list
    for {
      line <- lines.toList
      items = line.split(",")
      id = items.head
      bounds = items.tail.take(4).map(_.toInt)
      data = items.drop(5).toSet
      image = spriteSheet.getSubimage(bounds(0), bounds(1), bounds(2), bounds(3))
    } yield Sprite(id, image, bounds(2), bounds(3), data)
  }

  // With this method we can assign sprite numbners into the original sprite sheet.
  // This method is not called in the project.  You can try it yourself.


  def markSpriteNumbers(dataFile: String, imageFile: String, outFileName: String) = {
    // Read sprite ids and positions in the image
    val lines = Source.fromFile(dataFile).getLines()

    // Read image file
    val spriteSheet = ImageIO.read(new File(imageFile))

    // Get graphics object, with which we can draw
    val sheetGraphics = spriteSheet.getGraphics()

    // Traverse sprites and mark their numbers in the big picture
    for (
      line <- lines
    ) {
      val items = line.split(",")
      val id = items.head
      val bounds = items.tail.take(4).map(_.toInt)

      sheetGraphics.setColor(java.awt.Color.BLACK)
      sheetGraphics.drawString(id, bounds(0), bounds(2))
    }

    ImageIO.write(spriteSheet, "png", new File(outFileName))
  }

  def top = new MainFrame {

    title = "City map tests"

    val width = 800
    val height = 560

    // The component declares here the minimum, maximum and preferred sizes, which Layout Manager
    // possibly can follow when positioning components on the screen.

    minimumSize = new Dimension(width, height)
    preferredSize = new Dimension(width, height)
    maximumSize = new Dimension(width, height)

    // Load sprites from a file

    val sprites = getSprites("./GraphicsExamples/sprites.csv", "./GraphicsExamples/cityTiles_sheet.png")

    val selectionMask = ImageIO.read(new File("./GraphicsExamples/selection.png"))
    /**
     * city component draws the map
     */

    val city = new Component {

      // Map with sprite numbers. Typically these would be in an external file.

      val cityMap = List(
        List(124, 103, 126, 76, 4),
        List(44, 29, 30, 75, 4),
        List(56, 40, 81, 61, 16),
        List(3, 43, 56, 52, 53),
        List(122, 57, 96, 100, 80))

      // Later this class includes code to select sprites with a mouse.
      // This variable stores sprite locations in the map.
      var selectedSprite: Option[(Int, Int)] = None

      val leftPadding = 80
      val topPadding = 300
      val tileWidth = 132
      val halfWidth = 66
      val fullHeight = 66
      val halfHeight = 33

      // Calculate coordinates on the screen
      val positions = Vector.tabulate(5, 5) { (x: Int, y: Int) =>

        val sprite = sprites(cityMap(x)(y))

        val xc = leftPadding + x * halfWidth + y * fullHeight
        val yc = topPadding - x * halfHeight - sprite.height + y * halfHeight

        new Point(xc, yc)
      }

      /**
       * Paint function - Note that most calculates have been carried out elsewhere
       */
      override def paintComponent(g: Graphics2D) = {

        for {
          y <- 0 until 5
          x <- 4 to 0 by -1
        } {
          val place = positions(x)(y)
          val sprite = sprites(cityMap(x)(y))

          g.drawImage(sprite.image, place.x, place.y, null)

          // If selectedSprite is not empty, let us see if it is the selected one.
          selectedSprite.foreach {
            mapCoords =>
              if (mapCoords._1 == x && mapCoords._2 == y) {
                g.drawImage(selectionMask, place.x, place.y + sprite.height - selectionMask.getHeight, null)
              }
          }
        }
      }

      // To enable selecting the sprites we count a bounding box around them.
      val boundingBoxes = (
        for {
          x <- 0 until 5
          y <- 4 to 0 by -1
          pos = positions(x)(y)
          sprite = sprites(cityMap(x)(y))
        } yield new Rectangle(pos.x, pos.y, sprite.width, sprite.height) -> (x, y))

      // For selection - listen the mouse events
      listenTo(mouse.moves)

      // and tell how to react with things
      reactions += {
        case MouseMoved(c, point, mods) => {

          // Save the location of the of the possibly selected spring on the map
          selectedSprite = boundingBoxes.find(box => box._1.contains(point)).map(_._2)

          // the components must be redrawn to make the selection visible
          repaint()
        }
      }

    }
    contents = city
  }

}
