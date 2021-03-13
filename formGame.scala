import java.io.{BufferedReader, FileNotFoundException, FileReader, IOException}
import scala.collection.mutable

object formGame {
 val sourceFile = "gameInfo.txt"
 var sourceFileText = mutable.Buffer[String]()

 var gamePlayer = new Player(100, 300)
 var waves = collection.mutable.Buffer[Int]()

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
   var text: Vector[String] = sourceFileText.mkString.split('#').toVector

   text.head.take(6) match {
     case "PLAYER" => gamePlayer = formPlayer(text.head)
     case _ => throw new IOException()
   }

   for(each <- text.tail) {

     var waveNumber = 1
      var waveNumberString = ""
      var index = 0
      while(each.drop(4)(index).isDigit) {
        waveNumberString += each.drop(4)(index)
        index += 1
      }
     waveNumber = waveNumberString.toInt

     var enemyText = each.drop(4 + waveNumber)
     var enemyList = collection.mutable.Buffer[Enemy]()
     waves += waveNumber
   }
 }

 def formPlayer(data: String): Player = {
    var stats = data.drop(6)
    var startHealth = 100
    var startMoney = 100
    stats.take(2) match {
      case "HP" => startHealth = stats.slice(2, 6).toInt
      case "MN" => startMoney = stats.slice(2, 6).toInt
    }
    new Player(startHealth, startMoney)
 }

 def getEnemies(enemy: Enemy, data: String) = {
   var newEnemy: Enemy = new Zombie
   var amount = data.slice(1, 4).toInt
   data.take(1) match {
     case "Z" => newEnemy = new Zombie
     case "C" => newEnemy = new ZombieCarriage
     case _   => newEnemy = new Zombie
   }
 }

}
