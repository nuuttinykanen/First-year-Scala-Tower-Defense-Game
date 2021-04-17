abstract class Status {

 def effect(enemy: Enemy): Unit

}

class Bleeding extends Status {
  effect
}