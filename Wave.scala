import util.Random

class Wave(enemyMap: Map[Enemy, Int]) {

 private def amount = enemyMap.keys.size
 private var generator = new Random(amount)

 var enemyList = enemyMap

 private var enemiesLeft: Map[Enemy, Int] = enemyMap

 def enemyOnTop = {
   if(enemyList.nonEmpty) enemyList.head._1
 }

 def removeEnemy(enemy: Enemy) = {
   if(enemyList.keys.exists(_ == enemy)) {
      val key = enemyList.keys.find(_ == enemy).get
      val value = enemyList(key)
      // modify
      if((value - 1) < 1) enemyList -= key
      else enemyList = enemyList.updated(key, value - 1)
   }
 }

 def popNext = {
   if(this.enemiesLeft.isEmpty) None
   else {
     val output = this.enemiesLeft.head._1
     this.removeEnemy(output)
     Some(output)
   }
 }
}
