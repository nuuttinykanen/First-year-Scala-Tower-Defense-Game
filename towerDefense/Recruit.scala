package towerDefense

abstract class Recruit(name: String, description: String, range: Int, cost: Int, upgrade: Option[Recruit]) {

  def getName = this.name
  def getDescription = this.description
  def getSeparatedDesc = {
     val splitted = this.description.split(" ").flatMap(_ +: " ")
     var addition = ""
     var result = collection.mutable.Buffer[String]()
     for(each <- splitted) {
       if((addition + each).length > 20) {
         result += addition
         addition = ""
         addition += each
       }
       else addition += each
     }
     result.toVector
  }

  def getRange = this.range

  def setLocation(square: RecruitSquare) = currentLocation = square

  private var currentLocation = new RecruitSquare(0, 0, this)
  def getLocation = currentLocation

  private var sellPrice = (cost / 8) * 5

  def getSellPrice = sellPrice
  def getCost = cost

  def getUpgrade = this.upgrade

}
