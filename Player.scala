import scala.collection.mutable
import scala.collection.mutable.Buffer

class Player(health: Int, money: Int, map: LevelMap) {

 def getMap = map

 private var currentHealth = health
 private var currentMoney = money
 private var currentRecruits = Buffer[Recruit]()

 def getHealth: Int = currentHealth
 def getMoney: Int = currentMoney
 def getRecruits: Buffer[Recruit] = currentRecruits

 def changeHealth(amount: Int)() = {
   currentHealth += amount
 }

 def changeMoney(amount: Int)() = {
   currentMoney += amount
 }

 def affordableRecruits: Vector[String] = {
    def compareCost(recruit: Recruit, list: Buffer[String]) = if(recruit.getCost <= this.getMoney) list += recruit.getName
    var affordable = Buffer[String]()
    val store = new RecruitStore
    store.getRecruits.foreach(compareCost(_, affordable))
    affordable.toVector
 }

 def hireRecruit(recruit: Recruit): String = {
   if(affordableRecruits.contains(recruit.getName)) {
     this.currentRecruits += recruit
     this.changeMoney(-1 * recruit.getCost)()
     s"You recruited ${recruit.getName} for ${recruit.getCost} coins."
   }
   else "You can't afford that recruit."
 }
 def sellRecruit(recruit: Recruit)() =  {
   val returnPrice = recruit.getSellPrice
 }

 def upgradeRecruit(recruit: Recruit)() = {
   recruit
 }
}
