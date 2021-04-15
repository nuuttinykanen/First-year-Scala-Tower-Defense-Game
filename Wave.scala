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

 def waveSize = this.enemyMap.values.sum

 def getEnemyList = enemyList

 def enemyListEmpty = enemyList.isEmpty

 def popNext = {
   if(this.enemyList.isEmpty) None
   else {
     var output = this.enemyList.head._1
     this.removeEnemy(output)
     output match {
       case some: Zombie => output = new Zombie
       case some: ZombieCarriage => output = new ZombieCarriage
       case some: MichaelMyers =>  output = new MichaelMyers
       case _ => output = new Zombie
     }
     Some(output)
   }
 }
}
