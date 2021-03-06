package towerDefense
import scala.collection.mutable.Buffer

class RecruitStore {

  // Towers that are available to purchase during the game. Does not contain upgrades.
  private val recruits = {
     val list = Buffer[Recruit](new Suzy, new Simon, new MacReady, new VanHelsing, new Venkman, new Ash, new FatherMerrin, new DrFrankenstein)
     require(list.length < 9)
     list.toVector
  }

  def getRecruits: Vector[Recruit] = recruits.toVector
}
