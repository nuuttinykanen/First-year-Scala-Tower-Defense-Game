import util.Random

class Wave(enemyMap: Map[Enemy, Int]) {

 private def amount = enemyMap.keys.size
 private var generator = new Random(amount)

 private var enemyList = enemyMap

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

 def getEnemyList = enemyList

 def enemyListEmpty = enemyList.isEmpty

 def popNext = {
   if(this.enemyList.isEmpty) None
   else {
     val output = this.enemyList.head._1
     this.removeEnemy(output)
     Some(output)
   }
 }
}
