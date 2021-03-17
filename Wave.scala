class Wave(enemyMap: Map[Enemy, Int]) {

 private var enemiesLeft: Map[Enemy, Int] = enemyMap

 def popNext = {
   if(this.enemiesLeft.isEmpty) None
   else {
     var output = this.enemiesLeft.head
     this.enemiesLeft = this.enemiesLeft.drop(1)
     Some(output)
   }
 }

}
