package towerDefense

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
   if(currentHealth + amount <= 0) currentHealth = 0
   else currentHealth += amount
 }

 def changeMoney(amount: Int)() = {
   if(currentMoney + amount < 0) currentMoney = 0
   else currentMoney += amount
 }

 def affordableRecruits: Vector[String] = {
    def compareCost(recruit: Recruit, list: Buffer[String]) = if(recruit.getCost <= this.getMoney) list += recruit.getName
    var affordable = Buffer[String]()
    val store = new RecruitStore
    store.getRecruits.foreach(compareCost(_, affordable))
    affordable.toVector
 }

 def hireRecruit(recruit: Recruit, square: MapSquare): String = {
   if(affordableRecruits.contains(recruit.getName) && square.isFree && !square.isOccupied) {
     this.getMap.placeRecruit(recruit, square)
     this.currentRecruits += recruit
     this.changeMoney(-1 * recruit.getCost)()
     s"You recruited ${recruit.getName} for ${recruit.getCost} coins."
   }
   else "You can't afford that recruit."
 }

 def sellRecruit(recruit: Recruit)() =  {
   val returnPrice = recruit.getSellPrice
   this.changeMoney(returnPrice)()
   this.map.removeRecruit(recruit.getLocation)
 }

 def upgradeRecruit(recruit: Recruit)() = {
   if(recruit.getUpgrade.isDefined) {
      val returnRec = this.getMap.upgradeRecruit(recruit)
      this.changeMoney(recruit.getUpgrade.get.getCost * -1)()
      println(s"You upgraded ${recruit.getName} to ${recruit.getUpgrade.get.getName}")
      returnRec
   }
 }
}
