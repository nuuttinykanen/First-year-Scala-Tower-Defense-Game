import o1.GridPos
import scala.collection.mutable.Buffer
class RecruitStore {

  // Towers that are available to purchase during the game.
  private val recruits = Buffer[Recruit](new Simon(new LevelMap(200, 200)))

  def getRecruits: Vector[Recruit] = recruits.toVector

  def getCosts = getRecruits.map(_.getCost)

  def addRecruit(tower: Recruit) = recruits += tower

  def removeRecruit(tower: Recruit) = recruits -= tower

}
