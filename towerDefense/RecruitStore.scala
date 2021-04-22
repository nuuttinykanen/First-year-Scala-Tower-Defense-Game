package towerDefense

import scala.collection.mutable.Buffer
class RecruitStore {

  // Towers that are available to purchase during the game.
  private val recruits = {
     val list = Buffer[Recruit](new Suzy, new Simon, new MacReady, new VanHelsing, new Venkman, new Ash, new FatherMerrin)
     require(list.length < 10)
     list
  }

  def getRecruits: Vector[Recruit] = recruits.toVector

  def getCosts = getRecruits.map(_.getCost)

  def addRecruit(tower: Recruit) = recruits += tower

  def removeRecruit(tower: Recruit) = recruits -= tower

}
