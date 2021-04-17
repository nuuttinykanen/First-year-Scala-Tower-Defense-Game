abstract class SupportRecruit(name: String, description: String, range: Int, cost: Int, upgrade: Option[Recruit]) extends Recruit(name, description, range, cost, upgrade) {
   def supportEffect(square: MapSquare): Unit
}

class FatherMerrin extends SupportRecruit("Father Merrin", "", 3, 1, None) {

  def supportEffect(square: MapSquare): Unit = {
    square match {
      case attSquare: RecruitSquare => {
         attSquare.getRecruit match {
           case attRecruit: AttackRecruit => attRecruit.addCooldownModifier(-4)
           case _ =>
         }
      }
      case _ =>
    }
  }

}