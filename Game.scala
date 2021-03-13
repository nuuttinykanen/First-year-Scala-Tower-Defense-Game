import java.io.{BufferedReader, FileNotFoundException, FileReader, IOException}
import scala.collection.mutable
class Game(player: Player, waves: Vector[Wave]) {

 private var round = 1
 private var wave = if(waves.nonEmpty) this.waves.head
 private var waveList = waves

 val sourceFile = "gameInfo.txt"
 var sourceFileText = mutable.Buffer[String]()

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

 def processData = {
   sourceFile.split("#")
 }

 def startWave() = ???

 def endWave() = {
   waveList = waveList.drop(1)
   if(waveList.isEmpty) endGame()
 }

 def endGame() = ???
 def passTime() = ???
 def spawnEnemy(enemy: Enemy) = ???
 def placeTower(map: Map, tower: Tower, x: Double, y: Double) = ???

}
