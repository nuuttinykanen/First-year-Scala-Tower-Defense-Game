import java.io.{BufferedReader, FileNotFoundException, FileReader, IOException}
import scala.collection.mutable
import o1.grid._

object formGame {
 val sourceFile = "gameInfo.txt"
 var sourceFileText = mutable.Buffer[String]()

 var gamePlayer = new Player(100, 300, new LevelMap(200))
 var gameMap = new LevelMap(200)
 var waves: Option[Map[Int, Wave]] = None

 def readFile = {
  try {
   val fileIn = new FileReader(sourceFile)
   val linesIn = new BufferedReader(fileIn)
   try {
      var oneLine = linesIn.readLine()
      while (oneLine != null) {
          sourceFileText += oneLine
          oneLine = linesIn.readLine()
      }
   } finally {
      fileIn.close()
      linesIn.close()
    }
   } catch {
    case notFound: FileNotFoundException => "What you just gave me was practically nothing. Thanks. Did not find a file."
    case e: IOException => "What you just gave me was quite difficult to read."
   }
 }

 def processData: Game = {

   var returnMap = Map[Int, Map[Enemy, Int]]()

   var text: Vector[String] = sourceFileText.mkString.split('#').toVector.drop(1)

   text.head.take(6) match {
     case "PLAYER" => gamePlayer = formPlayer(text.take(2))
     case _ => throw new IOException()
   }

   text = text.drop(1)

   for(each <- text.tail) {
     var waveNumbers = collection.mutable.Buffer[Int]()
     var enemyMap = Map[Enemy, Int]()
     var waveNumber = 1
      var waveNumberString = ""
      var index = 0
      while(each.drop(4)(index).isDigit) {
        waveNumberString += each.drop(4)(index)
        index += 1
      }
     waveNumber = waveNumberString.toInt

     var enemyText = each.drop(4 + waveNumber.toString.length)

     waveNumbers += waveNumber
     enemyMap = getEnemies(enemyText, enemyMap)

     returnMap += (waveNumber -> enemyMap)
   }
   val waveList = returnMap.map(n => new Wave(n._2)).toVector



   new Game(gamePlayer, waveList)
 }

 def formPlayer(data: Vector[String]): Player = {
    var playerData = data.head.drop(6)
    var mapData = data.last.drop(3)

     var startHealth = 100
     var startMoney = 100
     for(each <- 1 to 2) {
       playerData.take(2) match {
        case "HP" => startHealth = playerData.slice(2, 6).toInt
        case "MN" => startMoney = playerData.slice(2, 6).toInt
        case _    => throw new IOException("Error in reading player data.")
       }
       playerData = playerData.drop(6)
     }

   var mapSize = 200

   mapData.take(4) match {
       case "SIZE" => mapSize = mapData.slice(4, 7).toInt
       case _ => throw new IOException("Error in reading map size.")
   }
   mapData = mapData.drop(7)

   gameMap = new LevelMap(mapSize)
   initializeMap(gameMap, mapData)

   new Player(startHealth, startMoney, gameMap)
 }

 def getEnemies(data: String, map: Map[Enemy, Int]): Map[Enemy, Int] = {

   var enemyMap = map
   var newEnemy: Enemy = new Zombie

   for(each <- data.grouped(4)) {
     var amount = each.tail.toInt
     each.take(1) match {
       case "Z" => newEnemy = new Zombie
       case "C" => newEnemy = new ZombieCarriage
       case "M" => newEnemy = new MichaelMyers
       case "D" => newEnemy = new Dracula
       case "B" => newEnemy = new Bat
       case _   => newEnemy = new Zombie
     }
     enemyMap += newEnemy -> amount
   }
  enemyMap
 }

 def initializeMap(map: LevelMap, coordsText: String) = {

   val coords = coordsText.split('<').toVector.map(_.trim)



   def pathIsConnected(coords: Vector[String]): Boolean = {
      coords.dropRight(1).forall(
        n => n.split(':').last == coords(coords.indexOf(n) + 1).split(':').head
      )
   }

   def pathReachesEdges(coords: Vector[String]): Boolean = {
      val startCoords = coords.head.split(':').head.split(',').map(_.trim)
      val endCoords = coords.last.split(':').last.split(',').map(_.trim)

      val possibleEdges = {
        var edges = mutable.Buffer[Int](0, 0)
        edges += this.gameMap.width
        edges += this.gameMap.height
      }
      def startOnEdge = possibleEdges.contains(startCoords.head.toInt) ||  possibleEdges.contains(startCoords.last.toInt)
      def endOnEdge = possibleEdges.contains(endCoords.head.toInt) || (possibleEdges.contains(endCoords.last.toInt))
      startOnEdge && endOnEdge
   }

   if(!pathIsConnected(coords)) throw new IOException("The input path is not connected! Check the input coordinates in gameInfo.txt.")
   if(!pathReachesEdges(coords)) throw new IOException("The input path does not reach the map edges! Check the input coordinates in gameInfo.txt.")

   var coordList = mutable.Buffer[GridPos]()
   val tuples = coords.map(_.split(':').map(n => ((n.split(',').head.toInt, n.split(',').last.toInt))))

   def addCoords(coords: Vector[String]) = {
       val tuples = coords.map(n => (n.split(':').head, n.split(':').last))
       val tuplesAgain = tuples.map(n => (n._1.split(',').map(_.toInt), n._2.split(',').map(_.toInt)))
       val gridPoses = tuplesAgain.map(n => (new GridPos(n._1.head, n._1.last), new GridPos(n._2.head, n._2.last)))

       def addGridPoses(gridpos1: GridPos, gridpos2: GridPos) = {
          val difference = gridpos1.diff(gridpos2)
          val direction: CompassDir = {
            if(gridpos1.x < gridpos2.x)      CompassDir.East
            else if(gridpos1.x > gridpos2.x) CompassDir.West
            else if(gridpos1.y < gridpos2.y) CompassDir.South
            else if(gridpos1.y > gridpos2.y) CompassDir.North
            else throw new IOException("Could not determine CompassDir for creating enemy path.")
          }
          coordList += gridpos1
          var pathCounter = 0
          def checkPathTowards(pos: GridPos): Boolean = {
             pathCounter += 1
             if(pathCounter > gameMap.size) throw new IOException("The enemy path appears to be diagonal or it goes beyond the map, which is not possible.")
             pos != gridpos2
          }

          gridpos1.pathTowards(direction).takeWhile(n => checkPathTowards(n)).foreach(coordList += _)
        }
         gridPoses.foreach(n => addGridPoses(n._1, n._2))
     }

     addCoords(coords)
     gameMap.initializeEnemyPath(coordList.toVector)

     def doesNotOverlap(coords: Vector[String]) = {
       gameMap.enemyTravelPath == gameMap.enemyTravelPath.distinct
     }

     println(gameMap.enemyTravelPath)
     if(!doesNotOverlap(coords)) throw new IOException("The input path overlaps with itself! Check the input coordinates in gameInfo.txt.")
   }
}
