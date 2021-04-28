package towerDefense

abstract class Recruit(name: String, description: String, strength: Int, range: Int, cost: Int, upgrade: Option[Recruit]) {

  def getName = this.name
  def getDescription = this.description

  // FOR GUI PURPOSES
  def getSeparatedDesc = {
     val splitted = this.description.split(" ")
     var addition = ""
     var result = collection.mutable.Buffer[String]()
     for(each <- splitted) {
       if((addition + each).length > 20) {
         result += addition
         addition = ""
         addition += (each + " ")
       }
       else addition += (each + " ")
     }
     result += addition
     result.toVector
  }

  private var currentLocation = new RecruitSquare(0, 0, this)
  def getLocation = currentLocation
  def setLocation(square: RecruitSquare) = currentLocation = square

  def getRange = this.range

  private var sellPrice = (cost / 8) * 5
  def getSellPrice = sellPrice

  def getCost = cost
  def getStrength = this.strength
  def getUpgrade = this.upgrade

}
