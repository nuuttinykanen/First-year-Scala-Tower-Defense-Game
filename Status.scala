abstract class Status {

 def effect(enemy: Enemy): Unit

}

class Confused extends Status {
 def effect(enemy: Enemy) = ???
}

class Slowed extends Status {
 def effect(enemy: Enemy) = enemy.changeSpeed(-20)()
}