import scala.swing._
import java.awt.{Color, BasicStroke, Graphics2D}
import java.awt.RenderingHints
import java.awt.event.ActionListener


object StaticGraphics extends SimpleSwingApplication {
  
  def top = new MainFrame {
    title     = "Kello"
    resizable = false
    
    val width      = 200
    val height     = 200
    val fullHeight = 210
    
    // The component declares here the minimum, maximum and preferred sizes, which Layout Manager 
    // possibly can follow when positioning components on the screen.
    minimumSize   = new Dimension(width,fullHeight)
    preferredSize = new Dimension(width,fullHeight)
    maximumSize   = new Dimension(width,fullHeight)

    /* Thin and thick line */
    val secondStroke = new BasicStroke(1)
    val minuteStroke = new BasicStroke(2)
    val hourStroke   = new BasicStroke(4)
    
    // coordinates for clock center
    val cx = width / 2 
    val cy = width / 2
    
    // Due to the height of the letters their central points have been lowered a bit
    val textCy = cy + 10 
    val hourMarkRadius   = 0.70 * width / 2 
    val textPosRadius    = 0.86 * width / 2

    // Hours and minutes are translated into radians
    def hourToAngle(h: Double)   = (h * 30.0 - 90) / 360 * 2 * math.Pi
    def minuteToAngle(h: Double) = (h * 6.0  - 90) / 360 * 2 * math.Pi

    // central point, angle and distance -> x, y
    def cartesianX(xCenter: Int, angle: Double, length: Double) =
      (xCenter + math.cos(angle) * length).toInt

    def cartesianY(yCenter: Int, angle: Double, length: Double) =
      (yCenter + math.sin(angle) * length).toInt


    def drawClock( g: Graphics2D ) = {
      /** draw clock panel */
      for (hour <- 1 to 12) {
        val angle = hourToAngle(hour)
      
        g.fillOval( cartesianX(cx, angle, hourMarkRadius) - 5, cartesianY(cy, angle, hourMarkRadius) - 5, 10, 10)

        g.drawString(hour.toString(), cartesianX(cx, angle, textPosRadius) - 5, cartesianY(textCy, angle, textPosRadius) - 5)
      }
        
      // Get time and translate it into angle in radians
      import java.util.Calendar
      val hours = Calendar.getInstance.get(Calendar.HOUR)
      val mins  = Calendar.getInstance.get(Calendar.MINUTE)
      val secs  = Calendar.getInstance.get(Calendar.SECOND)
      
      val hourHand   = hourToAngle(hours + mins / 60.0)
      val minuteHand = minuteToAngle(mins)
      val secondHand = minuteToAngle(secs)
      

      // Draw clock hands with lines with different thickness and length
      g.setStroke(hourStroke)
      g.drawLine( cx, cy,
          cartesianX(cx, hourHand, hourMarkRadius * 0.7),
          cartesianY(cy, hourHand, hourMarkRadius * 0.7))

      g.setStroke(minuteStroke)
      g.drawLine( cx, cy,
          cartesianX(cx, minuteHand, hourMarkRadius * 0.9),
          cartesianY(cy, minuteHand, hourMarkRadius * 0.9))

      g.setStroke(secondStroke)
      g.drawLine( cx, cy,
          cartesianX(cx, secondHand, hourMarkRadius * 0.95),
          cartesianY(cy, secondHand, hourMarkRadius * 0.95))

    }
      
      
    val clock = new Panel {

      // Overriding this method enables drawing own graphics.  It gets as a parameter
      // a Graphics2D object, through which it is possible to draw into the object, 
      // change colors, coordinates, line thickness etc.
      override def paintComponent(g: Graphics2D) = {

        g.setColor(new Color(80, 180, 235))
        g.fillRect(0, 0, width, fullHeight)

        // Smoothened graphics, i.e., antialiasing
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)          

        g.translate(-1, -1) // Draw the shadow a little off 
        g.setColor(Color.black)
        drawClock(g)
        g.translate(1, 1)   // and reset the coordinates.   

        // Actual text is drawn on the shadow
        g.setColor(Color.white)
        drawClock(g)
        
      }    
      
    }  
    
    // SimpleSwingApplication includes only the clock panel
    contents = clock

    // This event listener and swing timer enable regularly repeated actions in the 
    // event listening thread without additional buffers or threads.
    
    val listener = new ActionListener(){
      def actionPerformed(e : java.awt.event.ActionEvent) = clock.repaint()  
    }

    // Timer sends to ActionListener an ActionEvent every 1000ms (1 second)
    // This event is executed in the event listener -thread.
    val timer = new javax.swing.Timer(1000, listener)
    timer.start()
    
    
  }
 
}