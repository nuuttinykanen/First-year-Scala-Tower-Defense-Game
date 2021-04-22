package towerDefense

abstract class SupportRecruit(name: String, description: String, strength: Int, range: Int, cost: Int, upgrade: Option[Recruit]) extends Recruit(name, description, strength, range, cost, upgrade) {
   def supportEffect(square: MapSquare): Unit
}

class FatherMerrin extends SupportRecruit("Father Merrin", s"Reduces the countdown of every recruit in a range of 3 by 2.", 2, 3, 1, Some(new EnlightenedMerrin)) {

  def supportEffect(square: MapSquare): Unit = {
    square match {
      case attSquare: RecruitSquare => {
         attSquare.getRecruit match {
           case attRecruit: AttackRecruit => attRecruit.addCooldownModifier(-1 * this.getStrength)
           case _ =>
         }
      }
      case _ =>
    }
  }

}

class EnlightenedMerrin extends SupportRecruit("Enlightened Merrin", s"Reduces the countdown of every recruit in a range of 5 by 3.", 3, 5, 1, Some(new LightkeeperMerrin)) {

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

class LightkeeperMerrin extends SupportRecruit("Lightkeeper Merrin", s"Reduces the countdown of every recruit in a range of 6 by 5.", 5, 6, 1, None) {

  def supportEffect(square: MapSquare): Unit = {
    square match {
      case attSquare: RecruitSquare => {
         attSquare.getRecruit match {
           case attRecruit: AttackRecruit => attRecruit.addCooldownModifier(-6)
           case _ =>
         }
      }
      case _ =>
    }
  }

}