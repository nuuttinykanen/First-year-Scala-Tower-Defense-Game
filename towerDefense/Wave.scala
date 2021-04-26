package towerDefense

import scala.util.Random

class Wave(enemyMap: Map[Enemy, Int]) {

 private def amount = enemyList.size
 private var generator = new Random()

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
     def genNumber = generator.nextInt(amount)
     var output = Random.shuffle(this.getEnemyList.keys).head
     this.removeEnemy(output)
     output match {
       case some: Zombie         => output = new Zombie
       case some: ZombieHorde => output = new ZombieHorde
       case some: MichaelMyers   => output = new MichaelMyers
       case some: Dracula        => output = new Dracula
       case some: Bat            => output = new Bat
       case _                    => output = new Zombie
     }
     Some(output)
   }
 }
}
