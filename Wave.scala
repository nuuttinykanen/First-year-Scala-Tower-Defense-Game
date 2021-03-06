class Wave(enemies: Vector[Enemy]) {

 private var enemiesLeft: Vector[Enemy] = enemies

 def popNext: Option[Enemy] = {
   if(this.enemiesLeft.isEmpty) None
   else {
     var output = this.enemiesLeft.head
     this.enemiesLeft = this.enemiesLeft.drop(1)
     Some(output)
   }
 }

}
