package towerDefense

abstract class SupportRecruit(name: String, description: String, strength: Int, range: Int, cost: Int, upgrade: Option[Recruit]) extends Recruit(name, description, strength, range, cost, upgrade) {
   def supportEffect(square: MapSquare): Unit
}

class FatherMerrin extends SupportRecruit("Father Merrin", s"Reduces the countdown of every recruit in its range by strength.", 1, 1, 1, Some(new EnlightenedMerrin)) {

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

class EnlightenedMerrin extends SupportRecruit("Enlightened Merrin", s"Reduces the countdown of every recruit in its range by strength.", 2, 1, 1, Some(new LightkeeperMerrin)) {

  def supportEffect(square: MapSquare): Unit = {
    square match {
      case attSquare: RecruitSquare => {
         attSquare.getRecruit match {
           case attRecruit: AttackRecruit => attRecruit.addCooldownModifier(-1 * getStrength)
           case _ =>
         }
      }
      case _ =>
    }
  }
}

class LightkeeperMerrin extends SupportRecruit("Lightkeeper Merrin", s"Reduces the countdown of every recruit in its range by strength.", 2, 2, 1, None) {

  def supportEffect(square: MapSquare): Unit = {
    square match {
      case attSquare: RecruitSquare => {
         attSquare.getRecruit match {
           case attRecruit: AttackRecruit => attRecruit.addCooldownModifier(-1 * getStrength)
           case _ =>
         }
      }
      case _ =>
    }
  }

}

class DrFrankenstein extends SupportRecruit("Dr. Frankenstein", "Extends the range of all attacking recruits within his range by strength.", 1, 1, 500, Some(new MadDrFrankenstein)) {
  def supportEffect(square: MapSquare): Unit = {
    square match {
      case attSquare: RecruitSquare => {
         attSquare.getRecruit match {
           case attRecruit: AttackRecruit => attRecruit.addRangeModifier(getStrength)
           case _ =>
         }
      }
      case _ =>
    }
  }
}

class MadDrFrankenstein extends SupportRecruit("Mad Dr. Frankenstein", "Extends the range of all attacking recruits within his range by strength.", 2, 1, 550, Some(new InsaneDrFrankenstein)) {
  def supportEffect(square: MapSquare): Unit = {
    square match {
      case attSquare: RecruitSquare => {
         attSquare.getRecruit match {
           case attRecruit: AttackRecruit => attRecruit.addRangeModifier(getStrength)
           case _ =>
         }
      }
      case _ =>
    }
  }
}

class InsaneDrFrankenstein extends SupportRecruit("INSANE Dr. Frankenstein", "Extends the range of all attacking recruits within his range by strength.", 2, 2, 600, None) {
  def supportEffect(square: MapSquare): Unit = {
    square match {
      case attSquare: RecruitSquare => {
         attSquare.getRecruit match {
           case attRecruit: AttackRecruit => attRecruit.addRangeModifier(getStrength)
           case _ =>
         }
      }
      case _ =>
    }
  }
}